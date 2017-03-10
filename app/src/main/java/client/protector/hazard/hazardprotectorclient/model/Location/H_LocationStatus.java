package client.protector.hazard.hazardprotectorclient.model.Location;

import java.util.Observable;

/**
 * Created by Tautvilas on 09/03/2017.
 */

public class H_LocationStatus extends Observable
{
    public boolean canGetLocation = false;

    public H_LocationStatus()
    {

    }

    public void setCanGetLocation(boolean value)
    {
        canGetLocation = value;
        setChanged();
        notifyObservers();
    }
}
