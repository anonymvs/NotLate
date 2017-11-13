package hu.bme.aut.notlateapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import hu.bme.aut.notlateapp.EventCreateActivity;
import hu.bme.aut.notlateapp.MainActivity;
import hu.bme.aut.notlateapp.R;
import hu.bme.aut.notlateapp.model.Event;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by hegedus on 2017.11.04..
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{

    public static final int CONTEXT_ACTION_DELETE = 10;
    public static final int CONTEXT_ACTION_EDIT = 11;
    public static final int EDIT_EVENT = 2;

    private final List<Event> events = new ArrayList<>();
    private Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_row, parent, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Event event = events.get(position);

        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.JAPAN);
        holder.date.setText(df.format(event.getDate().getTime()));
        holder.timeLeft.setText(event.getTimeLeft());
        holder.title.setText(event.getTitle());
        holder.owner.setText(event.getOwner());
        holder.location.setText(event.getLocation());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: i dont know yet, what to do with this...
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return true;
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
                            removeEvent(events.get(position));
                        }
                        if(R.id.edit == item.getItemId()) {
                            Event selectedPlace = (Event) getItem(position);
                            Intent i = new Intent(context, EventCreateActivity.class);
                            i.setClass(context, EventCreateActivity.class);
                            i.putExtra(EventCreateActivity.KEY_EDIT_EVENT, selectedPlace);
                            i.putExtra(EventCreateActivity.KEY_EDIT_ID, position);
                            //(context).startActivity(i);
                            ((Activity) context).startActivityForResult(i, EDIT_EVENT);
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

    public void addEvent(Event e) {
        events.add(e);
        notifyItemInserted(events.indexOf(e));
    }

    public void removeEvent(Event e) {
        int position = events.indexOf(e);
        events.remove(e);
        notifyItemRemoved(position);
    }

    public void setEvent(int position, Event e) {
        events.set(position, e);
        notifyItemChanged(position);
    }

    public Event getItem(int i) {
        return events.get(i);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView date;
        public final TextView timeLeft;
        public final TextView title;
        public final TextView owner;
        public final TextView location;
        public Event mEvent;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            date = (TextView) view.findViewById(R.id.eventDate);
            timeLeft = (TextView) view.findViewById(R.id.eventTimeLeft);
            title = (TextView) view.findViewById(R.id.eventTitle);
            owner = (TextView) view.findViewById(R.id.eventOwner);
            location = (TextView) view.findViewById(R.id.eventAddress);
        }

    }
}
