package com.anilugale.testapplication.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.anilugale.testapplication.R;
import com.anilugale.testapplication.adapter.CallAdapter;
import com.anilugale.testapplication.model.Call;

import java.util.ArrayList;
import java.util.List;

/**
      Created by Anil Ugale on 14/10/2015.
 */

public class CallLog extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private static CallLog instance;
    public static String TAG="CallLog";
    private static final int CALL_LOG_LOADER_ID = 1;
    RelativeLayout progress;
    RecyclerView call_list;
    List<Call> callsData;
    public static CallLog newInstance()
    {
        if(instance==null)
        instance=new CallLog();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root=inflater.inflate(R.layout.call_log,container,false);
        init(root);
        return root;
    }

    private void init(View root) {
        progress=(RelativeLayout) root.findViewById(R.id.progress);
        call_list=(RecyclerView) root.findViewById(R.id.call_list);
        if(callsData==null)
        getLoaderManager().initLoader(CALL_LOG_LOADER_ID,
                null,
                this);
        else
            setCalls();
    }

    private void setCalls() {
        call_list.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        CallAdapter adapter=new CallAdapter(callsData,getActivity());
        call_list.setAdapter(adapter);
        progress.setVisibility(View.GONE);
        call_list.setVisibility(View.VISIBLE);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.

        if (id == CALL_LOG_LOADER_ID) {
            progress.setVisibility(View.VISIBLE);
            call_list.setVisibility(View.GONE);
            return contactsLoader();
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        callsData = instance.callFromCursor(cursor);
        setCalls();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private  Loader<Cursor> contactsLoader() {
        Uri contactsUri = android.provider.CallLog.Calls.CONTENT_URI;

        String[] projection = {
                android.provider.CallLog.Calls.NUMBER,
                android.provider.CallLog.Calls.TYPE,
                android.provider.CallLog.Calls.DATE,
                android.provider.CallLog.Calls.DURATION,
        } ;

        String selection = null;
        String[] selectionArgs = {};
        String sortOrder = null;

        return new CursorLoader(
                getActivity(),
                contactsUri,
                projection,
                selection,
                selectionArgs,
                sortOrder);
    }

    private List<Call> callFromCursor(Cursor cursor) {
        List<Call> calls = new ArrayList();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Call call=new Call();

                String number = cursor.getString(cursor.getColumnIndex( android.provider.CallLog.Calls.NUMBER));
                String type = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.TYPE));
                String date = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.DATE));
                String duration = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.DURATION));

                call.setDate(date);
                call.setNumber(number);
                call.setType(type);
                call.setDuration(duration);

                calls.add(call);
            } while (cursor.moveToNext()&& calls.size()<=50);


        }
        return  calls;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                getLoaderManager().initLoader(CALL_LOG_LOADER_ID,
                        null,
                        this);
                return true;

        }

        return false;
    }
}
