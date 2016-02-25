package com.pietrantuono.pietrantuonoevaluationtask.pojos;

public class Item {
    public static final String ITEM_KEY ="item";
    public static final String ID_KEY ="id";
    public static final String TITLE_KEY ="title";
    public static final String SUBTITLE_KEY ="subtitle";
    public static final String DATE_KEY ="date";
    public static final String BODY_KEY ="body";
    private long id;
    private String title;
    private String subtitle;
    private String body;
    private String date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
