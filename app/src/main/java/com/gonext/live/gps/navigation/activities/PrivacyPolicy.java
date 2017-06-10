package com.gonext.live.gps.navigation.activities;

import android.os.Bundle;
import android.webkit.WebView;


import com.gonext.live.gps.navigation.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrivacyPolicy extends BaseActivity {

    @BindView(R.id.wbPrivacy)
    WebView wbPrivacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        ButterKnife.bind(this);

        wbPrivacy.clearCache(true);
        wbPrivacy.getSettings().setJavaScriptEnabled(true);
        wbPrivacy.getSettings().setBuiltInZoomControls(true);
        wbPrivacy.setInitialScale(1);
        wbPrivacy.getSettings().setLoadWithOverviewMode(true);
        wbPrivacy.getSettings().setUseWideViewPort(true);
        wbPrivacy.loadUrl("file:///android_asset/gonextappsdeveolpers.html");
    }

}
