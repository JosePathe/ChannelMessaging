package berthet.johann.channelmessaging;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

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
        View rowView = inflater.inflate(R.layout.messagerowlayout, parent, false);
        TextView messageTextView = (TextView) rowView.findViewById(R.id.textViewUsername);
        TextView authorTextView = (TextView) rowView.findViewById(R.id.textViewMessage);
        ImageView userImageView = (ImageView) rowView.findViewById(R.id.userImageView);

        messageTextView.setText(messages.get(position).message);
        authorTextView.setText(messages.get(position).date);

        Picasso.with(context).load(messages.get(position).imageUrl).transform(new CircleImageTransformation()).into(userImageView);

        return rowView;
    }

    private class CircleImageTransformation implements Transformation {
        @Override public Bitmap transform(Bitmap source) {
            Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, source.getWidth(), source.getHeight());
            final RectF rectF = new RectF(rect);
            final float roundPx = 75;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(source, rect, rect, paint);

            source.recycle();

            return output;
        }

        @Override public String key() { return "rounded()"; }
    }

}
