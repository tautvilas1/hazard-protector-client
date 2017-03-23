package client.protector.hazard.hazardprotectorclient.model.User;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.Callable;

import client.protector.hazard.hazardprotectorclient.model.Common.DbResponse;

import static org.jsoup.Jsoup.connect;

/**
 * Created by Tautvilas on 26/02/2017.
 */

public class RegisterUser implements Callable<DbResponse>
{
    private String firstname, surname, gcmId, terror, flood, war, earthquake, political, criminal, registrationId;
    private int colourCode;

    public RegisterUser(String firstname, String surname, String gcmId, String terror, String flood,
       String war, String earthquake, String political, String criminal ,int colourCode, String registrationId)
    {
        this.firstname = firstname;
        this.surname = surname;
        this.gcmId = gcmId;
        this.terror = terror;
        this.flood = flood;
        this.war = war;
        this.earthquake = earthquake;
        this.political = political;
        this.criminal = criminal;
        this.colourCode = colourCode;
        this.registrationId = registrationId;
    }
    @Override
    public DbResponse call() {
        Document doc = null;
        DbResponse dbResponse = new DbResponse();
        dbResponse.setStatus(400);
        dbResponse.setMsg("JSON failed");
        try {
            doc = connect("http://www.odontologijos-erdve.lt/hazardprotector/registerUser.php")
                    .data("firstname", firstname)
                    .data("surname", surname)
                    .data("gcmId", gcmId)
                    .data("terror", terror)
                    .data("flood", flood)
                    .data("war", war)
                    .data("earthquake", earthquake)
                    .data("political", political)
                    .data("criminal", criminal)
                    .data("colourCode", String.valueOf(colourCode))
                    .data("registrationId", String.valueOf(registrationId))
                    .userAgent("Mozilla")
                    .timeout(10000)
                    .post();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            JSONObject response = new JSONObject(doc.body().text());
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
