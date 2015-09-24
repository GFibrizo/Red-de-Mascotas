package services;

import model.MascotaAdopcion;
import model.externo.FiltrosBusquedaAdopcion;
import model.externo.MascotaAdopcionPublicacion;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MascotaAdopcionServicio {

    public void publicarMascota(MascotaAdopcionPublicacion mascotaPublicacion) {
        MascotaAdopcion mascota = new MascotaAdopcion(mascotaPublicacion.nombre,
                                                      mascotaPublicacion.tipo,
                                                      mascotaPublicacion.duenioId,
                                                      mascotaPublicacion.domicilio,
                                                      mascotaPublicacion.raza,
                                                      mascotaPublicacion.sexo,
                                                      mascotaPublicacion.edad,
                                                      mascotaPublicacion.tamanio,
                                                      mascotaPublicacion.colores,
                                                      mascotaPublicacion.colorDeOjos,
                                                      mascotaPublicacion.conducta,
                                                      mascotaPublicacion.necesitaHogarDeTransito,
                                                      mascotaPublicacion.estaCastrada,
                                                      mascotaPublicacion.tomaMedicinaTemporal,
                                                      mascotaPublicacion.tomaMedicinaCronica,
                                                      mascotaPublicacion.descripcion);
        MascotaAdopcion.crear(mascota);
    }

    public List<MascotaAdopcion> buscarMascotas(FiltrosBusquedaAdopcion filtros) {
        return MascotaAdopcion.buscar(filtros);
    }

}
