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


import client.protector.hazard.hazardprotectorclient.model.Common.DbResponse;

import static org.jsoup.Jsoup.connect;


public class GetUser implements Callable<User>
{

    Context context;
    String gcmId;

    public GetUser(Context context,String gcmId)
    {
        this.context = context;
        this.gcmId = gcmId;
    }

    public User getData() throws JSONException {
        String result = null;
        try
        {

            Document doc = connect("http://t-simkus.com/final_project/getUser")
                    .data("gcmId", gcmId)
                    .userAgent("Mozilla")
                    .timeout(1800000)
                    .post();


            result = doc.body().text();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return processResponse(result);
    }


    public User processResponse(String response)
    {
        User user = null;
        JSONObject jsonObject = null;
        try
        {
            jsonObject = new JSONObject(response);
            DbResponse dbResponse = new DbResponse();
            dbResponse.setStatus(jsonObject.getInt("status"));
            dbResponse.setMsg(jsonObject.getString("msg"));

            if(dbResponse.getStatus() == 200)
            {
                user = new User();
                user.setFirstname(jsonObject.getString("firstname"));
                user.setSurname(jsonObject.getString("surname"));
                user.setGcm_id(jsonObject.getString("gcm_id"));
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
