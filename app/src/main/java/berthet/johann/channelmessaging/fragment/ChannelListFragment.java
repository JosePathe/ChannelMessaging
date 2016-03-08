package berthet.johann.channelmessaging.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

import berthet.johann.channelmessaging.ChannelAdapter;
import berthet.johann.channelmessaging.ChannelListActivity;
import berthet.johann.channelmessaging.R;
import berthet.johann.channelmessaging.network.ChannelList;
import berthet.johann.channelmessaging.network.WSRequestHandler;
import berthet.johann.channelmessaging.network.onWSRequestListener;

/**
 * Created by Johann on 01/03/2016.
 */
public class ChannelListFragment extends Fragment implements onWSRequestListener {
    private static final String PREFS_ACCESS_TOKEN = "MyAccessToken";
    private ListView lvFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_listchannel,container);
        lvFragment = (ListView)v.findViewById(R.id.lvFragment_channel);
        lvFragment.setOnItemClickListener((ChannelListActivity)getActivity());

        SharedPreferences settings = getContext().getSharedPreferences(PREFS_ACCESS_TOKEN, 0);
        String myAccessToken = settings.getString("accesstoken", null);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("accesstoken", myAccessToken);

        WSRequestHandler getChannelListRequest = new WSRequestHandler(params, "http://www.raphaelbischof.fr/messaging/?function=getchannels", 0);
        getChannelListRequest.setOnWSRequestListener((onWSRequestListener) this);
        getChannelListRequest.execute();

        return v;
    }

    @Override
    public void onWSRequestCompleted(int requestCode, String response) {
        Gson gson = new Gson();
        ChannelList myList = new ChannelList();
        myList.channels = gson.fromJson(response, ChannelList.class).channels;
        lvFragment.setAdapter(new ChannelAdapter(getContext(), android.R.layout.simple_list_item_1, myList.channels));
    }

    @Override
    public void onWSRequestError(int requestCode, String exception) {
        Toast.makeText(getContext(), exception, Toast.LENGTH_SHORT).show();
    }

    public void search(String filter){
        ((ChannelAdapter) lvFragment.getAdapter()).sort(filter);
    }

}
