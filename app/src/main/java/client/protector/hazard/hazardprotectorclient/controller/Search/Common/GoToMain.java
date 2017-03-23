package client.protector.hazard.hazardprotectorclient.controller.Search.Common;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.common.api.GoogleApiClient;

import client.protector.hazard.hazardprotectorclient.model.User.User;
import client.protector.hazard.hazardprotectorclient.view.FeedActivity;
import client.protector.hazard.hazardprotectorclient.view.MainActivity;

/**
 * Created by Tautvilas on 04/03/2017.
 */

public class GoToMain
{
    public GoToMain(Context context)
    {
        Intent intent = new Intent(context, FeedActivity.class);
        context.startActivity(intent);
    }
}
