package client.protector.hazard.hazardprotectorclient.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import client.protector.hazard.hazardprotectorclient.R;
import client.protector.hazard.hazardprotectorclient.model.User.GetUser;
import client.protector.hazard.hazardprotectorclient.model.User.User;

public class StartingActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener

{
    public String gcmId;
    GoogleApiClient googleApiClient;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        gcmId = InstanceID.getInstance(this).getId();
        getUser();
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            System.out.println("Location permission not granted");
            return;
        }
        else
        {
            System.out.println("Location granted");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
                goToMain(user);
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

    public void goToMain(User user)
    {
        Intent intent = new Intent(this, MainActivity.class);
        System.out.println("In the main: "+user.toString());
        intent.putExtra("user",user);
        startActivity(intent);
    }

}
