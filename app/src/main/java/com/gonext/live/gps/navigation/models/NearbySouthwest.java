package com.gonext.live.gps.navigation.models;

/**
 * Created by mishti on 7/6/17.
 */

public class NearbySouthwest
{
    private double lat;
    private double lng;

    /**
     * No args constructor for use in serialization
     *
     */
    public NearbySouthwest() {
    }

    /**
     *
     * @param lng
     * @param lat
     */
    public NearbySouthwest(double lat, double lng) {
        super();
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
