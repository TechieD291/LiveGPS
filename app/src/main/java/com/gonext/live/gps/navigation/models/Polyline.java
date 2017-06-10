package com.gonext.live.gps.navigation.models;

/**
 * Created by mishti on 8/6/17.
 */

public class Polyline
{
    private String points;

    /**
     * No args constructor for use in serialization
     *
     */
    public Polyline() {
    }

    /**
     *
     * @param points
     */
    public Polyline(String points) {
        super();
        this.points = points;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

}
