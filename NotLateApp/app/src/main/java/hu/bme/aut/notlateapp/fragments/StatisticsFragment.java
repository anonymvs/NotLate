package hu.bme.aut.notlateapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import hu.bme.aut.notlateapp.R;

public class StatisticsFragment extends Fragment {
    public static StatisticsFragment newInstance() {
        StatisticsFragment fragment = new StatisticsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setBackgroundColor(Color.WHITE);
        getView().setClickable(true);
    }
}