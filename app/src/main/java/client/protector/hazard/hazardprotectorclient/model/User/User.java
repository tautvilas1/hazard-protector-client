package client.protector.hazard.hazardprotectorclient.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import client.protector.hazard.hazardprotectorclient.controller.Search.User.SaveUser;
import client.protector.hazard.hazardprotectorclient.model.Common.DbResponse;

/**
 * Created by Tautvilas on 11/02/2017.
 */

public class User extends Observable implements Serializable
{

    public String firstname = "";
    public String surname = "";
    public String gcm_id = "";
    public String terror = "true";
    public String flood = "true";
    public String war = "true";
    public String earthquake = "true";
    public String political = "true";
    public String criminal = "true";
    public String hazardArticles = "";
    public ArrayList<String> hazardArticlesList = new ArrayList<String>();

    public String registrationId = "";
    public int colourCode = 0;
    double latitude = 0, longitude = 0;

    public User()
    {
    }

    public void setHazardArticlesList(ArrayList<String> hazardArticlesList)
    {
        this.hazardArticlesList = hazardArticlesList;
        setChanged();
        notifyObservers();
    }

    public void setHazardArticles(String hazardArticles)
    {
        if(!hazardArticles.equals("0"))
        {
            String[] articles = hazardArticles.split(",");
            for(String article: articles)
            {
                hazardArticlesList.add(article.trim());
            }
        }
    }

    public ArrayList<String> getHazardArticlesList()
    {
        return hazardArticlesList;
    }


    public String getPolitical() {
        return political;
    }

    public void setPolitical(String political) {
        this.political = political;
    }

    public String getCriminal() {
        return criminal;
    }

    public void setCriminal(String criminal) {
        this.criminal = criminal;
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

    public ArrayList<String> getPreferences()
    {
        ArrayList<String> preferences = new ArrayList<String>();
        if(this.getTerror().equals("true"))
        {
            preferences.add("terror");
            preferences.add("bomb");
            preferences.add("explosion");
        }
        if(this.getFlood().equals("true"))
        {
            preferences.add("flood");
        }
        if(this.getWar().equals("true"))
        {
            preferences.add("war");
            preferences.add("military");
        }
        if(this.getEarthquake().equals("true"))
        {
            preferences.add("earthquake");
        }
        if(this.getPolitical().equals("true"))
        {
            preferences.add("political");
            preferences.add("government");
        }
        if(this.getCriminal().equals("true"))
        {
            preferences.add("criminal");
            preferences.add("jail");
            preferences.add("police");
        }
        return preferences;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", gcm_id='" + gcm_id + '\'' +
                ", terror='" + terror + '\'' +
                ", flood='" + flood + '\'' +
                ", war='" + war + '\'' +
                ", earthquake='" + earthquake + '\'' +
                ", political='" + political + '\'' +
                ", criminal='" + criminal + '\'' +
                ", hazardArticles='" + hazardArticles + '\'' +
                ", registrationId='" + registrationId + '\'' +
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
