package com.gonext.livegps.routenavigation.models;

/**
 * Created by mishti on 8/6/17.
 */

public class OverviewPolyline
{
    private String points;

    /**
     * No args constructor for use in serialization
     *
     */
    public OverviewPolyline() {
    }

    /**
     *
     * @param points
     */
    public OverviewPolyline(String points) {
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
