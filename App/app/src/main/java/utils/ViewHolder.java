package utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.support.android.designlibdemo.R;

/**
 * Created by fabrizio on 18/10/15.
 */
public class ViewHolder extends RecyclerView.ViewHolder {
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
