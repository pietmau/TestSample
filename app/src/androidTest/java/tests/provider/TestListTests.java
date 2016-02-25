package tests.provider;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import com.pietrantuono.pietrantuonoevaluationtask.contentprovider.Contract;
import com.pietrantuono.pietrantuonoevaluationtask.contentprovider.Provider;

/**
 * Created by Maurizio Pietrantuono, maurizio.pietrantuono@gmail.com.
 */
public class TestListTests extends ProviderTestCase2 {

    private MockContentResolver resolver;

    public TestListTests() {
        super(Provider.class, Contract.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        resolver = getMockContentResolver();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testInsert() {
        Uri uri = Contract.URI_CONTENTLIST;
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.CONTENTLIST_DOWNLOAD_DATE, 777);
        resolver.insert(uri, contentValues);
        @SuppressLint("Recycle") Cursor c = resolver.query(uri, null, null, null, null);
        assertTrue(c.getCount() > 0);
        assertTrue(c.getCount() == 1);
        assertTrue(c.moveToFirst());

        assertEquals(c.getInt(c.getColumnIndexOrThrow(Contract.CONTENTLIST_DOWNLOAD_DATE)), 777);

    }

    public void testUpdate() {
        Uri uri = Contract.URI_CONTENTLIST;
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.CONTENTLIST_DOWNLOAD_DATE, 777);
        resolver.insert(uri, contentValues);
        ContentValues newcontentValues = new ContentValues();
        newcontentValues.put(Contract.CONTENTLIST_DOWNLOAD_DATE, 66);
        resolver.update(uri, contentValues, null, null);

        @SuppressLint("Recycle") Cursor c = resolver.query(uri, null, null, null, null);
        assertTrue(c.getCount() > 0);
        assertTrue(c.getCount() == 1);
        assertTrue(c.moveToFirst());
        assertFalse(c.getInt(c.getColumnIndexOrThrow(Contract.CONTENTLIST_DOWNLOAD_DATE)) == 66);
        assertEquals(c.getInt(c.getColumnIndexOrThrow(Contract.CONTENTLIST_DOWNLOAD_DATE)), 777);

    }
}
