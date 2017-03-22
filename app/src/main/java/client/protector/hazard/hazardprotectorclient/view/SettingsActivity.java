package client.protector.hazard.hazardprotectorclient.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import client.protector.hazard.hazardprotectorclient.R;
import client.protector.hazard.hazardprotectorclient.controller.Search.Core.App;
import client.protector.hazard.hazardprotectorclient.model.User.User;

public class SettingsActivity extends AppCompatActivity
{
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        user = App.user;
        setupActionBar();
    }
    private void setupActionBar()
    {

    }
}
