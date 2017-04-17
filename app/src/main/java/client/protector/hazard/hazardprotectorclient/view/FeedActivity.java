package client.protector.hazard.hazardprotectorclient.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import client.protector.hazard.hazardprotectorclient.GooglePlay.RegistrationService;
import client.protector.hazard.hazardprotectorclient.R;
import client.protector.hazard.hazardprotectorclient.controller.Search.Core.App;
import client.protector.hazard.hazardprotectorclient.controller.Search.Feed.ViewPagerAdapter;
import client.protector.hazard.hazardprotectorclient.model.Articles.Article;
import client.protector.hazard.hazardprotectorclient.model.Location.H_LocationBuilder;
import client.protector.hazard.hazardprotectorclient.model.Location.H_LocationRequest;
import client.protector.hazard.hazardprotectorclient.model.Location.H_LocationStatus;
import client.protector.hazard.hazardprotectorclient.model.User.User;

public class FeedActivity extends AppCompatActivity implements HazardsFeed.OnFragmentInteractionListener,
        NewsFeed.OnFragmentInteractionListener, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,
        Observer
{
    private User user;
    private GoogleApiClient googleApiClient;

    private Location location;
    private LocationRequest locationRequest;
    private LocationSettingsRequest.Builder locationBuilder;
    private H_LocationStatus locationStatus = new H_LocationStatus();
    private LocationManager locationManager;
    private Criteria criteria;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String provider;

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTheme(App.theme);
        setContentView(R.layout.activity_feed);
        setupToolbar();
        startRegistrationService();
        user = App.user;
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        locationStatus.addObserver(this);
        buildLocationManager();
        buildLocationServices();
        promptLocationServices();
        App.getArticles(this,300,0);
        openTabFromIntent();
    }

    private void openTabFromIntent()
    {
        if(getIntent().getStringExtra("tab") != null)
        {
            String tab = getIntent().getStringExtra("tab");
            if(tab.equals("hazard"))
            {
                viewPager.setCurrentItem(1);

            }
            else if(tab.equals("regular"))
            {
                viewPager.setCurrentItem(0);
            }
        }
    }

    private void setupToolbar()
    {

        toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Hazard Protector");

        LayoutInflater inflator = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        View v = inflator.from(this).inflate(R.layout.settings, null);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.TOP | Gravity.RIGHT);
        getSupportActionBar().setCustomView(v,layoutParams);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new NewsFeed(), "News");
        adapter.addFragment(new HazardsFeed(), "Hazards");
        viewPager.setAdapter(adapter);
    }

    public void startRegistrationService()
    {
        Intent intent = new Intent(this,RegistrationService.class);
        startService(intent);
    }

    public void openSettings(View view)
    {
        Intent intent = new Intent(this,SettingsActivity.class);
        startActivity(intent);
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
        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(criteria,false);
    }

    @Override
    protected void onStart()
    {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
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
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, new LocationCallback()
            {
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
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
                            status.startResolutionForResult(FeedActivity.this, 0);
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
        if (resultCode == -1)
        {
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

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }


}
