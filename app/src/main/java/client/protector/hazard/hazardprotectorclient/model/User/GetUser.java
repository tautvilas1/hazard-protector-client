package client.protector.hazard.hazardprotectorclient.model.User;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Created by Tautvilas Simkus on 11/02/2017.
 */


import client.protector.hazard.hazardprotectorclient.controller.Search.Core.App;
import client.protector.hazard.hazardprotectorclient.model.Common.DbResponse;

import static org.jsoup.Jsoup.connect;


public class GetUser implements Callable<User>
{

    private Context context;
    private String gcmId;

    public GetUser(Context context,String gcmId)
    {
        this.context = context;
        this.gcmId = gcmId;
    }

    public User getData() throws JSONException {
        String result = null;
        try
        {

            Document doc = connect("http://www.odontologijos-erdve.lt/hazardprotector/getUser.php")
                    .data("gcmId", gcmId)
                    .userAgent("Mozilla")
                    .timeout(20000)
                    .post();


            result = doc.body().text();
        }
        catch (IOException e)
        {
            Log.d("log","connection timed out to t-simkus.com");
            e.printStackTrace();
        }

        return processResponse(result);
    }


    public User processResponse(String response)
    {
        User user = new User();
        JSONObject jsonObject = null;
        try
        {
            jsonObject = new JSONObject(response);
            DbResponse dbResponse = new DbResponse();
            dbResponse.setStatus(jsonObject.getInt("status"));
            dbResponse.setMsg(jsonObject.getString("msg"));

            if(dbResponse.getStatus() == 200)
            {
                user.setFirstname(jsonObject.getString("firstname"));
                user.setSurname(jsonObject.getString("surname"));
                user.setGcm_id(jsonObject.getString("gcm_id"));
                user.setTerror(jsonObject.getString("terror"));
                user.setFlood(jsonObject.getString("flood"));
                user.setWar(jsonObject.getString("war"));
                user.setEarthquake(jsonObject.getString("earthquake"));
                user.setColourCode(Integer.parseInt(jsonObject.getString("colourCode")));
                user.setRadius(Integer.parseInt(jsonObject.getString("radius")));
                user.setRegistrationId(jsonObject.getString("registrationId"));
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


        return user;
    }

    @Override
    public User call() throws Exception
    {
        return getData();
    }
}
