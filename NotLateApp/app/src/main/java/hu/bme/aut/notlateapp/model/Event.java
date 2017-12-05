package hu.bme.aut.notlateapp.model;

import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static hu.bme.aut.notlateapp.R.id.eventDate;

/**
 * Created by hegedus on 2017.11.04..
 */

public class Event implements Serializable {
    public String eventID;
    public String title;
    public String owner;
    public String ownerID;
    public String date;
    public String time;
    public String location; //android location conversion
    public String locationID;
    public boolean isArchived;
    public List<String> members; //separated list of user IDs

    public Event() {}

    public Event(String eventID, String title, String owner, String ownerID, String date, String time, String location, String locationID, boolean isArchived, List<String> members) {
        this.eventID = eventID;
        this.title = title;
        this.owner = owner;
        this.ownerID = ownerID;
        this.date = date;
        this.time = time;
        this.location = location;
        this.locationID = locationID;
        this.isArchived = isArchived;
        this.members = members;
    }

    public Event(String title, Calendar date, String location, String locationID, List<String> members) {
        this.title = title;
        this.date = calendarToString(date);
        this.time = calendarToHourAndMinute(date);
        this.location = location;
        this.locationID = locationID;
        this.members = members;
        this.isArchived = false;
    }

    public String getDate() { return this.date; }
    public void setDate(String date) { this.date = date; }

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

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
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

    public boolean isArchived() {
        return isArchived;
    }
    public void setArchived(boolean archived) {
        isArchived = archived;
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

    public String askCalculatedTimeLeft() {
        Calendar eventDate = stringToCalendar(date);
        String[] strarray = time.split(":");
        int hour = Integer.parseInt(strarray[0]);
        int minute = Integer.parseInt(strarray[1]);
        eventDate.set(Calendar.HOUR_OF_DAY, hour);
        eventDate.set(Calendar.MINUTE, minute);

        Calendar currentDate = Calendar.getInstance();

        long diff = eventDate.getTimeInMillis() - currentDate.getTimeInMillis();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if(isArchived) {
            return "archived";
        }
        if (eventDate.get(Calendar.DAY_OF_MONTH) == currentDate.get(Calendar.DAY_OF_MONTH) &&
                hours > 0) {
            return "today";
        }
        currentDate.add(Calendar.DATE, 1);
        if (eventDate.get(Calendar.DATE) == currentDate.get(Calendar.DATE) ) {
            return "tomorrow";
        }
        if(diff < 0) {
            return "already late";
        }
        return "in " + (days + 1) + " days";
    }

    public String askFormattedTime() {
        if(time != null) {
            String[] strarray = time.split(":");
            int hour = Integer.parseInt(strarray[0]);
            int minute = Integer.parseInt(strarray[1]);
            String strH, strM;
            String zero = "0";

            if(hour < 10) {
                strH = zero + hour;
            } else {
                strH = "" + hour;
            }
            if(minute < 10) {
                strM = zero + minute;
            } else {
                strM = "" + minute;
            }
            return strH + ":" + strM;
        }
        return "00:00";
    }

    public void setDateByCalendar(Calendar date) {
        this.date = calendarToString(date);
    }

    public Calendar askDateAsCalendar() {
        Calendar ret = stringToCalendar(date);
        String[] strarray = time.split(":");
        int hour = Integer.parseInt(strarray[0]);
        int minute = Integer.parseInt(strarray[1]);
        ret.set(Calendar.HOUR_OF_DAY, hour);
        ret.set(Calendar.MINUTE, minute);
        return ret;
    }

    public String askAllMembers() {
        String allMembers = "";
        for(int i = 0; i < members.size()-1; i++) {
            allMembers += members.get(i) + ", ";
        }
        allMembers += members.get(members.size() - 1);
        return allMembers;
    }

    public String askAllMembersWithoutComma() {
        String allMembers = "";
        for(int i = 0; i < members.size(); i++) {
            allMembers += members.get(i) + " ";
        }
        return allMembers;
    }

    public boolean hasMembers() {
        if(members.size() != 0 || members.get(0).equals("")) {
            return true;
        }
        return false;
    }
}
