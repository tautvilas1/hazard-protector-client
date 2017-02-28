package client.protector.hazard.hazardprotectorclient.model.User;

import java.io.Serializable;

/**
 * Created by deadlylife on 11/02/2017.
 */

public class User implements Serializable{

    public String firstname, surname, gcm_id;
    public int id;
    double latitude, longitude;

    public User() {

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString()
    {
        return "User{" +
                "firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", gcm_id='" + gcm_id + '\'' +
                ", id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
