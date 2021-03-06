package utils;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Encoder {

    public static String encode(String str) {
        String encodedStr = "";
        try {
            encodedStr = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("Error al encodear: " + str, e.getMessage());
        }
        return encodedStr;
    }

}
