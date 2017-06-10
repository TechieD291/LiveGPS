package com.gonext.livegps.routenavigation.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.gonext.livegps.routenavigation.R;

public class SplashActivity extends BaseActivity {


    private static  final String TAG = SplashActivity.class.getSimpleName();
    private static int SPLASH_TIME_OUT = 2000;
    private static final int PERMISSION_REQUEST_CODE = 1;
    CountDownTimer waitTimer;

    private String[] permissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        waitTimer = new CountDownTimer(SPLASH_TIME_OUT, 1000) {

            public void onTick(long millisUntilFinished) {
                //called every 300 milliseconds, which could be used to
                //send messages or some other action

            }

            public void onFinish() {

                if (hasPermissions(SplashActivity.this, permissions)) {
                    sendIntentToMapActivity();
                } else {
                    shouldShowRequestPermissions();
                }
            }


        }.start();

    }

    private void sendIntentToMapActivity() {
        Intent intent = new Intent(SplashActivity.this, MapActivity.class);
        startActivity(intent);
        intent.putExtra("tag",TAG);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
sendIntentToMapActivity();
            } else {

            }

        }
    }
}
