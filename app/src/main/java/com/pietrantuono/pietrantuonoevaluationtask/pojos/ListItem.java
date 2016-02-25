package com.pietrantuono.pietrantuonoevaluationtask.pojos;

public class ListItem {
    public static final String ITEMS_KEY ="items";
    public static final String ID_KEY ="id";
    public static final String TITLE_KEY ="title";
    public static final String SUBTITLE_KEY ="subtitle";
    public static final String DATE_KEY ="date";

    private long id;
    private String title;
    private String subtitle;
    private String date;
    private int contentListRowID;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContentListRowID(int contentListRowID) {
        this.contentListRowID = contentListRowID;
    }

    public int getContentListRowID() {
        return contentListRowID;
    }
}
