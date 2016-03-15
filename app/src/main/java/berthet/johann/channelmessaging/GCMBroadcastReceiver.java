package berthet.johann.channelmessaging;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Collection;

public class GCMBroadcastReceiver extends BroadcastReceiver {
    private static final int MESSAGE_NOTIFICATION = 1;

    public GCMBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Collection<String> key = new ArrayList<String>() {
            {
                add("message");
                add("channelid");
                add("fromUser");
            }
        };


        Collection<String> receivedKey = intent.getExtras().keySet();

        if (receivedKey.containsAll(key)){
            showNotification(context, intent.getStringExtra("message"), intent.getStringExtra("channelid"), intent.getStringExtra("fromUser"));
        }
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    public void showNotification(Context context, String message, String channelID, String username) {
        Intent intentStreamingUI = new Intent(context, LoginActivity.class);

        //intentStreamingUI.setAction(MY_CUSTOM_ACTION);
        //intentStreamingUI.putExtra(EXTRA_MY_EXTRA,someExtraData);
        intentStreamingUI.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            intentStreamingUI.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intentStreamingUI,
                PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder noti = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setContentTitle("Message sent on channel "+channelID)
                .setContentText("Sent by "+username+" : "+message)
                .setVibrate(new long[]{0, 100, 100, 100, 100, 250, 200, 250, 100, 100, 100, 100, 100, 100})
                        //Set the color of the notification led (example on Nexus 5)
                .setLights(Color.parseColor("#006ab9"), 2000, 1000)
                .setSmallIcon(R.drawable.notif);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            noti.setVisibility(Notification.VISIBILITY_PUBLIC);
        }
        noti.setContentIntent(pendingIntent);
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        // Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("Message sent on channel " + channelID);
        // Moves events into the expanded layout
        inboxStyle.addLine("Message : "+message);
        inboxStyle.addLine("Author : "+username);
        // Moves the expanded layout object into the notification object.
        noti.setStyle(inboxStyle);
        Notification notification = noti.build();
        notification.flags|= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MESSAGE_NOTIFICATION, notification);
    }
}
