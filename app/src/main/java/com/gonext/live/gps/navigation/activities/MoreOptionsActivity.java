package com.gonext.live.gps.navigation.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gonext.live.gps.navigation.R;
import com.gonext.live.gps.navigation.sqllite.SqlLiteDbHelper;
import com.gonext.live.gps.navigation.utils.PopUtils;
import com.gonext.live.gps.navigation.utils.StaticUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

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

    @BindView(R.id.ad_view)
    AdView adView;
    @BindView(R.id.rlAds)
    RelativeLayout rlAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_options);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (rlAds != null) {
                    rlAds.setVisibility(View.VISIBLE);
                }
                super.onAdLoaded();

            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();

            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }
        });

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
                if (StaticUtils.isLocationEnabled(MoreOptionsActivity.this)) {
                    senIntent(ShowRouteActivity.class.getName());
                } else {
                    PopUtils.showCustomTwoButtonAlertDialog(MoreOptionsActivity.this, getString(R.string.dialog_location_title), getString(R.string.dialog_location_message), getString(R.string.dialog_location_positive_button), getString(R.string.dialog_location_nagative_button), true, false,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                }
                break;
            case R.id.llStreetView:
                if (StaticUtils.isLocationEnabled(MoreOptionsActivity.this)) {

                    senIntent(StreetViewActivity.class.getName());
                } else {
                    PopUtils.showCustomTwoButtonAlertDialog(MoreOptionsActivity.this, getString(R.string.dialog_location_title), getString(R.string.dialog_location_message), getString(R.string.dialog_location_positive_button), getString(R.string.dialog_location_nagative_button), true, false,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                }
                break;
            case R.id.llPlaceNearByMe:
                if (StaticUtils.isLocationEnabled(MoreOptionsActivity.this)) {

                    senIntent(PlaceNearByMeActivity.class.getName());
                } else {
                    PopUtils.showCustomTwoButtonAlertDialog(MoreOptionsActivity.this, getString(R.string.dialog_location_title), getString(R.string.dialog_location_message), getString(R.string.dialog_location_positive_button), getString(R.string.dialog_location_nagative_button), true, false,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                }

                break;
            case R.id.llMyLocation:
                if (StaticUtils.isLocationEnabled(MoreOptionsActivity.this)) {

                    senIntent(LocationActivity.class.getName());
                } else {
                    PopUtils.showCustomTwoButtonAlertDialog(MoreOptionsActivity.this, getString(R.string.dialog_location_title), getString(R.string.dialog_location_message), getString(R.string.dialog_location_positive_button), getString(R.string.dialog_location_nagative_button), true, false,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                }

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
