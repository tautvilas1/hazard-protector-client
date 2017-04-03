package client.protector.hazard.hazardprotectorclient.controller.Search.User;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import client.protector.hazard.hazardprotectorclient.R;

/**
 * Created by Tautvilas on 02/04/2017.
 */

public class DetailsValidator
{
    private Context context;
    public DetailsValidator(Context context)
    {
        this.context = context;
    }
    public boolean validateForm(TextView txtFirstname, TextView txtSurname)
    {
        String firstname = String.valueOf(txtFirstname.getText());
        String surname = String.valueOf(txtSurname.getText());
        boolean result = false;
        if(!firstname.equals("") && !surname.equals(""))
        {
            result = true;
        }
        else
        {
            if(firstname.equals(""))
            {
                txtFirstname.setTextColor(Color.RED);
                Toast toast = Toast.makeText(context, R.string.firstname_missing, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            if(surname.equals(""))
            {
                txtSurname.setTextColor(Color.RED);
                Toast toast = Toast.makeText(context,R.string.surname_missing, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
        return result;
    }
}
