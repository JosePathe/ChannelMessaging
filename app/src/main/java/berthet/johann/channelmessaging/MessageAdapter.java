package berthet.johann.channelmessaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import berthet.johann.channelmessaging.network.Channel;
import berthet.johann.channelmessaging.network.Message;

/**
 * Created by Johann on 29/02/2016.
 */
public class MessageAdapter extends ArrayAdapter {
    private final Context context;
    private final List<Message> messages;

    public MessageAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.context = context;
        this.messages = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        TextView messageTextView = (TextView) rowView.findViewById(R.id.textViewNameChannel);
        TextView authorTextView = (TextView) rowView.findViewById(R.id.textViewNbUsers);
        messageTextView.setText(messages.get(position).message);
        authorTextView.setText(messages.get(position).date);
        return rowView;
    }

}
