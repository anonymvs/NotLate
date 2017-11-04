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
import android.view.View;

import java.util.Date;

import hu.bme.aut.notlateapp.adapter.EventAdapter;
import hu.bme.aut.notlateapp.model.Event;


public class MainActivity extends AppCompatActivity {

    private RecyclerView myRecyclerView;
    private EventAdapter myAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myRecyclerView = (RecyclerView) findViewById(R.id.event_list);
        assert myRecyclerView != null;

        myAdapter = new EventAdapter();

        myAdapter.addEvent(new Event(new Date(2017, 4, 14), "2 nap", "Title1", "by: Owner", "Here: Bp, bla utca 45."));
        myAdapter.addEvent(new Event(new Date(2017, 4, 14), "2 nap", "Title2", "by: Owner", "Here: Bp, bla utca 45."));
        myAdapter.addEvent(new Event(new Date(2017, 4, 14), "2 nap", "Title3", "by: Owner", "Here: Bp, bla utca 45."));

        myRecyclerView.setAdapter(myAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, EventCreateActivity.class);
                startActivity(intent);
            }
        });
    }
}
