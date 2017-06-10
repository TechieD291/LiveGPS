package com.gonext.live.gps.navigation.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;

import com.gonext.live.gps.navigation.R;
import com.gonext.live.gps.navigation.sqllite.SqlLiteDbHelper;
import com.gonext.live.gps.navigation.utils.StaticUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoreOptionsActivity extends BaseActivity {


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

    private static final String TAG = MoreOptionsActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_options);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initDatabase();
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick({R.id.ivShowRoute, R.id.ivStreetView, R.id.ivPlaceNearByMe, R.id.ivMyLocation, R.id.ivPrivacyPolicy, R.id.ivRate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivShowRoute:
                senIntent(ShowRouteActivity.class.getName());
                break;
            case R.id.ivStreetView:

                break;
            case R.id.ivPlaceNearByMe:
                senIntent(PlaceNearByMeActivity.class.getName());
                break;
            case R.id.ivMyLocation:
                senIntent(LocationActivity.class.getName());
                break;
            case R.id.ivPrivacyPolicy:
                senIntent(PrivacyPolicy.class.getName());
                break;
            case R.id.ivRate:
                StaticUtils.rateapp(getApplicationContext());
                break;
        }
    }

    private void senIntent(String name) {
        try {
            startActivity(new Intent(MoreOptionsActivity.this, Class.forName(name)));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
