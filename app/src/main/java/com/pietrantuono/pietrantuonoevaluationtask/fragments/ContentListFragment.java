package com.pietrantuono.pietrantuonoevaluationtask.fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pietrantuono.pietrantuonoevaluationtask.BuildConfig;
import com.pietrantuono.pietrantuonoevaluationtask.R;
import com.pietrantuono.pietrantuonoevaluationtask.contentprovider.Contract;
import com.pietrantuono.pietrantuonoevaluationtask.fragments.adapters.RecyclerViewCursorAdapter;
import com.pietrantuono.pietrantuonoevaluationtask.service.FetchService;

public class ContentListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int CONTENTLIST_LOADER = 1;
    private static final String TAG = "ContentListFragment";
    private RecyclerView recycler;
    private RecyclerViewCursorAdapter adapter;
    private Callback callback;

    public ContentListFragment() {
    }

    public static ContentListFragment newInstance() {
        ContentListFragment fragment = new ContentListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getLoaderManager().initLoader(CONTENTLIST_LOADER, null, this);
        View v=inflater.inflate(R.layout.fragment_list, container, false);
        recycler= (RecyclerView) v.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(recycler.getContext()));
        adapter = new RecyclerViewCursorAdapter(getActivity());
        adapter.setOnItemClickListener(new RecyclerViewCursorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(long itemID) {
                if(callback!=null)callback.showDetailFragment(itemID);
            }
        });
        recycler.setAdapter(adapter);
        v.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfNewDataAvailable();
            }
        });
        return v;
    }

    private void checkIfNewDataAvailable() {
        if(getActivity()==null)return;// Sometimes this happens, shouldn't..
        getActivity().startService(new Intent(getActivity(), FetchService.class));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.callback=(Callback)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.callback=null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case CONTENTLIST_LOADER:
                /**
                 * We get all the items that we have, if any
                 */
                return new CursorLoader(
                        getActivity(),
                        Contract.URI_TYPE_MULTIPLE_ITEMS,
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
        int count = cursor.getCount();
        Log.d(TAG,"onLoadFinished, count = "+count);
        adapter.changeCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.changeCursor(null);
    }

    public interface Callback{
        void showDetailFragment(long itemId);
    }
}
