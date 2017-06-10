package com.gonext.live.gps.navigation.models;

/**
 * Created by mishti on 8/6/17.
 */

public class EndLocation_
{
    private double lat;
    private double lng;

    /**
     * No args constructor for use in serialization
     *
     */
    public EndLocation_() {
    }

    /**
     *
     * @param lng
     * @param lat
     */
    public EndLocation_(double lat, double lng) {
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
