package berthet.johann.channelmessaging.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import berthet.johann.channelmessaging.ChannelActivity;
import berthet.johann.channelmessaging.GPSActivity;
import berthet.johann.channelmessaging.MapActivity;
import berthet.johann.channelmessaging.MessageAdapter;
import berthet.johann.channelmessaging.R;
import berthet.johann.channelmessaging.network.Channel;
import berthet.johann.channelmessaging.network.Message;
import berthet.johann.channelmessaging.network.MessageList;
import berthet.johann.channelmessaging.network.WSRequestHandler;
import berthet.johann.channelmessaging.network.onWSRequestListener;

/**
 * Created by Johann on 07/03/2016.
 */
public class MessageListFragment extends Fragment implements View.OnClickListener, onWSRequestListener, AdapterView.OnItemClickListener {
    private static final String PREFS_ACCESS_TOKEN = "MyAccessToken";
    private static final int REQUEST_SEND_MESSAGES = 1;
    private static final int REQUEST_GET_MESSAGES = 2;

    private ListView lvFragment;
    private String myAccessToken;
    private String myChannelID;
    private EditText myInputText;
    private Button mySendButton;
    private Handler handler;

    private ArrayList<Message> messagesList;
    private BroadcastReceiver mMessageReceiver;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_listmessage,container);
        lvFragment = (ListView)v.findViewById(R.id.lvFragment_message);
        myInputText = (EditText)v.findViewById(R.id.inputEditText);
        mySendButton = (Button)v.findViewById(R.id.sendButton);
        mySendButton.setOnClickListener(this);
        lvFragment.setOnItemClickListener(this);

        messagesList = new ArrayList<Message>();

        SharedPreferences settings = getContext().getSharedPreferences(PREFS_ACCESS_TOKEN, 0);
        myAccessToken = settings.getString("accesstoken", null);

        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                abortBroadcast();
                long channelID = getActivity().getIntent().getLongExtra("channelID", 1);
                refreshMessages(channelID);
            }
        };

        long channelID = getActivity().getIntent().getLongExtra("channelID", 1);
        myChannelID = String.valueOf(channelID);
        refreshMessages(channelID);

        /*
        handler = new Handler();
        handler.postDelayed(RunnableRefreshingMessage, 1000);*/

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter =  new IntentFilter("com.google.android.c2dm.intent.RECEIVE");
        intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        getActivity().registerReceiver(mMessageReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mMessageReceiver);
    }

    /*
    final Runnable RunnableRefreshingMessage = new Runnable()
    {
        public void run()
        {
            refreshMessages(0);
            handler.postDelayed(this, 1000);
        }
    };*/

    @Override
    public void onWSRequestCompleted(int requestCode, String response) {
        System.out.println("request completed");
        if (requestCode == REQUEST_GET_MESSAGES) {
            Gson gson = new Gson();
            MessageList myList = new MessageList();
            myList.messages = gson.fromJson(response, MessageList.class).messages;
            if (messagesList != null){
                messagesList.addAll(myList.messages);
            }
            if (getContext() != null) {
                lvFragment.setAdapter(new MessageAdapter(getContext(), android.R.layout.simple_list_item_1, myList.messages));
            }
        }

    }

    @Override
    public void onWSRequestError(int requestCode, String exception) {
        Toast.makeText(getContext(), exception, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        String myMessage = myInputText.getText().toString();
        myInputText.setText("");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("accesstoken", myAccessToken);
        params.put("channelid", myChannelID);
        params.put("message", myMessage);

        if ( ((GPSActivity) getActivity()).getCurrentLocation() != null ) {
            String latitude = String.valueOf(((GPSActivity) getActivity()).mCurrentLocation.getLatitude());
            String longitude = String.valueOf(((GPSActivity) getActivity()).mCurrentLocation.getLongitude());
            params.put("latitude", latitude);
            params.put("longitude", longitude);
        }

        WSRequestHandler sendMessageRequest = new WSRequestHandler(params, "http://www.raphaelbischof.fr/messaging/?function=sendmessage", REQUEST_SEND_MESSAGES);
        sendMessageRequest.setOnWSRequestListener(this);
        sendMessageRequest.execute();
    }

    public void refreshMessages(long id) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("accesstoken", myAccessToken);
        if (id != 0) {
            myChannelID = String.valueOf(id);
        }
        params.put("channelid", myChannelID);

        WSRequestHandler refreshMessageRequest = new WSRequestHandler(params, "http://www.raphaelbischof.fr/messaging/?function=getmessages", REQUEST_GET_MESSAGES);
        refreshMessageRequest.setOnWSRequestListener(this);
        refreshMessageRequest.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        List<String> listItems = new ArrayList<String>();
        listItems.add("Ajouter un ami");
        listItems.add("Voir sur la carte");
        final CharSequence[] arr = listItems.toArray(new CharSequence[listItems.size()]);

        new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)//drawable de l'icone à gauche du titre
                .setTitle("Choisissez une option")//Titre de l'alert dialog
                .setItems(arr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//which = la position de l'item appuyé
                        if (which == 0) {
                            //Do some stuff (1st item touched)
                        } else {
                            //Do some over stuff (2nd item touched)
                            Intent myIntent = new Intent(getContext(), MapActivity.class);
                            myIntent.putExtra("username", messagesList.get(position).username); //Optional parameters
                            myIntent.putExtra("latitude", messagesList.get(position).latitude); //Optional parameters
                            myIntent.putExtra("longitude", messagesList.get(position).longitude); //Optional parameters
                            startActivity(myIntent);
                        }
                    }
                })//items de l'alert dialog
                .show();
    }

}
