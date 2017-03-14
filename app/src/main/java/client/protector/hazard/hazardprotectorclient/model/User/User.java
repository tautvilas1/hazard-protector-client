package client.protector.hazard.hazardprotectorclient.model.User;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import client.protector.hazard.hazardprotectorclient.controller.Search.User.SaveUser;
import client.protector.hazard.hazardprotectorclient.model.Common.DbResponse;

/**
 * Created by Tautvilas on 11/02/2017.
 */

public class User implements Serializable
{

    public String firstname = "";
    public String surname = "";
    public String gcm_id = "";
    public String terror = "true";
    public String flood = "true";
    public String war = "true";
    public String earthquake = "true";

    public String registrationId = "";
    public int radius = 1000;
    public int colourCode = 0;
    double latitude = 0, longitude = 0;

    public User()
    {

    }
    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getTerror() {
        return terror;
    }

    public void setTerror(String terror) {
        this.terror = terror;
    }

    public String getFlood() {
        return flood;
    }

    public void setFlood(String flood) {
        this.flood = flood;
    }

    public String getWar() {
        return war;
    }

    public void setWar(String war) {
        this.war = war;
    }

    public String getEarthquake() {
        return earthquake;
    }

    public void setEarthquake(String earthquake) {
        this.earthquake = earthquake;
    }

    public int getColourCode() {
        return colourCode;
    }

    public void setColourCode(int colourCode) {
        this.colourCode = colourCode;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGcm_id() {
        return gcm_id;
    }

    public void setGcm_id(String gcm_id) {
        this.gcm_id = gcm_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", gcm_id='" + gcm_id + '\'' +
                ", terror='" + terror + '\'' +
                ", flood='" + flood + '\'' +
                ", war='" + war + '\'' +
                ", earthquake='" + earthquake + '\'' +
                ", registrationId='" + registrationId + '\'' +
                ", radius=" + radius +
                ", colourCode=" + colourCode +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public boolean save()
    {
        boolean result = false;
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future f = es.submit(new SaveUser(this));
        DbResponse response = null;

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

        if(response.getStatus() == 200)
        {
            result = true;
        }
        else
        {
            result = false;
        }
        return result;
    }

}
