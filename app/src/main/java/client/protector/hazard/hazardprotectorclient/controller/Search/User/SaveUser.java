package client.protector.hazard.hazardprotectorclient.controller.Search.User;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.Callable;

import client.protector.hazard.hazardprotectorclient.model.Common.DbResponse;
import client.protector.hazard.hazardprotectorclient.model.User.User;

import static org.jsoup.Jsoup.connect;

/**
 * Created by Tautvilas on 05/03/2017.
 */

public class SaveUser implements Callable<DbResponse>
{
    User user;
    public SaveUser(User user)
    {
        this.user = user;
    }

    @Override
    public DbResponse call()
    {

        Document doc = null;
        try
        {
            doc = connect("http://www.t-simkus.com/final_project/saveUser.php")
                    .data("firstname", user.firstname)
                    .data("surname", user.surname)
                    .data("gcm_id", user.getGcm_id())
                    .data("latitude", Double.toString(user.getLatitude()))
                    .data("longitude", Double.toString(user.getLongitude()))
                    .data("terror", user.getTerror())
                    .data("flood", user.getFlood())
                    .data("war", user.getWar())
                    .data("earthquake", user.getEarthquake())
                    .data("colourCode", String.valueOf(user.getColourCode()))
                    .data("radius", String.valueOf(user.getRadius()))
                    .data("registrationId", String.valueOf(user.getRegistrationId()))
                    .userAgent("Mozilla")
                    .timeout(10000)
                    .post();
        }
        catch (IOException e)
        {
            Log.d("log","Save user exception");
            e.printStackTrace();
        }
        DbResponse dbResponse = new DbResponse();
        JSONObject response = null;
        try
        {
            response = new JSONObject(doc.body().text());
            dbResponse.setMsg(response.getString("msg"));
            dbResponse.setStatus(response.getInt("status"));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return dbResponse;
    }
}
