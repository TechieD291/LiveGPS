package com.gonext.live.gps.navigation.interfaces;

import java.util.HashMap;

import com.gonext.live.gps.navigation.models.RouteExample;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by mishti on 7/6/17.
 */

public interface RetrofitMapInterface
{

    @GET("directions/json?sensor=false&")
    Call<RouteExample> getRoute(@QueryMap HashMap<String, String> hashMap);

}
