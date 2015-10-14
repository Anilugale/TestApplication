package com.anilugale.testapplication.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.anilugale.testapplication.R;
import com.anilugale.testapplication.adapter.CallAdapter;
import com.anilugale.testapplication.adapter.ContactAdapter;
import com.anilugale.testapplication.model.Call;

import java.util.ArrayList;
import java.util.List;

/**
      Created by Anil Ugale on 14/10/2015.
 */

public class CallLog extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CALL_LOG_LOADER_ID = 1;
    private static CallLog instance;
    public static String TAG="CallLog";

    RelativeLayout progress;
    RecyclerView call_list;
    public static CallLog newInstance()
    {
        if(instance==null)
        instance=new CallLog();
        return instance;
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
        getLoaderManager().initLoader(CALL_LOG_LOADER_ID,
                null,
                this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.

        if (id == CALL_LOG_LOADER_ID) {
            return contactsLoader();
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        contactsFromCursor(cursor);

        List<Call> contacts = contactsFromCursor(cursor);
        call_list.setLayoutManager(new GridLayoutManager(getActivity(),1));
        CallAdapter adapter=new CallAdapter(contacts,getActivity());
        call_list.setAdapter(adapter);

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

    private List<Call> contactsFromCursor(Cursor cursor) {
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
            } while (cursor.moveToNext());


        }
        return  calls;
    }
}
