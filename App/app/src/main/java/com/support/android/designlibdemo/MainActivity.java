/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.support.android.designlibdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.support.android.designlibdemo.model.Address;
import com.support.android.designlibdemo.model.ReportLostPet;
import com.support.android.designlibdemo.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

import utils.Constants;

/**
 * TODO
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private TabLayout mTabLayout;
    private FragmentManager fragmentManager;
    User loginUser;
    private JSONObject userData = new JSONObject();
    SharedPreferences prefs = null;
    Boolean isInitialized = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        loadUserData();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        if (viewPager != null)
            mTabLayout.setupWithViewPager(viewPager);

        try {
            Parse.initialize(this, Constants.PARSE_APPLICATION_ID, Constants.PARSE_CLIENT_KEY);
            ParseInstallation.getCurrentInstallation().saveInBackground();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private void loadUserData() {
        try {
            JSONObject object = new JSONObject(prefs.getString("userData", "{}")); //getIntent().getStringExtra("user")
            Log.e("USER DATA", prefs.getString("userData", "{}"));

            if (object.length() == 0) {
                Toast.makeText(getApplicationContext(), "Error cargando datos de usuario", Toast.LENGTH_SHORT).show();
                return;
            }

            this.loginUser = new User(object);
            userData.put("ownerId", loginUser.getId());
            Log.e("ID",loginUser.getId());
            Address addr = loginUser.getAddress();
            if (addr != null) {
                JSONObject address = new JSONObject();
                address.put("street", addr.getStreet());
                address.put("number", addr.getNumber());
                address.put("neighbourhood", addr.getNeighbourhood());
                address.put("city", addr.getCity());
                address.put("province", addr.getProvince());
                address.put("country", addr.getCountry());
                userData.put("address", address);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**********************************************************************************************/
    /**********************************************************************************************/

    @Override
    public void onStop() {
        super.onStop();
        Log.e("STOP", "STOPPED MAIN");
        isInitialized = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("START", "STARTED MAIN");
        if (!isInitialized) return;
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        updateViewPager(viewPager);
        isInitialized = false;
    }



    /**********************************************************************************************/
    /**********************************************************************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**********************************************************************************************/
    /**********************************************************************************************/


    private void setupViewPager(ViewPager viewPager) {
        fragmentManager = getSupportFragmentManager();
        Adapter adapter = new Adapter(fragmentManager);
        Log.e("SETUP", "SET UP VIEW PAGER");

        PetsListFragment adoption = new AdoptionPetListFragment();
        Bundle bundleAdoption = new Bundle();
        bundleAdoption.putInt("type", Constants.LAST_ADOPTION);
        adoption.setArguments(bundleAdoption);

        PetsListFragment publications = new PetsListFragment();
        Bundle bundlePublication = new Bundle();
        bundlePublication.putInt("type", Constants.PUBLICATION);
        publications.setArguments(bundlePublication);

        adapter.addFragment(adoption, "Últimos\n en adopción");
        //adapter.addFragment(new PetsListFragment(), "Mis búsquedas");
        adapter.addFragment(publications, "Mis\n publicaciones");
        viewPager.setAdapter(adapter);
    }

    private void updateViewPager(ViewPager viewPager) {
        fragmentManager = getSupportFragmentManager();
        Log.e("UPDATE", "UPDATE VIEW PAGER");
        Adapter adapter = (Adapter) viewPager.getAdapter();

        List<Fragment> fragments = adapter.mFragments;

        PetsListFragment adoption = (PetsListFragment) fragments.get(0);
        PetsListFragment publications = (PetsListFragment) fragments.get(1);

        adoption.update();
        publications.update();
        adapter.notifyDataSetChanged();
    }


    /**********************************************************************************************/
    /**********************************************************************************************/

    private void setupDrawerContent(NavigationView navigationView) {
        TextView nameAndLastName = (TextView) findViewById(R.id.profile_name);
        nameAndLastName.setText(loginUser.getName() + " " + loginUser.getLastName());
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        Intent intent = null;
                        switch (menuItem.getItemId()) {
                            case R.id.adopt:
                                intent = new Intent(getApplicationContext(), SearchInAdoptionActivity.class);
                                break;
                            case R.id.offer_in_adoption:
                                intent = new Intent(getApplicationContext(), PublishInAdoptionActivity.class);
                                break;
                            case R.id.notification:
                                intent = new Intent(getApplicationContext(), NotificationActivity.class);
                                break;
                            case R.id.report_missing:
                                intent = new Intent(getApplicationContext(), ReportLostPet.class);
                                break;
                            case R.id.report_found:
                                intent = new Intent(getApplicationContext(), FoundPetActivity.class);
                                break;
                            case R.id.config:
                                //intent = new Intent(getApplicationContext(), NotificationHandlerActivity.class);
                                break;
                            case R.id.about:
                                break;
                            case R.id.logout:
                                closeApp();
                                break;
                            default:
                                mDrawerLayout.closeDrawers();
                                return true;
                        }
                        //      menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        intent.putExtra("data", userData.toString());
                        if (intent != null)
                            startActivity(intent);
                        return true;

                    }
                });
    }

    private void closeApp() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.edit().clear().commit();
        LoginManager.getInstance().logOut();
        finishAffinity();
        System.exit(0);
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

}
