package com.gonext.livegps.routenavigation.activities;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.gonext.livegps.routenavigation.R;

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

    private static final String TAG = MoreOptionsActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_options);
        ButterKnife.bind(this);

    }


    @Override
    public void onBackPressed()
    {
        finish();
    }

    @OnClick({R.id.ivShowRoute, R.id.ivStreetView, R.id.ivPlaceNearByMe, R.id.ivMyLocation, R.id.ivPrivacyPolicy, R.id.ivRate})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.ivShowRoute:
                Intent showRouteIntent = new Intent(MoreOptionsActivity.this, ShowRouteActivity.class);
                startActivity(showRouteIntent);
                break;
            case R.id.ivStreetView:
                break;
            case R.id.ivPlaceNearByMe:
                break;
            case R.id.ivMyLocation:
                break;
            case R.id.ivPrivacyPolicy:
                break;
            case R.id.ivRate:
                break;
        }
    }
}
