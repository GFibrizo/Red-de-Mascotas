package model;

import com.mongodb.BasicDBObject;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;
import org.joda.time.DateTime;
import play.modules.mongodb.jackson.MongoDB;

import java.util.List;

public class MascotaAdopcion {

    @Id
    @ObjectId
    public String id;

    public String nombre;

    public String tipoDeMascota;

    public String duenioId;

    public Domicilio domicilio;

    public String raza;

    public String sexo;

    public String edad;

    public String tamanio;

    public List<String> colores;

    public String colorDeOjos;

    public String conducta;

    public List<String> imagenes;

    public List<String> videos;

    public Boolean necesitaHogarDeTransito;

    public String usuarioHogarDeTransito;

    public Boolean estaCastrada;

    public Boolean tomaMedicinaTemporal;

    public Boolean tomaMedicinaCronica;

    public String descripcion;

    public String estadoPublicacion;

    public DateTime fechaDePublicacion;


    private static JacksonDBCollection<MascotaAdopcion, String> coleccion = MongoDB.getCollection("mascotasEnAdopcion", MascotaAdopcion.class, String.class);


    public MascotaAdopcion() { }

    public MascotaAdopcion(String nombre, String tipoDeMascota, String duenioId, Domicilio domicilio, String raza,
                           String sexo, String edad, String tamanio, List<String> colores, String colorDeOjos,
                           String conducta, Boolean necesitaHogarDeTransito, Boolean estaCastrada,
                           Boolean tomaMedicinaTemporal, Boolean tomaMedicinaCronica, String descripcion) {
        this.nombre = nombre;
        this.tipoDeMascota = tipoDeMascota;
        this.duenioId = duenioId;
        this.domicilio = domicilio;
        this.raza = raza;
        this.sexo = sexo;
        this.edad = edad;
        this.tamanio = tamanio;
        this.colores = colores;
        this.colorDeOjos = colorDeOjos;
        this.conducta = conducta;
        this.necesitaHogarDeTransito = necesitaHogarDeTransito;
        this.estaCastrada = estaCastrada;
        this.tomaMedicinaTemporal = tomaMedicinaTemporal;
        this.tomaMedicinaCronica = tomaMedicinaCronica;
        this.descripcion = descripcion;
    }

    private void actualizarEstadoPublicacion(String estado) {
        this.estadoPublicacion = estado;
        this.fechaDePublicacion = DateTime.now();
    }

    public static List<MascotaAdopcion> traerPorDuenioId(String duenioId) {
        return MascotaAdopcion.coleccion.find(new BasicDBObject("duenioId", duenioId)).toArray();
    }

    public static void crear(MascotaAdopcion mascotaAdopcion) {
        mascotaAdopcion.actualizarEstadoPublicacion("PUBLICADO");
        MascotaAdopcion.coleccion.save(mascotaAdopcion);
    }

    public static void eliminar(String id) {
        MascotaAdopcion mascotaAdopcion = MascotaAdopcion.coleccion.findOneById(id);
        if (mascotaAdopcion != null)
            MascotaAdopcion.coleccion.remove(mascotaAdopcion);
    }

}
