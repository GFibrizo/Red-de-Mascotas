package utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import utils.ViewHolder;

import com.support.android.designlibdemo.PetsDetailActivity;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.data.communications.ImageUrlView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fabrizio on 18/10/15.
 */
public class SimpleStringRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    protected final TypedValue mTypedValue = new TypedValue();
    protected int mBackground;
    protected JSONArray mValues;



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

    public JSONObject getAt(int position) throws JSONException {
        return (JSONObject) mValues.get(position);
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
            holder.mPetTypeView.setText("Tipo: " + (String) object.get("type"));
            holder.mBreedView.setText("Raza: " + (String) object.get("breed"));
            holder.mGenderView.setText("Género: " + (String) object.get("gender"));
            String pubType = object.getString("publicationType");
            if (pubType.equals("LOST")) {
                holder.mTypeView.setText("Estado: Perdido");
            } else if (pubType.equals("FOUND")) {
                holder.mTypeView.setText("Estado: Encontrado");
            } else {
                holder.mTypeView.setText("Estado: En adopción");
            }
            holder.mDateView.setText("Fecha: " + (String) object.get("publicationDate"));
            images =  object.getJSONArray("images");

        } catch (JSONException e) {

        }

        changeTypeVisibility(holder);
        //setDetailActivity(holder);

            /*Glide.with(holder.mImageView.getContext())
                    .load(getRandomCheeseDrawable())    //TODO: ACA CARGA LA IMAGEN
                    .fitCenter()
                    .into(holder.mImageView);*/

        try {
            String strImage = ((String) images.get(0)).replace("[", "").replace("]", "");
            String[] imageArray = strImage.split(", ");

            Log.e("IMAGE ARRAY", imageArray[0]);
            String baseUrlForImage = RequestHandler.getServerUrl() + "/pet/image/" + imageArray[0];
            new ImageUrlView(baseUrlForImage, holder.mImageView).connect();
        } catch (JSONException | NullPointerException e) {}
    }

    protected void changeTypeVisibility(ViewHolder holder) {

    }

    /******************************************************************************************/
    /******************************************************************************************/

    @Override
    public int getItemCount() {
        return mValues.length();
    }

    protected void setDetailActivity(final ViewHolder holder) {

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, PetsDetailActivity.class);
                intent.putExtra(PetsDetailActivity.EXTRA_NAME, holder.mBoundString);

                context.startActivity(intent);
            }
        });
    }

}