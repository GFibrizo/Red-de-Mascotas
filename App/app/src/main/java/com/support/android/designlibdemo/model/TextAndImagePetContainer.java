package com.support.android.designlibdemo.model;


import com.support.android.designlibdemo.TextAndImage;

import java.util.List;

public class TextAndImagePetContainer implements TextAndImage {
    private String id;
    private String ownerId;
    private String nombre;
    private String raza;
    private String sexo;
    private String edad;
    private String tamanio;
    private String barrio;
    private String colorPelaje;
    private String colorOjos;
    private String descripcion;
    private Boolean transito;
    private Boolean castrada;
    public String transitHomeUser;
    private Boolean temporaryMedicine;
    private Boolean chronicMedicine;
    private String publicationStatus;
    private String publicationDate;
    private String conducta;
    private String caracteristicas;
    private List<String> images;

    public TextAndImagePetContainer(){

    }

    public TextAndImagePetContainer(PetAdoption mascotas){
        this.id = mascotas.getId();
        this.ownerId = mascotas.getOwnerId();
        this.nombre = mascotas.getName();
        this.raza = mascotas.getBreed();
        this.sexo = mascotas.getGender();
        this.edad = mascotas.getAge();
        this.tamanio = mascotas.getSize();
        this.barrio = "";
        if (mascotas.getAddress() != null)
            this.barrio = mascotas.getAddress().getNeighbourhood();
        this.caracteristicas = "";
        this.colorPelaje = "";
        if (mascotas.getColors() != null) {
            for (int i = 0; i < mascotas.getColors().size(); i++) {
                this.colorPelaje += mascotas.getColors().get(i);
                this.colorPelaje += " ";
            }
        }
        this.colorOjos = mascotas.getEyeColor();
        this.descripcion = mascotas.getDescription();
        this.transito = mascotas.getNeedsTransitHome();
        this.castrada = mascotas.getIsCastrated();
        this.temporaryMedicine = mascotas.getIsOnTemporaryMedicine();
        this.chronicMedicine = mascotas.getIsOnChronicMedicine();
        this.publicationStatus = mascotas.getPublicationStatus();
        this.publicationDate = mascotas.getPublicationDate();
        this.conducta = "";
        if (mascotas.getBehavior() != null) {
            for (int i = 0; i < mascotas.getBehavior().size(); i++) {
                this.conducta += mascotas.getBehavior().get(i);
                if (i != mascotas.getBehavior().size() - 1) {
                    this.conducta += "\n";
                }
            }
        }
        this.transitHomeUser = mascotas.getTransitHomeUser();
        this.images = mascotas.getImages();
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText(){
        return "";
    }

    public String getNombre() {
        return nombre;
    }

    public String getSexo() {
        return sexo;
    }

    public String getEdad() {
        return edad;
    }

    public String getTamanio() {
        return tamanio;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public void setTamanio(String tamanio) {
        this.tamanio = tamanio;
    }

    @Override
    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    @Override
    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    @Override
    public String getColorPelaje() {
        return colorPelaje;
    }

    public void setColorPelaje(String colorPelaje) {
        this.colorPelaje = colorPelaje;
    }

    @Override
    public String getColorOjos() {
        return colorOjos;
    }

    public void setColorOjos(String colorOjos) {
        this.colorOjos = colorOjos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getTransito() {
        return transito;
    }

    public void setTransito(Boolean transito) {
        this.transito = transito;
    }

    public Boolean getCastrada() {
        return castrada;
    }

    public void setCastrada(Boolean castrada) {
        this.castrada = castrada;
    }

    public Boolean getTemporaryMedicine() {
        return temporaryMedicine;
    }

    public void setTemporaryMedicine(Boolean temporaryMedicine) {
        this.temporaryMedicine = temporaryMedicine;
    }

    public Boolean getChronicMedicine() {
        return chronicMedicine;
    }

    public void setChronicMedicine(Boolean chronicMedicine) {
        this.chronicMedicine = chronicMedicine;
    }

    public String getPublicationStatus() {
        return publicationStatus;
    }

    public void setPublicationStatus(String publicationStatus) {
        this.publicationStatus = publicationStatus;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getConducta() {
        return conducta;
    }

    public void setConducta(String conducta) {
        this.conducta = conducta;
    }

    public String getTransitHomeUser() {
        return transitHomeUser;
    }

    public void setTransitHomeUser(String transitHomeUser) {
        this.transitHomeUser = transitHomeUser;
    }

    public String getCaracteristicas() {
        if (this.transito){
            caracteristicas += "Necesita hogar de tránsito";
        }
        if ((this.castrada != null) && (this.castrada)){
            caracteristicas += "\nEstá Castrada";
        }
        if ((this.temporaryMedicine != null) && (this.temporaryMedicine)){
            caracteristicas += "\nNecesita tomar alguna medicina de manera temporal.";
        }
        if ((this.chronicMedicine != null) && (this.chronicMedicine)) {
            caracteristicas += "\nNecesita tomar alguna medicina de manera permanente.";
        }
        return this.caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public String getImages() {
        String imagenes = "";
        imagenes += this.images.get(0);
        for (int i = 1; i < this.images.size(); i++){
            imagenes += ", ";
            imagenes += this.images.get(i);
        }
        return imagenes;
    }

}