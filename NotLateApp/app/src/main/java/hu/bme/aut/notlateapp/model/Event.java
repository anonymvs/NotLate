package hu.bme.aut.notlateapp.model;

import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by hegedus on 2017.11.04..
 */

public class Event implements Serializable{
    public String eventID;
    public String title;
    public String owner;
    public String ownerID;
    public String date;
    public String time;
    public String timeLeft;
    public String location; //android location conversion
    public List<String> members; //separated list of user IDs

    public Event() {}

    public Event(String eventID, String title, String owner, String ownerID, String date, String time, String timeLeft, String location, List<String> members) {
        this.eventID = eventID;
        this.title = title;
        this.owner = owner;
        this.ownerID = ownerID;
        this.date = date;
        this.time = time;
        this.timeLeft = timeLeft;
        this.location = location;
        this.members = members;
    }

    public Event(String title, Calendar date, String location, List<String> members) {
        this.title = title;
        this.date = calendarToString(date);
        this.time = calendarToHourAndMinute(date);
        this.location = location;
        this.members = members;
        this.timeLeft = calculateDaysRemaining(date);
    }

    public String getDate() { return this.date; }
    public void setDate(String date) { this.date = date; }

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

    public List<String> getMembers() {
        return members;
    }
    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getEventID() {
        return eventID;
    }
    public void setEventID(String eventID) { this.eventID = eventID; }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getOwnerID() {
        return ownerID;
    }
    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String calendarToHourAndMinute(Calendar date) {
        return date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE);
    }

    private String calculateDaysRemaining(Calendar date) {
        Calendar current = Calendar.getInstance();
        return Long.toString(TimeUnit.DAYS.convert(date.getTime().getTime() - current.getTime().getTime(), TimeUnit.DAYS));
    }

    public String calendarToString(Calendar date) {
        DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
        String ret;
        ret = df.format(date.getTime());
        return ret;
    }

    public Calendar stringToCalendar(String str) {
        DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(df.parse(str));
            return cal;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setDateByCalendar(Calendar date) {
        this.date = calendarToString(date);
        timeLeft = calculateDaysRemaining(date);
    }

    public Calendar getDateAsCalendar() {
        return stringToCalendar(date);
    }
}
