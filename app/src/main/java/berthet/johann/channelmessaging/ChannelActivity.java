package berthet.johann.channelmessaging;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

import berthet.johann.channelmessaging.network.MessageList;
import berthet.johann.channelmessaging.network.WSRequestHandler;
import berthet.johann.channelmessaging.network.onWSRequestListener;

public class ChannelActivity extends AppCompatActivity implements View.OnClickListener ,onWSRequestListener {
    private static final String PREFS_ACCESS_TOKEN = "MyAccessToken";
    private static final int REQUEST_SEND_MESSAGES = 1;
    private static final int REQUEST_GET_MESSAGES = 2;

    private String myAccessToken;
    private String myChannelID;
    private ListView myListView;
    private EditText myInputText;
    private Button mySendButton;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        myListView = (ListView)findViewById(R.id.messageListView);
        myInputText = (EditText)findViewById(R.id.inputEditText);
        mySendButton = (Button)findViewById(R.id.sendButton);
        mySendButton.setOnClickListener(this);

        long channelID = (long)getIntent().getSerializableExtra("channelID");
        myChannelID = String.valueOf(channelID);

        SharedPreferences settings = getSharedPreferences(PREFS_ACCESS_TOKEN, 0);
        myAccessToken = settings.getString("accesstoken", null);

        this.refreshMessages();

        handler = new Handler();
        System.out.println("entree handler");
        handler.postDelayed(DoThings, 1000);
        System.out.println("sortie handler");

    }

    final Runnable DoThings = new Runnable()
    {
        public void run()
        {
            System.out.println("entree run");
            refreshMessages();
            handler.postDelayed(this, 1000);
            System.out.println("sortie run");
        }
    };

    @Override
    public void onWSRequestCompleted(int requestCode, String response) {
        if (requestCode == REQUEST_GET_MESSAGES) {
            Gson gson = new Gson();
            MessageList myList = new MessageList();
            myList.messages = gson.fromJson(response, MessageList.class).messages;
            myListView.setAdapter(new MessageAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, myList.messages));
        }

    }

    @Override
    public void onWSRequestError(int requestCode, String exception) {
        Toast.makeText(getApplicationContext(), exception, Toast.LENGTH_SHORT).show();
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

    public void refreshMessages() {
        System.out.println("entree refresh");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("accesstoken", myAccessToken);
        params.put("channelid", myChannelID);

        WSRequestHandler refreshMessageRequest = new WSRequestHandler(params, "http://www.raphaelbischof.fr/messaging/?function=getmessages", REQUEST_GET_MESSAGES);
        refreshMessageRequest.setOnWSRequestListener(this);
        refreshMessageRequest.execute();
        System.out.println("sortie refresh");
    }
}
