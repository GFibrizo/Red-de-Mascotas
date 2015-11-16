package utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.support.android.designlibdemo.model.Address;
import com.support.android.designlibdemo.model.PetAdoption;
import com.support.android.designlibdemo.model.TextAndImagePetContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    public void update(JSONArray newValues) {
        mValues = newValues;
        notifyDataSetChanged();
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
        JSONObject object = null;
        try {
            object = (JSONObject) mValues.get(position);
            holder.mBoundString = (String) object.get("name");

            String status = object.getString("publicationStatus");
            Log.e("STATUS", status);
            if (status.equals("BLOCKED")) {
                holder.mNameView.setText("BLOQUEADO");
                holder.mNameView.setTextColor(Color.RED);
            } else {
                holder.mNameView.setText((String) object.get("name"));
            }

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
        if (object != null) {
            setDetailActivity(holder, object);
        }

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

    /*protected void setDetailActivity(final ViewHolder holder) {

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, PetsDetailActivity.class);
                intent.putExtra(PetsDetailActivity.EXTRA_NAME, holder.mBoundString);

                context.startActivity(intent);
            }
        });
    }*/



    private PetAdoption getPetPublication(JSONObject object) {

        PetAdoption mascota = null;
        try {
            if (!object.has("name")) {
                object.put("name", object.getString("state"));
            }

            if (!object.has("age")) {
                object.put("age", "Desconocida");
            }

            if (!object.has("needsTransitHome")) {
                object.put("needsTransitHome", false);
            }

            List<String> colors = new ArrayList<>();
            List<String> behavior = new ArrayList<>();
            List<String> images = new ArrayList<>();
            if (!object.get("colors").toString().equals("null")) {
                for (int j = 0; j < object.getJSONArray("colors").length(); j++) {
                    colors.add((String) object.getJSONArray("colors").get(j));
                }
            }

            if (!object.get("images").toString().equals("null")) {
                for (int j = 0; j < object.getJSONArray("images").length(); j++) {
                    images.add(((String) object.getJSONArray("images").get(j)).replace("[","").replace("]", ""));
                }
            }
            mascota = new PetAdoption(object.getString("id"),
                    object.getString("name"),
                    object.getString("type"),
                    object.getString("ownerId"),
                    null,
                    object.getString("breed"),
                    object.getString("gender"),
                    object.getString("age"),
                    object.getString("size"),
                    colors,
                    object.getString("eyeColor"),
                    behavior,
                    images,
                    object.getBoolean("needsTransitHome"),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
        } catch (JSONException e) {
            Log.e("Error al crear el JSON", e.getMessage());
        }
        return mascota;
    }




    protected void setDetailActivity(final ViewHolder holder, final JSONObject object) {


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (object == null) return;
                Context context = v.getContext();
                Intent intent = new Intent(context, PetsDetailActivity.class);
                //intent.putExtra(PetsDetailActivity.EXTRA_NAME, holder.mBoundString);
                PetAdoption adoption = getPetPublication(object);
                //Intent intent = new Intent(context, PetsDetailActivity.class);
                TextAndImagePetContainer petContainer = new TextAndImagePetContainer(adoption);
                intent.putExtra("id", petContainer.getId());
                intent.putExtra("ownerId", petContainer.getOwnerId());
                intent.putExtra("nombre", petContainer.getNombre());
                intent.putExtra("raza", petContainer.getRaza());
                intent.putExtra("sexo", petContainer.getSexo());
                intent.putExtra("edad", petContainer.getEdad());
                intent.putExtra("tamanio", petContainer.getTamanio());
                intent.putExtra("colorPelaje", petContainer.getColorPelaje());
                intent.putExtra("colorOjos", petContainer.getColorOjos());
                intent.putExtra("ubicacion", petContainer.getBarrio());
                intent.putExtra("caracteristicas", petContainer.getCaracteristicas());
                intent.putExtra("descripcion", petContainer.getDescripcion());
                intent.putExtra("conducta", petContainer.getConducta());
                intent.putExtra("images", petContainer.getImages());
                intent.putExtra("transitHome", "false");
                intent.putExtra("mine", true);
                intent.putExtra("informers", getInformers(object));
                try {
                    String pubType = object.getString("publicationType");
                    intent.putExtra("publicationType", pubType);
                } catch (JSONException e){}
                context.startActivity(intent);
            }
        });
    }


    private ArrayList<String> getInformers(JSONObject object) {
        ArrayList<String> informers = new ArrayList<>();
        try {
            JSONArray reports = object.getJSONArray("reports");
            int length = reports.length();
            for (int i = 0; i < length; i++) {
                JSONObject report = (JSONObject) reports.get(i);
                if (report.getString("status").equals("PENDING")) {
                    informers.add(report.getString("informer"));
                }
            }
        } catch (JSONException e) {}
        return informers;
    }




}
