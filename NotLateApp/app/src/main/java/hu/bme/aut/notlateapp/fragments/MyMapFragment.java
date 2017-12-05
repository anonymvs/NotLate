package hu.bme.aut.notlateapp.fragments;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import hu.bme.aut.notlateapp.R;
import hu.bme.aut.notlateapp.model.Event;

import static hu.bme.aut.notlateapp.adapter.EventAdapter.FRAGMENT_PAYLOAD;

/**
 * Created by hegedus on 2017.12.04..
 */

public class MyMapFragment extends Fragment implements OnMapReadyCallback, android.location.LocationListener{

    private static final String TAG = "MY MYMAPFRAGMENT: ";
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private String locationID;
    private Place myPlace;

    public static MyMapFragment newInstance() {
        MyMapFragment fragment = new MyMapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return rootView;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            final Event myEvent = (Event) bundle.getSerializable(FRAGMENT_PAYLOAD);
            locationID = myEvent.getLocationID();

            GeoDataClient mGeoDataClient = Places.getGeoDataClient(getActivity(), null);
            mGeoDataClient.getPlaceById(locationID).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
                @Override
                public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                    if(task.isSuccessful()) {
                        PlaceBufferResponse places = task.getResult();
                        myPlace = places.get(0);

                        mMap.addMarker(new MarkerOptions().position(myPlace.getLatLng()).title(myEvent.getTitle()));
                        CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(myPlace.getLatLng(), 15);
                        mMap.animateCamera(zoom);

                        places.release();
                    }
                }
            });
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setBackgroundColor(Color.WHITE);
        getView().setClickable(true);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

      /*  LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }
}
