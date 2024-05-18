package com.ezediaz.inmobiliaria.model;

import java.io.Serializable;

public class Tipo implements Serializable {
    private int id;
    private String nombre;

    public Tipo(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Tipo(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
