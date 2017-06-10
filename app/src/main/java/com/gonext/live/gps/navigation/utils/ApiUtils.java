package com.gonext.live.gps.navigation.utils;

import com.gonext.live.gps.navigation.activities.RetrofitClient;
import com.gonext.live.gps.navigation.interfaces.RetrofitMapInterface;

import static com.gonext.live.gps.navigation.utils.Constants.API_BASE_URL;

/**
 * Created by mishti on 7/6/17.
 */

public class ApiUtils
{
    private ApiUtils() {}

    public static RetrofitMapInterface getAPIService()
    {

        return RetrofitClient.getClient(API_BASE_URL).create(RetrofitMapInterface.class);

    }
}
