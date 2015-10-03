package com.support.android.designlibdemo.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by agu_k_000 on 26/09/2015.
 */
public class Password {
    private String encryption;
    private String salt;

    public String getEncryption() {
        return encryption;
    }

    public String getSalt() {
        return salt;
    }

    public Password(String encryption, String salt) {
        this.encryption = encryption;
        this.salt = salt;

    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public JSONObject toJson() {
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("encryption",this.encryption);
            jsonObject.put("salt",this.salt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
