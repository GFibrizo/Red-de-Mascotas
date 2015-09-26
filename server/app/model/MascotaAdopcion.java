package model;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import model.externo.FiltrosBusquedaAdopcion;
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

    public String tipo;

    public String duenioId;

    public Domicilio domicilio;

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


    private static JacksonDBCollection<MascotaAdopcion, String> coleccion = MongoDB.getCollection("mascotasEnAdopcion", MascotaAdopcion.class, String.class);


    public MascotaAdopcion() { }

    public MascotaAdopcion(String nombre, String tipo, String duenioId, Domicilio domicilio, String raza,
                           String sexo, String edad, String tamanio, List<String> colores, String colorDeOjos,
                           List<String> conducta, List<String> imagenes, Boolean necesitaHogarDeTransito,
                           Boolean estaCastrada, Boolean tomaMedicinaTemporal, Boolean tomaMedicinaCronica, String descripcion) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.duenioId = duenioId;
        this.domicilio = domicilio;
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

    public static List<MascotaAdopcion> traerPorDuenioId(String duenioId) {
        return MascotaAdopcion.coleccion.find(new BasicDBObject("duenioId", duenioId)).toArray();
    }

    public static List<MascotaAdopcion> buscar(FiltrosBusquedaAdopcion filtros) {
        return MascotaAdopcion.coleccion.find(construirFiltrosDeBusqueda(filtros)).toArray();
    }

    public static List<MascotaAdopcion> traerUltimasPublicaciones() {
        return MascotaAdopcion.coleccion.find().sort(new BasicDBObject("fechaDePublicacion", -1)).limit(MAX_ULTIMAS_PUBLICACIONES).toArray();
    }

    public static void crear(MascotaAdopcion mascotaAdopcion) {
        mascotaAdopcion.actualizarEstadoPublicado();
        MascotaAdopcion.coleccion.save(mascotaAdopcion);
    }

    public static void eliminar(String id) {
        MascotaAdopcion mascotaAdopcion = MascotaAdopcion.coleccion.findOneById(id);
        if (mascotaAdopcion != null)
            MascotaAdopcion.coleccion.remove(mascotaAdopcion);
    }

    private static DBObject construirFiltrosDeBusqueda(FiltrosBusquedaAdopcion filtros) {
        BasicDBObjectBuilder query = BasicDBObjectBuilder.start();
        if (filtros.tipo != null) query.add("tipo", filtros.tipo);
        if (filtros.raza != null) query.add("raza", filtros.raza);
        if (filtros.sexos != null) query.push("sexo").add("$in", filtros.sexos.toArray());
        if (filtros.edades != null) query.push("edad").add("$in", filtros.edades.toArray());
        if (filtros.tamanios != null) query.push("tamanio").add("$in", filtros.tamanios.toArray());
        if (filtros.colores != null) query.push("colores").add("$in", filtros.colores.toArray());
        if (filtros.coloresDeOjos != null) query.push("colorDeOjos").add("$in", filtros.coloresDeOjos.toArray());
        if (filtros.barrio != null) query.add("barrio", filtros.barrio);
        if (filtros.ciudad != null) query.add("raza", filtros.ciudad);
        return query.get();
    }

}
