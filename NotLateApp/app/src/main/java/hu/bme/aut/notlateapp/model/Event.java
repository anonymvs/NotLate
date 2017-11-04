package hu.bme.aut.notlateapp.model;

import android.view.View;
import android.widget.TextView;

import java.util.Date;

/**
 * Created by hegedus on 2017.11.04..
 */

public class Event {
    public Date date;
    public String timeLeft;
    public String title;
    //TODO: owner should resemble the user who made the event
    public String owner;
    //Android.location class maybe...
    public String location;

    public Event(Date date, String timeLeft, String title, String owner, String location) {
        this.date = date;
        this.timeLeft = timeLeft;
        this.title = title;
        this.owner = owner;
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(String timeLeft) {
        this.timeLeft = timeLeft;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
