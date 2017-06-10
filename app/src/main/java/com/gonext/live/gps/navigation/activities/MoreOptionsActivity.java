package com.gonext.live.gps.navigation.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;

import com.gonext.live.gps.navigation.R;
import com.gonext.live.gps.navigation.sqllite.SqlLiteDbHelper;
import com.gonext.live.gps.navigation.utils.StaticUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoreOptionsActivity extends BaseActivity {


    SqlLiteDbHelper dbHelper = new SqlLiteDbHelper(this);

    private static final String TAG = MoreOptionsActivity.class.getSimpleName();
    @BindView(R.id.llShowRoute)
    LinearLayout llShowRoute;
    @BindView(R.id.llStreetView)
    LinearLayout llStreetView;
    @BindView(R.id.llPlaceNearByMe)
    LinearLayout llPlaceNearByMe;
    @BindView(R.id.llMyLocation)
    LinearLayout llMyLocation;
    @BindView(R.id.llPrivacyPolicy)
    LinearLayout llPrivacyPolicy;
    @BindView(R.id.llRate)
    LinearLayout llRate;


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

    @OnClick({R.id.llShowRoute, R.id.llStreetView, R.id.llPlaceNearByMe, R.id.llMyLocation, R.id.llPrivacyPolicy, R.id.llRate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llShowRoute:
                senIntent(ShowRouteActivity.class.getName());
                break;
            case R.id.llStreetView:
                senIntent(StreetViewActivity.class.getName());
                break;
            case R.id.llPlaceNearByMe:
                senIntent(PlaceNearByMeActivity.class.getName());
                break;
            case R.id.llMyLocation:
                senIntent(LocationActivity.class.getName());
                break;
            case R.id.llPrivacyPolicy:
                senIntent(PrivacyPolicy.class.getName());
                break;
            case R.id.llRate:
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
