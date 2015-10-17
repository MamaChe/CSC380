package com.example.ericm.gui;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.ericm.helloworld.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private UiSettings mUiSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        LatLng position = new LatLng(37.614372, -122.385340);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 18));
        //mMap.addMarker(new MarkerOptions().position(position).title("Shineman Center"));
        mUiSettings = mMap.getUiSettings();

        mUiSettings.setZoomControlsEnabled(false);
        mUiSettings.setScrollGesturesEnabled(false);
        mUiSettings.setZoomGesturesEnabled(false);
        mUiSettings.setTiltGesturesEnabled(false);

        // Instantiates a new Polyline object and adds points to define a rectangle
        PolylineOptions rectOptions = new PolylineOptions()
                .add(new LatLng(37.614372, -122.385340))
                .add(new LatLng(37.614330, -122.385345))  // North of the previous point, but at the same longitude
                .add(new LatLng(37.6144, -122.3855)); // Closes the polyline.

// Get back the mutable Polyline
        Polyline polyline = mMap.addPolyline(rectOptions);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
