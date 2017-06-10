package com.gonext.livegps.routenavigation.models;

import java.util.List;

/**
 * Created by mishti on 7/6/17.
 */

public class OpeningHours
{
    private boolean open_now;
    private List<Object> weekday_text = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public OpeningHours() {
    }

    /**
     *
     * @param weekday_text
     * @param open_now
     */
    public OpeningHours(boolean open_now, List<Object> weekday_text) {
        super();
        this.open_now = open_now;
        this.weekday_text = weekday_text;
    }

    public boolean isOpenNow() {
        return open_now;
    }

    public void setOpenNow(boolean open_now) {
        this.open_now = open_now;
    }

    public List<Object> getWeekdayText() {
        return weekday_text;
    }

    public void setWeekdayText(List<Object> weekday_text) {
        this.weekday_text = weekday_text;
    }
}
