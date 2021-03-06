package com.gonext.live.gps.navigation.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.BuildConfig;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Lenovo on 17-04-2017.
 */

public class StaticUtils {

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }

        return false;
    }

    /**
     * Method to check internet connection.
     *
     * @param context context
     * @return true/false
     */
    public static boolean isConnectingToInternet(Context context) {
        try {
            ConnectivityManager connectivityManager = null;
            try {
                connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            if (connectivityManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Network[] networks = connectivityManager.getAllNetworks();
                    NetworkInfo networkInfo;
                    for (Network mNetwork : networks) {
                        networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                        if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                            return true;
                        }
                    }
                } else {

                    //noinspection deprecation
                    NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                    if (info != null) {
                        for (NetworkInfo anInfo : info) {
                            if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                                Log.d("Utils", "isConnectingToInternet: NETWORKNAME=" + anInfo.getTypeName());
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Method to navigate playstore with user entered package name
     *
     * @param context     context of activity as well as frgament
     * @param packageName package name what will navigate in playstore based on this package name
     */
    public static void navigateToPlayStoreWithSpecificPackage(Context context, String packageName) {
        try {
            Uri uri = Uri.parse("market://details?id=" + packageName + "&hl=en");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            } else {
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            }
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to check app is currently running or not
     *
     * @param context
     * @return
     */
    public static boolean isAppRunning(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }


    /**
     * This method is used set window dimensions
     *
     * @param context context of current screen.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void setWindowDimensions(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        StaticData.SCREEN_WIDTH = size.x;
        StaticData.SCREEN_HEIGHT = size.y;
    }

    /**
     * This method is used hide keyboard
     *
     * @param activity instance of associated activity
     */
    public static void hideKeyboard(FragmentActivity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used show keyboard
     *
     * @param activity instance of associated activity
     */
    public static void showKeyboard(FragmentActivity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * This method is used show keyboard
     *
     * @param activity instance of associated activity
     */
    public static void showKeyboard(FragmentActivity activity, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(view.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }


    /**
     * This method is used hide keyboard
     *
     * @param activity instance of associated activity
     * @param view     instance of view
     */
    public static void hideKeyboard(FragmentActivity activity, View view) {
        try {
            if (activity != null && view != null) {
                InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            Log.e("KeyBoardUtil", e.toString());
        }
    }

    /**
     * method to get device mac address
     *
     * @return string of device mac address
     */
    public static String getDveviceMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF)).append(":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    /**
     * Method to code text in clip board
     *
     * @param context context
     * @param text    text what wan to copy in clipboard
     * @param label   label what want to copied
     */
    public static void copyCodeInClipBoard(Context context, String text, String label) {
        if (context != null) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(label, text);
            clipboard.setPrimaryClip(clip);

        }
    }

    /**
     * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
     *
     * @param context Context reference to get the TelephonyManager instance from
     * @return country code or null
     */
    private static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                Locale loc = new Locale("", simCountry.toLowerCase(Locale.US));

                return loc.getDisplayCountry();
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    Locale loc = new Locale("", networkCountry.toLowerCase(Locale.US));
                    return loc.getDisplayCountry();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get user's current conuntry. if user has not simcard detected then will return country from device.
     *
     * @param context context
     * @return device's current country name
     */
    public static String getUserDeviceCountry(Context context) {

        String userCountry = getUserCountry(context);

        if (userCountry == null) {
            userCountry = context.getResources().getConfiguration().locale.getDisplayCountry();
        }
        return userCountry;
    }

    /**
     * Method to check third party application is installed in device or not.
     *
     * @param activity    Instance on the activity
     * @param packageName Third party application package name to chaek is it available in device.
     * @return true if requested package name is installed in device alfe false
     */
    public static boolean isAppInstalled(Activity activity, String packageName) {
        PackageManager pm = activity.getPackageManager();
        boolean installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    public static HashMap<String, Object> getDeviceInfo() {
        HashMap<String, Object> mDeviceInfoParams = new HashMap<>();
        try {
            mDeviceInfoParams.put("ManuFacturer", Build.MANUFACTURER);
            mDeviceInfoParams.put("Serial", Build.SERIAL);
            mDeviceInfoParams.put("Model", Build.MODEL);
            mDeviceInfoParams.put("Id", Build.ID);
            mDeviceInfoParams.put("Brand", Build.BRAND);
            mDeviceInfoParams.put("Type", Build.TYPE);
            mDeviceInfoParams.put("Incremental", Build.VERSION.INCREMENTAL);
            mDeviceInfoParams.put("Board", Build.BOARD);
            mDeviceInfoParams.put("Host", Build.HOST);
            mDeviceInfoParams.put("VersionCode", Build.VERSION.RELEASE);
            mDeviceInfoParams.put("AppVersion", BuildConfig.VERSION_NAME);
            mDeviceInfoParams.put("Display", Build.DISPLAY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mDeviceInfoParams;
    }

    public static JSONObject getDeviceInfoInJSON() {
        JSONObject deviceInfoJObject = new JSONObject();
        try {
            deviceInfoJObject.put("ManuFacturer", Build.MANUFACTURER);
            deviceInfoJObject.put("Serial", Build.SERIAL);
            deviceInfoJObject.put("Model", Build.MODEL);
            deviceInfoJObject.put("Id", Build.ID);
            deviceInfoJObject.put("Brand", Build.BRAND);
            deviceInfoJObject.put("Type", Build.TYPE);
            deviceInfoJObject.put("Incremental", Build.VERSION.INCREMENTAL);
            deviceInfoJObject.put("Board", Build.BOARD);
            deviceInfoJObject.put("Host", Build.HOST);
            deviceInfoJObject.put("VersionCode", Build.VERSION.RELEASE);
            deviceInfoJObject.put("AppVersion", BuildConfig.VERSION_NAME);
            deviceInfoJObject.put("Display", Build.DISPLAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return deviceInfoJObject;
    }

    public static boolean isLocationEnabled(Context context) {
        String le = Context.LOCATION_SERVICE;
        LocationManager locationManager = (LocationManager) context.getSystemService(le);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return false;
        } else {
            return true;
        }
    }

    public static void rateapp(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }


    /**
     * This method is for go to settings screen for perticular app:
     *
     * @param context     pass the context
     * @param packageName pass the perticular app packagename
     */
    public static void startInstalledAppDetailsActivity(final Activity context, String packageName) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + packageName));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }
}
