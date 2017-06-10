package com.gonext.live.gps.navigation.models;

import java.util.List;

/**
 * Created by mishti on 8/6/17.
 */

public class GeocodedWaypoint
{
    private String geocoder_status;
    private String place_id;
    private List<String> types = null;
    private boolean partial_match;

    /**
     * No args constructor for use in serialization
     *
     */
    public GeocodedWaypoint() {
    }

    /**
     *
     * @param geocoder_status
     * @param place_id
     * @param types
     * @param partial_match
     */
    public GeocodedWaypoint(String geocoder_status, String place_id, List<String> types, boolean partial_match) {
        super();
        this.geocoder_status = geocoder_status;
        this.place_id = place_id;
        this.types = types;
        this.partial_match = partial_match;
    }

    public String getGeocoderStatus() {
        return geocoder_status;
    }

    public void setGeocoderStatus(String geocoder_status) {
        this.geocoder_status = geocoder_status;
    }

    public String getPlaceId() {
        return place_id;
    }

    public void setPlaceId(String place_id) {
        this.place_id = place_id;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public boolean isPartialMatch() {
        return partial_match;
    }

    public void setPartialMatch(boolean partial_match) {
        this.partial_match = partial_match;
    }

}
