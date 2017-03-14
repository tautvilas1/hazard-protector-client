package client.protector.hazard.hazardprotectorclient.GooglePlay;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by Tautvilas on 13/03/2017.
 */

public class NotificationsListenerService extends GcmListenerService
{
    @Override
    public void onMessageReceived(String s, Bundle bundle)
    {
        super.onMessageReceived(s, bundle);
        Log.d("log","message received "+s);
    }
}
