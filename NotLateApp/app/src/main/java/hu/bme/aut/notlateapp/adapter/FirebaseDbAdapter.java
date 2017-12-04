package hu.bme.aut.notlateapp.adapter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import hu.bme.aut.notlateapp.model.Event;

/**
 * Created by hegedus on 2017.11.21..
 */

public class FirebaseDbAdapter {
    private static volatile FirebaseDbAdapter instance = new FirebaseDbAdapter();
    private static final String TAG = "MY-FirebaseDbAdapter";

    private FirebaseAuth mAuth;
    private DatabaseReference database;
    private DatabaseReference eventCloudEndPoint;
    private EventAdapter eventAdapter;

    private List<Event> events;

    private FirebaseDbAdapter() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        eventCloudEndPoint = database.child("events");

        events = new ArrayList<>();

        initDatabase();

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
        return events;
    }

    public void createEvent(Event e) {
        e.setOwner(mAuth.getCurrentUser().getDisplayName());
        e.setOwnerID(mAuth.getCurrentUser().getUid());
        String key = eventCloudEndPoint.push().getKey();
        e.setEventID(key);
        eventCloudEndPoint.child(key).setValue(e).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, e.getMessage());
            }
        });
    }

    public void removeEvent(String eventID) {
        eventCloudEndPoint.child(eventID).removeValue();
    }

    public void updateEvent(String eventID, Event e) {
        e.setEventID(eventID);
        e.setOwner(mAuth.getCurrentUser().getDisplayName());
        e.setOwnerID(mAuth.getCurrentUser().getUid());
        eventCloudEndPoint.child(eventID).setValue(e).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onComplete: Failed=" + e.getMessage());
            }
        });
        eventAdapter.notifyOnDataSetChanged();
        Log.d(TAG, "waaa");
    }

    public Event getEvent(String eventID) {
        //TODO: ...
        return null;
    }

    private void initDatabase() {
        eventCloudEndPoint.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                events.clear();
                for(DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    events.add(event);
                }
                if(eventAdapter != null) {
                    eventAdapter.notifyOnDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, databaseError.getMessage());
            }
        });
    }

    public void setEventAdapter(EventAdapter ea) {
        eventAdapter = ea;
    }
}
