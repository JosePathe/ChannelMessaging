package berthet.johann.channelmessaging;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;

import berthet.johann.channelmessaging.fragment.ChannelListFragment;
import berthet.johann.channelmessaging.fragment.MessageListFragment;

/**
 * Created by Johann on 08/02/2016.
 */
public class ChannelListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_channel);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ChannelListFragment listChannel = (ChannelListFragment) getSupportFragmentManager().findFragmentById(R.id.channelListFragment);
        MessageListFragment listMessage = (MessageListFragment) getSupportFragmentManager().findFragmentById(R.id.messageListFragment);
        if (listChannel.isInLayout() && listMessage==null) {
            long channelID = id;
            Intent myIntent = new Intent(ChannelListActivity.this, ChannelActivity.class);
            myIntent.putExtra("channelID", channelID); //Optional parameters
            startActivity(myIntent);
        }

        if (listMessage != null && listMessage.isInLayout()) {
            listMessage.refreshMessages(id);
        }

    }
}
