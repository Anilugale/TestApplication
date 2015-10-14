package com.anilugale.testapplication.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.anilugale.testapplication.R;
import com.anilugale.testapplication.adapter.CallAdapter;
import com.anilugale.testapplication.adapter.SmsAdapter;
import com.anilugale.testapplication.model.Call;
import com.anilugale.testapplication.model.SMS;

import java.util.ArrayList;
import java.util.List;

/**
      Created by Anil Ugale on 14/10/2015.
 */

public class SMSLog extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static SMSLog instance;
    public static String TAG="SMSLog";
    private static final int SMS_LOADER_ID = 1;
    RelativeLayout progress;
    RecyclerView sms_list;

    public static SMSLog newInstance()
    {
        if(instance==null)
        instance=new SMSLog();
        return instance;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root=inflater.inflate(R.layout.sms_log,container,false);
        init(root);
        return root;
    }


    private void init(View root) {
        progress=(RelativeLayout) root.findViewById(R.id.progress);
        sms_list=(RecyclerView) root.findViewById(R.id.call_list);
        getLoaderManager().initLoader(SMS_LOADER_ID,
                null,
                this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.

        if (id == SMS_LOADER_ID) {
            return contactsLoader();
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        contactsFromCursor(cursor);

        List<SMS> contacts = contactsFromCursor(cursor);
        sms_list.setLayoutManager(new GridLayoutManager(getActivity(),1));
        SmsAdapter adapter=new SmsAdapter(contacts,getActivity());
        sms_list.setAdapter(adapter);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private  Loader<Cursor> contactsLoader() {
        Uri contactsUri =Uri.parse("content://sms/inbox");;

        String[] projection = {
                "address","body","person"
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

    private List<SMS> contactsFromCursor(Cursor cursor) {
        List<SMS> calls = new ArrayList();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                SMS sms=new SMS();

                String address = cursor.getString(cursor.getColumnIndex("address"));
                String body = cursor.getString(cursor.getColumnIndex("body"));
                String person = cursor.getString(cursor.getColumnIndex("person"));


                sms.setAddress(address);
                sms.setBody(body);
                sms.setPerson(person);

                calls.add(sms);
            } while (cursor.moveToNext());


        }
        return  calls;
    }
}
