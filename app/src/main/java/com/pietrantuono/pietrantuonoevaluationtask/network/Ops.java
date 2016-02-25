package com.pietrantuono.pietrantuonoevaluationtask.network;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.pietrantuono.pietrantuonoevaluationtask.contentprovider.Contract;
import com.pietrantuono.pietrantuonoevaluationtask.pojos.Item;
import com.pietrantuono.pietrantuonoevaluationtask.pojos.ListItem;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Maurizio Pietrantuono, maurizio.pietrantuono@gmail.com.
 */
public class Ops {

    private static final String TAG = "Ops";

    /**
     * Gets the content list
     *
     * @return a non empty array only if the list has been modified since last time we checked,
     * an empty list otherwise and in case of error
     */
    public static ArrayList<ListItem> getContentList(Context context) {
        ArrayList<ListItem> results = new ArrayList<>();
        HttpURLConnection httpURLConnection = null;
        /**
         * We get from the provider the date of the last check, if any
         */
        long lastDownloadDate = -1;
        final String[] dateProjection = new String[]
                {
                        Contract.ROW_ID,
                        Contract.CONTENTLIST_DOWNLOAD_DATE
                };
        Cursor cursor = null;
        try {
            URL mainUrl = new URL(URLs.MAIN_URL);
            httpURLConnection = (HttpURLConnection) mainUrl.openConnection();
            cursor = context.getContentResolver().query(
                    Contract.URI_CONTENTLIST,
                    dateProjection,
                    null,
                    null,
                    null);
            if (null != cursor && cursor.moveToFirst()) {
                lastDownloadDate =cursor.getLong(cursor.getColumnIndex(Contract.CONTENTLIST_DOWNLOAD_DATE));
            }
            int responseCode = httpURLConnection.getResponseCode();

            switch (responseCode) {
                case 200:
                    long lastModifiedDate = httpURLConnection.getLastModified();
                    ContentValues dateValues = new ContentValues();

                    dateValues.put(Contract.CONTENTLIST_DOWNLOAD_DATE, lastModifiedDate);

                    /**
                     *  We did not have a date for the last check, hence we need to insert it for the first time
                     */
                    if (lastDownloadDate == -1) {
                        //We insert it
                        context.getContentResolver().insert(
                                Contract.URI_CONTENTLIST,
                                dateValues
                        );
                        /**
                         * We get the rowid of the newly inserted row
                         * we will need it so, because so we can retrieve the items at a date
                         */
                        cursor = context.getContentResolver().query(
                                Contract.URI_CONTENTLIST,
                                dateProjection,
                                null,
                                null,
                                null);
                        if(cursor.moveToFirst()) {
                            int listRowID = cursor.getInt(cursor.getColumnIndex(Contract.ROW_ID));
                            InputStream inputStream = httpURLConnection.getInputStream();
                            /**
                             *  We pass the rowid to be saved together with the items  to be used as foreign key
                             */
                            results.addAll(SimpleJSONParser.parseItemsList(inputStream, listRowID));
                        }
                    }
                    /**
                     *  We have a date for the last check
                     *  but things might be changed we need to update the items
                     */
                    else if (lastDownloadDate < lastModifiedDate) {
                        String where = Contract.ROW_ID + "=";
                        int listRowID = cursor.getInt(cursor.getColumnIndex(Contract.ROW_ID));
                        String[] args = new String[]{"" + listRowID};
                        context.getContentResolver().update(
                                Contract.URI_CONTENTLIST,
                                dateValues,
                                where,
                                args
                        );
                        /**
                         *  We pass the rowid to be saved together with the items
                         */
                        InputStream inputstream = httpURLConnection.getInputStream();
                        results.addAll(SimpleJSONParser.parseItemsList(inputstream, listRowID));
                    }
                    break;
            }
            ///TODO handle error response codes
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor) {
                cursor.close();
            }
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
        return results;
    }

