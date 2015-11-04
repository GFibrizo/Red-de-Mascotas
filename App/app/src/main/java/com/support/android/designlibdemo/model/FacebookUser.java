package com.support.android.designlibdemo.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by agu on 04/11/15.
 */
public class FacebookUser extends  User {

    public FacebookUser(JSONObject object) {
        try {

            this.facebookId = object.getString("facebookId");
            this.userName = object.getString("userName");
            this.name = object.getString("name");
            this.lastName = object.getString("lastName");
            this.email = "";
            this.password = new Password();
            this.phone = "";
            this.address = new Address();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
