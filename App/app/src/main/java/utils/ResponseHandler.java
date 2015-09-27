package utils;

import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

/**
 * Created by agu_k_000 on 26/09/2015.
 */
public class ResponseHandler {
    public static final String OK = "OK" ;
    String response;
    String  message;
    private boolean status;

    public void setResponse(JSONObject response) {
        this.response = response.toString();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setOkStatus() {
        this.status = true;
    }

    public void setErrorStatus() {
        this.status = false;
    }

    public String getResponse() {return response;}

    public boolean getStatus() {
        return status;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
