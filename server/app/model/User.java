package model;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import play.modules.mongodb.jackson.MongoDB;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.ObjectId;

public class User {

    @Id
    @ObjectId
    public String id;

    public String nombreDeUsuario;

    public String nombre;

    public String apellido;

    public String email;

    public Password password;

    public String facebookId;

    public String telefono;

    public Address address;


    private static JacksonDBCollection<User, String> coleccion = MongoDB.getCollection("users", User.class, String.class);


    public User() { }

    public User(String nombreDeUsuario, String nombre, String apellido, String email,
                Password password, String telefono, Address address) {
        this.nombreDeUsuario = nombreDeUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.address = address;
    }

    public User(String nombre, String apellido, String email, String facebookId,
                String telefono, Address address) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.facebookId = facebookId;
        this.telefono = telefono;
        this.address = address;
    }


    public static User traerPorId(String id) {
        return User.coleccion.findOne(new BasicDBObject("_id", id));
    }

    public static User traerPorFacebookId(String facebookId) {
        return User.coleccion.findOne(new BasicDBObject("facebookId", facebookId));
    }

    public static User traerPorNombreDeUsuario(String nombreDeUsuario) {
        return User.coleccion.findOne(new BasicDBObject("userName", nombreDeUsuario));
    }

    public static void crear(User user) {
        User.coleccion.save(user);
    }

    public static Boolean existente(String nombreDeUsuario, String email) {
        BasicDBList value = new BasicDBList();
        value.add(new BasicDBObject("userName", nombreDeUsuario));
        value.add(new BasicDBObject("email", email));
        return User.coleccion.find(new BasicDBObject("$or", value)).toArray().size() > 0;
    }

    public static void eliminar(String id) {
        User user = User.coleccion.findOneById(id);
        if (user != null)
            User.coleccion.remove(user);
    }

}
