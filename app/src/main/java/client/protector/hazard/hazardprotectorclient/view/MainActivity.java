package client.protector.hazard.hazardprotectorclient.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import client.protector.hazard.hazardprotectorclient.R;
import client.protector.hazard.hazardprotectorclient.controller.Search.Core.App;
import client.protector.hazard.hazardprotectorclient.controller.Search.Image.LoadImage;
import client.protector.hazard.hazardprotectorclient.controller.Search.Search.Finder;
import client.protector.hazard.hazardprotectorclient.model.Articles.Article;
import client.protector.hazard.hazardprotectorclient.model.Articles.TableArticle;
import client.protector.hazard.hazardprotectorclient.model.User.GetUser;
import client.protector.hazard.hazardprotectorclient.model.User.User;

public class MainActivity extends AppCompatActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private ArrayList<Article> articlesList = new ArrayList<Article>();
    private App app;
    private User user;
    private GoogleApiClient googleApiClient;
    public Context context = this;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private Location location;
    private boolean canGetLocation = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        user = (User) intent.getExtras().getSerializable("user");
        getSupportActionBar().setTitle("Hey, " + user.getFirstname());
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        promptLocationServices();
//        user.setFirstname("test");
//        user.setLatitude(4.0055);
//        user.setLongitude(4.9922);
//
//        user.save();

    }

    @Override
    public void onLocationChanged(Location location) {
        if (user != null) {
            user.setLatitude(location.getLatitude());
            user.setLongitude(location.getLongitude());
            System.out.println("latitude" + user.getLatitude());
        }
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
        Toast.makeText(this, R.string.connected_google_api_main, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("Connection failed " + connectionResult.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //result code -1 is ok
        if (resultCode == -1) {
            canGetLocation = true;
        }
        System.out.println(requestCode + " " + resultCode + " " + data);
    }

//    public void requestLocation() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
//                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            promptLocationServices();
//        } else {
//            canGetLocation = true;
//        }
//
//
//    }

    public void promptLocationServices() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 10000);
        locationRequest.setFastestInterval(5 * 10000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.d("log", "All location settings are satisfied.");
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                        {
                            return;
                        }
                        canGetLocation = true;
                        location = LocationServices.FusedLocationApi.getLastLocation(
                                googleApiClient);
                        Log.d("log","location got");
                        System.out.println(location.getLatitude()+" "+location.getLongitude());

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        canGetLocation = false;
                        Log.d("log", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        try
                        {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, 0);
                        }
                        catch (IntentSender.SendIntentException e)
                        {
                            Log.d("log", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        canGetLocation = false;
                        Log.d("log", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }


    public void loadFeed() throws IOException
    {
        getArticles();
        String keywords[] = {"war","terror" ,"flood","hazard","earthquake","disaster"};

        ExecutorService es = Executors.newSingleThreadExecutor();
        Future f = es.submit(new Finder(keywords,articlesList));

        ArrayList<Article> articlesListFiltered = new ArrayList<Article>();

        try
        {
            articlesListFiltered = (ArrayList<Article>) f.get();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        final TableLayout tableLayout = (TableLayout) findViewById(R.id.feedTable);

        if(articlesListFiltered.size() != 0) {

            for (int i = 0; i < 5; i++) {
                // Creation row
                final TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                Random r = new Random();

                ImageView image = new ImageView(this);
                image.setMinimumHeight(350);
                image.setMinimumWidth(450);
                image.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                new LoadImage(articlesListFiltered.get(i).getThumbnail(),image).execute();

                final TextView title = new TextView(this);
                title.setText(articlesListFiltered.get(i).getTitle());
                title.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                final TextView text = new TextView(this);
                text.setText(articlesListFiltered.get(i).getDescription());
                text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                tableRow.addView(image);
                tableRow.addView(title);
//                tableRow.addView(text);

                tableLayout.addView(tableRow);
            }
        }



    }


    public void getArticles() throws IOException
    {
//        TextView content = (TextView) findViewById(R.id.lblContent);
//        content.setMovementMethod(new ScrollingMovementMethod());
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future f = es.submit(new TableArticle(this));
        try {
            articlesList = (ArrayList<Article>) f.get();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
