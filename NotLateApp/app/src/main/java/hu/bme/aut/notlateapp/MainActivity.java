package hu.bme.aut.notlateapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.Calendar;
import java.util.Date;

import hu.bme.aut.notlateapp.adapter.EventAdapter;
import hu.bme.aut.notlateapp.model.Event;

import static hu.bme.aut.notlateapp.EventCreateActivity.KEY_EDIT_EVENT;
import static hu.bme.aut.notlateapp.EventCreateActivity.KEY_EDIT_ID;
import static hu.bme.aut.notlateapp.R.id.btnCreate;


public class MainActivity extends AppCompatActivity {

    private RecyclerView myRecyclerView;
    private EventAdapter myAdapter;
    private final int CREATE_EVENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myRecyclerView = (RecyclerView) findViewById(R.id.event_list);
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, EventCreateActivity.class);
                startActivityForResult(intent, CREATE_EVENT);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
