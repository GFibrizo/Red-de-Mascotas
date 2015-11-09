package utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.support.android.designlibdemo.PetsDetailActivity;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.ResultImageAndTextArrayAdapter;
import com.support.android.designlibdemo.TextAndImage;
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
public class AdoptionAdapter  extends SimpleStringRecyclerViewAdapter {


    /*public AdoptionAdapter(Context context, JSONArray items) {
        super(context, items);
    }*/
    protected final TypedValue mTypedValue = new TypedValue();
    protected int mBackground;
    protected JSONArray mValues;


    public AdoptionAdapter(Context context, JSONArray items) {
        super(context,items);
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
    }

    public void remove(int position) {
        mValues.remove(position);
        notifyItemRemoved(position);
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



    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        //holder.mBoundString = mValues.get(position);
        //holder.mBoundString =
        JSONArray images = null;
        JSONObject object = null;
        try {
            object = (JSONObject) mValues.get(position);
            holder.mBoundString = (String) object.get("name");

            holder.mNameView.setText((String) object.get("name"));
            images =  object.getJSONArray("images");
            holder.mPetTypeView.setText("Tipo: " + (String) object.get("type"));
            holder.mBreedView.setText("Raza: " + (String) object.get("breed"));
            holder.mGenderView.setText("Género: " + (String) object.get("gender"));
            holder.mTypeView.setText("Barrio: " + ((JSONObject)object.get("address")).getString("neighbourhood"));

            holder.mDateView.setText("Fecha: " + (String) object.get("publicationDate"));

        } catch (JSONException e) {
            Log.e("ERROR JSON RESP", e.getMessage());
        }

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

            Log.e("IMAGE ARRAY1", imageArray[0]);
            String baseUrlForImage = RequestHandler.getServerUrl() + "/pet/image/" + imageArray[0];
            new ImageUrlView(baseUrlForImage, holder.mImageView).connect();
        } catch (JSONException | NullPointerException e) {
            Log.e("ERROR IMAGE", e.getMessage());
        }
    }


    /******************************************************************************************/
    /******************************************************************************************/

    @Override
    public int getItemCount() {
        return mValues.length();
    }

    protected void setDetailActivity(final ViewHolder holder, final JSONObject object) {


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, PetsDetailActivity.class);
                //intent.putExtra(PetsDetailActivity.EXTRA_NAME, holder.mBoundString);
                PetAdoption adoption = getPetAdoption(object);
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
                try {
                    Boolean transitHome = object.getBoolean("needsTransitHome");
                    String transitHomeUser = object.getString("transitHomeUser");
                    Log.e("Adoption TransitHome", transitHome + ", " + transitHomeUser);
                    if ((transitHome != false) && (transitHomeUser.equals("null"))) {
                        Log.e("TRUE", "transit");
                        intent.putExtra("transitHome", "true");
                    } else {
                        Log.e("FALSE", "transit");
                        intent.putExtra("transitHome", "false");
                    }
                } catch (JSONException e) {}
                context.startActivity(intent);

            }
        });
    }


    private String getHairColor(String colors) {
        String strColors = null;
        try {
            JSONArray colorArray = new JSONArray(colors);
            if (colorArray != null) {
                for (int i = 0; i < colorArray.length(); i++) {
                    strColors += colorArray.get(i);
                    strColors += " ";
                }
            }
        } catch (JSONException e) {

        }
        return strColors;
    }


    //    public PetAdoption(String name, String type, String ownerId, Address address, String breed,
//                       String gender, String age, String size, List<String> colors, String eyeColor,
//                       List<String> behavior, List<String> images, Boolean needsTransitHome,
//                       Boolean isCastrated, Boolean isOnTemporaryMedicine, Boolean isOnChronicMedicine, String description) {
    private PetAdoption getPetAdoption(JSONObject object) {

        PetAdoption mascota = null;
        try {
            Address address = new Address();
            String barrio = ((JSONObject) object.get("address")).getString("neighbourhood");
            address.setNeighbourhood(barrio);
            List<String> colors = new ArrayList<>();
            List<String> behavior = new ArrayList<>();
            List<String> images = new ArrayList<>();
            if (!object.get("colors").toString().equals("null")) {
                for (int j = 0; j < object.getJSONArray("colors").length(); j++) {
                    colors.add((String) object.getJSONArray("colors").get(j));
                }
            }
            if (!object.get("behavior").toString().equals("null")) {
                for (int j = 0; j < object.getJSONArray("behavior").length(); j++) {
                    behavior.add((String) object.getJSONArray("behavior").get(j));
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
                    address,
                    object.getString("breed"),
                    object.getString("gender"),
                    object.getString("age"),
                    object.getString("size"),
                    colors,
                    object.getString("eyeColor"),
                    behavior,
                    images,
                    object.getBoolean("needsTransitHome"),
                    object.getBoolean("isCastrated"),
                    object.getBoolean("isOnTemporaryMedicine"),
                    object.getBoolean("isOnChronicMedicine"),
                    object.getString("description"),
                    null);
        } catch (JSONException e) {
            Log.e("Error al crear el JSON", e.getMessage());
        }
        return mascota;
    }

}