    /**
     * Here we download the items sequentially one after the other on the IntentService Thread,
     * to speed up things we could instead launch a batch of AsyncTasks, or use Threads and a ThreadPoolExecutor, etc.
     * (an Executor would be better because there might be many items in the content list)
     */
    public static ArrayList<Item> getItems(Context context, ArrayList<ListItem> list) {
        ArrayList<Item>result=new ArrayList<>();
        for (ListItem listItem : list) {
            /**
             * We check if we already have this item in the contentprovider
             * We use the contentprovider uri to get a single item, appending the item ID
             */
            long itemID = listItem.getId();

            Uri singleItemUri = Contract.URI_TYPE_MULTIPLE_ITEMS.buildUpon().appendPath("" + itemID).build();
            Cursor c = context.getContentResolver().query(singleItemUri, null, null, null, null);
            /**
             * We already have it, we need to update it
             */
            if (c.getCount() > 0) {
                long itemId = listItem.getId();
                /**
                 * We fetch the item sequentially
                 */
                Item item = fetchItem(itemId);
                Log.d(TAG, "Fetched item " + itemId);
                result.add(item);
                if (item != null) {
                    ContentValues contentValues = new ContentValues();
                    if (item.getTitle() != null)
                        contentValues.put(Contract.ITEM_TITLE, item.getTitle());
                    if (item.getBody() != null)
                        contentValues.put(Contract.ITEM_BODY, item.getBody());
                    if (item.getDate() != null)
                        contentValues.put(Contract.ITEM_DATE, item.getDate());
                    if (item.getSubtitle() != null)
                        contentValues.put(Contract.ITEM_SUBTITLE, item.getSubtitle());
                    contentValues.put(Contract.ITEM_ID, item.getId());
                    contentValues.put(Contract.LIST_ROW_ID,listItem.getContentListRowID());

                    String where = Contract.ITEM_ID + " = ?";
                    String[] args = new String[]{"" + itemID};
                    /**
                     * We update
                     */
                    context.getContentResolver().update(singleItemUri, contentValues, where, args);
                }
                /**
                 * We don't have it, we need to insert it
                 */
            } else {

                long itemId = listItem.getId();
                /**
                 * We fetch the item
                 */
                Item item = fetchItem(itemId);
                Log.d(TAG, "Fetched item " + itemId);

                result.add(item);
                if (item != null) {
                    ContentValues contentValues = new ContentValues();
                    if (item.getTitle() != null)
                        contentValues.put(Contract.ITEM_TITLE, item.getTitle());
                    if (item.getBody() != null)
                        contentValues.put(Contract.ITEM_BODY, item.getBody());
                    if (item.getDate() != null)
                        contentValues.put(Contract.ITEM_DATE, item.getDate());
                    if (item.getSubtitle() != null)
                        contentValues.put(Contract.ITEM_SUBTITLE, item.getSubtitle());
                    contentValues.put(Contract.ITEM_ID, item.getId());
                    contentValues.put(Contract.LIST_ROW_ID,listItem.getContentListRowID());
                    /**
                     * We insert
                     */
                    context.getContentResolver().insert(singleItemUri, contentValues);
                }
            }
            c.close();
        }
        return result;
    }

    /**
     * Fetches a singe content item based on the id of the item in the contentList
     * @param itemId
     * @return the parsed Item
     */
    private static Item fetchItem(long itemId) {
        Item item=null;
        HttpURLConnection httpURLConnection = null;

        try {

            URL itemUrl = new URL(URLs.getItemURL(itemId));

            httpURLConnection = (HttpURLConnection) itemUrl.openConnection();
            int responseCode = httpURLConnection.getResponseCode();

            switch (responseCode) {
                case 200:
                    InputStream inputStream = httpURLConnection.getInputStream();
                    item=SimpleJSONParser.parseSingleItem(inputStream);
                    break;
            }
            ///TODO handle error response codes
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
        return item;
    }
}
