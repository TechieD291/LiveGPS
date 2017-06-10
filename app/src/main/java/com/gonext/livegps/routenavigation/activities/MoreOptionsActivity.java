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
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.gonext.livegps.routenavigation.R;

public class MoreOptionsActivity extends BaseActivity {

    @BindView(R.id.ivShowRoute)
    ImageView ivShowRoute;
    @BindView(R.id.tvShowRoute)
    TextView tvShowRoute;
    @BindView(R.id.ivDirections)
    ImageView ivDirections;
    @BindView(R.id.tvDirections)
    TextView tvDirections;
    @BindView(R.id.ivStreetView)
    ImageView ivStreetView;
    @BindView(R.id.tvStreetView)
    TextView tvStreetView;
    @BindView(R.id.ivShareLocation)
    ImageView ivShareLocation;
    @BindView(R.id.tvShareLocation)
    TextView tvShareLocation;
    @BindView(R.id.ivCopyLocation)
    ImageView ivCopyLocation;
    @BindView(R.id.ivRate)
    ImageView ivRate;
    @BindView(R.id.ivBack)
    ImageView ivBack;

    private static final String TAG = MoreOptionsActivity.class.getSimpleName();

    private double source_lat, source_lng;
    private double dest_lat, dest_lng;
    private String address = "";
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_options);
        ButterKnife.bind(this);

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

    @OnClick({R.id.ivShowRoute, R.id.ivDirections, R.id.ivStreetView, R.id.ivShareLocation, R.id.ivCopyLocation, R.id.ivBack, R.id.ivRate})
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
            case R.id.ivDirections:

                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" + "saddr=" + source_lat + "," + source_lng + "&daddr=" + dest_lat + "," + dest_lng));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);

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
            case R.id.ivShareLocation:

                String location = "http://maps.google.com/maps?q=" + source_lat + "," + source_lng + "\n" + address;

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, location);
                startActivity(Intent.createChooser(sharingIntent, "Location"));

                break;
            case R.id.ivCopyLocation:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    ClipboardManager clipboard = (ClipboardManager)
                            this.getSystemService(this.CLIPBOARD_SERVICE);
                    clipboard.setText(address);
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                            this.getSystemService(this.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData
                            .newPlainText("Copied to clipboard", address);
                    clipboard.setPrimaryClip(clip);
                }

                Toast.makeText(this, "Location copied to clipboard", Toast.LENGTH_SHORT).show();

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
    public void onBackPressed()
    {
        Intent backIntent = new Intent(MoreOptionsActivity.this, MapActivity.class);
        backIntent.putExtra("tag",TAG);
        backIntent.putExtra("lat",dest_lat+"");
        backIntent.putExtra("lng",dest_lng+"");
        startActivity(backIntent);
    }

}
