package client.protector.hazard.hazardprotectorclient;

import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import client.protector.hazard.hazardprotectorclient.model.Common.DbResponse;
import client.protector.hazard.hazardprotectorclient.model.User.RegisterUser;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.mock.*;

/**
 * Created by Tautvilas on 27/02/2017.
 */
@RunWith(MockitoJUnitRunner.class);
public class UnitTests
{
    @Mock
    ExecutorService es = Executors.newSingleThreadExecutor();

    @Mock
    Future f = es.submit(new RegisterUser("Tester","Serner","Test_GCM",0,0));

    @Mock
    DbResponse response = null;

    @Test
    public void Register_User_Text()
    {

        try
        {
            response = (DbResponse) f.get();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }

//        when(response.getStatus())
//                .thenReturn(FAKE_STRING);
//        ClassUnderTest myObjectUnderTest = new ClassUnderTest(mMockContext);
//
//        // ...when the string is returned from the object under test...
//        String result = myObjectUnderTest.getHelloWorldString();

        // ...then the result should be the expected one.
        assertThat(response.getStatus(), is(200));

        if(response != null)
        {
            assertEquals("User was not saved",200,response.getStatus());
        }
        else
        {
            System.out.println("Response is null");
        }
    }
}
