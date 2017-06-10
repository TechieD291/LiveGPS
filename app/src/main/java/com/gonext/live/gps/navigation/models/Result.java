package com.gonext.live.gps.navigation.models;

import java.util.List;

/**
 * Created by mishti on 7/6/17.
 */

public class Result
{
    private Geometry geometry;
    private String icon;
    private String id;
    private String name;
    private List<Photo> photos = null;
    private String place_id;
    private String reference;
    private String scope;
    private List<String> types = null;
    private String vicinity;
    private float rating;
    private OpeningHours opening_hours;

    /**
     * No args constructor for use in serialization
     *
     */
    public Result() {
    }

    /**
     *
     * @param photos
     * @param id
     * @param icon
     * @param vicinity
     * @param scope
     * @param opening_hours
     * @param place_id
     * @param name
     * @param rating
     * @param types
     * @param reference
     * @param geometry
     */
    public Result(Geometry geometry, String icon, String id, String name, List<Photo> photos, String place_id, String reference, String scope, List<String> types, String vicinity, float rating, OpeningHours opening_hours) {
        super();
        this.geometry = geometry;
        this.icon = icon;
        this.id = id;
        this.name = name;
        this.photos = photos;
        this.place_id = place_id;
        this.reference = reference;
        this.scope = scope;
        this.types = types;
        this.vicinity = vicinity;
        this.rating = rating;
        this.opening_hours = opening_hours;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getPlaceId() {
        return place_id;
    }

    public void setPlaceId(String place_id) {
        this.place_id = place_id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public OpeningHours getOpeningHours() {
        return opening_hours;
    }

    public void setOpeningHours(OpeningHours opening_hours) {
        this.opening_hours = opening_hours;
    }
}
