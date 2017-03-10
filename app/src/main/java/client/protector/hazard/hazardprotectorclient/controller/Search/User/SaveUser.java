package client.protector.hazard.hazardprotectorclient.controller.Search.User;

import org.json.JSONObject;
import org.jsoup.nodes.Document;

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
    public DbResponse call() throws Exception
    {

        Document doc = connect("http://t-simkus.com/final_project/saveUser.php")
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
                .userAgent("Mozilla")
                .timeout(10000)
                .post();

        JSONObject response = new JSONObject(doc.body().text());
        DbResponse dbResponse = new DbResponse();
        dbResponse.setMsg(response.getString("msg"));
        dbResponse.setStatus(response.getInt("status"));
        return dbResponse;
    }
}
