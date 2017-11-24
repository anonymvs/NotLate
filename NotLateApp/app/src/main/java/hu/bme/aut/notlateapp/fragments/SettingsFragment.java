package hu.bme.aut.notlateapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;

import hu.bme.aut.notlateapp.MainActivity;
import hu.bme.aut.notlateapp.R;

public class SettingsFragment extends PreferenceFragment {

    private CheckBoxPreference cbp;

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Indicate here the XML resource you created above that holds the preferences
        addPreferencesFromResource(R.xml.preferences);

        CheckBoxPreference cbp = (CheckBoxPreference) getPreferenceManager().findPreference("logged_in");
        cbp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                return true;
            }
        });
    }
}