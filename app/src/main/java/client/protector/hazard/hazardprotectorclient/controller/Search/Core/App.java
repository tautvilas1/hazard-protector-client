package client.protector.hazard.hazardprotectorclient.controller.Search.Core;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import client.protector.hazard.hazardprotectorclient.R;

/**
 * Created by Tautvilas on 05/03/2017.
 */

public class App extends Application implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public GoogleApiClient googleApiClient;

    public GoogleApiClient setGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        return googleApiClient;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        Toast.makeText(this, R.string.connected_google_api,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Toast.makeText(this, R.string.connection_suspended,Toast.LENGTH_SHORT).show();
        System.out.println("Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Toast.makeText(this, R.string.connection_failed,Toast.LENGTH_SHORT).show();
        System.out.println("Connection failed " + connectionResult.toString());
    }

}
