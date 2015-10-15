package com.anilugale.testapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.anilugale.testapplication.fragment.CallLog;
import com.anilugale.testapplication.fragment.Contact;
import com.anilugale.testapplication.fragment.SMSLog;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setNavigation(toolbar);
        init();
        goToContact();
    }

    private void init() {
        manager=getSupportFragmentManager();
    }

    private void setNavigation(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.contact:
                goToContact();
               break;
            case R.id.callLog:
                goToCallLog();
               break;

            case R.id.smsLog:
                goToSMSLog();
               break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goToContact() {

       Contact contact=(Contact) manager.findFragmentByTag(Contact.TAG);
        if(contact==null)
            contact=Contact.newInstance();
        manager.beginTransaction()
                .replace(R.id.frame,contact)
                .commit();

    }
    private void goToCallLog() {

       CallLog contact=(CallLog) manager.findFragmentByTag(CallLog.TAG);
        if(contact==null)
            contact=CallLog.newInstance();
        manager.beginTransaction()
                .replace(R.id.frame,contact)
                .commit();

    }
    private void goToSMSLog() {

        SMSLog contact=(SMSLog) manager.findFragmentByTag(SMSLog.TAG);
        if(contact==null)
            contact=SMSLog.newInstance();
        manager.beginTransaction()
                .replace(R.id.frame,contact)
                .commit();

    }
}
