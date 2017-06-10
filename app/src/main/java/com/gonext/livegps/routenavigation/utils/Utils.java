package com.gonext.livegps.routenavigation.utils;


import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * Created by mishti on 5/6/17.
 */

public class Utils
{

    /**
     *
     * @return boolean value stating whether internet connectivity is available or not
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected();
    }


    /**
     *
     * @param context reference to the calling activity
     * @param permissions String array of the permissions requested
     * @return returns the boolean value true if permissions granted and false if not granted
     */
    public static boolean hasPermissions(Context context, String... permissions)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions)
            {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                {
                    return false;
                }
            }
        }
        return true;
    }

}
