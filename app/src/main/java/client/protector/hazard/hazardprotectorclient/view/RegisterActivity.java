package client.protector.hazard.hazardprotectorclient.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.iid.InstanceID;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import client.protector.hazard.hazardprotectorclient.R;
import client.protector.hazard.hazardprotectorclient.controller.Search.Common.GoToMain;
import client.protector.hazard.hazardprotectorclient.controller.Search.Core.App;
import client.protector.hazard.hazardprotectorclient.controller.Search.Listeners.ColourListener;
import client.protector.hazard.hazardprotectorclient.controller.Search.User.DetailsValidator;
import client.protector.hazard.hazardprotectorclient.model.Common.DbResponse;
import client.protector.hazard.hazardprotectorclient.model.User.RegisterUser;
import client.protector.hazard.hazardprotectorclient.model.User.User;

public class RegisterActivity extends AppCompatActivity
{
    public String gcmId;
    private Button btnRegister;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTheme(App.theme);
        setContentView(R.layout.activity_register);
        user = new User();
        gcmId = InstanceID.getInstance(this).getId();
        btnRegister = (Button) findViewById(R.id.btnRegister);
        addListeners();
    }

    public void registerUser(View view)
    {
        findViewById(R.id.loadingRegister).setVisibility(View.VISIBLE);
        TextView txtFirstname = (TextView) findViewById(R.id.txtFirstname);
        TextView txtSurname = (TextView) findViewById(R.id.txtSurname);
        String firstname = txtFirstname.getText().toString();
        String surname = txtSurname.getText().toString();
        DetailsValidator validator = new DetailsValidator(this);
        boolean validForm = validator.validateForm(txtFirstname,txtSurname);
        if(validForm == false)
        {
            return;
        }

        CheckBox ckTerror = (CheckBox) findViewById(R.id.ckTerror);
        CheckBox ckWar = (CheckBox) findViewById(R.id.ckWar);
        CheckBox ckFlood = (CheckBox) findViewById(R.id.ckFlood);
        CheckBox ckEarthquake = (CheckBox) findViewById(R.id.ckEarthquake);
        CheckBox ckPolitical = (CheckBox) findViewById(R.id.ckPolitical);
        CheckBox ckCriminal = (CheckBox) findViewById(R.id.ckCriminal);

        String terror = String.valueOf(ckTerror.isChecked());
        String flood = String.valueOf(ckFlood.isChecked());
        String war = String.valueOf(ckWar.isChecked());
        String earthquake = String.valueOf(ckEarthquake.isChecked());
        String political = String.valueOf(ckPolitical.isChecked());
        String criminal = String.valueOf(ckCriminal.isChecked());

        ExecutorService es = Executors.newSingleThreadExecutor();
        Future f = es.submit(new RegisterUser(firstname,surname,gcmId,terror,flood,war,earthquake,political,criminal,user.getColourCode(),null));
        try
        {
            btnRegister.setEnabled(false);
            DbResponse dbResponse = (DbResponse) f.get();
            if(dbResponse.getStatus() == 200)
            {
                user.setFirstname(firstname);
                user.setSurname(surname);
                user.setGcm_id(gcmId);
                user.setTerror(terror);
                user.setFlood(flood);
                user.setWar(war);
                user.setEarthquake(earthquake);
                user.setPolitical(political);
                user.setCriminal(criminal);
                user.save();
                App.setUser(user);
                GoToMain goToMain = new GoToMain(this);
                findViewById(R.id.loadingRegister).setVisibility(View.GONE);
            }

        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    private int extractRadius(String value)
    {
        int radius = 0;
        switch (value)
        {
            case "50 KM" : radius = 50;
                break;
            case "100 KM" : radius = 100;
                break;
            case "200 KM" : radius = 200;
                break;
            case "500 KM" : radius = 500;
                break;
            case "1000+ KM" : radius = 1000;
                break;
            default : radius = 1000;
                break;
        }
        return radius;
    }

    public void addListeners()
    {
        ImageButton red = (ImageButton) findViewById(R.id.red);
        ImageButton orange = (ImageButton) findViewById(R.id.orange);
        ImageButton blue = (ImageButton) findViewById(R.id.blue);
        ImageButton green = (ImageButton) findViewById(R.id.green);

        red.setOnClickListener(new ColourListener(this,user,red,orange,blue,green));
        orange.setOnClickListener(new ColourListener(this,user,red,orange,blue,green));
        blue.setOnClickListener(new ColourListener(this,user,red,orange,blue,green));
        green.setOnClickListener(new ColourListener(this,user,red,orange,blue,green));
    }

}
