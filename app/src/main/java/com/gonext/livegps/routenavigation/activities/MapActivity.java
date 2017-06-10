package com.gonext.livegps.routenavigation.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;

import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.gonext.livegps.routenavigation.R;


import static com.gonext.livegps.routenavigation.utils.Utils.hasPermissions;


public class MapActivity extends BaseActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    @BindView(R.id.tvActivityTitle)
    TextView tvActivityTitle;
    @BindView(R.id.ibSearch)
    ImageButton ibSearch;
    @BindView(R.id.ibMenu)
    ImageButton ibMenu;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ibZoonIn)
    ImageButton ibZoonIn;
    @BindView(R.id.ibZoomOut)
    ImageButton ibZoomOut;
    @BindView(R.id.tvAddressDisplay)
    TextView tvAddressDisplay;
    @BindView(R.id.btMapType)
    Button btMapType;
    @BindView(R.id.btNearbyPlaces)
    Button btNearbyPlaces;

    private static final String TAG = MapActivity.class.getSimpleName();
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Check Permissions Now
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    //Intent for place autocomplete
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 2;

    //Intent for place picker
    private static final int PLACE_PICKER_REQUEST = 3;

    private boolean place_selection_flag = false;

    private Location mLastKnownLocation;
    private CameraPosition mCameraPosition;

    // A default location (Gujarat, India) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(22.2587, 71.1924);
    private static final int DEFAULT_ZOOM = 15;
    private boolean mLocationPermissionGranted;

    // The entry point to Google Play services, used by the Places API and Fused Location Provider.
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap googleMap;

    private Geocoder geocoder;
    private Marker mCurrLocationMarker;
    double latitude;
    double longitude;

    private String tag = "";

    private LatLng source = null, destination = null;

    TextView title;
    TextView snippet;
    TextView clickhere;
    View infoWindow;

    private String[] PERMISSIONS =
            {
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        tag = getIntent().getStringExtra("tag");

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

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (googleMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, googleMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Builds the map when the Google Play services client is successfully connected.
     */
    @Override
    public void onConnected(Bundle bundle) {

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Handles suspension of the connection to the Google Play services client.
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Play services connection suspended");
    }

    /**
     * Handles failure to connect to the Google Play services client.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Refer to the reference doc for ConnectionResult to see what error codes might
        // be returned in onConnectionFailed.
        Log.d(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {

        this.googleMap = googleMap;


        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(final Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                clickhere = ((TextView) infoWindow.findViewById(R.id.tvClickHere));
                clickhere.setText(R.string.click_here);

                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Intent intent = new Intent(MapActivity.this, MoreOptionsActivity.class);
                        if (source == null) {
                            source = new LatLng(mDefaultLocation.latitude, mDefaultLocation.longitude);
                        }
                        if (destination == null) {
                            destination = source;
                        }

                        intent.putExtra("source_Latitude", source.latitude);
                        intent.putExtra("source_Longitude", source.longitude);

                        intent.putExtra("dest_Latitude", destination.latitude);
                        intent.putExtra("dest_Longitude", destination.longitude);

                        intent.putExtra("title", marker.getTitle());

                        startActivity(intent);

                    }
                });

                return infoWindow;
            }
        });

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

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
        if (!hasPermissions(MapActivity.this, PERMISSIONS)) {
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
            mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            source = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
            googleMap.clear();
        }

        // Set the map's camera position to the current location of the device.
        if (mCameraPosition != null) {

            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
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
        }


    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i));
                }

                strReturnedAddress.append('\n').append(LATITUDE).append(", ").append(LONGITUDE);
                strAdd = strReturnedAddress.toString();
                Log.w("My Current address", "" + strReturnedAddress.toString());

            } else {
                Log.w("My Current address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current address", "Canont get Address!");
        }
        return strAdd;
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

        updateLocationUI();
    }

    private void updateLocationUI() {
        if (googleMap == null) {
            return;
        }

        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

        if (mLocationPermissionGranted) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            googleMap.setMyLocationEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PLACE_AUTOCOMPLETE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    final Place place = PlaceAutocomplete.getPlace(this, data);

                    place_selection_flag = true;

                    googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                        @Override
                        public View getInfoWindow(Marker marker) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {
                            // Inflate the layouts for the info window, title and snippet.
                            infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                                    (FrameLayout) findViewById(R.id.map), false);

                            title = ((TextView) infoWindow.findViewById(R.id.title));
                            title.setText(place.getAddress());

                            snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                            snippet.setText(place.getLatLng().latitude + "," + place.getLatLng().longitude);

                            clickhere = ((TextView) infoWindow.findViewById(R.id.tvClickHere));
                            clickhere.setText(R.string.click_here);

                            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                @Override
                                public void onInfoWindowClick(Marker marker) {
                                    Intent intent = new Intent(MapActivity.this, MoreOptionsActivity.class);
                                    if (source == null) {
                                        source = new LatLng(mDefaultLocation.latitude, mDefaultLocation.longitude);
                                    }
                                    if (destination == null) {
                                        destination = source;
                                    }

                                    intent.putExtra("source_Latitude", source.latitude);
                                    intent.putExtra("source_Longitude", source.longitude);

                                    intent.putExtra("dest_Latitude", destination.latitude);
                                    intent.putExtra("dest_Longitude", destination.longitude);

                                    intent.putExtra("title", place.getAddress());
                                    intent.putExtra("place_selection", place_selection_flag);

                                    startActivity(intent);

                                }
                            });

                            return infoWindow;
                        }
                    });

                    destination = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);

                    String strAddress = place.getAddress().toString() + '\n' + place.getLatLng().latitude + "," + place.getLatLng().longitude;
                    tvAddressDisplay.setText(strAddress);

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(place.getLatLng().latitude,
                                    place.getLatLng().longitude), DEFAULT_ZOOM));

                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(place.getLatLng())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                    googleMap.clear();
                    mCurrLocationMarker = googleMap.addMarker(markerOptions);
                    mCurrLocationMarker.showInfoWindow();
                    Log.i(TAG, "Place: " + place.getName());

                } else {
                    Toast.makeText(this, "No location selected", Toast.LENGTH_SHORT).show();
                }
                break;
            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);
                    String toastMsg = String.format("Place: %s", place.getName());
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d("onLocationChanged", "entered");

        mLastKnownLocation = location;

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");

        googleMap.clear();

        mCurrLocationMarker = googleMap.addMarker(markerOptions);

        //move map camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        Toast.makeText(MapActivity.this, "Your Current Location", Toast.LENGTH_LONG).show();

        Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f", latitude, longitude));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }

        Log.d("onLocationChanged", "Exit");

    }

    @OnClick({R.id.ibSearch, R.id.ibMenu, R.id.ibZoonIn, R.id.ibZoomOut, R.id.btMapType, R.id.btNearbyPlaces})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibSearch:



                break;
            case R.id.ibMenu:

                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MapActivity.this, ibMenu);

                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_current_place, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //showCurrentPlace();
                        Toast.makeText(MapActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                //showing popup menu
                popup.show();

                break;

            case R.id.ibZoonIn:

                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                break;

            case R.id.ibZoomOut:
                googleMap.animateCamera(CameraUpdateFactory.zoomOut());
                break;

            case R.id.btMapType:

                //Creating the instance of PopupMenu for changing Map Type
                PopupMenu popMapType = new PopupMenu(MapActivity.this, btMapType);

                //Inflating the Popup using xml file
                popMapType.getMenuInflater().inflate(R.menu.menu_map_type, popMapType.getMenu());

                //registering popup with OnMenuItemClickListener
                popMapType.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getTitle().toString()) {
                            case "Normal":
                                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                break;

                            case "Hybrid":
                                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                                break;

                            case "Terrain":
                                googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                break;

                            case "Satellite":
                                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                break;

                            case "None":
                                googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                                break;

                            default:
                                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                break;

                        }

                        return true;
                    }
                });

                //showing popup menu
                popMapType.show();

                break;

            case R.id.btNearbyPlaces:

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
