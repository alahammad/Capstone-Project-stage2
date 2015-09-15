package net.ahammad.udacitycapstone.util;

import io.realm.RealmObject;

/**
 * Created by alahammad on 9/5/15.
 */
public class ReminderBean  extends RealmObject{

    private String title;
    private String exDate;
    private String numberOfTimes;
    private String imagePath;
    private double lat=-1;
    private double lon=-1;

    public ReminderBean(String title, String exDate, String numberOfTimes, String imagePath, double lat, double lon) {
        this.title = title;
        this.exDate = exDate;
        this.numberOfTimes = numberOfTimes;
        this.imagePath = imagePath;
        this.lat = lat;
        this.lon = lon;
    }

    public ReminderBean() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExDate() {
        return exDate;
    }

    public void setExDate(String exDate) {
        this.exDate = exDate;
    }

    public String getNumberOfTimes() {
        return numberOfTimes;
    }

    public void setNumberOfTimes(String numberOfTimes) {
        this.numberOfTimes = numberOfTimes;
    }


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
