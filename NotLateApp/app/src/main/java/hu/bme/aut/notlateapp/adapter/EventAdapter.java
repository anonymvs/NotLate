package hu.bme.aut.notlateapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.notlateapp.R;
import hu.bme.aut.notlateapp.model.Event;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by hegedus on 2017.11.04..
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{
    private final List<Event> events = new ArrayList<>();
    private Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_row, parent, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Event event = events.get(position);

        holder.date.setText(event.getDate().toString());
        //TODO: a readable format
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
    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    public void addEvent(Event e) {
        events.add(e);
        notifyItemInserted(events.indexOf(e));
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
