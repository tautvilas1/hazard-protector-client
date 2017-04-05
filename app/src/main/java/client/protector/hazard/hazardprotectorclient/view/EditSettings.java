package client.protector.hazard.hazardprotectorclient.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import client.protector.hazard.hazardprotectorclient.R;
import client.protector.hazard.hazardprotectorclient.controller.Search.Core.App;
import client.protector.hazard.hazardprotectorclient.model.User.User;

public class EditSettings extends AppCompatActivity
{

    private User user = App.user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTheme(App.theme);
        setContentView(R.layout.activity_edit_settings);
    }
}
