package com.ezediaz.inmobiliaria.model;

import java.io.Serializable;

public class Uso implements Serializable {
    private int id;
    private String nombre;

    public Uso(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Uso(){
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
