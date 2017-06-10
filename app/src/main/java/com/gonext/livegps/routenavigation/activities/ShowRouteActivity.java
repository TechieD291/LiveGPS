package com.gonext.livegps.routenavigation.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import com.gonext.livegps.routenavigation.utils.view.CustomTextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gonext.livegps.routenavigation.utils.Utils.hasPermissions;

public class ShowRouteActivity extends BaseActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.tvSource)
    CustomTextView tvSource;
    @BindView(R.id.tvDestination)
    CustomTextView tvDestination;
    @BindView(R.id.ibCar)
    ImageButton ibCar;
    @BindView(R.id.llCar)
    LinearLayout llCar;
    @BindView(R.id.ibWalk)
    ImageButton ibWalk;
    @BindView(R.id.llWalk)
    LinearLayout llWalk;
    @BindView(R.id.ibCycle)
    ImageButton ibCycle;
    @BindView(R.id.llBicycle)
    LinearLayout llBicycle;
    @BindView(R.id.llBottom)
    LinearLayout llBottom;

    private static final String TAG = ShowRouteActivity.class.getSimpleName();

    // Check Permissions Now
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    //Get Source Request Code
    private static final int GET_SOURCE_REQUEST_CODE = 2;

    //Get Destination Request Code
    private static final int GET_DESTINATION_REQUEST_CODE = 3;

    private boolean mLocationPermissionGranted;

    // The entry point to Google Play services, used by the Places API and Fused Location Provider.
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap googleMap;

    private Location mCurrentLocation;
    private CameraPosition mCameraPosition;

    private boolean source_flag = true;
    private boolean dest_flag = false;
    private boolean source_dest_flag = false;

    private final int DEFAULT_ZOOM = 15;

    //String for getAddress method
    Double lattitude;
    Double longditude;

    String address, city, country, state;
    String strSource = "";
    String strDestination = "";

    // Creating MarkerOptions
    MarkerOptions options = new MarkerOptions();

    ArrayList markerPoints = new ArrayList();
    HashMap<String, String> hashMap = new HashMap<>();
    String mode = "driving";

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

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        mGoogleApiClient.connect();

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

        googleMap.clear();

        getDeviceLocation();
        strSource = geocoderAddressfind(source.latitude, source.longitude);
        tvSource.setText(strSource);

        googleMap.addMarker(new MarkerOptions().position(source));
        options.position(source);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        googleMap.addMarker(options);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(source, DEFAULT_ZOOM));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GET_SOURCE_REQUEST_CODE:
                if (resultCode == RESULT_OK)
                {
                    final Place place = PlaceAutocomplete.getPlace(this, data);

                    source = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                    strSource = geocoderAddressfind(place.getLatLng().latitude, place.getLatLng().longitude);
                    tvSource.setText(strSource);

                    googleMap.clear();

                    if(dest_flag)
                    {
                        llCar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        ibCar.setImageResource(R.drawable.ic_car_selected);

                        googleMap.clear();
                        addMarkerToMap(source, BitmapDescriptorFactory.HUE_RED);
                        addMarkerToMap(destination, BitmapDescriptorFactory.HUE_GREEN);

                    }
                    else
                    {
                        googleMap.clear();
                        tvDestination.setText("");
                        addMarkerToMap(source, BitmapDescriptorFactory.HUE_RED);
                    }



                    source_dest_flag = source_flag && dest_flag;

                    if (source_dest_flag)
                    {
                        googleMap.clear();
                        drawPath(source, destination);
                    }
                    else
                    {
                        Toast.makeText(this, "Please select destination...", Toast.LENGTH_SHORT).show();
                    }


                }
                else
                {
                    getDeviceLocation();
                    strSource = geocoderAddressfind(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                    tvSource.setText(strSource);

                    if(dest_flag)
                    {
                        llCar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        ibCar.setImageResource(R.drawable.ic_car_selected);

                        googleMap.clear();
                        addMarkerToMap(source, BitmapDescriptorFactory.HUE_RED);
                        addMarkerToMap(destination, BitmapDescriptorFactory.HUE_GREEN);

                    }
                    else
                    {
                        googleMap.clear();
                        tvDestination.setText("");
                        addMarkerToMap(source, BitmapDescriptorFactory.HUE_RED);
                    }

                }
                break;
            case GET_DESTINATION_REQUEST_CODE:
                if (resultCode == RESULT_OK)
                {
                    dest_flag = true;

                    final Place place = PlaceAutocomplete.getPlace(this, data);

                    destination = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                    strDestination = geocoderAddressfind(place.getLatLng().latitude, place.getLatLng().longitude);

                    tvDestination.setText(strDestination);

                    googleMap.clear();

                    addMarkerToMap(source, BitmapDescriptorFactory.HUE_RED);
                    addMarkerToMap(destination, BitmapDescriptorFactory.HUE_GREEN);

                    source_dest_flag = source_flag && dest_flag;

                    if (source_dest_flag)
                    {
                        llBottom.setVisibility(View.VISIBLE);
                        drawPath(source, destination);

                        builder.include(source);
                        builder.include(destination);
                        LatLngBounds bounds = builder.build();

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, DEFAULT_ZOOM));
                    }
                    else
                    {
                        llBottom.setVisibility(View.GONE);
                    }

                }
                else
                {
                    dest_flag = false;

                    llBottom.setVisibility(View.GONE);
                    googleMap.clear();

                    addMarkerToMap(source, BitmapDescriptorFactory.HUE_RED);

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(source, DEFAULT_ZOOM));

                    tvDestination.setText("");

                    Toast.makeText(this, "No location selected", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick({R.id.tvSource, R.id.tvDestination, R.id.ibCar, R.id.ibWalk, R.id.ibCycle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvSource:
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(this);
                    startActivityForResult(intent, GET_SOURCE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                    // Refer to the reference doc for ConnectionResult to see what error codes might
                    // be returned in onConnectionFailed.
                    Log.d(TAG, "Play services connection failed");
                }
                break;
            case R.id.tvDestination:
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(this);
                    startActivityForResult(intent, GET_DESTINATION_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                    // Refer to the reference doc for ConnectionResult to see what error codes might
                    // be returned in onConnectionFailed.
                    Log.d(TAG, "Play services connection failed");
                }
                break;
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

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {

        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */

        if (!hasPermissions(ShowRouteActivity.this, PERMISSIONS)) {
            mLocationPermissionGranted = false;
            ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            mLocationPermissionGranted = true;

        }

        if (mLocationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }

            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            source = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            googleMap.clear();
        }

        // Set the map's camera position to the current location of the device.
        if (mCameraPosition != null) {

            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));

        }

        /*else if (mLastKnownLocation != null)
        {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

            String address = getCompleteAddressString(mLastKnownLocation.getLatitude(),
                    mLastKnownLocation.getLongitude());

            googleMap.clear();

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()))
                    .title(address)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            mCurrLocationMarker = googleMap.addMarker(markerOptions);
            mCurrLocationMarker.showInfoWindow();
            tvAddressDisplay.setText(address);

        } else {
            Log.d(MapActivity.class.getSimpleName(), "Current location is null. Using defaults.");
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }*/


    }

    /**
     * @param lattitude  Double latitude value
     * @param longditude Double longitude value
     * @return Address with city, state and country also saves latitude in lat variable and longitude in lng variable
     */
    private String geocoderAddressfind(Double lattitude, Double longditude) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        String final_address = "";

        try {
            List<Address> addressList = geocoder.getFromLocation(lattitude, longditude, 1);
            if (addressList != null && addressList.size() > 0) {
                for (int i = 0; i < addressList.get(0).getMaxAddressLineIndex(); i++) {
                    address = addressList.get(0).getAddressLine(1);
                    city = addressList.get(0).getLocality();
                    country = addressList.get(0).getCountryName();
                    state = addressList.get(0).getAdminArea();
                    this.lattitude = addressList.get(0).getLatitude();
                    this.longditude = addressList.get(0).getLongitude();
                }

            }

            final_address = address + " " + city + "," + state + "," + country;

        } catch (IOException ioException) {
            ioException.getStackTrace();
        }

        return final_address;

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

    private void drawPath(LatLng source, LatLng destination)
    {

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


}
