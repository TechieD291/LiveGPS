package com.gonext.livegps.routenavigation.models;

/**
 * Created by mishti on 8/6/17.
 */

public class Step
{
    private Distance_ distance;
    private Duration_ duration;
    private EndLocation_ end_location;
    private String html_instructions;
    private Polyline polyline;
    private StartLocation_ start_location;
    private String travel_mode;
    private String maneuver;

    /**
     * No args constructor for use in serialization
     *
     */
    public Step() {
    }

    /**
     *
     * @param duration
     * @param distance
     * @param polyline
     * @param end_location
     * @param html_instructions
     * @param start_location
     * @param maneuver
     * @param travel_mode
     */
    public Step(Distance_ distance, Duration_ duration, EndLocation_ end_location, String html_instructions, Polyline polyline, StartLocation_ start_location, String travel_mode, String maneuver) {
        super();
        this.distance = distance;
        this.duration = duration;
        this.end_location = end_location;
        this.html_instructions = html_instructions;
        this.polyline = polyline;
        this.start_location = start_location;
        this.travel_mode = travel_mode;
        this.maneuver = maneuver;
    }

    public Distance_ getDistance() {
        return distance;
    }

    public void setDistance(Distance_ distance) {
        this.distance = distance;
    }

    public Duration_ getDuration() {
        return duration;
    }

    public void setDuration(Duration_ duration) {
        this.duration = duration;
    }

    public EndLocation_ getEndLocation() {
        return end_location;
    }

    public void setEndLocation(EndLocation_ end_location) {
        this.end_location = end_location;
    }

    public String getHtmlInstructions() {
        return html_instructions;
    }

    public void setHtmlInstructions(String html_instructions) {
        this.html_instructions = html_instructions;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public StartLocation_ getStartLocation() {
        return start_location;
    }

    public void setStartLocation(StartLocation_ start_location) {
        this.start_location = start_location;
    }

    public String getTravelMode() {
        return travel_mode;
    }

    public void setTravelMode(String travel_mode) {
        this.travel_mode = travel_mode;
    }

    public String getManeuver() {
        return maneuver;
    }

    public void setManeuver(String maneuver) {
        this.maneuver = maneuver;
    }
}
