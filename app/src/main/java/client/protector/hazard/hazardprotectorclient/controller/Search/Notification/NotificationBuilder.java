package client.protector.hazard.hazardprotectorclient.controller.Search.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import client.protector.hazard.hazardprotectorclient.R;
import client.protector.hazard.hazardprotectorclient.view.FeedActivity;

/**
 * Created by Tautvilas on 17/04/2017.
 */

public class NotificationBuilder
{

    public NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private Context context;
    public NotificationBuilder(Context context)
    {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        this.context = context;
        notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Hazard Protector")
                        .setContentText("A hazard in your area was detected")
                        .setOngoing(true)
                        .setSound(sound);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent feedActivity = new Intent(context, FeedActivity.class);
        feedActivity.putExtra("tab","hazard");
        PendingIntent contentIntent = PendingIntent.getActivity(context,0,feedActivity,PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);
    }

    public void sendNotification()
    {
        notificationManager.notify(001, notificationBuilder.build());
    }

}
