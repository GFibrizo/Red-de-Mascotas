package com.support.android.designlibdemo;


import android.util.Log;
import utils.ResultsRequest;
import utils.SimpleStringRecyclerViewAdapter;
import utils.ViewHolder;


public class AdoptionPetListFragment extends PetsListFragment {



    protected class AdoptionQueryTask extends QueryTask {

        @Override
        protected Boolean doInBackground(Void... params) {
            ResultsRequest request = new ResultsRequest(getContext());
            response = request.searchAdoption(ownerId);
            Log.e("RESPONSE", response.toString());
            return true;
        }
    }
}
