package client.protector.hazard.hazardprotectorclient.model.Location;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;

/**
 * Created by Tautvilas on 09/03/2017.
 */

public class H_LocationRequest
{
    public LocationRequest locationRequest;

    public H_LocationRequest()
    {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(20000);
        locationRequest.setFastestInterval(19000);
    }

}
