package net.ahammad.udacitycapstone.util;

import android.widget.TextView;

import io.realm.RealmObject;

/**
 * Created by alahammad on 9/5/15.
 */
public class ReminderBean  extends RealmObject{

    private String title;
    private String exDate;
    private String numberOfTimes;
    private String imagePath;

    public ReminderBean(String imagePath, String title, String exDate, String numberOfTimes) {
        this.imagePath = imagePath;
        this.title = title;
        this.exDate = exDate;
        this.numberOfTimes = numberOfTimes;
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
}
