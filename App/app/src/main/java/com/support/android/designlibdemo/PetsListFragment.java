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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.support.android.designlibdemo.model.SearchForAdoptionFilters;
import com.support.android.designlibdemo.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import utils.ViewHolder;
import utils.ResultsRequest;
import utils.SimpleItemTouchHelperCallback;
import utils.SimpleStringRecyclerViewAdapter;

public class PetsListFragment extends Fragment {

    protected int type;
    protected SharedPreferences preferences;
    protected JSONArray result = null;
    protected RecyclerView rv;
    protected SimpleStringRecyclerViewAdapter viewHolderAdapter;

    @Override
    public void setArguments(Bundle bundle) {
        this.type = bundle.getInt("type");
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        QueryTask resultTask = setQuery();
        resultTask.execute();
    }

    protected QueryTask setQuery() {
        return new QueryTask();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = (RecyclerView) inflater.inflate(R.layout.fragment_cheese_list, container, false);
        return rv;
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    protected void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        //recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), result));
        setAdapter(recyclerView);
    }

    protected void setTouchCallback(RecyclerView recyclerView) {
        SimpleItemTouchHelperCallback callback = new SimpleItemTouchHelperCallback((SimpleStringRecyclerViewAdapter) recyclerView.getAdapter(), getActivity());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    protected void setAdapter(RecyclerView recyclerView) {
        viewHolderAdapter = new SimpleStringRecyclerViewAdapter(getActivity(), result);
        recyclerView.setAdapter(viewHolderAdapter);
    }


    public void update() {
        Log.e("Update fragment", this.getClass().toString());
        QueryTask resultTask = setQuery();
        resultTask.execute();
        if (viewHolderAdapter != null)
            viewHolderAdapter.update(result);
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    protected List<String> getRandomSublist(String[] array, int amount) {
        ArrayList<String> list = new ArrayList<>(amount);
        Random random = new Random();
        while (list.size() < amount) {
            list.add(array[random.nextInt(array.length)]);
        }
        return list;
    }

    /**********************************************************************************************/
    /**********************************************************************************************/


    /******************************************************************************************/
    /******************************************************************************************/




    /**********************************************************************************************/
    /**********************************************************************************************/

    protected class QueryTask extends AsyncTask<Void, Void, Boolean> {

        SearchForAdoptionFilters filters;
        JSONArray response;
        JSONObject object;
        String ownerId = "";

        QueryTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            try {
                object = new JSONObject(preferences.getString("userData", "{}")); //getIntent().getStringExtra("user")
                Log.e("USER DATA", preferences.getString("userData", "{}"));
            } catch (JSONException e) {

            }
            if (object.length() == 0) {
                Toast.makeText(getContext(), "Error cargando datos de usuario", Toast.LENGTH_SHORT).show();
                return;
            }
            User loginUser = new User(object);
            ownerId = loginUser.getId();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ResultsRequest request = new ResultsRequest(getContext());
            int count = 0;
            response = null;
            if (response == null) {
                response = request.searchPublications(ownerId);
            }
            //Log.e("RESPONSE", response.toString());
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                //Intent intent = new Intent(getContext(), ResultListActivity.class);
                if (response != null) {
                    result = response;
                    setupRecyclerView(rv);
                    setTouchCallback(rv);
                    rv.invalidate();
                }
            }
        }

        @Override
        protected void onCancelled() { }

    }

}
