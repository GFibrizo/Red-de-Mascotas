package services;

import model.MascotaAdopcion;
import model.Usuario;
import model.external.SearchForAdoptionFilters;
import model.external.PublishForAdoptionPet;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetAdoptionService {

    public Boolean publishPet(PublishForAdoptionPet mascotaPublicacion) {
        Usuario duenio = Usuario.traerPorId(mascotaPublicacion.ownerId);
        if (duenio == null)
            return false;
        MascotaAdopcion mascota = new MascotaAdopcion(mascotaPublicacion.name,
                                                      mascotaPublicacion.type,
                                                      mascotaPublicacion.ownerId,
                                                      duenio.address,
                                                      mascotaPublicacion.breed,
                                                      mascotaPublicacion.gender,
                                                      mascotaPublicacion.age,
                                                      mascotaPublicacion.size,
                                                      mascotaPublicacion.colors,
                                                      mascotaPublicacion.eyeColor,
                                                      mascotaPublicacion.behavior,
                                                      mascotaPublicacion.images,
                                                      mascotaPublicacion.needsTransitHome,
                                                      mascotaPublicacion.isCastrated,
                                                      mascotaPublicacion.isOnTemporaryMedicine,
                                                      mascotaPublicacion.isOnChronicMedicine,
                                                      mascotaPublicacion.description);
        MascotaAdopcion.crear(mascota);
        return true;
    }

    public List<MascotaAdopcion> searchPets(SearchForAdoptionFilters filtros) {
        return MascotaAdopcion.buscar(filtros);
    }

    public List<MascotaAdopcion> getLastPublished() {
        return MascotaAdopcion.traerUltimasPublicaciones();
    }

}
