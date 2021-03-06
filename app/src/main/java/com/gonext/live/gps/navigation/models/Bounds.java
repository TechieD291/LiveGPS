package com.gonext.live.gps.navigation.models;

/**
 * Created by mishti on 8/6/17.
 */

public class Bounds
{
    private Northeast northeast;
    private Southwest southwest;

    /**
     * No args constructor for use in serialization
     *
     */
    public Bounds() {
    }

    /**
     *
     * @param southwest
     * @param northeast
     */
    public Bounds(Northeast northeast, Southwest southwest) {
        super();
        this.northeast = northeast;
        this.southwest = southwest;
    }

    public Northeast getNortheast() {
        return northeast;
    }

    public void setNortheast(Northeast northeast) {
        this.northeast = northeast;
    }

    public Southwest getSouthwest() {
        return southwest;
    }

    public void setSouthwest(Southwest southwest) {
        this.southwest = southwest;
    }

}
