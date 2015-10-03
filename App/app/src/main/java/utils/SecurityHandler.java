package utils;

import com.support.android.designlibdemo.model.Password;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.SecureRandom;



/**
 * Created by agu_k_000 on 26/09/2015.
 */
public class SecurityHandler {

    SecureRandom secureRandom;

    public SecurityHandler() {
        this.secureRandom = new SecureRandom();
    }

    public Password createPassword(String password) {
        byte[] salt = new byte[20];
        secureRandom.nextBytes(salt);
        String saltStr = salt.toString();
        return new Password(encryptionPassword(password, saltStr), saltStr);
    }
    public Password createPassword(String password,String salt) {
        return new Password(encryptionPassword(password, salt), salt);
    }
    private String encryptionPassword(String password, String salt) {
        //return DigestUtils.sha1(salt + password).toString();
        String encryptedPass = salt + password;//TODO: ver porque el sha1 cambia "aleatoriamente"
        return encryptedPass;
    }

    public Boolean validPassword(String password, Password savePassword) {
        return savePassword.getEncryption().equals(encryptionPassword(password, savePassword.getSalt()));
    }

}
