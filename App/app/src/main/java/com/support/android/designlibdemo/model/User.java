package com.support.android.designlibdemo.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by agu on 02/10/15.
 */
public class User {


    public String id;

    public String userName;

    public String name;

    public String lastName;

    public String email;

    public Password password;

    public String facebookId;

    public String notificationId;

    public String phone;

    public Address address;


    public User() {
    }

    public User(String userName, String name, String lastName, String email,
                Password password, String phone, Address address) {
        this.userName = userName;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
    }

    public User(String name, String lastName, String email, String facebookId,
                String phone, Address address) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.facebookId = facebookId;
        this.phone = phone;
        this.address = address;
    }

    public User(JSONObject object) {
        try {
            this.id = object.getString("id");
            this.userName = object.getString("userName");
            this.name = object.getString("name");
            this.lastName = object.getString("lastName");
            this.email = object.getString("email");
            this.password = new Password(object.getJSONObject("password"));
            this.phone = object.getString("phone");
            this.address = new Address(object.getJSONObject("address"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.facebookId = object.getString("facebookId");
        }
        catch (JSONException e) {
            this.facebookId = "";
        }
    }


    public JSONObject toJson() {
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("id",this.id);
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

    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public void setPassword(JSONObject jsonPassword) {
        try {
            String encryption = jsonPassword.getString("encryption");
            String salt = jsonPassword.getString("salt");
            this.password = new Password(encryption,salt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public String getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }
}