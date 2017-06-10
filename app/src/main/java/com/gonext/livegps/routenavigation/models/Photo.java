package com.gonext.livegps.routenavigation.models;

import java.util.List;

/**
 * Created by mishti on 7/6/17.
 */

public class Photo
{
    private int height;
    private List<String> html_attributions = null;
    private String photo_reference;
    private int width;

    /**
     * No args constructor for use in serialization
     *
     */
    public Photo() {
    }

    /**
     *
     * @param height
     * @param width
     * @param html_attributions
     * @param photo_reference
     */
    public Photo(int height, List<String> html_attributions, String photo_reference, int width) {
        super();
        this.height = height;
        this.html_attributions = html_attributions;
        this.photo_reference = photo_reference;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<String> getHtmlAttributions() {
        return html_attributions;
    }

    public void setHtmlAttributions(List<String> html_attributions) {
        this.html_attributions = html_attributions;
    }

    public String getPhotoReference() {
        return photo_reference;
    }

    public void setPhotoReference(String photo_reference) {
        this.photo_reference = photo_reference;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

}
