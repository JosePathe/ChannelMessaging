package berthet.johann.channelmessaging;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.logging.Handler;

import berthet.johann.channelmessaging.network.ChannelList;
import berthet.johann.channelmessaging.network.MessageList;
import berthet.johann.channelmessaging.network.WSRequestHandler;
import berthet.johann.channelmessaging.network.onWSRequestListener;

public class ChannelActivity extends AppCompatActivity implements View.OnClickListener ,onWSRequestListener {
    private static final String PREFS_ACCESS_TOKEN = "MyAccessToken";
    private String myAccessToken;
    private String myChannelID;
    private ListView myListView;
    private EditText myInputText;
    private Button mySendButton;
    private Boolean messageSended = false;

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

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

    }

    @Override
    public void onWSRequestCompleted(String response) {
        if (!messageSended) {
            Gson gson = new Gson();
            MessageList myList = new MessageList();
            myList.messages = gson.fromJson(response, MessageList.class).messages;
            myListView.setAdapter(new MessageAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, myList.messages));
            messageSended = false;
        } else {
            this.refreshMessages();
        }

    }

    @Override
    public void onWSRequestError(String exception) {
        Toast.makeText(getApplicationContext(), exception, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        messageSended = true;
        String myMessage = myInputText.getText().toString();
        myInputText.setText("");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("accesstoken", myAccessToken);
        params.put("channelid", myChannelID);
        params.put("message", myMessage);

        WSRequestHandler sendMessageRequest = new WSRequestHandler(params, "http://www.raphaelbischof.fr/messaging/?function=sendmessage");
        sendMessageRequest.setOnWSRequestListener(this);
        sendMessageRequest.execute();
    }

    public void refreshMessages() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("accesstoken", myAccessToken);
        params.put("channelid", myChannelID);

        WSRequestHandler getChannelListRequest = new WSRequestHandler(params, "http://www.raphaelbischof.fr/messaging/?function=getmessages");
        getChannelListRequest.setOnWSRequestListener((onWSRequestListener) this);
        getChannelListRequest.execute();
    }
}
