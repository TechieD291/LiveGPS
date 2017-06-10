package com.gonext.live.gps.navigation.models;

import java.util.List;

/**
 * Created by mishti on 8/6/17.
 */

public class RouteExample
{
    private List<GeocodedWaypoint> geocoded_waypoints = null;
    private List<Route> routes = null;
    private String status;

    /**
     * No args constructor for use in serialization
     *
     */
    public RouteExample() {
    }

    /**
     *
     * @param status
     * @param routes
     * @param geocoded_waypoints
     */
    public RouteExample(List<GeocodedWaypoint> geocoded_waypoints, List<Route> routes, String status) {
        super();
        this.geocoded_waypoints = geocoded_waypoints;
        this.routes = routes;
        this.status = status;
    }

    public List<GeocodedWaypoint> getGeocodedWaypoints() {
        return geocoded_waypoints;
    }

    public void setGeocodedWaypoints(List<GeocodedWaypoint> geocodedWaypoints) {
        this.geocoded_waypoints = geocodedWaypoints;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
