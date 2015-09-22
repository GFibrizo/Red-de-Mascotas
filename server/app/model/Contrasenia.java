package model;

public class Contrasenia {

    public String encriptacion;

    public String salt;

    public Contrasenia() { }

    public Contrasenia(String encriptacion, String salt) {
        this.encriptacion = encriptacion;
        this.salt = salt;
    }

}
