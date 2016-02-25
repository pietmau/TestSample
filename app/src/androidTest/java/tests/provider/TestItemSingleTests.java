package tests.provider;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import com.pietrantuono.pietrantuonoevaluationtask.contentprovider.Contract;
import com.pietrantuono.pietrantuonoevaluationtask.contentprovider.Provider;

import java.net.URI;

/**
 * Created by Maurizio Pietrantuono, maurizio.pietrantuono@gmail.com.
 */
public class TestItemSingleTests extends ProviderTestCase2 {

    private MockContentResolver resolver;

    public TestItemSingleTests() {
        super(Provider.class, Contract.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        resolver = getMockContentResolver();
        Uri uriContentlist = Contract.URI_CONTENTLIST;
        resolver.delete(uriContentlist,null,null);
        Uri uriTypeMultipleItems = Contract.URI_TYPE_MULTIPLE_ITEMS;
        resolver.delete(uriContentlist, null, null);
        resolver.delete(uriTypeMultipleItems, null, null);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Uri uriContentlist = Contract.URI_CONTENTLIST;
        resolver.delete(uriContentlist,null,null);
        Uri uriTypeMultipleItems = Contract.URI_TYPE_MULTIPLE_ITEMS;
        resolver.delete(uriContentlist, null, null);
        resolver.delete(uriTypeMultipleItems, null, null);
    }

    public void testInsert() {
        ContentValues dateValues = new ContentValues();
        dateValues.put(Contract.CONTENTLIST_DOWNLOAD_DATE, 1);
        resolver.insert(
                Contract.URI_CONTENTLIST,
                dateValues
        );

        Uri uri = Contract.URI_TYPE_MULTIPLE_ITEMS;
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.ITEM_TITLE, "Nice title");
        contentValues.put(Contract.ITEM_BODY, "Nice body");
        contentValues.put(Contract.ITEM_DATE, "Nice date");
        contentValues.put(Contract.ITEM_SUBTITLE, "Nice subtitle");
        contentValues.put(Contract.ITEM_ID, 11);
        contentValues.put(Contract.LIST_ROW_ID, 1);
        resolver.insert(uri, contentValues);
        uri = Contract.URI_TYPE_MULTIPLE_ITEMS;
        Uri singleItemUri = uri.buildUpon().appendPath("" + 11).build();
        @SuppressLint("Recycle") Cursor c = resolver.query(singleItemUri, null, null, null, null);
        assertTrue(c.getCount() > 0);
        assertTrue(c.getCount() == 1);
        assertTrue(c.moveToFirst());

        assertEquals(c.getString(c.getColumnIndexOrThrow(Contract.ITEM_BODY)), "Nice body");
        assertEquals(c.getString(c.getColumnIndexOrThrow(Contract.ITEM_TITLE)), "Nice title");
        assertEquals(c.getString(c.getColumnIndexOrThrow(Contract.ITEM_DATE)), "Nice date");
        assertEquals(c.getString(c.getColumnIndexOrThrow(Contract.ITEM_SUBTITLE)), "Nice subtitle");
        assertEquals(c.getInt(c.getColumnIndexOrThrow(Contract.ITEM_ID)), 11);
        assertEquals(c.getInt(c.getColumnIndexOrThrow(Contract.LIST_ROW_ID)), 1);

    }

    @SuppressLint("Recycle")
    public void testUpdate() {
        ContentValues dateValues = new ContentValues();
        dateValues.put(Contract.CONTENTLIST_DOWNLOAD_DATE, 1);
        resolver.insert(
                Contract.URI_CONTENTLIST,
                dateValues
        );
        Uri uri = Contract.URI_TYPE_MULTIPLE_ITEMS;
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.ITEM_TITLE, "Nice title");
        contentValues.put(Contract.ITEM_BODY, "Nice body");
        contentValues.put(Contract.ITEM_DATE, "Nice date");
        contentValues.put(Contract.ITEM_SUBTITLE, "Nice subtitle");
        contentValues.put(Contract.ITEM_ID, 11);
        contentValues.put(Contract.LIST_ROW_ID, 1);
        resolver.insert(uri, contentValues);
        uri = Contract.URI_TYPE_MULTIPLE_ITEMS;
        Uri singleItemUri = uri.buildUpon().appendPath("" + 11).build();
        @SuppressLint("Recycle") Cursor c = resolver.query(singleItemUri, null, null, null, null);
        assertTrue(c.getCount() > 0);
        assertTrue(c.getCount() == 1);
        assertTrue(c.moveToFirst());

        assertEquals(c.getString(c.getColumnIndexOrThrow(Contract.ITEM_BODY)), "Nice body");
        assertEquals(c.getString(c.getColumnIndexOrThrow(Contract.ITEM_TITLE)), "Nice title");
        assertEquals(c.getString(c.getColumnIndexOrThrow(Contract.ITEM_DATE)), "Nice date");
        assertEquals(c.getString(c.getColumnIndexOrThrow(Contract.ITEM_SUBTITLE)), "Nice subtitle");
        assertEquals(c.getInt(c.getColumnIndexOrThrow(Contract.ITEM_ID)), 11);
        assertEquals(c.getInt(c.getColumnIndexOrThrow(Contract.LIST_ROW_ID)), 1);

        ContentValues newcontentValues = new ContentValues();
        newcontentValues.put(Contract.ITEM_TITLE, "Better title");
        resolver.update(singleItemUri, newcontentValues, null, null);
        c = resolver.query(singleItemUri, null, null, null, null);
        assertTrue(c.getCount() > 0);
        assertTrue(c.getCount() == 1);
        assertTrue(c.moveToFirst());

        assertEquals(c.getString(c.getColumnIndexOrThrow(Contract.ITEM_BODY)), "Nice body");
        assertFalse(c.getString(c.getColumnIndexOrThrow(Contract.ITEM_TITLE)).equalsIgnoreCase("Nice title"));
        assertEquals(c.getString(c.getColumnIndexOrThrow(Contract.ITEM_TITLE)), "Better title");
        assertEquals(c.getString(c.getColumnIndexOrThrow(Contract.ITEM_DATE)), "Nice date");
        assertEquals(c.getString(c.getColumnIndexOrThrow(Contract.ITEM_SUBTITLE)), "Nice subtitle");
        assertEquals(c.getInt(c.getColumnIndexOrThrow(Contract.ITEM_ID)), 11);
        assertEquals(c.getInt(c.getColumnIndexOrThrow(Contract.LIST_ROW_ID)), 1);


    }
}
