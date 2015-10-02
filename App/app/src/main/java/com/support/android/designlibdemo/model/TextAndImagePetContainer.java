package com.support.android.designlibdemo.model;


import com.support.android.designlibdemo.TextAndImage;

public class TextAndImagePetContainer implements TextAndImage {
    private int id;
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

    public TextAndImagePetContainer(){

    }

    public TextAndImagePetContainer(PetAdoption mascotas){
        this.id = 1;
        this.nombre = mascotas.getName();
        this.raza = mascotas.getBreed();
        this.sexo = mascotas.getGender();
        this.edad = mascotas.getAge();
        this.tamanio = mascotas.getSize();
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
        this.colorPelaje = "";
        if (mascotas.getBehavior() != null) {
            for (int i = 0; i < mascotas.getBehavior().size(); i++) {
                this.colorPelaje += mascotas.getBehavior().get(i);
                this.colorPelaje += " ";
            }
        }
        this.transitHomeUser = mascotas.getTransitHomeUser();
    }

    @Override
    public int getId() {
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

    public void setId(int id) {
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
        if (this.castrada){
            caracteristicas += "\nEstá Castrada";
        }
        if (this.temporaryMedicine){
            caracteristicas += "\nNecesita tomar alguna medicina de manera temporal.";
        }
        if (this.chronicMedicine){
            caracteristicas += "\nNecesita tomar alguna medicina de manera permanente.";
        }
        return this.caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }
}