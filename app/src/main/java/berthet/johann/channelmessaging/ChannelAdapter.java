package berthet.johann.channelmessaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import berthet.johann.channelmessaging.network.Channel;

/**
 * Created by Johann on 08/02/2016.
 */
public class ChannelAdapter extends ArrayAdapter {
    private final Context context;
    private final ArrayList<Channel> channels;
    private final ArrayList<Channel> channelArray;

    public ChannelAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.context = context;
        this.channels = (ArrayList<Channel>) objects;
        this.channelArray = (ArrayList<Channel>) channels.clone();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.channelrowlayout, parent, false);
        TextView channelTextView = (TextView) rowView.findViewById(R.id.textViewNameChannel);
        TextView nbUsersTextView = (TextView) rowView.findViewById(R.id.textViewNbUsers);
        //channelTextView.setText(channels.get(position).name);
        channelTextView.setText(channelArray.get(position).name);
        nbUsersTextView.setText("Nombre d'utilisateurs connectés : " + channelArray.get(position).connectedusers);
        return rowView;
    }

    @Override
    public long getItemId(int position) {
        return (long)channelArray.get(position).channelID;
    }

    @Override
    public int getCount() {
        return channelArray.size();
    }


    public void sort(String filter) {
        channelArray.clear();
        for (Channel channel : channels) {
            if (channel.name.toLowerCase().contains(filter.toLowerCase())) {
                channelArray.add(channel);
            }
        }
        notifyDataSetChanged();
    }
}

