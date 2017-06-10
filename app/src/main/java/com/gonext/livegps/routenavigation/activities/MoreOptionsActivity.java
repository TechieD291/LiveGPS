package com.gonext.livegps.routenavigation.activities;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.gonext.livegps.routenavigation.R;
import com.gonext.livegps.routenavigation.sqllite.SqlLiteDbHelper;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoreOptionsActivity extends BaseActivity {


    private static final String TAG = MoreOptionsActivity.class.getSimpleName();
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ivShowRoute)
    ImageView ivShowRoute;
    @BindView(R.id.ivStreetView)
    ImageView ivStreetView;
    @BindView(R.id.ivPlaceNearByMe)
    ImageView ivPlaceNearByMe;
    @BindView(R.id.ivMyLocation)
    ImageView ivMyLocation;
    @BindView(R.id.ivPrivacyPolicy)
    ImageView ivPrivacyPolicy;
    @BindView(R.id.ivRate)
    ImageView ivRate;
    SqlLiteDbHelper dbHelper = new SqlLiteDbHelper(this);

    private double source_lat, source_lng;
    private double dest_lat, dest_lng;
    private String address = "";
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_options);
        ButterKnife.bind(this);
        initDatabase();
        Intent i = getIntent();
        source_lat = i.getDoubleExtra("source_Latitude", 0.0);
        source_lng = i.getDoubleExtra("source_Longitude", 0.0);

        Log.d("Source Loc options", source_lat + "," + source_lng);

        dest_lat = i.getDoubleExtra("dest_Latitude", 0.0);
        dest_lng = i.getDoubleExtra("dest_Longitude", 0.0);

        Log.d("Destination Loc options", dest_lat + "," + dest_lng);

        address = i.getStringExtra("title");
        flag = i.getBooleanExtra("place_selection", false);

    }

    private void initDatabase() {
        dbHelper = new SqlLiteDbHelper(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!prefs.getBoolean("firstTime", false)) {
            SharedPreferences.Editor editor1 = prefs.edit();
            editor1.putBoolean("firstTime", true);
            editor1.commit();

            try {

                dbHelper.CopyDataBaseFromAsset();
            } catch (IOException e) {
                e.printStackTrace();
            }
            dbHelper.openDataBase();
        }
    }

    @OnClick({R.id.ivShowRoute, R.id.ivStreetView, R.id.ivBack, R.id.ivRate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivShowRoute:

                Intent showRoute = new Intent(MoreOptionsActivity.this, ShowRouteActivity.class);
                showRoute.putExtra("source_Latitude", source_lat);
                showRoute.putExtra("source_Longitude", source_lng);

                showRoute.putExtra("dest_Latitude", dest_lat);
                showRoute.putExtra("dest_Longitude", dest_lng);

                startActivity(showRoute);
                break;

            case R.id.ivStreetView:

                Intent streetView = new Intent(MoreOptionsActivity.this, StreetViewActivity.class);

                if (flag == false) {
                    streetView.putExtra("source_Latitude", source_lat);
                    streetView.putExtra("source_Longitude", source_lng);
                } else {
                    streetView.putExtra("source_Latitude", dest_lat);
                    streetView.putExtra("source_Longitude", dest_lng);
                }

                startActivity(streetView);

                break;

            case R.id.ivRate:
                break;

            case R.id.ivBack:
                Intent backIntent = new Intent(MoreOptionsActivity.this, MapActivity.class);
                startActivity(backIntent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent(MoreOptionsActivity.this, MapActivity.class);
        backIntent.putExtra("tag", TAG);
        backIntent.putExtra("lat", dest_lat + "");
        backIntent.putExtra("lng", dest_lng + "");
        startActivity(backIntent);
    }

}
