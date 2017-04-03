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
    private User user;
    public SaveUser(User user)
    {
        this.user = user;
    }

    @Override
    public DbResponse call()
    {

        DbResponse dbResponse = new DbResponse();
        JSONObject response = null;
        Document doc = null;
        try
        {
            String hazardArticles = user.getHazardArticlesList().toString();
            hazardArticles = hazardArticles.replaceAll("\\]","");
            hazardArticles = hazardArticles.replaceAll("\\[","");

            doc = connect("http://www.odontologijos-erdve.lt/hazardprotector/saveUser.php")
                    .data("firstname", user.getFirstname())
                    .data("surname", user.getSurname())
                    .data("gcm_id", user.getGcm_id())
                    .data("latitude", Double.toString(user.getLatitude()))
                    .data("longitude", Double.toString(user.getLongitude()))
                    .data("terror", user.getTerror())
                    .data("flood", user.getFlood())
                    .data("war", user.getWar())
                    .data("earthquake", user.getEarthquake())
                    .data("political", user.getPolitical())
                    .data("criminal", user.getCriminal())
                    .data("colourCode", String.valueOf(user.getColourCode()))
                    .data("hazardArticles", hazardArticles)
                    .data("registrationId", String.valueOf(user.getRegistrationId()))
                    .userAgent("Mozilla")
                    .timeout(10000)
                    .post();

            response = new JSONObject(doc.body().text());
            System.out.println("response save: "+dbResponse.toString());
            dbResponse.setMsg(response.getString("msg"));
            dbResponse.setStatus(response.getInt("status"));
        }
        catch (IOException e)
        {
            Log.d("log","Save user exception");
            e.printStackTrace();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return dbResponse;
    }
}
