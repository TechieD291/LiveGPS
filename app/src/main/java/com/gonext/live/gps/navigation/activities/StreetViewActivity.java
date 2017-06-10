package com.gonext.livegps.routenavigation.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gonext.livegps.routenavigation.R;
import com.gonext.livegps.routenavigation.utils.view.CustomTextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.gonext.livegps.routenavigation.utils.Utils.hasPermissions;

public class StreetViewActivity extends BaseActivity
        implements StreetViewPanorama.OnStreetViewPanoramaChangeListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.tvLocation)
    CustomTextView tvLocation;

    private static final String TAG = StreetViewActivity.class.getSimpleName();

    // Check Permissions Now
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    //Get Source Request Code
    private static final int GET_LOCATION_REQUEST_CODE = 2;

    private StreetViewPanorama streetViewPanorama;

    private Location mCurrentLocation;
    private LatLng location;

    //String for getAddress method
    Double latitude;
    Double longitude;

    String address, city, country, state;

    private Bundle savedInstanceState;

    private boolean mLocationPermissionGranted;

    // The entry point to Google Play services, used by the Places API and Fused Location Provider.
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap googleMap;

    private String[] PERMISSIONS =
            {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street_view);
        ButterKnife.bind(this);

        this.savedInstanceState = savedInstanceState;

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
    public void onStreetViewPanoramaChange(StreetViewPanoramaLocation streetViewPanoramaLocation) {

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

        if (!hasPermissions(StreetViewActivity.this, PERMISSIONS)) {
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
            location = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        } else {

        }
    }

     /* @param lattitude  Double latitude value
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
                    this.latitude = addressList.get(0).getLatitude();
                    this.longitude = addressList.get(0).getLongitude();
                }

            }

            final_address = address + " " + city + "," + state + "," + country;

        } catch (IOException ioException) {
            ioException.getStackTrace();
        }

        return final_address;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case GET_LOCATION_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    final Place place = PlaceAutocomplete.getPlace(this, data);

                    location = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);

                    tvLocation.setText(geocoderAddressfind(location.latitude, location.longitude));
                    streetViewPanorama.setPosition(location);
                }
                else
                {
                    getDeviceLocation();
                    tvLocation.setText(geocoderAddressfind(location.latitude, location.longitude));

                    streetViewPanorama.setPosition(location);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getDeviceLocation();

        SupportStreetViewPanoramaFragment streetViewPanoramaFragment =
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

                        getDeviceLocation();
                        tvLocation.setText(geocoderAddressfind(location.latitude, location.longitude));

                        streetViewPanorama.setPosition(location);

                        // Only need to set the position once as the streetview fragment will maintain
                        // its state.
                        if (savedInstanceState == null) {
                            streetViewPanorama.setPosition(location);
                        }
                    }
                });
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

    @OnClick(R.id.tvLocation)
    public void onViewClicked()
    {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, GET_LOCATION_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
            // Refer to the reference doc for ConnectionResult to see what error codes might
            // be returned in onConnectionFailed.
            Log.d(TAG, "Play services connection failed");
        }
    }
}
