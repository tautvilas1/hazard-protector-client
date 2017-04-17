package client.protector.hazard.hazardprotectorclient.view;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.iid.InstanceID;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import client.protector.hazard.hazardprotectorclient.R;
import client.protector.hazard.hazardprotectorclient.controller.Search.Common.GoToMain;
import client.protector.hazard.hazardprotectorclient.controller.Search.Core.App;
import client.protector.hazard.hazardprotectorclient.controller.Search.User.GetUser;
import client.protector.hazard.hazardprotectorclient.model.User.User;

public class StartingActivity extends AppCompatActivity
{
    private ProgressBar loader;
    private Button btnRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTheme(App.theme);
        setContentView(R.layout.activity_starting);
        getWidgets();
        getUser();
    }

    private void getWidgets()
    {
        loader = (ProgressBar) findViewById(R.id.loaderRetry);
        btnRetry = (Button) findViewById(R.id.btnConnect);
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void getUser()
    {
        if(isNetworkAvailable())
        {
            String gcmId = InstanceID.getInstance(this).getId();
            ExecutorService es = Executors.newSingleThreadExecutor();
            Future f = es.submit(new GetUser(gcmId));
            User user = null;
            try
            {
                user = (User) f.get();
                if(user.getFirstname() == "")
                {
                    Intent register = new Intent(this,RegisterActivity.class);
                    startActivity(register);
                }
                else
                {
                    App.setUser(user);
                    GoToMain goToMain = new GoToMain(this);
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            catch (ExecutionException e)
            {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        getUser();
    }

    public void clickRetry(View view)
    {

        getUser();
        new CountDownTimer(2000, 1000)
        {
            public void onFinish()
            {
                loader.setVisibility(View.INVISIBLE);
                btnRetry.setVisibility(View.VISIBLE);
            }

            public void onTick(long millisUntilFinished)
            {
                btnRetry.setVisibility(View.INVISIBLE);
                loader.setVisibility(View.VISIBLE);
            }
        }.start();

    }
}
