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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.anilugale.testapplication.R;
import com.anilugale.testapplication.adapter.ContactAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 Created by Anil Ugale on 14/10/2015.
 */

public class Contact extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CONTACTS_LOADER_ID = 1;
    private static Contact instance;
    public static String TAG="Contact";
    RelativeLayout progress;
    RecyclerView contact_list;

    List<com.anilugale.testapplication.model.Contact> contacts;
    public static Contact newInstance()
    {
        if(instance==null)
            instance=new Contact();
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
        View root=inflater.inflate(R.layout.contact,container,false);
        init(root);
        return root;
    }

    private void init(View root) {

        progress=(RelativeLayout) root.findViewById(R.id.progress);
        contact_list=(RecyclerView) root.findViewById(R.id.contact_list);
        if(contacts==null)
            getLoaderManager().initLoader(CONTACTS_LOADER_ID,
                    null,
                    this);
        else
            setContacts();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (id == CONTACTS_LOADER_ID) {

            progress.setVisibility(View.VISIBLE);
            contact_list.setVisibility(View.GONE);
            return contactsLoader();
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader,final Cursor cursor) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                contacts = contactsFromCursor(cursor);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setContacts();
                    }
                });
            }
        }).start();



    }

    private void setContacts() {

        contact_list.setLayoutManager(new GridLayoutManager(getActivity(),1));
        ContactAdapter adapter=new ContactAdapter(contacts,getActivity());
        contact_list.setAdapter(adapter);
        progress.setVisibility(View.GONE);
        contact_list.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private  Loader<Cursor> contactsLoader() {
        Uri contactsUri = ContactsContract.Contacts.CONTENT_URI;

        String[] projection = {
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
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

    private List<com.anilugale.testapplication.model.Contact> contactsFromCursor(Cursor cursor) {
        List<com.anilugale.testapplication.model.Contact> contacts = new ArrayList();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String  id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                com.anilugale.testapplication.model.Contact contact=new com.anilugale.testapplication.model.Contact();
                contact.setName(name);

                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                    phones.moveToFirst();
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contact.setMobile(phoneNumber);
                    phones.close();
                }
                contacts.add(contact);
            } while (cursor.moveToNext()&& contacts.size()<=100);
        }

        return contacts;
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
                getLoaderManager().initLoader(CONTACTS_LOADER_ID,
                        null,
                        this);
                return true;

        }

        return false;
    }
}
