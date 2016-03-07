package berthet.johann.channelmessaging.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

import berthet.johann.channelmessaging.MessageAdapter;
import berthet.johann.channelmessaging.R;
import berthet.johann.channelmessaging.network.MessageList;
import berthet.johann.channelmessaging.network.WSRequestHandler;
import berthet.johann.channelmessaging.network.onWSRequestListener;

/**
 * Created by Johann on 07/03/2016.
 */
public class MessageListFragment extends Fragment implements View.OnClickListener, onWSRequestListener {
    private static final String PREFS_ACCESS_TOKEN = "MyAccessToken";
    private static final int REQUEST_SEND_MESSAGES = 1;
    private static final int REQUEST_GET_MESSAGES = 2;

    private ListView lvFragment;
    private String myAccessToken;
    private String myChannelID;
    private EditText myInputText;
    private Button mySendButton;
    private Handler handler;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_listmessage,container);
        lvFragment = (ListView)v.findViewById(R.id.lvFragment_message);
        myInputText = (EditText)v.findViewById(R.id.inputEditText);
        mySendButton = (Button)v.findViewById(R.id.sendButton);
        mySendButton.setOnClickListener(this);

        long channelID = getActivity().getIntent().getLongExtra("channelID", 1);
        myChannelID = String.valueOf(channelID);

        SharedPreferences settings = getContext().getSharedPreferences(PREFS_ACCESS_TOKEN, 0);
        myAccessToken = settings.getString("accesstoken", null);

        handler = new Handler();
        handler.postDelayed(RunnableRefreshingMessage, 1000);

        return v;
    }

    final Runnable RunnableRefreshingMessage = new Runnable()
    {
        public void run()
        {
            refreshMessages(0);
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onWSRequestCompleted(int requestCode, String response) {
        System.out.println("request completed");
        if (requestCode == REQUEST_GET_MESSAGES) {
            Gson gson = new Gson();
            MessageList myList = new MessageList();
            myList.messages = gson.fromJson(response, MessageList.class).messages;
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

}
