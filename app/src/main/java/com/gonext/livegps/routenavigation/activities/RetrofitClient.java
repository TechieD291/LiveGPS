package com.gonext.livegps.routenavigation.activities;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.gonext.livegps.routenavigation.utils.Constants.API_BASE_URL;

/**
 * Created by mishti on 7/6/17.
 */

public class RetrofitClient
{
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit==null)
        {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            Retrofit.Builder builder =
                    new Retrofit.Builder()
                            .baseUrl(API_BASE_URL)
                            .addConverterFactory(
                                    GsonConverterFactory.create()
                            );

            retrofit =
                    builder.client(httpClient.build()).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
