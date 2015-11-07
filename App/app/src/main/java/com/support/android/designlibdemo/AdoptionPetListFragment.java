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

    @Override
    protected QueryTask setUpdateTask() {return new UpdateTask();}


    protected class AdoptionQueryTask extends QueryTask {

        public AdoptionQueryTask() {super();}

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


    protected class UpdateTask extends AdoptionQueryTask {

        public UpdateTask() {super();}

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                //Intent intent = new Intent(getContext(), ResultListActivity.class);
                if (response != null) {
                    result = response;
                    if (viewHolderAdapter != null)
                        viewHolderAdapter.update(result);
                    rv.invalidate();
                }
            }
        }
    }

}
