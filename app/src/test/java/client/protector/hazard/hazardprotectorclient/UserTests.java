package client.protector.hazard.hazardprotectorclient;

import org.junit.Test;
import org.mockito.Mock;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


import client.protector.hazard.hazardprotectorclient.controller.Search.User.GetUser;
import client.protector.hazard.hazardprotectorclient.controller.Search.User.SaveUser;
import client.protector.hazard.hazardprotectorclient.model.Common.DbResponse;

import client.protector.hazard.hazardprotectorclient.model.User.RegisterUser;
import client.protector.hazard.hazardprotectorclient.model.User.User;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Tautvilas on 27/02/2017.
 */
public class UserTests
{
    @Mock
    ExecutorService es = Executors.newSingleThreadExecutor();

    @Test
    public void Register_New_User()
    {
        Future future = es.submit(new RegisterUser("Tester","Serner","Test_GCM","true","true","true","false","true","false",0,"TEST_REGISTRATION_ID"));
        DbResponse response = new DbResponse();
        try
        {
            response = (DbResponse) future.get();
            assertThat(response.getStatus(), is(200));
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

    @Test
    public void Save_User_To_Database()
    {
        User user = new User();
        user.setGcm_id("Test_GCM");
        user.setRegistrationId("TEST_REGISTRATION_ID");
        Future future = es.submit(new SaveUser(user));
        try
        {
            DbResponse response = (DbResponse) future.get();
            assertEquals(200,response.getStatus());
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

    @Test
    public void Get_User_From_Database()
    {
        String testGcmId = "eVC_76I7ig4";
        Future future = es.submit(new GetUser(testGcmId));
        try
        {
            User user = (User) future.get();
            assertTrue(user.getGcm_id().equals(testGcmId));
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
