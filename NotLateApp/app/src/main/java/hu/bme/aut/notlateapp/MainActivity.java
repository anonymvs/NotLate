package hu.bme.aut.notlateapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import hu.bme.aut.notlateapp.fragments.AccountFragment;
import hu.bme.aut.notlateapp.fragments.EventListFragment;
import hu.bme.aut.notlateapp.fragments.SettingsFragment;
import hu.bme.aut.notlateapp.fragments.StatisticsFragment;

import static android.R.attr.defaultFocusHighlightEnabled;
import static android.R.attr.id;
import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);

        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_one:
                                selectedFragment = EventListFragment.newInstance();
                                break;
                            case R.id.action_two:
                                selectedFragment = StatisticsFragment.newInstance();
                                break;
                            case R.id.action_three:
                                selectedFragment = AccountFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, EventListFragment.newInstance());
        transaction.commit();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case (R.id.action_settings) :
                /*android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, SettingsFragment.newInstance());
                transaction.commit();*/
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
                break;
            case (R.id.action_refresh) :
                //TODO: action_refresh menu implementation
                Toast.makeText(MainActivity.this, "Refresh", Toast.LENGTH_SHORT).show();
                break;
            case (R.id.action_logout) :
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
        }
        return true;
    }
}

