package utils;

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
    private String encryptionPassword(String contrasenia, String salt) {
        return DigestUtils.sha1(salt + contrasenia).toString();
    }

    public Boolean validPassword(String password, Password savePassword) {
        return savePassword.encriptacion.equals(encryptionPassword(password, savePassword.salt));
    }

}
