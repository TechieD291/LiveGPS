package com.gonext.livegps.routenavigation.utils;

import com.gonext.livegps.routenavigation.activities.RetrofitClient;
import com.gonext.livegps.routenavigation.interfaces.RetrofitMapInterface;

import static com.gonext.livegps.routenavigation.utils.Constants.API_BASE_URL;

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
