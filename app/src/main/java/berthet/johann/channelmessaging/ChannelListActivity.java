package berthet.johann.channelmessaging;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import berthet.johann.channelmessaging.network.Channel;
import berthet.johann.channelmessaging.network.ChannelList;
import berthet.johann.channelmessaging.network.WSRequestHandler;
import berthet.johann.channelmessaging.network.onWSRequestListener;

/**
 * Created by Johann on 08/02/2016.
 */
public class ChannelListActivity extends AppCompatActivity implements onWSRequestListener, AdapterView.OnItemClickListener {
    private static final String PREFS_ACCESS_TOKEN = "MyAccessToken";
    private ListView myListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_channel);
        myListView = (ListView)findViewById(R.id.channelListView);
        myListView.setOnItemClickListener(this);
        //Intent intent = getIntent();
        
        SharedPreferences settings = getSharedPreferences(PREFS_ACCESS_TOKEN, 0);
        String myAccessToken = settings.getString("accesstoken", null);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("accesstoken", myAccessToken);

        WSRequestHandler getChannelListRequest = new WSRequestHandler(params, "http://www.raphaelbischof.fr/messaging/?function=getchannels");
        getChannelListRequest.setOnWSRequestListener((onWSRequestListener) this);
        getChannelListRequest.execute();
    }

    @Override
    public void onWSRequestCompleted(String response) {
        Gson gson = new Gson();
        ChannelList myList = new ChannelList();
        myList.channels = gson.fromJson(response, ChannelList.class).channels;
        myListView.setAdapter(new ChannelAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, myList.channels));
    }

    @Override
    public void onWSRequestError(String exception) {
        Toast.makeText(getApplicationContext(), exception, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long channelID = myListView.getAdapter().getItemId(position);
        Intent myIntent = new Intent(ChannelListActivity.this, ChannelActivity.class);
        myIntent.putExtra("channelID", channelID); //Optional parameters
        startActivity(myIntent);
    }
}
