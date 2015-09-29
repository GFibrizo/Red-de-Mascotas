package com.support.android.designlibdemo.model;

import java.util.List;

public class PetAdoption {

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDuenioId() {
        return duenioId;
    }

    public void setDuenioId(String duenioId) {
        this.duenioId = duenioId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getTamanio() {
        return tamanio;
    }

    public void setTamanio(String tamanio) {
        this.tamanio = tamanio;
    }

    public List<String> getColores() {
        return colores;
    }

    public void setColores(List<String> colores) {
        this.colores = colores;
    }

    public String getColorDeOjos() {
        return colorDeOjos;
    }

    public void setColorDeOjos(String colorDeOjos) {
        this.colorDeOjos = colorDeOjos;
    }

    public List<String> getConducta() {
        return conducta;
    }

    public void setConducta(List<String> conducta) {
        this.conducta = conducta;
    }

    public List<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<String> imagenes) {
        this.imagenes = imagenes;
    }

    public List<String> getVideos() {
        return videos;
    }

    public void setVideos(List<String> videos) {
        this.videos = videos;
    }

    public Boolean getNecesitaHogarDeTransito() {
        return necesitaHogarDeTransito;
    }

    public void setNecesitaHogarDeTransito(Boolean necesitaHogarDeTransito) {
        this.necesitaHogarDeTransito = necesitaHogarDeTransito;
    }

    public String getUsuarioHogarDeTransito() {
        return usuarioHogarDeTransito;
    }

    public void setUsuarioHogarDeTransito(String usuarioHogarDeTransito) {
        this.usuarioHogarDeTransito = usuarioHogarDeTransito;
    }

    public Boolean getEstaCastrada() {
        return estaCastrada;
    }

    public void setEstaCastrada(Boolean estaCastrada) {
        this.estaCastrada = estaCastrada;
    }

    public Boolean getTomaMedicinaTemporal() {
        return tomaMedicinaTemporal;
    }

    public void setTomaMedicinaTemporal(Boolean tomaMedicinaTemporal) {
        this.tomaMedicinaTemporal = tomaMedicinaTemporal;
    }

    public Boolean getTomaMedicinaCronica() {
        return tomaMedicinaCronica;
    }

    public void setTomaMedicinaCronica(Boolean tomaMedicinaCronica) {
        this.tomaMedicinaCronica = tomaMedicinaCronica;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstadoPublicacion() {
        return estadoPublicacion;
    }

    public void setEstadoPublicacion(String estadoPublicacion) {
        this.estadoPublicacion = estadoPublicacion;
    }

    public String getFechaDePublicacion() {
        return fechaDePublicacion;
    }

    public void setFechaDePublicacion(String fechaDePublicacion) {
        this.fechaDePublicacion = fechaDePublicacion;
    }
}
