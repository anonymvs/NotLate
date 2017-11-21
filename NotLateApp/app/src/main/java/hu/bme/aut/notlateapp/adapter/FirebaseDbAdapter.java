package hu.bme.aut.notlateapp.adapter;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hu.bme.aut.notlateapp.model.Event;

import static android.R.string.no;

/**
 * Created by hegedus on 2017.11.21..
 */

public class FirebaseDbAdapter {
    private static volatile FirebaseDbAdapter instance = new FirebaseDbAdapter();

    private static final String TAG = "MY-FirebaseDbAdapter";

    private FirebaseAuth mAuth;

    private FirebaseDatabase database;
    private DatabaseReference eventCloudEndPoint;

    private List<Event> events;
    private boolean b = true;

    private FirebaseDbAdapter() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        eventCloudEndPoint = database.getReference();
        events = new ArrayList<>();
    }

    public static FirebaseDbAdapter getInstance() {
        if (instance == null) {
            synchronized(FirebaseDbAdapter.class) {
                if (instance == null) {
                    instance = new FirebaseDbAdapter();

                }
            }
        }
        return instance;
    }

    public List<Event> getEvents() {
        if(b) {
            List<String> members = new ArrayList<>();
            members.add("memberA");
            Event e1 = new Event("TitleA", Calendar.getInstance(), "Budapest, Placeholder street 45", members);
            e1.setOwner(mAuth.getCurrentUser().getDisplayName());
            members.clear();
            members.add("memberB");
            Event e2 = new Event("TitleB", Calendar.getInstance(), "Budapest, Placeholder street 45", members);
            e2.setOwner(mAuth.getCurrentUser().getDisplayName());
            members.clear();
            members.add("memberc");
            Event e3 = new Event("TitleC", Calendar.getInstance(), "Budapest, Placeholder street 45", members);
            e3.setOwner(mAuth.getCurrentUser().getDisplayName());
            events.add(e1);
            events.add(e2);
            events.add(e3);
            b = false;
        }
        return events;
    }

    public void createEvent(Event e) {
        Log.w(TAG, "Event creation asked");
        String str = mAuth.getCurrentUser().getDisplayName();
        if(str != null) {
            e.setOwner(str);
            events.add(e);
        } else {
            Log.e(TAG, "There is no displayname");
        }
    }

    public void removeEvent(String eventID) {
        Log.w(TAG, "An events removal has been requested");
    }

    public void updateEvent(String eventID, Event e) {
        Log.w(TAG, "An events update has been requested");
    }

    public Event getEvent(String eventID) {
        Log.w(TAG, "A specific event has been requested");
        return events.get(events.size() - 1);
    }
}
