package com.pietrantuono.pietrantuonoevaluationtask.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.pietrantuono.pietrantuonoevaluationtask.BuildConfig;

public class Provider extends ContentProvider {
    public static final int LIST_QUERY = 1;
    public static final int MULTIPLE_ITEMS_QUERY = 2;
    public static final int SINGLE_ITEMS_QUERY = 3;
    public static final int INVALID_URI = -1;

    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private static final UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(0);
        sUriMatcher.addURI(Contract.AUTHORITY, Contract.ITEMS_TABLE_NAME, MULTIPLE_ITEMS_QUERY);
        sUriMatcher.addURI(Contract.AUTHORITY, Contract.ITEMS_TABLE_NAME + "/*", SINGLE_ITEMS_QUERY);
        sUriMatcher.addURI(Contract.AUTHORITY, Contract.CONTENTLIST_TABLE_NAME, LIST_QUERY);
    }

    private class MySQLiteOpenHelper extends SQLiteOpenHelper {
        MySQLiteOpenHelper(Context context) {
            super(context,
                    Contract.DB_NAME,
                    null,
                    Contract.DB_VERSION);
        }

        private void dropTables(SQLiteDatabase db) {
            db.execSQL("DROP TABLE IF EXISTS " + Contract.ITEMS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Contract.CONTENTLIST_TABLE_NAME);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Contract.CREATE_ITEMS_TABLE);
            db.execSQL(Contract.CREATE_CONTENTLIST_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int version1, int version2) {
            dropTables(db);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int version1, int version2) {
            dropTables(db);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        mySQLiteOpenHelper = new MySQLiteOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {

        SQLiteDatabase db = mySQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (sUriMatcher.match(uri)) {
            /**
             * Here we want to get only the items that were downloaded the last time,
             * using the rowid of the latest contentList downloaded as an foreign key
             */
            case MULTIPLE_ITEMS_QUERY:
                if (projection != null || selection != null || selectionArgs != null || sortOrder != null)
                    throw new IllegalArgumentException("Query argds must be null " + uri);
                String orderBy = Contract.CONTENTLIST_DOWNLOAD_DATE + " DESC";
                Cursor idRowCursor = db.query(
                        Contract.CONTENTLIST_TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        orderBy
                );
                long rowId = 0;
                if (idRowCursor.moveToFirst()) {
                    rowId = idRowCursor.getLong(idRowCursor.getColumnIndexOrThrow(Contract.ROW_ID));
                }
                idRowCursor.close();
                String rowIdselection = Contract.LIST_ROW_ID + " = ?";
                String[] rowIdselectionArgs = new String[]{"" + rowId};
                cursor = db.query(
                        Contract.ITEMS_TABLE_NAME,
                        null,
                        rowIdselection,
                        rowIdselectionArgs,
                        null,
                        null,
                        null);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            case SINGLE_ITEMS_QUERY:
                if (projection != null || selection != null || selectionArgs != null)
                    throw new IllegalArgumentException("Unsupported:" + uri);
                String id = uri.getLastPathSegment();
                selection = Contract.ITEM_ID + " = ?";
                selectionArgs = new String[]{id};
                cursor = db.query(
                        Contract.ITEMS_TABLE_NAME,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;

            case LIST_QUERY:
                cursor = db.query(
                        Contract.CONTENTLIST_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;

            case INVALID_URI:
                throw new IllegalArgumentException("Invalid URI:" + uri);
        }
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return "";
    } //TODO

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        long id = -1;
        switch (sUriMatcher.match(uri)) {
            case LIST_QUERY:
                id = db.insert(
                        Contract.CONTENTLIST_TABLE_NAME,
                        Contract.CONTENTLIST_DOWNLOAD_DATE,
                        values
                );
                if (-1 != id) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return Uri.withAppendedPath(uri, Long.toString(id));
                } else {
                    throw new SQLiteException("Error:" + uri);
                }
            case MULTIPLE_ITEMS_QUERY:
                id = db.insert(
                        Contract.ITEMS_TABLE_NAME,
                        Contract.ITEM_TITLE,
                        values
                );
                if (-1 != id) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return Uri.withAppendedPath(uri, Long.toString(id));
                } else {
                    throw new SQLiteException("Error:" + uri);
                }
            case SINGLE_ITEMS_QUERY:
                id = db.insert(
                        Contract.ITEMS_TABLE_NAME,
                        Contract.ITEM_TITLE,
                        values
                );
                if (-1 != id) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return Uri.withAppendedPath(uri, Long.toString(id));
                } else {
                    throw new SQLiteException("Error:" + uri);
                }
        }
        return null;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] contentValues) {
        switch (sUriMatcher.match(uri)) {

            case MULTIPLE_ITEMS_QUERY:
                SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
                db.beginTransaction();
                int length = contentValues.length;
                try {
                    for (int i = 0; i < length; i++) {
                        long rowid = db.insert(
                                Contract.ITEMS_TABLE_NAME,
                                Contract.ITEM_TITLE,
                                contentValues[i]);
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                    db.close();
                    getContext().getContentResolver().notifyChange(uri, null);

                }
                return length;

            default:
                throw new IllegalArgumentException("Support only insertion in bulk of items");

        }

    }

    /**
     * CURRENTLY UNSUPPORTED!
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case LIST_QUERY:
                return db.delete(
                        Contract.CONTENTLIST_TABLE_NAME,
                        selection,
                        selectionArgs
                );

            case MULTIPLE_ITEMS_QUERY:
                return db.delete(
                        Contract.ITEMS_TABLE_NAME,
                        selection,
                        selectionArgs
                );

            case SINGLE_ITEMS_QUERY:
                return db.delete(
                        Contract.ITEMS_TABLE_NAME,
                        selection,
                        selectionArgs
                );
            default:
                return 0;
        }
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase localSQLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
        int rows = 0;
        switch (sUriMatcher.match(uri)) {
            case LIST_QUERY:
                rows = localSQLiteDatabase.update(
                        Contract.CONTENTLIST_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                if (0 != rows) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return rows;
                } else {
                    throw new SQLiteException("Update error:" + uri);
                }
            case MULTIPLE_ITEMS_QUERY:
                rows = localSQLiteDatabase.update(
                        Contract.ITEMS_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                if (0 != rows) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return rows;
                } else {
                    throw new SQLiteException("Update error:" + uri);
                }
            case SINGLE_ITEMS_QUERY:
                String id = uri.getLastPathSegment();
                selection = Contract.ITEM_ID + " = ?";
                selectionArgs = new String[]{id};

                rows = localSQLiteDatabase.update(
                        Contract.ITEMS_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                if (0 != rows) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return rows;
                } else {
                    throw new SQLiteException("Update error:" + uri);
                }
        }
        return rows;
    }
}