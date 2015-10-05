package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Oliver on 04/10/2015.
 */
public class Validators {

    public boolean validateEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();

    }

    public boolean isPasswordValid(String password) {
        //TODO: Longitud mínima: 6 caracteres - Longitud máxima: 12 caracteres
        //*return ((password.length() >= 6 && password.length() <= 12) && password.matches("[a-zA-Z][0-9]+"));
        return ((password.length() >= 6 && password.length() <= 12));

    }
}
