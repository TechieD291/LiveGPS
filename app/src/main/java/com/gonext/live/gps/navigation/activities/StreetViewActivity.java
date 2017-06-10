package com.gonext.live.gps.navigation.activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;

import com.gonext.live.gps.navigation.R;

public class StreetViewActivity extends BaseActivity
        implements GoogleMap.OnMarkerDragListener, StreetViewPanorama.OnStreetViewPanoramaChangeListener,
        OnMapReadyCallback
{

    private static final String TAG = StreetViewActivity.class.getSimpleName();

    private static final String MARKER_POSITION_KEY = "MarkerPosition";

    private StreetViewPanorama streetViewPanorama;
    private Marker marker;

    private double source_lat, source_lng;
    private LatLng location,markerPosition;

    private GoogleMap googleMap;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street_view);

        Intent i = getIntent();
        source_lat = i.getDoubleExtra("source_Latitude", 0.0);
        source_lng = i.getDoubleExtra("source_Longitude", 0.0);

        location = new LatLng(source_lat, source_lng);

        final LatLng markerPosition;
        if (savedInstanceState == null) {
            markerPosition = location;
        } else {
            markerPosition = savedInstanceState.getParcelable(MARKER_POSITION_KEY);
        }

        this.markerPosition = markerPosition;

        SupportStreetViewPanoramaFragment  streetViewPanoramaFragment =
                (SupportStreetViewPanoramaFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.streetviewpanorama);

        streetViewPanoramaFragment.getStreetViewPanoramaAsync(
                new OnStreetViewPanoramaReadyCallback() {
                    @Override
                    public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
                        streetViewPanorama = panorama;
                        streetViewPanorama.setOnStreetViewPanoramaChangeListener(
                                StreetViewActivity.this);

                        streetViewPanorama.setZoomGesturesEnabled(true);
                        // Only need to set the position once as the streetview fragment will maintain
                        // its state.
                        if (savedInstanceState == null) {
                            streetViewPanorama.setPosition(location);
                        }
                    }
                });

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MARKER_POSITION_KEY, marker.getPosition());
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        streetViewPanorama.setPosition(marker.getPosition(),150);
    }

    @Override
    public void onStreetViewPanoramaChange(StreetViewPanoramaLocation streetViewPanoramaLocation) {
        if (streetViewPanoramaLocation != null) {
            marker.setPosition(streetViewPanoramaLocation.position);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap  = googleMap;

        this.googleMap.setOnMarkerDragListener(StreetViewActivity.this);

        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Creates a draggable marker. Long press to drag.
        marker = this.googleMap.addMarker(new MarkerOptions()
                .position(markerPosition)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon))
                .draggable(true));

        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent(StreetViewActivity.this, MoreOptionsActivity.class);
        startActivity(backIntent);
    }

}
