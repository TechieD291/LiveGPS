package com.gonext.live.gps.navigation.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.gonext.live.gps.navigation.R;
import com.gonext.live.gps.navigation.utils.PermissionUtils;
import com.gonext.live.gps.navigation.utils.PopUtils;
import com.gonext.live.gps.navigation.utils.StaticUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class SplashActivity extends BaseActivity {
    boolean isEnable = false;

    boolean isIntentStart = false;
    private static final String TAG = SplashActivity.class.getSimpleName();
    private static int SPLASH_TIME_OUT = 15000;
    private static final int PERMISSION_REQUEST_CODE = 1;
    CountDownTimer waitTimer;
    InterstitialAd mInterstitialAd;

    private String[] permissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mInterstitialAd = new InterstitialAd(SplashActivity.this);
        mInterstitialAd.setAdUnitId(getString(R.string.intestial_ad_unit_id));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (!isIntentStart) {
                    sendIntentToMapActivity();
                }

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

                cancelTimer();
                new Handler().postDelayed(new Runnable() {
                    // Using handler with postDelayed called runnable run method
                    @Override
                    public void run() {
                        cancelTimer();
                        if (PermissionUtils.hasPermissions(SplashActivity.this, permissions)) {
                            if (!isIntentStart) {
                                sendIntentToMapActivity();
                            }
                        } else {
                            shouldShowRequestPermissions();
                        }


                    }
                }, 3 * 1000); // wait for 3 seconds

            }

            @Override
            public void onAdLoaded() {
                // Change the button text and enable the button.
                cancelTimer();
                if (PermissionUtils.hasPermissions(SplashActivity.this, permissions)) {
                    if (!isIntentStart) {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        }
                    }
                } else {
                    shouldShowRequestPermissions();
                }
            }

        });
        requestNewInterstitial();
        waitTimer = new CountDownTimer(SPLASH_TIME_OUT, 1000) {

            public void onTick(long millisUntilFinished) {
                //called every 300 milliseconds, which could be used to
                //send messages or some other action
            }

            public void onFinish() {

                if (PermissionUtils.hasPermissions(SplashActivity.this, permissions)) {
                    if (!isIntentStart) {
                        sendIntentToMapActivity();
                    }
                } else {
                    shouldShowRequestPermissions();
                }

            }
        }.start();


    }

    private void sendIntentToMapActivity() {
        Intent intent = new Intent(SplashActivity.this, MoreOptionsActivity.class);
        startActivity(intent);
        intent.putExtra("tag", TAG);
        finish();

    }

    public void shouldShowRequestPermissions() {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    public static boolean hasPermissions(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    private void cancelTimer() {
        if (waitTimer != null) {
            waitTimer.cancel();
            waitTimer = null;
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mInterstitialAd.loadAd(adRequest);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    if (!isIntentStart) {
                        sendIntentToMapActivity();
                    }
                }
            } else {

                PopUtils.showCustomTwoButtonAlertDialog(SplashActivity.this, "Permission", "location permission is require To use this application", "Permission", "Finish", true, false, new DialogInterface.OnClickListener() {
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


}
