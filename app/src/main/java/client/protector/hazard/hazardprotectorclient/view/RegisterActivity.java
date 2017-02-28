package client.protector.hazard.hazardprotectorclient.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.iid.InstanceID;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import client.protector.hazard.hazardprotectorclient.R;
import client.protector.hazard.hazardprotectorclient.model.Common.DbResponse;
import client.protector.hazard.hazardprotectorclient.model.User.GetUser;
import client.protector.hazard.hazardprotectorclient.model.User.RegisterUser;

public class RegisterActivity extends AppCompatActivity
{
    public String gcmId;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Please enter a few details");
        gcmId = getIntent().getStringExtra("gcmId");
    }

    public void registerUser(View view)
    {
        TextView txtFirstname = (TextView) findViewById(R.id.txtFirstname);
        TextView txtSurname = (TextView) findViewById(R.id.txtSurname);
        String firstname = txtFirstname.getText().toString();
        String surname = txtSurname.getText().toString();

        ExecutorService es = Executors.newSingleThreadExecutor();
        Future f = es.submit(new RegisterUser(firstname,surname,gcmId,0,0));
        try
        {
            DbResponse dbResponse = (DbResponse) f.get();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
