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
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import client.protector.hazard.hazardprotectorclient.R;
import client.protector.hazard.hazardprotectorclient.controller.Search.Common.GoToMain;
import client.protector.hazard.hazardprotectorclient.controller.Search.Core.App;
import client.protector.hazard.hazardprotectorclient.model.User.GetUser;
import client.protector.hazard.hazardprotectorclient.model.User.User;

public class StartingActivity extends AppCompatActivity
{
    private String gcmId;
    private GoogleApiClient googleApiClient;
    private Location location;
    private Activity activity;
    private LocationRequest locationRequest;
    private App app;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

//        googleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();

        gcmId = InstanceID.getInstance(this).getId();
        activity = this.getParent();
        getUser();

//        promptLocationServices();
    }

//    @Override
//    protected void onStart()
//    {
//        googleApiClient.connect();
//        super.onStart();
//    }
//
//    @Override
//    protected void onStop()
//    {
//        googleApiClient.disconnect();
//        super.onStop();
//    }


//    @Override
//    public void onConnected(@Nullable Bundle bundle)
//    {
//        Toast.makeText(this,R.string.connected_google_api,Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onConnectionSuspended(int i)
//    {
//        System.out.println("Connection suspended");
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
//    {
//        System.out.println("Connection failed " + connectionResult.toString());
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
//        System.out.println("activity result "+requestCode+" "+resultCode+" "+data.toString());
//        System.out.println("extras:"+data.getExtras().toString());
    }



    public void promptLocationServices()
    {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>()
        {
            @Override
            public void onResult(LocationSettingsResult result)
            {
                final Status status = result.getStatus();
                switch (status.getStatusCode())
                {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.d("log", "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.d("log", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try
                        {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(StartingActivity.this, REQUEST_CHECK_SETTINGS);
                        }
                        catch (IntentSender.SendIntentException e)
                        {
                            Log.d("log", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.d("log", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }


    /*CHECKS IF USER WITH THIS GCM
            ALREADY EXISTS, IF NO TAKES
            TO REGISTER ACTIVITY*/
    public void getUser()
    {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future f = es.submit(new GetUser(this,gcmId));
        User user = null;

        try
        {
            user = (User) f.get();
            if(user == null)
            {
                Intent register = new Intent(this,RegisterActivity.class);
                register.putExtra("gcmId",gcmId);
                startActivity(register);
            }
            else
            {
                GoToMain goToMain = new GoToMain(user,this);
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

}
