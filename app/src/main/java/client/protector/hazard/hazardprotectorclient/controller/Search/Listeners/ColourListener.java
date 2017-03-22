package client.protector.hazard.hazardprotectorclient.controller.Search.Listeners;

/**
 * Created by Tautvilas on 06/03/2017.
 */
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.View;
import android.widget.ImageButton;

import client.protector.hazard.hazardprotectorclient.R;
import client.protector.hazard.hazardprotectorclient.model.User.User;


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
                break;
            case "orange" : user.setColourCode(2);
                break;
            case "blue" : user.setColourCode(3);
                break;
            case "green" : user.setColourCode(4);
                break;
            default: user.setColourCode(0);
                break;
        }

        btnColour.setBackgroundColor(Color.RED);
    }
}
