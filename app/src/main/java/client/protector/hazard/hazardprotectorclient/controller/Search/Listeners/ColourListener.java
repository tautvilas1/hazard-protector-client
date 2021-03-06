package client.protector.hazard.hazardprotectorclient.controller.Search.Listeners;

/**
 * Created by Tautvilas on 06/03/2017.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.view.View;
import android.widget.ImageButton;

import client.protector.hazard.hazardprotectorclient.R;
import client.protector.hazard.hazardprotectorclient.controller.Search.Core.App;
import client.protector.hazard.hazardprotectorclient.model.User.User;
import client.protector.hazard.hazardprotectorclient.view.SettingsActivity;


public class ColourListener implements View.OnClickListener
{
    public User user;
    public Context context;
    ImageButton red, orange, blue , green;

    public ColourListener(Context context, User user, ImageButton red, ImageButton orange, ImageButton blue, ImageButton green)
    {
        this.user = user;
        this.context = context;
        this.red = red;
        this.orange = orange;
        this.blue = blue;
        this.green = green;
    }

    @Override
    public void onClick(View v)
    {
        //Reset all button colours
        red.setBackgroundColor(Color.TRANSPARENT);
        orange.setBackgroundColor(Color.TRANSPARENT);
        blue.setBackgroundColor(Color.TRANSPARENT);
        green.setBackgroundColor(Color.TRANSPARENT);

        ImageButton btnColour = (ImageButton) v;
        String colourSelected = context.getResources().getResourceEntryName(v.getId());

        switch (colourSelected)
        {
            case "red" : user.setColourCode(1);
                App.theme = R.style.RedTheme;
                break;
            case "orange" : user.setColourCode(2);
                App.theme = R.style.OrangeTheme;
                break;
            case "blue" : user.setColourCode(3);
                App.theme = R.style.LightTest;
                break;
            case "green" : user.setColourCode(4);
                App.theme = R.style.GreenTheme;
                break;
            case "editRed" : user.setColourCode(1);
                App.theme = R.style.RedTheme;
                break;
            case "editOrange" : user.setColourCode(2);
                App.theme = R.style.OrangeTheme;
                break;
            case "editBlue" : user.setColourCode(3);
                App.theme = R.style.LightTest;
                break;
            case "editGreen" : user.setColourCode(4);
                App.theme = R.style.GreenTheme;
                break;
            default: user.setColourCode(0);
                break;
        }
        ((Activity)(context)).finish();
        context.startActivity(new Intent(context, SettingsActivity.class));
        btnColour.setBackgroundColor(Color.RED);
    }
}
