package client.protector.hazard.hazardprotectorclient.controller.Search.Notification;

import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import client.protector.hazard.hazardprotectorclient.R;

/**
 * Created by Tautvilas on 14/03/2017.
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
                        .setSound(sound);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    }

    public void sendNotification()
    {
        notificationManager.notify(001, notificationBuilder.build());
    }

}
