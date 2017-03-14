package client.protector.hazard.hazardprotectorclient.GooglePlay;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import client.protector.hazard.hazardprotectorclient.R;
import client.protector.hazard.hazardprotectorclient.controller.Search.Core.App;
import client.protector.hazard.hazardprotectorclient.model.User.User;

/**
 * Created by Tautvilas on 13/03/2017.
 */

public class RegistrationService extends IntentService
{
    public RegistrationService()
    {
        super("Registration service");

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        InstanceID myID = InstanceID.getInstance(this);
        try
        {
            String registrationToken = myID.getToken(
                    getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                    null
            );
            App.user.setRegistrationId(registrationToken);
            App.user.save();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
