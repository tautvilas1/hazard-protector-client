package client.protector.hazard.hazardprotectorclient.GooglePlay;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;

import client.protector.hazard.hazardprotectorclient.controller.Search.Notification.NotificationBuilder;

public class MyGcmListenerService extends GcmListenerService
{

    @Override
    public void onCreate()
    {
        super.onCreate();

    }

    @Override
    public void onMessageReceived(String from, Bundle data)
    {

        String message = data.getString("message");
        Log.d("log", String.valueOf(data));
        NotificationBuilder notificationBuilder = new NotificationBuilder(this);
        notificationBuilder.sendNotification();
    }

//    private void sendNotification(String message, int i)
//    {
//
//    }
}