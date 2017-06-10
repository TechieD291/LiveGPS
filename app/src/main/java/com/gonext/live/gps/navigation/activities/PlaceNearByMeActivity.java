package com.gonext.live.gps.navigation.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.View;

import com.gonext.live.gps.navigation.R;
import com.gonext.live.gps.navigation.utils.view.CustomTextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlaceNearByMeActivity extends BaseActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    Fragment fragment;
    public static String lat, lon;
    Location location;
    Location location1;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    Double lattitude;
    Double longditude;
    int REQUEST_PLACE_PICKER = 100;
    View view;
    @BindView(R.id.tvNearByMe)
    CustomTextView tvNearByMe;
    @BindView(R.id.tvNearByLocation)
    CustomTextView tvNearByLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_near_by_me);
        ButterKnife.bind(this);
        buildGoogleApiClient();
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();

        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 2;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        if (ActivityCompat.checkSelfPermission(PlaceNearByMeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PlaceNearByMeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        location = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (location != null) {
            lat = String.valueOf(location.getLatitude());
            lon = String.valueOf(location.getLongitude());
            location1 = new Location("");

            lattitude = location.getLatitude();
            longditude = location.getLongitude();

            location1.setLatitude(lattitude);
            location1.setLongitude(longditude);

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        buildGoogleApiClient();

    }

    @Override
    public void onLocationChanged(Location location) {
        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());
        location1 = new Location("");

        lattitude = location.getLatitude();
        longditude = location.getLongitude();

        location1.setLatitude(lattitude);
        location1.setLongitude(longditude);
    }

    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(PlaceNearByMeActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @OnClick({R.id.tvNearByMe, R.id.tvNearByLocation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvNearByMe:
                sendIntentToCategoryListActivity(lattitude, longditude);
                break;
            case R.id.tvNearByLocation:
                pickOtherLocation(PLACE_AUTOCOMPLETE_REQUEST_CODE);
                //sendIntentToCategoryListActivity(lattitude, longditude);
                break;
        }
    }

    private void pickOtherLocation(int requestCOde) {

        try {
            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(PlaceNearByMeActivity.this);
            // Start the Intent by requesting a result, identified by a request code.
            startActivityForResult(intent, requestCOde);


        } catch (GooglePlayServicesRepairableException e) {

            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), PlaceNearByMeActivity.this, 0);
        } catch (GooglePlayServicesNotAvailableException e) {

        }
    }

    private void sendIntentToCategoryListActivity(double lati, double loni) {
        Intent intent = new Intent(PlaceNearByMeActivity.this, CategoryListActivity.class);
        intent.putExtra("lat", lati);
        intent.putExtra("lon", loni);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PLACE_AUTOCOMPLETE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    final Place place = PlaceAutocomplete.getPlace(this, data);
                    lattitude = place.getLatLng().latitude;
                    longditude = place.getLatLng().longitude;
                    sendIntentToCategoryListActivity(lattitude, longditude);
                    break;
                }

        }

    }
}