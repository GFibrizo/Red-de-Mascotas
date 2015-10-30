package utils;

import android.content.Context;

/**
 * Created by fabrizio on 30/10/15.
 */
public class TransitHomeRequest extends BasicOwnerPetRequest {

    public TransitHomeRequest(Context context) {
        super(context);
        PATH = "/pet/petId/adoption";
        PET_ID = "petId";
        OWNER_ID = "transitHomeUser";
    }
}
