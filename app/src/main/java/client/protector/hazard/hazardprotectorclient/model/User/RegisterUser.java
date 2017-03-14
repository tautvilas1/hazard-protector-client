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
    private String firstname, surname, gcmId, terror, flood, war, earthquake, registrationId;
    private int radius, colourCode;
    private JSONObject preferences;

    public RegisterUser(String firstname, String surname, String gcmId, String terror, String flood,
       String war, String earthquake, int radius, int colourCode, String registrationId)
    {
        this.firstname = firstname;
        this.surname = surname;
        this.gcmId = gcmId;
        this.terror = terror;
        this.flood = flood;
        this.war = war;
        this.earthquake = earthquake;
        this.radius = radius;
        this.colourCode = colourCode;
        this.registrationId = registrationId;
    }
    @Override
    public DbResponse call() throws Exception {
        Document doc = connect("http://www.t-simkus.com/final_project/registerUser.php")
                .data("firstname", firstname)
                .data("surname", surname)
                .data("gcmId", gcmId)
                .data("terror", terror)
                .data("flood", flood)
                .data("war", war)
                .data("earthquake", earthquake)
                .data("radius", String.valueOf(radius))
                .data("colourCode", String.valueOf(colourCode))
                .data("registrationId", String.valueOf(registrationId))
                .userAgent("Mozilla")
                .post();

        JSONObject response = new JSONObject(doc.body().text());
        DbResponse dbResponse = new DbResponse();
        dbResponse.setMsg(response.getString("msg"));
        dbResponse.setStatus(response.getInt("status"));
        return dbResponse;
    }
}
