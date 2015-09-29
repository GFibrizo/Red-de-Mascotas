package utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class Decoder {

    public static String decode(String str) {
        String decodedStr = "";
        try {
            if (str == null) {
                return null;
            }
            decodedStr = URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) { }
        return decodedStr;
    }

    public static List<String> decode(List<String> list) {
        List<String> decodedList = new ArrayList<>();
        if (list == null) {
            return null;
        }
        for (String item : list) {
            decodedList.add(Decoder.decode(item));
        }
        return decodedList;
    }

}
