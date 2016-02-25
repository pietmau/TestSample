package tests.service;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.test.ServiceTestCase;

import com.pietrantuono.pietrantuonoevaluationtask.contentprovider.Contract;
import com.pietrantuono.pietrantuonoevaluationtask.service.FetchService;

/**
 * Created by Maurizio Pietrantuono, maurizio.pietrantuono@gmail.com.
 */
public class ServiceTestTests extends ServiceTestCase {

    private ContentResolver contentresolver;
    private Context context;

    public ServiceTestTests() {
        super(FetchService.class);
    }

    @SuppressLint("Recycle")
    public void testNoInitialData() throws InterruptedException {
        contentresolver = context.getContentResolver();
        Uri uriContentlist = Contract.URI_CONTENTLIST;
        contentresolver.delete(uriContentlist,null,null);
        Uri uriTypeMultipleItems = Contract.URI_TYPE_MULTIPLE_ITEMS;
        @SuppressLint("Recycle") Cursor c=contentresolver.query(uriContentlist,null,null,null,null);
        c=contentresolver.query(uriTypeMultipleItems,null,null,null,null);
        //assertTrue(c.getCount()>0);
        contentresolver.delete(uriContentlist, null, null);
        contentresolver.delete(uriTypeMultipleItems, null, null);
        c=contentresolver.query(uriContentlist,null,null,null,null);
        assertTrue(c.getCount() <= 0);
        c=contentresolver.query(uriTypeMultipleItems,null,null,null,null);
        assertTrue(c.getCount() <= 0);
        Intent intent= new Intent(context,FetchService.class);
        startService(intent);
        Thread.sleep(10 * 1000);
        c=contentresolver.query(uriContentlist,null,null,null,null);
        assertTrue(c.getCount()>0);
        c=contentresolver.query(uriTypeMultipleItems,null,null,null,null);
        assertTrue(c.getCount() > 0);


    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getSystemContext();

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
