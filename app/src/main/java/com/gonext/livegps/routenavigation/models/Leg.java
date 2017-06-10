package com.gonext.livegps.routenavigation.models;

import java.util.List;

/**
 * Created by mishti on 8/6/17.
 */

public class Leg
{
    private Distance distance;
    private Duration duration;
    private String end_address;
    private EndLocation end_location;
    private String start_address;
    private StartLocation start_location;
    private List<Step> steps = null;
    private List<Object> via_waypoint = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Leg() {
    }

    /**
     *
     * @param start_address
     * @param duration
     * @param distance
     * @param end_location
     * @param start_location
     * @param steps
     * @param end_address
     * @param via_waypoint
     */
    public Leg(Distance distance, Duration duration, String end_address, EndLocation end_location, String start_address, StartLocation start_location, List<Step> steps, List<Object> via_waypoint) {
        super();
        this.distance = distance;
        this.duration = duration;
        this.end_address = end_address;
        this.end_location = end_location;
        this.start_address = start_address;
        this.start_location = start_location;
        this.steps = steps;
        this.via_waypoint = via_waypoint;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getEndAddress() {
        return end_address;
    }

    public void setEndAddress(String end_address) {
        this.end_address = end_address;
    }

    public EndLocation getEndLocation() {
        return end_location;
    }

    public void setEndLocation(EndLocation end_location) {
        this.end_location = end_location;
    }

    public String getStartAddress() {
        return start_address;
    }

    public void setStartAddress(String startAddress) {
        this.start_address = start_address;
    }

    public StartLocation getStartLocation() {
        return start_location;
    }

    public void setStartLocation(StartLocation start_location) {
        this.start_location = start_location;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public List<Object> getViaWaypoint() {
        return via_waypoint;
    }

    public void setViaWaypoint(List<Object> via_waypoint) {
        this.via_waypoint = via_waypoint;
    }
}
