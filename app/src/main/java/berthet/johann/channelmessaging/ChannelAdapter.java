package berthet.johann.channelmessaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import berthet.johann.channelmessaging.network.Channel;

/**
 * Created by Johann on 08/02/2016.
 */
public class ChannelAdapter extends ArrayAdapter {
    private final Context context;
    private final List<Channel> channels;

    public ChannelAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.context = context;
        this.channels = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        TextView channelTextView = (TextView) rowView.findViewById(R.id.textViewNameChannel);
        TextView nbUsersTextView = (TextView) rowView.findViewById(R.id.textViewNbUsers);
        channelTextView.setText(channels.get(position).name);
        nbUsersTextView.setText("Nombre d'utilisateurs connectés : " + channels.get(position).connectedusers);
        return rowView;
    }
}

