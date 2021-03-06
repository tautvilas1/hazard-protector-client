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

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import client.protector.hazard.hazardprotectorclient.controller.Search.Core.App;
import client.protector.hazard.hazardprotectorclient.controller.Search.Notification.NotificationBuilder;

import client.protector.hazard.hazardprotectorclient.controller.Search.User.GetUser;
import client.protector.hazard.hazardprotectorclient.model.User.User;

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

        String gcm_id = data.getString("message");
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future f = es.submit(new GetUser(gcm_id));
        try
        {
            User getUpdates = (User) f.get();
            App.user.setHazardArticlesList(getUpdates.getHazardArticlesList());
            NotificationBuilder notificationBuilder = new NotificationBuilder(this);
            notificationBuilder.sendNotification();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        Log.d("log", String.valueOf(data));

    }

}