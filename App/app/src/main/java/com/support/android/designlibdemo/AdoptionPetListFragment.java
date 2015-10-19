package com.support.android.designlibdemo;


import android.support.v7.widget.RecyclerView;
import android.util.Log;

import utils.AdoptionAdapter;
import utils.ResultsRequest;

public class AdoptionPetListFragment extends PetsListFragment {

    @Override
    protected void setAdapter(RecyclerView recyclerView) {
        recyclerView.setAdapter(new AdoptionAdapter(getActivity(), result));
    }


    @Override
    protected QueryTask setQuery() {
        return new AdoptionQueryTask();
    }


    protected class AdoptionQueryTask extends QueryTask {

        @Override
        protected Boolean doInBackground(Void... params) {
            ResultsRequest request = new ResultsRequest(getContext());
            response = request.searchAdoption();
            Log.e("RESPONSE1", response.toString());
            return true;
        }
    }
}
