package com.gonext.live.gps.navigation.activities;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.gonext.live.gps.navigation.R;
import com.gonext.live.gps.navigation.utils.PermissionUtils;
import com.gonext.live.gps.navigation.utils.PopUtils;
import com.gonext.live.gps.navigation.utils.view.CustomTextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationActivity extends BaseActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    Location location;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE};
    int PERMISSION = 0;
    Double lattitude;
    Double longditude;

    String address, city, country, state, lat, lon;
    @BindView(R.id.tvLatitude)
    CustomTextView tvLatitude;
    @BindView(R.id.tvLongitude)
    CustomTextView tvLongitude;
    @BindView(R.id.tvAddress)
    CustomTextView tvAddress;
    @BindView(R.id.tvCountry)
    CustomTextView tvCountry;
    @BindView(R.id.ivShare)
    ImageView ivShare;
    @BindView(R.id.ivCopy)
    ImageView ivCopy;
    @BindView(R.id.ivMap)
    ImageView ivMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);

        if (PermissionUtils.hasPermissions(LocationActivity.this, PERMISSIONS)) {
            buildGoogleApiClient();

            PopUtils.showProgressDialog(LocationActivity.this);
        } else {
            shouldShowRequestPermissions();
        }
    }

    @Override
    protected void onStart() {
        if (PermissionUtils.hasPermissions(LocationActivity.this, PERMISSIONS)) {
            if (mGoogleApiClient == null) {
                buildGoogleApiClient();
            }
            mGoogleApiClient.connect();
        } else {
            shouldShowRequestPermissions();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());

        lattitude = location.getLatitude();
        longditude = location.getLongitude();
        geocoderAddressfind(lattitude, longditude);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        PopUtils.hideProgressDialog();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

            lattitude = location.getLatitude();
            longditude = location.getLongitude();

            geocoderAddressfind(lattitude, longditude);
        }

    }

    private void geocoderAddressfind(Double lattitude, Double longditude) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
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
                insertValueInTextview();
            }

        } catch (IOException ioException) {
            ioException.getStackTrace();
        }

    }

    private void insertValueInTextview() {
        tvLatitude.setText("" + lattitude);
        tvLongitude.setText("" + longditude);
        tvAddress.setText(address + ", \n" + city + ", \n" + state);
        tvCountry.setText(country);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        buildGoogleApiClient();
    }


    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(LocationActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @OnClick({R.id.ivShare, R.id.ivCopy, R.id.ivMap})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivShare:
                shareLocation();
                break;
            case R.id.ivCopy:
                copydetail();
                break;
            case R.id.ivMap:
                shareToMap();
                break;
        }
    }

    private void copydetail() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Address", address + ", \n" + city + ", \n" + state + "\n" + country);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "copy Address", Toast.LENGTH_LONG).show();

    }

    private void shareToMap() {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?q=" + lat + "," + lon + " (" + address + ")"));
        startActivity(intent);
    }

    private void shareLocation() {
        String location = "http://maps.google.com/maps?q=" + lat + "," + lon + " (" + address + ")" + "\n" + address + ", \n" + city + ", \n" + state + "\n" + country;
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, location);
        startActivity(Intent.createChooser(sharingIntent, "Location"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mGoogleApiClient != null) {
                    mGoogleApiClient.connect();
                } else {
                    buildGoogleApiClient();
                    CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            mGoogleApiClient.connect();
                        }
                    }.start();

                }

            } else {
                PopUtils.showCustomTwoButtonAlertDialog(LocationActivity.this, "Permission", "location permission is require To use this application", "Permission", "Finish", true, false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


            }

        }
    }

    public void shouldShowRequestPermissions() {
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
