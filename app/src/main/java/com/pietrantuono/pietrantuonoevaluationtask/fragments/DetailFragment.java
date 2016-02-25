package com.pietrantuono.pietrantuonoevaluationtask.fragments;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pietrantuono.pietrantuonoevaluationtask.R;
import com.pietrantuono.pietrantuonoevaluationtask.contentprovider.Contract;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String ITEM_ID = "item_id";
    private static final int DETAIL_LOADER = 2;
    private static final String TAG = "DetailFragment";
    private long itemId;

    public DetailFragment() {
    }

    public static DetailFragment newInstance(long itemId) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putLong(ITEM_ID, itemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemId = getArguments().getLong(ITEM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        return v;
    }


    @SuppressLint("SetTextI18n")
    private void updateView(View view, Cursor cursor) {
        if (cursor.moveToFirst()) {
            String title=cursor.getString(cursor.getColumnIndexOrThrow(Contract.ITEM_TITLE));
            long itemID=cursor.getLong(cursor.getColumnIndexOrThrow(Contract.ITEM_ID));
            String date=cursor.getString(cursor.getColumnIndexOrThrow(Contract.ITEM_DATE));
            String sub=cursor.getString(cursor.getColumnIndexOrThrow(Contract.ITEM_SUBTITLE));
            String body=cursor.getString(cursor.getColumnIndexOrThrow(Contract.ITEM_BODY));
            TextView titleTextView= (TextView) view.findViewById(R.id.title);
            TextView subTextView= (TextView) view.findViewById(R.id.sub);
            TextView idTextView= (TextView) view.findViewById(R.id.item_id);
            TextView dateTextView= (TextView) view.findViewById(R.id.date);
            TextView bodyTextView= (TextView) view.findViewById(R.id.body);
            titleTextView.setText(title!=null?title:"");
            subTextView.setText(sub!=null?sub:"");
            idTextView.setText(""+itemID);
            dateTextView.setText(date!=null?date:"");
            bodyTextView.setText(body!=null?body:"");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case DETAIL_LOADER:
                /**
                 * We get only one item
                 */
                Uri singleItemUri = Contract.URI_TYPE_MULTIPLE_ITEMS.buildUpon().appendPath("" + itemId).build();
                return new CursorLoader(
                        getActivity(),
                        singleItemUri,
                        null,
                        null,
                        null,
                        null
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(TAG,"onLoadFinished");
        updateView(getView(), cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
