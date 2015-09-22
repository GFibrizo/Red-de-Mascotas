package model;

import java.util.*;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import play.modules.mongodb.jackson.MongoDB;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.ObjectId;

public class Usuario {

    @Id
    @ObjectId
    public String id;

    public String nombreDeUsuario;

    public String nombre;

    public String apellido;

    public String email;

    public Contrasenia contrasenia;

    public String facebookId;

    public String telefono;

    public Domicilio domicilio;

    // TODO: Parametros de busquedas


    private static JacksonDBCollection<Usuario, String> coleccion = MongoDB.getCollection("usuarios", Usuario.class, String.class);


    public Usuario() { }

    public Usuario(String nombreDeUsuario, String nombre, String apellido, String email,
                   Contrasenia contrasenia, String telefono, Domicilio domicilio) {
        this.nombreDeUsuario = nombreDeUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasenia = contrasenia;
        this.telefono = telefono;
        this.domicilio = domicilio;
    }

    public Usuario(String nombre, String apellido, String email, String facebookId,
                   String telefono, Domicilio domicilio) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.facebookId = facebookId;
        this.telefono = telefono;
        this.domicilio = domicilio;
    }


    public static Usuario traerPorFacebookId(String facebookId) {
        return Usuario.coleccion.findOne(new BasicDBObject("facebookId", facebookId));
    }

    public static Usuario traerPorNombreDeUsuario(String nombreDeUsuario) {
        return Usuario.coleccion.findOne(new BasicDBObject("nombreDeUsuario", nombreDeUsuario));
    }

    public static void crear(Usuario usuario) {
        Usuario.coleccion.save(usuario);
    }

    public static Boolean existente(String nombreDeUsuario, String email) {
        BasicDBList value = new BasicDBList();
        value.add(new BasicDBObject("nombreDeUsuario", nombreDeUsuario));
        value.add(new BasicDBObject("email", email));
        return Usuario.coleccion.find(new BasicDBObject("$or", value)).toArray().size() > 0;
    }

    public static void eliminar(String id) {
        Usuario usuario = Usuario.coleccion.findOneById(id);
        if (usuario != null)
            Usuario.coleccion.remove(usuario);
    }

}
