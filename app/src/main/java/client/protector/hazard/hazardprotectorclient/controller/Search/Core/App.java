package client.protector.hazard.hazardprotectorclient.controller.Search.Core;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Observer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import client.protector.hazard.hazardprotectorclient.R;
import client.protector.hazard.hazardprotectorclient.model.Articles.Article;
import client.protector.hazard.hazardprotectorclient.model.Articles.TableArticle;
import client.protector.hazard.hazardprotectorclient.model.User.User;

/**
 * Created by Tautvilas on 05/03/2017.
 */
public class App
{

    public static User user;
    public static ArrayList<Article> articlesList = new ArrayList<Article>();
    public static void setUser(User u)
    {
        user = u;
    }
    public static int theme = R.style.LightTest;

    public static ArrayList<Article> getArticles(Context context, int limit, int offset)
    {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future f = es.submit(new TableArticle(context,limit,offset));
        try
        {
            articlesList = (ArrayList<Article>) f.get();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        return articlesList;
    }

}