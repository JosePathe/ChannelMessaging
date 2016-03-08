package berthet.johann.channelmessaging;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import berthet.johann.channelmessaging.fragment.ChannelListFragment;
import berthet.johann.channelmessaging.fragment.MessageListFragment;

/**
 * Created by Johann on 08/02/2016.
 */
public class ChannelListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {
    private MenuItem searchMenuItem;

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);

        searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchViewAction = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchViewAction.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String search) {
        ChannelListFragment listChannel = (ChannelListFragment) getSupportFragmentManager().findFragmentById(R.id.channelListFragment);
        MenuItemCompat.collapseActionView(searchMenuItem);
        listChannel.search(search);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String search) {
        ChannelListFragment listChannel = (ChannelListFragment) getSupportFragmentManager().findFragmentById(R.id.channelListFragment);
        listChannel.search(search);

        return false;
    }
}
