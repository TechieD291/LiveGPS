package com.gonext.livegps.routenavigation.models;

/**
 * Created by mishti on 7/6/17.
 */

public class Geometry
{
    private Location location;
    private Viewport viewport;

    /**
     * No args constructor for use in serialization
     *
     */
    public Geometry() {
    }

    /**
     *
     * @param viewport
     * @param location
     */
    public Geometry(Location location, Viewport viewport) {
        super();
        this.location = location;
        this.viewport = viewport;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }
}
