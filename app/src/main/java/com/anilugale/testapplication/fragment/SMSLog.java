package com.anilugale.testapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anilugale.testapplication.R;

/**
      Created by Anil Ugale on 14/10/2015.
 */

public class SMSLog extends Fragment {

    private static SMSLog instance;
    public static String TAG="SMSLog";

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
        return root;
    }
}
