package com.gonext.livegps.routenavigation.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gonext.livegps.routenavigation.R;
import com.gonext.livegps.routenavigation.interfaces.RetrofitMapInterface;
import com.gonext.livegps.routenavigation.models.Leg;
import com.gonext.livegps.routenavigation.models.Route;
import com.gonext.livegps.routenavigation.models.RouteExample;
import com.gonext.livegps.routenavigation.models.Step;
import com.gonext.livegps.routenavigation.utils.ApiUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowRouteActivity extends BaseActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.ibCar)
    ImageButton ibCar;
    @BindView(R.id.ibWalk)
    ImageButton ibWalk;
    @BindView(R.id.ibCycle)
    ImageButton ibCycle;
    @BindView(R.id.llCar)
    LinearLayout llCar;
    @BindView(R.id.llWalk)
    LinearLayout llWalk;
    @BindView(R.id.llBicycle)
    LinearLayout llBicycle;

    private static final String TAG = ShowRouteActivity.class.getSimpleName();

    // Check Permissions Now
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private static final int DEFAULT_ZOOM = 15;

    private boolean mLocationPermissionGranted;

    // The entry point to Google Play services, used by the Places API and Fused Location Provider.
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap googleMap;

    // Creating MarkerOptions
    MarkerOptions options = new MarkerOptions();

    ArrayList markerPoints = new ArrayList();
    HashMap<String, String> hashMap = new HashMap<>();
    String mode = "driving";

    private double source_lat, source_lng;
    private double dest_lat, dest_lng;
    private LatLng destination = null;
    private LatLng source = null;

    //Retrofit Interface
    private RetrofitMapInterface retrofitMapInterface;

    private String[] PERMISSIONS =
            {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_route);
        ButterKnife.bind(this);

        llCar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ibCar.setImageResource(R.drawable.ic_car_selected);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        mGoogleApiClient.connect();

        Intent i = getIntent();

        source_lat = i.getDoubleExtra("source_Latitude", 0.0);
        source_lng = i.getDoubleExtra("source_Longitude", 0.0);

        Log.d("Source Loc route", source_lat + "," + source_lng);

        dest_lat = i.getDoubleExtra("dest_Latitude", 0.0);
        dest_lng = i.getDoubleExtra("dest_Longitude", 0.0);

        Log.d("Destination Loc route", dest_lat + "," + dest_lng);

    }

    @OnClick({R.id.ibCar, R.id.ibWalk, R.id.ibCycle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibCar:
                mode = "driving";
                llCar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                llWalk.setBackgroundColor(Color.WHITE);
                llBicycle.setBackgroundColor(Color.WHITE);
                ibCar.setImageResource(R.drawable.ic_car_selected);
                ibCycle.setImageResource(R.drawable.ic_cycle);
                ibWalk.setImageResource(R.drawable.ic_walk);
                drawPath(source, destination);
                break;
            case R.id.ibWalk:
                mode = "walking";
                llWalk.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                llCar.setBackgroundColor(Color.WHITE);
                llBicycle.setBackgroundColor(Color.WHITE);
                ibWalk.setImageResource(R.drawable.ic_walk_selected);
                ibCar.setImageResource(R.drawable.ic_car);
                ibCycle.setImageResource(R.drawable.ic_cycle);
                drawPath(source, destination);
                break;
            case R.id.ibCycle:
                mode = "bicycling";
                llBicycle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                llCar.setBackgroundColor(Color.WHITE);
                llWalk.setBackgroundColor(Color.WHITE);
                ibCycle.setImageResource(R.drawable.ic_cycle_selected);
                ibCar.setImageResource(R.drawable.ic_car);
                ibWalk.setImageResource(R.drawable.ic_walk);
                drawPath(source, destination);
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Play services connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Refer to the reference doc for ConnectionResult to see what error codes might
        // be returned in onConnectionFailed.
        Log.d(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

        source = new LatLng(source_lat, source_lng);
        destination = new LatLng(dest_lat, dest_lng);

        googleMap.clear();

        googleMap.addMarker(new MarkerOptions().position(source));
        markerPoints.add(source);
        options.position(source);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        googleMap.addMarker(options);

        googleMap.addMarker(new MarkerOptions().position(destination));
        markerPoints.add(destination);
        options.position(destination);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        googleMap.addMarker(options);
        // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, DEFAULT_ZOOM));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(source);
        builder.include(destination);
        LatLngBounds bounds = builder.build();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));

        drawPath(source, destination);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mLocationPermissionGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                } else {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        mLocationPermissionGranted = false;
                    }
                }
            }
        }
    }

    /**
     * Method to decode polyline points
     */
    private List decodePoly(String encoded) {

        List poly = new ArrayList();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    private void drawPath(LatLng source, LatLng destination) {

        // Checks, whether start and end locations are captured
        if (source != null) {
            hashMap.put("origin", source.latitude + "," + source.longitude);
            if (destination == null) {
                hashMap.put("destination", source.latitude + "," + source.longitude);
            } else {
                hashMap.put("destination", destination.latitude + "," + destination.longitude);
            }
            hashMap.put("mode", mode);

            retrofitMapInterface = ApiUtils.getAPIService();

            Call<RouteExample> call = retrofitMapInterface.getRoute(hashMap);

            Log.e("url requested", call.request().toString());

            call.enqueue(new Callback<RouteExample>() {
                @Override
                public void onResponse(Call<RouteExample> call, Response<RouteExample> response) {
                    if (!response.isSuccessful()) {
                        Log.d("Success Status", "Not Successful");
                        return;
                    }

                    if (!response.body().getStatus().equals("OK")) {
                        Log.d("Response status", "Error");
                        if (response.body().getStatus().equals("NOT_FOUND")) {
                            Toast.makeText(ShowRouteActivity.this, "Route not available", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (response.body().getStatus().equals("ZERO_RESULTS")) {
                            Toast.makeText(ShowRouteActivity.this, "No route using " + mode, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        return;
                    }

                    ArrayList points = null;
                    PolylineOptions lineOptions = null;

                    List<Route> routesList = response.body().getRoutes();
                    List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();

                    for (int i = 0; i < routesList.size(); i++) {
                        List<Leg> jLegs = routesList.get(i).getLegs();
                        List path = new ArrayList<HashMap<String, String>>();

                        for (int j = 0; j < jLegs.size(); j++) {
                            List<Step> jSteps = jLegs.get(j).getSteps();

                            for (int k = 0; k < jSteps.size(); k++) {
                                String polyline = "";
                                polyline = (String) jSteps.get(k).getPolyline().getPoints();
                                List list = decodePoly(polyline);


                                /** Traversing all points */
                                for (int l = 0; l < list.size(); l++) {
                                    HashMap<String, String> hm = new HashMap<String, String>();
                                    hm.put("lat", Double.toString(((LatLng) list.get(l)).latitude));
                                    hm.put("lng", Double.toString(((LatLng) list.get(l)).longitude));
                                    path.add(hm);
                                }

                            }
                            routes.add(path);
                        }

                    }

                    for (int p = 0; p < routes.size(); p++) {
                        points = new ArrayList();
                        lineOptions = new PolylineOptions();

                        List<HashMap<String, String>> path1 = routes.get(p);

                        for (int j = 0; j < path1.size(); j++) {
                            HashMap<String, String> point = path1.get(j);

                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);

                            points.add(position);
                        }

                        lineOptions.addAll(points);
                        lineOptions.width(12);
                        lineOptions.color(Color.RED);
                        lineOptions.geodesic(true);
                    }

                    googleMap.addPolyline(lineOptions);

                }

                @Override
                public void onFailure(Call<RouteExample> call, Throwable t) {
                    Log.e("Failure status", t.toString());
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent(ShowRouteActivity.this, MoreOptionsActivity.class);
        startActivity(backIntent);
    }
}
