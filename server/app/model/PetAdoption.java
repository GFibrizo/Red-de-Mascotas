package model;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import model.external.SearchForAdoptionFilters;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;
import org.joda.time.DateTime;
import play.modules.mongodb.jackson.MongoDB;

import java.util.List;

public class PetAdoption {

    @Id
    @ObjectId
    public String id;

    public String nombre;

    public String tipo;

    public String duenioId;

    public Address address;

    public String raza;

    public String sexo;

    public String edad;

    public String tamanio;

    public List<String> colores;

    public String colorDeOjos;

    public List<String> conducta;

    public List<String> imagenes;

    public List<String> videos;

    public Boolean necesitaHogarDeTransito;

    public String usuarioHogarDeTransito;

    public Boolean estaCastrada;

    public Boolean tomaMedicinaTemporal;

    public Boolean tomaMedicinaCronica;

    public String descripcion;

    public String estadoPublicacion;

    public String fechaDePublicacion;

    private static final int MAX_ULTIMAS_PUBLICACIONES = 10;


    private static JacksonDBCollection<PetAdoption, String> coleccion = MongoDB.getCollection("mascotasEnAdopcion", PetAdoption.class, String.class);


    public PetAdoption() { }

    public PetAdoption(String nombre, String tipo, String duenioId, Address address, String raza,
                       String sexo, String edad, String tamanio, List<String> colores, String colorDeOjos,
                       List<String> conducta, List<String> imagenes, Boolean necesitaHogarDeTransito,
                       Boolean estaCastrada, Boolean tomaMedicinaTemporal, Boolean tomaMedicinaCronica, String descripcion) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.duenioId = duenioId;
        this.address = address;
        this.raza = raza;
        this.sexo = sexo;
        this.edad = edad;
        this.tamanio = tamanio;
        this.colores = colores;
        this.colorDeOjos = colorDeOjos;
        this.conducta = conducta;
        this.imagenes = imagenes;
        this.necesitaHogarDeTransito = necesitaHogarDeTransito;
        this.estaCastrada = estaCastrada;
        this.tomaMedicinaTemporal = tomaMedicinaTemporal;
        this.tomaMedicinaCronica = tomaMedicinaCronica;
        this.descripcion = descripcion;
    }

    private void actualizarEstadoPublicado() {
        this.estadoPublicacion = "PUBLICADO";
        this.fechaDePublicacion = DateTime.now().toString("yyyy/MM/dd HH:mm:ss.SSS");
    }

    public static List<PetAdoption> traerPorDuenioId(String duenioId) {
        return PetAdoption.coleccion.find(new BasicDBObject("ownerId", duenioId)).toArray();
    }

    public static List<PetAdoption> buscar(SearchForAdoptionFilters filtros) {
        return PetAdoption.coleccion.find(construirFiltrosDeBusqueda(filtros)).toArray();
    }

    public static List<PetAdoption> traerUltimasPublicaciones() {
        return PetAdoption.coleccion.find().sort(new BasicDBObject("fechaDePublicacion", -1)).limit(MAX_ULTIMAS_PUBLICACIONES).toArray();
    }

    public static void crear(PetAdoption petAdoption) {
        petAdoption.actualizarEstadoPublicado();
        PetAdoption.coleccion.save(petAdoption);
    }

    public static void eliminar(String id) {
        PetAdoption petAdoption = PetAdoption.coleccion.findOneById(id);
        if (petAdoption != null)
            PetAdoption.coleccion.remove(petAdoption);
    }

    private static DBObject construirFiltrosDeBusqueda(SearchForAdoptionFilters filtros) {
        BasicDBObjectBuilder query = BasicDBObjectBuilder.start();
        if (filtros.type != null) query.add("type", filtros.type);
        if (filtros.breed != null) query.add("breed", filtros.breed);
        if (filtros.genders != null) query.push("gender").add("$in", filtros.genders.toArray()).pop();
        if (filtros.ages != null) query.push("age").add("$in", filtros.ages.toArray()).pop();
        if (filtros.sizes != null) query.push("size").add("$in", filtros.sizes.toArray()).pop();
        if (filtros.colors != null) query.push("colors").add("$in", filtros.colors.toArray()).pop();
        if (filtros.eyeColors != null) query.push("eyeColor").add("$in", filtros.eyeColors.toArray()).pop();
        if (filtros.neighbourhood != null) query.add("neighbourhood", filtros.neighbourhood);
        if (filtros.city != null) query.add("breed", filtros.city);
        return query.get();
    }

}
