package client.protector.hazard.hazardprotectorclient.GooglePlay;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Tautvilas on 13/03/2017.
 */

public class TokenRefreshListenerService extends InstanceIDListenerService
{
    @Override
    public void onTokenRefresh()
    {
        Intent intent = new Intent(this, RegistrationService.class);
        startService(intent);
    }
}