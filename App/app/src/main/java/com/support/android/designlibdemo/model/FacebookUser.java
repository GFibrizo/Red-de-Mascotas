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
            this.notificationId = object.getString("notificationId");
            this.email = object.getString("email");
            this.password = new Password();
            this.phone = "";
            this.address = new Address();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJson() {
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("userName",this.userName);
            jsonObject.put("name",this.name);
            jsonObject.put("lastName",this.lastName);
            jsonObject.put("email",this.email);
            jsonObject.put("facebookId",this.facebookId);
            jsonObject.put("notificationId", this.notificationId);
            jsonObject.put("phone",this.phone);
            jsonObject.put("password",this.password.toJson());
            jsonObject.put("address", address.toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
