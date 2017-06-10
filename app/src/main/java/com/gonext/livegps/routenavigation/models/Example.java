package com.gonext.livegps.routenavigation.models;

import java.util.List;

/**
 * Created by mishti on 7/6/17.
 */

public class Example
{
    private List<Object> html_attributions = null;
    private String next_page_token;
    private List<Result> results = null;
    private String status;

    /**
     * No args constructor for use in serialization
     *
     */
    public Example() {
    }

    /**
     *
     * @param results
     * @param status
     * @param next_page_token
     * @param html_attributions
     */
    public Example(List<Object> html_attributions, String next_page_token, List<Result> results, String status) {
        super();
        this.html_attributions = html_attributions;
        this.next_page_token = next_page_token;
        this.results = results;
        this.status = status;
    }

    public List<Object> getHtmlAttributions() {
        return html_attributions;
    }

    public void setHtmlAttributions(List<Object> html_attributions) {
        this.html_attributions = html_attributions;
    }

    public String getNextPageToken() {
        return next_page_token;
    }

    public void setNextPageToken(String next_page_token) {
        this.next_page_token = next_page_token;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
