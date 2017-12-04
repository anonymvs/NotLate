package hu.bme.aut.notlateapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import hu.bme.aut.notlateapp.CreateEventActivity;
import hu.bme.aut.notlateapp.fragments.EventDetailsFragment;
import hu.bme.aut.notlateapp.R;
import hu.bme.aut.notlateapp.model.Event;

/**
 * Created by hegedus on 2017.11.04..
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    public static final String FRAGMENT_PAYLOAD = "FRAGMENT_PAYLOAD";

    private List<Event> events = new ArrayList<>();
    private FirebaseDbAdapter dbAdapter;
    private Context context;
    private Comparator<Event> cmp;

    public EventAdapter() {
        dbAdapter = FirebaseDbAdapter.getInstance();
        dbAdapter.setEventAdapter(this);
        events = dbAdapter.getEvents();
        cmp = new Comparator<Event>() {
            @Override
            public int compare(Event event, Event t1) {
                if(event.isArchived() && !t1.isArchived()) {
                    return 1;
                } else if(!event.isArchived() && t1.isArchived()) {
                    return -1;
                }
                Calendar calendarE = event.askDateAsCalendar();
                Calendar calendarT1 = t1.askDateAsCalendar();
                return calendarE.compareTo(calendarT1);
            }
        };
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_row, parent, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Event event = events.get(position);

        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.JAPAN);
        holder.date.setText(df.format(event.askDateAsCalendar().getTime()) + " - " + event.askFormattedTime());
        holder.timeLeft.setText(event.askCalculatedTimeLeft());
        holder.title.setText(event.getTitle());
        holder.owner.setText(event.getOwner());
        holder.location.setText(event.getLocation());
        holder.members.setText(event.askAllMembers());
        holder.mEvent = event;

        if(position % 2 == 0)
            holder.mView.setBackgroundColor(Color.parseColor("#e2e2e2"));
        else
            holder.mView.setBackgroundColor(Color.parseColor("#ffffff"));

        switch (event.askCalculatedTimeLeft()) {
            case ("already late") :
                holder.mView.setBackgroundColor(Color.parseColor("#ef9a9a"));
                break;
            case ("today") :
                holder.mView.setBackgroundColor(Color.parseColor("#fff59d"));
                break;
            case ("tomorrow") :
                holder.mView.setBackgroundColor(Color.parseColor("#fff59d"));
                break;
            case ("archived") :
                holder.mView.setBackgroundColor(Color.parseColor("#a5d6a7"));
                break;
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(FRAGMENT_PAYLOAD, event);

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = EventDetailsFragment.newInstance();
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                myFragment.setArguments(bundle);
                ft.replace(R.id.frame_layout, myFragment).addToBackStack(null).commit();

            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                popup.inflate(R.menu.menu_event);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(R.id.delete == item.getItemId()) {
                            removeEvent(position);
                        }
                        if(R.id.edit == item.getItemId()) {
                            Event selectedPlace = (Event) getItem(position);
                            Intent i = new Intent(context, CreateEventActivity.class);
                            i.setClass(context, CreateEventActivity.class);
                            i.putExtra(CreateEventActivity.KEY_EDIT_EVENT, selectedPlace);
                            i.putExtra(CreateEventActivity.KEY_EDIT_ID, position);
                            //(context).startActivity(i);
                            ((Activity) context).startActivity(i);
                        }
                        return false;
                    }
                });
                popup.show();
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    public void removeEvent(int position) {
        dbAdapter.removeEvent(events.get(position).getEventID());
        events.remove(events.get(position));
        //notifyItemRemoved(position);
        notifyOnDataSetChanged();
    }

    public void notifyOnDataSetChanged() {
        Collections.sort(events, cmp);
        notifyDataSetChanged();
    }

    public Event getItem(int i) {
        return events.get(i);
    }

    public void toggleArchived(int position) {
        Event e = events.get(position);
        if(e.isArchived()) {
            e.setArchived(false);
        } else {
            e.setArchived(true);
        }
        events.set(position, e);
        dbAdapter.updateEvent(e.getEventID(), e);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView date;
        public final TextView timeLeft;
        public final TextView title;
        public final TextView owner;
        public final TextView location;
        public final TextView members;
        public Event mEvent;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            date = (TextView) view.findViewById(R.id.eventDate);
            timeLeft = (TextView) view.findViewById(R.id.eventTimeLeft);
            title = (TextView) view.findViewById(R.id.eventTitle);
            owner = (TextView) view.findViewById(R.id.eventOwner);
            location = (TextView) view.findViewById(R.id.eventAddress);
            members = (TextView) view.findViewById(R.id.eventMembers);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(FRAGMENT_PAYLOAD, mEvent);

                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment myFragment = EventDetailsFragment.newInstance();
                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    myFragment.setArguments(bundle);
                    ft.replace(R.id.frame_layout, myFragment).addToBackStack(null).commit();

                }
            });
        }

    }
}
