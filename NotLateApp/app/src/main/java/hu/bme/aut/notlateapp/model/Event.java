package hu.bme.aut.notlateapp.model;

import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hegedus on 2017.11.04..
 */

public class Event implements Serializable{
    public Calendar date;
    public String timeLeft;
    public String title;
    //TODO: owner should resemble the user who made the event
    public String owner;
    //Android.location class maybe...
    public String location;
    //TODO: members should be something else than string, presumably its own class
    public String members;

    public Event(Calendar date, String timeLeft, String title, String owner, String location, String members) {
        this.date = date;
        this.timeLeft = timeLeft;
        this.title = title;
        this.owner = owner;
        this.location = location;
        this.members = members;

    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
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

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }
}
