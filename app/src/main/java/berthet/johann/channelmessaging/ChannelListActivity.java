package berthet.johann.channelmessaging;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

import berthet.johann.channelmessaging.fragment.ChannelListFragment;
import berthet.johann.channelmessaging.network.ChannelList;
import berthet.johann.channelmessaging.network.WSRequestHandler;
import berthet.johann.channelmessaging.network.onWSRequestListener;

/**
 * Created by Johann on 08/02/2016.
 */
public class ChannelListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String PREFS_ACCESS_TOKEN = "MyAccessToken";
    //private ListView myListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_channel);
        //myListView = (ListView)findViewById(R.id.channelListView);
        //myListView.setOnItemClickListener(this);
        /*
        SharedPreferences settings = getSharedPreferences(PREFS_ACCESS_TOKEN, 0);
        String myAccessToken = settings.getString("accesstoken", null);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("accesstoken", myAccessToken);

        WSRequestHandler getChannelListRequest = new WSRequestHandler(params, "http://www.raphaelbischof.fr/messaging/?function=getchannels", 0);
        getChannelListRequest.setOnWSRequestListener((onWSRequestListener) this);
        getChannelListRequest.execute();*/
    }
    /*
    @Override
    public void onWSRequestCompleted(int requestCode, String response) {
        Gson gson = new Gson();
        ChannelList myList = new ChannelList();
        myList.channels = gson.fromJson(response, ChannelList.class).channels;
        //myListView.setAdapter(new ChannelAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, myList.channels));
    }

    @Override
    public void onWSRequestError(int requestCode, String exception) {
        Toast.makeText(getApplicationContext(), exception, Toast.LENGTH_SHORT).show();
    }*/

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //long channelID = myListView.getAdapter().getItemId(position);
        Fragment listChannel = (ChannelListFragment) getSupportFragmentManager().findFragmentById(R.id.channelListFragment);
        if (listChannel.isInLayout()) {
            long channelID = id;
            Intent myIntent = new Intent(ChannelListActivity.this, ChannelActivity.class);
            myIntent.putExtra("channelID", channelID); //Optional parameters
            startActivity(myIntent);
        }

    }
}
