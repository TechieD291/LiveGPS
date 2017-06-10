package com.gonext.livegps.routenavigation.models;

/**
 * Created by mishti on 8/6/17.
 */

public class Duration_
{
    private String text;
    private int value;

    /**
     * No args constructor for use in serialization
     *
     */
    public Duration_() {
    }

    /**
     *
     * @param text
     * @param value
     */
    public Duration_(String text, int value) {
        super();
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
