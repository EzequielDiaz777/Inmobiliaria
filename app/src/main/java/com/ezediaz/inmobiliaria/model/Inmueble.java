package com.ezediaz.inmobiliaria.model;
import java.io.Serializable;

public class Inmueble implements Serializable {
    private int id;
    private int propietarioId;
    private int tipoId;
    private int usoId;
    private String direccion;
    private int ambientes;
    private double precio;
    private boolean estado;
    private Propietario duenio;
    private Tipo tipo;
    private Uso uso;
    private String imagenInmueble;

    public Inmueble(int id, int propietarioId, int tipoId, int usoId, String direccion, int ambientes, double precio, boolean estado, Propietario duenio, Tipo tipo, Uso uso) {
        this.id = id;
        this.propietarioId = propietarioId;
        this.tipoId = tipoId;
        this.usoId = usoId;
        this.direccion = direccion;
        this.ambientes = ambientes;
        this.precio = precio;
        this.estado = estado;
        this.duenio = duenio;
        this.tipo = tipo;
        this.uso = uso;
    }

    public Inmueble(int id, int propietarioId, int tipoId, int usoId, String direccion, int ambientes, double precio, boolean estado, Propietario duenio, Tipo tipo, Uso uso, String imagenInmueble) {
        this.id = id;
        this.propietarioId = propietarioId;
        this.tipoId = tipoId;
        this.usoId = usoId;
        this.direccion = direccion;
        this.ambientes = ambientes;
        this.precio = precio;
        this.estado = estado;
        this.duenio = duenio;
        this.tipo = tipo;
        this.uso = uso;
        this.imagenInmueble = imagenInmueble;
    }

    public String getImagenInmueble() {
        return imagenInmueble;
    }

    public void setImagenInmueble(String imagenInmueble) {
        this.imagenInmueble = imagenInmueble;
    }

    public Inmueble() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPropietarioId() {
        return propietarioId;
    }

    public void setPropietarioId(int propietarioId) {
        this.propietarioId = propietarioId;
    }

    public int getTipoId() {
        return tipoId;
    }

    public void setTipoId(int tipoId) {
        this.tipoId = tipoId;
    }

    public int getUsoId() {
        return usoId;
    }

    public void setUsoId(int usoId) {
        this.usoId = usoId;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getAmbientes() {
        return ambientes;
    }

    public void setAmbientes(int ambientes) {
        this.ambientes = ambientes;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Propietario getDuenio() {
        return duenio;
    }

    public void setDuenio(Propietario duenio) {
        this.duenio = duenio;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Uso getUso() {
        return uso;
    }

    public void setUso(Uso uso) {
        this.uso = uso;
    }
}
