package client.protector.hazard.hazardprotectorclient.view;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import client.protector.hazard.hazardprotectorclient.R;
import client.protector.hazard.hazardprotectorclient.controller.Search.Common.GoToMain;
import client.protector.hazard.hazardprotectorclient.controller.Search.Core.App;
import client.protector.hazard.hazardprotectorclient.controller.Search.Listeners.ColourListener;
import client.protector.hazard.hazardprotectorclient.controller.Search.User.DetailsValidator;
import client.protector.hazard.hazardprotectorclient.model.User.User;

public class SettingsActivity extends AppCompatActivity
{
    private User user;
    private TextView txtFirstname;
    private TextView txtSurname;
    private CheckBox ckTerror;
    private CheckBox ckWar;
    private CheckBox ckFlood;
    private CheckBox ckEarthquake;
    private CheckBox ckPolitical;
    private CheckBox ckCriminal;
    private ImageButton red;
    private ImageButton orange;
    private ImageButton blue;
    private ImageButton green;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTheme(App.theme);
        setContentView(R.layout.activity_settings);
        user = App.user;
        setupActionBar();
        getWidgets();
        addListeners();
        loadData();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loadData();
    }

    private void setupActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSettings);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Back");
    }

    private void getWidgets()
    {
        txtFirstname = (TextView) findViewById(R.id.txtEditFirstname);
        txtSurname = (TextView) findViewById(R.id.txtEditSurname);
        ckTerror = (CheckBox) findViewById(R.id.ckEditTerror);
        ckWar = (CheckBox) findViewById(R.id.ckEditWar);
        ckFlood = (CheckBox) findViewById(R.id.ckEditFlood);
        ckEarthquake = (CheckBox) findViewById(R.id.ckEditEarthquake);
        ckPolitical = (CheckBox) findViewById(R.id.ckEditPolitical);
        ckCriminal = (CheckBox) findViewById(R.id.ckEditCriminal);
        red = (ImageButton) findViewById(R.id.editRed);
        orange = (ImageButton) findViewById(R.id.editOrange);
        blue = (ImageButton) findViewById(R.id.editBlue);
        green = (ImageButton) findViewById(R.id.editGreen);
    }

    private void loadData()
    {
        txtFirstname.setText(user.getFirstname());
        txtSurname.setText(user.getSurname());
        ckTerror.setChecked(Boolean.parseBoolean(user.getTerror()));
        ckWar.setChecked(Boolean.parseBoolean(user.getWar()));
        ckFlood.setChecked(Boolean.parseBoolean(user.getFlood()));
        ckEarthquake.setChecked(Boolean.parseBoolean(user.getEarthquake()));
        ckPolitical.setChecked(Boolean.parseBoolean(user.getPolitical()));
        ckCriminal.setChecked(Boolean.parseBoolean(user.getCriminal()));

        switch (user.getColourCode())
        {
            case 1 : red.setBackgroundColor(Color.RED);
                break;
            case 2 : orange.setBackgroundColor(Color.RED);
                break;
            case 3 : blue.setBackgroundColor(Color.RED);
                break;
            case 4 : green.setBackgroundColor(Color.GREEN);
                break;
            default : break;
        }
    }

    public void saveDetails(View view)
    {
        String firstname = txtFirstname.getText().toString();
        String surname = txtSurname.getText().toString();

        DetailsValidator validator = new DetailsValidator(this);
        boolean validForm = validator.validateForm(txtFirstname,txtSurname);
        if(validForm == false)
        {
            return;
        }

        String terror = String.valueOf(ckTerror.isChecked());
        String flood = String.valueOf(ckFlood.isChecked());
        String war = String.valueOf(ckWar.isChecked());
        String earthquake = String.valueOf(ckEarthquake.isChecked());
        String political = String.valueOf(ckPolitical.isChecked());
        String criminal = String.valueOf(ckCriminal.isChecked());

        user.setFirstname(firstname);
        user.setSurname(surname);
        user.setTerror(terror);
        user.setFlood(flood);
        user.setWar(war);
        user.setEarthquake(earthquake);
        user.setPolitical(political);
        user.setCriminal(criminal);
        if(user.save())
        {
            Toast toast = Toast.makeText(this, R.string.user_saved, Toast.LENGTH_LONG);
            toast.show();
            GoToMain goToMain = new GoToMain(this);
        }
        else
        {
            Toast toast = Toast.makeText(this, R.string.user_not_saved, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void addListeners()
    {
        red.setOnClickListener(new ColourListener(this,user,red,orange,blue,green));
        orange.setOnClickListener(new ColourListener(this,user,red,orange,blue,green));
        blue.setOnClickListener(new ColourListener(this,user,red,orange,blue,green));
        green.setOnClickListener(new ColourListener(this,user,red,orange,blue,green));
    }
}
