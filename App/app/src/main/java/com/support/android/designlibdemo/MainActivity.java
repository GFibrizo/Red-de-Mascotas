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

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.support.android.designlibdemo.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private TabLayout mTabLayout;
    private FragmentManager fragmentManager;
    User loginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        if (viewPager != null)
            mTabLayout.setupWithViewPager(viewPager);


            if ((getIntent().getExtras() != null) && (getIntent().getStringExtra("user") != null)){
                try {
                    JSONObject object = new JSONObject(getIntent().getStringExtra("user"));
                    this.loginUser = new User(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

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
        adapter.addFragment(new PetsListFragment(), "En adopción");
        adapter.addFragment(new PetsListFragment(), "Mis búsquedas");
        adapter.addFragment(new PetsListFragment(), "Mis\n publicaciones");
        viewPager.setAdapter(adapter);
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private void setupDrawerContent(NavigationView navigationView) {
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
                        break;
                    case R.id.report_found:
                        break;
                    case R.id.invite_a_friend:
                        break;
                    case R.id.config:
                        break;
                    case R.id.about:
                        break;
                    case R.id.logout:
                        // ACA VA EL CODIGO PARA EL LOGOUT
                        LoginManager.getInstance().logOut();
                        finish();
                        break;
                    default:
                        mDrawerLayout.closeDrawers();
                        return true;
                }
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                if (intent != null) {
                    startActivity(intent);
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_in);
                }
                return true;

            }
        });
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
