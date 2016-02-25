package com.pietrantuono.pietrantuonoevaluationtask.contentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

public class Contract implements BaseColumns{
    public static final String SCHEME = "content://" ;

    public static final String AUTHORITY = "com.pietrantuono.pietrantuonoevaluationtask";

    public static final String ROW_ID = BaseColumns._ID;

    public static final String CONTENTLIST_TABLE_NAME = "download_date";


    /**
     *
     * Items table
     *
     */
    public static final String ITEMS_TABLE_NAME = "items";

    public static final Uri URI_TYPE_MULTIPLE_ITEMS
            = Uri.parse(SCHEME + AUTHORITY + "/"+ITEMS_TABLE_NAME);

    public static final Uri URI_TYPE_SINGLE_ITEM
            = Uri.parse(SCHEME + AUTHORITY + "/"+ITEMS_TABLE_NAME+"/");

    public static final String ITEM_ID = "item_id";

    public static final String ITEM_TITLE = "item_title";

    public static final String ITEM_SUBTITLE = "item_subtitle";

    public static final String ITEM_BODY = "item_body";

    public static final String ITEM_DATE = "item_date";

    public static final String LIST_ROW_ID = "list_id";

    static final String CREATE_ITEMS_TABLE = "CREATE TABLE" + " " +
            ITEMS_TABLE_NAME + " " +
            "(" + " " +
            ROW_ID + " " + Consts.PRIMARY_KEY_TYPE + " ," +
            ITEM_ID + " " + Consts.INTEGER_TYPE + " ," +
            ITEM_TITLE + " " + Consts.TEXT_TYPE + " ," +
            ITEM_SUBTITLE + " " + Consts.TEXT_TYPE + " ," +
            ITEM_BODY + " " + Consts.TEXT_TYPE + " ," +
            ITEM_DATE + " " + Consts.TEXT_TYPE + " ," +
            LIST_ROW_ID + " " + Consts.INTEGER_TYPE + " ," +
            Consts.FOREIGN_KEY +" ("+LIST_ROW_ID + ") "+"REFERENCES " + CONTENTLIST_TABLE_NAME +" ("+ROW_ID+") "+
            ")";

    /**
     *
     * contentList table
     *
     */

    public static final Uri URI_CONTENTLIST
            = Uri.parse(SCHEME + AUTHORITY + "/" + CONTENTLIST_TABLE_NAME);

    public static final String CONTENTLIST_DOWNLOAD_DATE = "date";
    static final String CREATE_CONTENTLIST_TABLE = "CREATE TABLE" + " " +
            CONTENTLIST_TABLE_NAME + " " +
            "(" + " " +
            ROW_ID + " " + Consts.PRIMARY_KEY_TYPE + " ," +
            CONTENTLIST_DOWNLOAD_DATE + " " + Consts.INTEGER_TYPE +
            ")";
    /**
     *
     * Database
     *
     */
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "pietrantuonoevaluationtaskdatabase";

    private Contract() { }

}
