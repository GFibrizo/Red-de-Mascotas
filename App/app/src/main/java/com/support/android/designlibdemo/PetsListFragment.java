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

import android.app.Activity;
import android.content.Context;
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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.support.android.designlibdemo.data.communications.ImageUrlView;
import com.support.android.designlibdemo.model.SearchForAdoptionFilters;
import com.support.android.designlibdemo.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import utils.RequestHandler;
import utils.ResultsRequest;
import utils.SearchRequest;

import static utils.Constants.CHEESE;
import static utils.Constants.getRandomCheeseDrawable;

public class PetsListFragment extends Fragment {

    private int type;
    SharedPreferences preferences;
    JSONArray result = null;

    @Override
    public void setArguments(Bundle bundle) {
        this.type = bundle.getInt("type");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        QueryTask resultTask = new QueryTask();
        resultTask.execute();
        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_cheese_list, container, false);
        setupRecyclerView(rv);
        return rv;
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    protected void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), result));     // TODO: CARGA LOS NOMBRES DE LOS ITEMS
        SimpleItemTouchHelperCallback callback = new SimpleItemTouchHelperCallback((SimpleStringRecyclerViewAdapter) recyclerView.getAdapter());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
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

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private JSONArray mValues;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public String mBoundString;

            public final View mView;
            public final ImageView mImageView;
            public final TextView mNameView;
            public final TextView mPetTypeView;
            public final TextView mBreedView;
            public final TextView mGenderView;
            public final TextView mTypeView;
            public final TextView mDateView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                mNameView = (TextView) view.findViewById(R.id.name_main);
                mPetTypeView = (TextView) view.findViewById(R.id.pet_type_main);
                mBreedView = (TextView) view.findViewById(R.id.breed_main);
                mGenderView = (TextView) view.findViewById(R.id.gender_main);
                mTypeView = (TextView) view.findViewById(R.id.type_main);
                mDateView = (TextView) view.findViewById(R.id.date_main);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mNameView.getText();
            }
        }

        /*public String getValueAt(int position) {
            return mValues.get(position);
        }*/

        public SimpleStringRecyclerViewAdapter(Context context, JSONArray items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
        }

        public void remove(int position) {
            mValues.remove(position);
            notifyItemRemoved(position);
        }

        /******************************************************************************************/
        /******************************************************************************************/

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        /******************************************************************************************/
        /******************************************************************************************/

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {


            //holder.mBoundString = mValues.get(position);
            //holder.mBoundString =
            JSONArray images = null;
            try {
                JSONObject object = (JSONObject) mValues.get(position);
                holder.mBoundString = (String) object.get("name");

                holder.mNameView.setText((String) object.get("name"));
                holder.mPetTypeView.setText((String) object.get("type"));
                holder.mBreedView.setText((String) object.get("breed"));
                holder.mGenderView.setText((String) object.get("gender"));
                holder.mTypeView.setText((String) object.get("publicationType"));
                holder.mDateView.setText((String) object.get("publicationDate"));
                images = (JSONArray) object.get("images");

            } catch (JSONException e) {

            }


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, PetsDetailActivity.class);
                    intent.putExtra(PetsDetailActivity.EXTRA_NAME, holder.mBoundString);

                    context.startActivity(intent);
                }
            });

            /*Glide.with(holder.mImageView.getContext())
                    .load(getRandomCheeseDrawable())    //TODO: ACA CARGA LA IMAGEN
                    .fitCenter()
                    .into(holder.mImageView);*/
            try {
                String baseUrlForImage = RequestHandler.getServerUrl() + "/pet/image/" + ((String) images.get(0));
                new ImageUrlView(baseUrlForImage, holder.mImageView);
            } catch (JSONException e) {}
        }

        /******************************************************************************************/
        /******************************************************************************************/

        @Override
        public int getItemCount() {
            return mValues.length();
        }
    }

    /******************************************************************************************/
    /******************************************************************************************/


    public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

        private final SimpleStringRecyclerViewAdapter mAdapter;

        public SimpleItemTouchHelperCallback(SimpleStringRecyclerViewAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            mAdapter.remove(viewHolder.getAdapterPosition());
        }
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private class QueryTask extends AsyncTask<Void, Void, Boolean> {

        SearchForAdoptionFilters filters;
        JSONArray response;
        JSONObject object;
        String ownerId = "";

        QueryTask() {
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
            response = request.searchPublications(ownerId);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Intent intent = new Intent(getContext(), ResultListActivity.class);
                if (response != null) {
                    result = response;
                }
            }
        }

        @Override
        protected void onCancelled() { }

    }

}
