package com.gonext.livegps.routenavigation.models;

/**
 * Created by mishti on 7/6/17.
 */

public class Southwest
{
    private double lat;
    private double lng;

    /**
     * No args constructor for use in serialization
     *
     */
    public Southwest() {
    }

    /**
     *
     * @param lng
     * @param lat
     */
    public Southwest(double lat, double lng) {
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
