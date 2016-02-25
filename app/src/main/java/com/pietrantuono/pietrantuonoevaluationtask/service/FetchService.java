package com.pietrantuono.pietrantuonoevaluationtask.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import com.pietrantuono.pietrantuonoevaluationtask.contentprovider.Contract;
import com.pietrantuono.pietrantuonoevaluationtask.network.Ops;
import com.pietrantuono.pietrantuonoevaluationtask.pojos.Item;
import com.pietrantuono.pietrantuonoevaluationtask.pojos.ListItem;

import java.util.ArrayList;

/**
 * Created by Maurizio Pietrantuono, maurizio.pietrantuono@gmail.com.
 */
public class FetchService extends IntentService{
    private static final String TAG = "FetchService";
    private ArrayList<Item> result = null;

    public FetchService() {
        super(FetchService.class.toString());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG,"onHandleIntent");
        /**
         * We get the contentList,
         * if it has been modified since last time we get the complete contentList
         * else we get an empty contentList
         */
        //TODO delete LISTITEM
        ArrayList<ListItem> contentList = Ops.getContentList(FetchService.this);
        /**
         * If we have a non empty contentList, things might to be changed,
         * we need to insert the data in te ContentProvider so that they are immediately available
         * and get the single item
         */
        if(contentList!=null && contentList.size()>0){
            bulkInsertItems(contentList);
            result=Ops.getItems(FetchService.this, contentList);
        }
    }

    private void bulkInsertItems(ArrayList<ListItem> contentList) {
        ContentValues[] contentValues= new ContentValues[contentList.size()];
        for(int i=0;i<contentList.size();i++){
            ListItem item = contentList.get(i);
            ContentValues singleValue=new ContentValues();
            singleValue.put(Contract.ITEM_ID,item.getId());
            singleValue.put(Contract.ITEM_TITLE,item.getTitle()!=null?item.getTitle():"");
            singleValue.put(Contract.ITEM_SUBTITLE,item.getSubtitle()!=null?item.getSubtitle():"");
            singleValue.put(Contract.ITEM_DATE,item.getDate()!=null ?item.getDate():"");
            singleValue.put(Contract.LIST_ROW_ID,item.getContentListRowID());
            contentValues[i]=singleValue;
        }
        int inserted = getContentResolver().bulkInsert(Contract.URI_TYPE_MULTIPLE_ITEMS, contentValues);
        Log.d(TAG, "inserted " + inserted);
    }

}
