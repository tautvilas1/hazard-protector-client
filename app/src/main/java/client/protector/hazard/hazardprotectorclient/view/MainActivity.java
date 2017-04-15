package client.protector.hazard.hazardprotectorclient.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import client.protector.hazard.hazardprotectorclient.GooglePlay.RegistrationService;
import client.protector.hazard.hazardprotectorclient.R;
import client.protector.hazard.hazardprotectorclient.controller.Search.Article.ArticleSort;
import client.protector.hazard.hazardprotectorclient.controller.Search.Core.App;
import client.protector.hazard.hazardprotectorclient.controller.Search.Feed.LoadFeed;
import client.protector.hazard.hazardprotectorclient.controller.Search.Image.LoadImage;
import client.protector.hazard.hazardprotectorclient.controller.Search.Search.Finder;
import client.protector.hazard.hazardprotectorclient.model.Articles.Article;
import client.protector.hazard.hazardprotectorclient.model.Articles.TableArticle;
import client.protector.hazard.hazardprotectorclient.model.Location.H_LocationBuilder;
import client.protector.hazard.hazardprotectorclient.model.Location.H_LocationRequest;
import client.protector.hazard.hazardprotectorclient.model.Location.H_LocationStatus;
import client.protector.hazard.hazardprotectorclient.model.User.User;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, Observer
{

    public Context context = this;

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private ArrayList<Article> articlesList = new ArrayList<Article>();
    private User user;
    private GoogleApiClient googleApiClient;

    private Location location;
    private LocationRequest locationRequest;
    private LocationSettingsRequest.Builder locationBuilder;
    private H_LocationStatus locationStatus = new H_LocationStatus();
    private LocationManager locationManager;
    private Criteria criteria;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startRegistrationService();
        user = App.user;
        getSupportActionBar().setTitle("Welcome, " + user.getFirstname());
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        loadFeed();
        locationStatus.addObserver(this);
        buildLocationManager();
        buildLocationServices();
        promptLocationServices();
    }

    public void startRegistrationService()
    {
        Intent intent = new Intent(this,RegistrationService.class);
        startService(intent);
    }

    public void buildLocationServices()
    {
        H_LocationRequest H_locationRequest = new H_LocationRequest();
        locationRequest = H_locationRequest.locationRequest;
        H_LocationBuilder H_locationBuilder = new H_LocationBuilder(locationRequest);
        locationBuilder = H_locationBuilder.builder;
    }

    public void buildLocationManager()
    {
        criteria = new Criteria();
        locationManager = (LocationManager) getSystemService(context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(criteria,false);
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
    public void onConnected(@Nullable Bundle bundle)
    {
        Toast.makeText(this, R.string.connected_google_api_main, Toast.LENGTH_SHORT).show();
        if(servicesAvailable())
        {
            getLocation();
        }
    }

    public void getLocation()
    {
        if (locationStatus.canGetLocation)
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                Log.d("log", "Something's wrong with permissions");
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult result)
                {
                    location = result.getLastLocation();
                    user.setLatitude(location.getLatitude());
                    user.setLongitude(location.getLongitude());
                    user.save();
                }

                @Override
                public void onLocationAvailability(LocationAvailability locationAvailability)
                {
                    Log.d("log","onLocationAvailability: isLocationAvailable =  " + locationAvailability.isLocationAvailable());
                }
            }, null);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    getLocation();

                }
                else {

                    Log.d("log","permission denied");
                }
                return;
            }
        }
    }

    public void promptLocationServices()
    {
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationBuilder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>()
        {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode())
                {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.d("log", "All location settings are satisfied.");
                        locationStatus.setCanGetLocation(true);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        locationStatus.setCanGetLocation(false);
                        Log.d("log", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        try
                        {
                            status.startResolutionForResult(MainActivity.this, 0);
                        }
                        catch (IntentSender.SendIntentException e)
                        {
                            Log.d("log", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        locationStatus.setCanGetLocation(false);
                        Log.d("log", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                    default:
                        Log.d("log","default");
                }
            }
        });
    }



    @Override
    public void onConnectionSuspended(int i)
    {
        System.out.println("Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        System.out.println("Connection failed " + connectionResult.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //result code -1 is ok
        if (resultCode == -1) {
            locationStatus.setCanGetLocation(true);
        }
    }


    @Override
    public void update(Observable o, Object arg)
    {
        if(locationStatus.canGetLocation && servicesAvailable())
        {
            getLocation();
        }
    }


    public void loadFeed()
    {
        getArticles();
        ArticleSort articleSort = new ArticleSort();
        articleSort.sortByDate(articlesList);
        Collections.reverse(articlesList);
        ListView articlesListView = (ListView) findViewById(R.id.articlesListView);
        if(articlesList.size() > 0)
        {
            LoadFeed loadFeed = new LoadFeed(this,articlesList,articlesListView);
            loadFeed.populateList();
        }
        else
        {
            TextView txtNewsFeed = (TextView) findViewById(R.id.txtNewsFeed);
            articlesListView.setVisibility(View.INVISIBLE);
            txtNewsFeed.setVisibility(View.VISIBLE);
            txtNewsFeed.setTextColor(Color.RED);
            txtNewsFeed.setText("No articles could be loaded");
        }
    }


    public void getArticles()
    {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future f = es.submit(new TableArticle(10,0));
        try
        {
            articlesList = (ArrayList<Article>) f.get();
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

    private boolean servicesAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (ConnectionResult.SUCCESS == resultCode)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
