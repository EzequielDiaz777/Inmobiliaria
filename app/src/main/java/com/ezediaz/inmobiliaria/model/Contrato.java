package com.ezediaz.inmobiliaria.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Contrato implements Serializable {
    private int id;
    private int inquilinoId;
    private int inmuebleId;
    private double precio;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Inquilino inquilino;
    private Inmueble inmueble;

    public Contrato(int id, int inquilinoId, int inmuebleId, double precio, LocalDate fechaInicio, LocalDate fechaFin, Inquilino inquilino, Inmueble inmueble) {
        this.id = id;
        this.inquilinoId = inquilinoId;
        this.inmuebleId = inmuebleId;
        this.precio = precio;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.inquilino = inquilino;
        this.inmueble = inmueble;
    }

    public Contrato(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInquilinoId() {
        return inquilinoId;
    }

    public void setInquilinoId(int inquilinoId) {
        this.inquilinoId = inquilinoId;
    }

    public int getInmuebleId() {
        return inmuebleId;
    }

    public void setInmuebleId(int inmuebleId) {
        this.inmuebleId = inmuebleId;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Inquilino getInquilino() {
        return inquilino;
    }

    public void setInquilino(Inquilino inquilino) {
        this.inquilino = inquilino;
    }

    public Inmueble getInmueble() {
        return inmueble;
    }

    public void setInmueble(Inmueble inmueble) {
        this.inmueble = inmueble;
    }
}