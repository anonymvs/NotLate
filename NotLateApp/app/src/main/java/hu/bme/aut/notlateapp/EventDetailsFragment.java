package hu.bme.aut.notlateapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hu.bme.aut.notlateapp.adapter.EventAdapter;
import hu.bme.aut.notlateapp.model.Event;

import static hu.bme.aut.notlateapp.R.id.tvTitle;

public class EventDetailsFragment extends Fragment {
    private Event myEvent;

    public static EventDetailsFragment newInstance() {
        EventDetailsFragment fragment = new EventDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        //myEvent = (Event) savedInstanceState.getSerializable(EventAdapter.FRAGMENT_PAYLOAD);
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            myEvent = (Event) bundle.getSerializable(EventAdapter.FRAGMENT_PAYLOAD);
        }

        TextView tvTitle = (TextView) getView().findViewById(R.id.tvTitle);
        tvTitle.setText(myEvent.getTitle());
        TextView tvDate = (TextView) getView().findViewById(R.id.tvDate);
        tvDate.setText(myEvent.getDateFormatted());
        TextView tvTimeLeft = (TextView) getView().findViewById(R.id.tvTimeLeft);
        tvTimeLeft.setText(myEvent.getTimeLeft());
        TextView tvLocation = (TextView) getView().findViewById(R.id.tvLocation);
        tvLocation.setText(myEvent.getLocation());
        TextView tvMembers = (TextView) getView().findViewById(R.id.tvMembers);
        tvMembers.setText(myEvent.getMembers());
        TextView tvOwner = (TextView) getView().findViewById(R.id.tvOwner);
        tvOwner.setText(myEvent.getOwner());
    }
}