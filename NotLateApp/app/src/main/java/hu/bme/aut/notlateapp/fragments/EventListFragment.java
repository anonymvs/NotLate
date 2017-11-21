package hu.bme.aut.notlateapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import hu.bme.aut.notlateapp.EventCreateActivity;
import hu.bme.aut.notlateapp.R;
import hu.bme.aut.notlateapp.adapter.EventAdapter;
import hu.bme.aut.notlateapp.model.Event;

public class EventListFragment extends Fragment {

    public static final int EVENT_DETAILS_REQUESTED = 3;
    private RecyclerView myRecyclerView;
    private EventAdapter myAdapter;
    private final int CREATE_EVENT = 1;

    public static EventListFragment newInstance() {
        EventListFragment fragment = new EventListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        myRecyclerView = (RecyclerView) getView().findViewById(R.id.event_list);
        assert myRecyclerView != null;

        myAdapter = new EventAdapter();

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;// true if moved, false otherwise
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                myAdapter.removeEvent(viewHolder.getAdapterPosition());
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(myRecyclerView);

        myAdapter.addEvent(new Event(Calendar.getInstance(), "2 nap", "Title1", "by: Owner", "Here: Bp, bla utca 45.", "members"));
        myAdapter.addEvent(new Event(Calendar.getInstance(), "2 nap", "Title2", "by: Owner", "Here: Bp, bla utca 45.", "members"));
        myAdapter.addEvent(new Event(Calendar.getInstance(), "2 nap", "Title3", "by: Owner", "Here: Bp, bla utca 45.", "members"));

        myRecyclerView.setAdapter(myAdapter);

        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(getContext(), EventCreateActivity.class);
                startActivityForResult(intent, CREATE_EVENT);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CREATE_EVENT:
                if(resultCode == EventCreateActivity.KEY_NEW_EVENT_CODE) {
                    Event newEvent = (Event) data.getSerializableExtra(EventCreateActivity.KEY_NEW_EVENT);
                    myAdapter.addEvent(newEvent);
                }

            case EventAdapter.EDIT_EVENT:
                if(resultCode == EventCreateActivity.KEY_EDIT_EVENT_CODE) {
                    int position = data.getIntExtra(EventCreateActivity.KEY_EDIT_ID, -1);
                    Event editedEvent = (Event) data.getSerializableExtra(EventCreateActivity.KEY_EDIT_EVENT);
                    if(position != -1)
                        myAdapter.setEvent(position, editedEvent);
                }
        }
    }
}