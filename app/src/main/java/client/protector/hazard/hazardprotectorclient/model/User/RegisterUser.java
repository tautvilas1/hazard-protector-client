package client.protector.hazard.hazardprotectorclient.model.User;

import org.json.JSONObject;
import org.jsoup.nodes.Document;

import java.util.concurrent.Callable;

import client.protector.hazard.hazardprotectorclient.model.Common.DbResponse;

import static org.jsoup.Jsoup.connect;

/**
 * Created by Tautvilas on 26/02/2017.
 */

public class RegisterUser implements Callable<DbResponse>
{
    private String firstname, surname, gcmId;
    double latitude = 0, longitude = 0;

    public RegisterUser(String firstname, String surname, String gcmId, double latitude, double longitude)
    {
        this.firstname = firstname;
        this.surname = surname;
        this.gcmId = gcmId;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    @Override
    public DbResponse call() throws Exception {
        Document doc = connect("http://t-simkus.com/final_project/registerUser.php")
                .data("firstname", firstname)
                .data("surname", surname)
                .data("gcmId", gcmId)
                .userAgent("Mozilla")
                .post();

        JSONObject response = new JSONObject(doc.body().text());
        DbResponse dbResponse = new DbResponse();
        dbResponse.setMsg(response.getString("msg"));
        dbResponse.setStatus(response.getInt("status"));
        System.out.println(dbResponse.toString());
        return dbResponse;
    }
}
