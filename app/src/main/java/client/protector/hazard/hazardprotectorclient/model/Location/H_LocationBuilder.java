package client.protector.hazard.hazardprotectorclient.model.Location;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;

/**
 * Created by Tautvilas on 09/03/2017.
 */

public class H_LocationBuilder
{
    public LocationSettingsRequest.Builder builder;

    public H_LocationBuilder(LocationRequest locationRequest)
    {
        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
    }
}
