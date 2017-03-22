package client.protector.hazard.hazardprotectorclient.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import client.protector.hazard.hazardprotectorclient.GooglePlay.RegistrationService;
import client.protector.hazard.hazardprotectorclient.R;
import client.protector.hazard.hazardprotectorclient.controller.Search.Common.GoToMain;
import client.protector.hazard.hazardprotectorclient.controller.Search.Core.App;
import client.protector.hazard.hazardprotectorclient.model.User.GetUser;
import client.protector.hazard.hazardprotectorclient.model.User.User;

public class StartingActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        getUser();
    }


    public void getUser()
    {
        String gcmId = InstanceID.getInstance(this).getId();
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future f = es.submit(new GetUser(this,gcmId));
        User user = null;
        try
        {
            user = (User) f.get();
            if(user.getFirstname() == "")
            {
                Intent register = new Intent(this,RegisterActivity.class);
                startActivity(register);
            }
            else
            {
                App.setUser(user);
                GoToMain goToMain = new GoToMain(this);
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        getUser();
    }

    public void clickRetry(View view)
    {
        getUser();
    }
}
