package com.gonext.live.gps.navigation.models;

import java.util.List;

/**
 * Created by mishti on 8/6/17.
 */

public class Route
{
    private Bounds bounds;
    private String copyrights;
    private List<Leg> legs = null;
    private OverviewPolyline overview_polyline;
    private String summary;
    private List<Object> warnings = null;
    private List<Object> waypoint_order = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Route() {
    }

    /**
     *
     * @param waypoint_order
     * @param summary
     * @param bounds
     * @param copyrights
     * @param legs
     * @param warnings
     * @param overview_polyline
     */
    public Route(Bounds bounds, String copyrights, List<Leg> legs, OverviewPolyline overview_polyline, String summary, List<Object> warnings, List<Object> waypoint_order) {
        super();
        this.bounds = bounds;
        this.copyrights = copyrights;
        this.legs = legs;
        this.overview_polyline = overview_polyline;
        this.summary = summary;
        this.warnings = warnings;
        this.waypoint_order = waypoint_order;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public String getCopyrights() {
        return copyrights;
    }

    public void setCopyrights(String copyrights) {
        this.copyrights = copyrights;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    public OverviewPolyline getOverviewPolyline() {
        return overview_polyline;
    }

    public void setOverviewPolyline(OverviewPolyline overview_polyline) {
        this.overview_polyline = overview_polyline;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Object> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<Object> warnings) {
        this.warnings = warnings;
    }

    public List<Object> getWaypointOrder() {
        return waypoint_order;
    }

    public void setWaypointOrder(List<Object> waypoint_order) {
        this.waypoint_order = waypoint_order;
    }
}
