package com.support.android.designlibdemo;


import android.support.v7.widget.RecyclerView;
import android.util.Log;

import utils.AdoptionAdapter;
import utils.ResultsRequest;

public class AdoptionPetListFragment extends PetsListFragment {

    @Override
    protected void setAdapter(RecyclerView recyclerView) {
        viewHolderAdapter = new AdoptionAdapter(getActivity(), result);
        recyclerView.setAdapter(viewHolderAdapter);
    }

    @Override
    protected void setTouchCallback(RecyclerView recyclerView) {
        /*Pass*/
    }

    @Override
    protected QueryTask setQuery() {
        return new AdoptionQueryTask();
    }


    protected class AdoptionQueryTask extends QueryTask {

        @Override
        protected Boolean doInBackground(Void... params) {
            ResultsRequest request = new ResultsRequest(getContext());
            response = null;
            if (response == null){
                response = request.searchAdoption();
            }
            //Log.e("RESPONSE1", response.toString());
            return true;
        }
    }
}
