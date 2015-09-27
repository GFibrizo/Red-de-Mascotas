package utils;

/**
 * Created by agu_k_000 on 26/09/2015.
 */
public class Password {
    public String encriptacion;
    public String salt;

    public String getEncriptacion() {
        return encriptacion;
    }

    public String getSalt() {
        return salt;
    }

    public Password(String encriptacion, String salt) {
        this.encriptacion = encriptacion;
        this.salt = salt;

    }
}
