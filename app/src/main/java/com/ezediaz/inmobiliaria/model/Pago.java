package com.ezediaz.inmobiliaria.model;

import java.io.Serializable;

public class Pago implements Serializable {
    private int id;
    private int contratoId;
    private double monto;
    private int numeroDePago;
    private String fecha;
    private Contrato contrato;

    public Pago(int id, int contratoId, double monto, int numeroDePago, String fecha, Contrato contrato) {
        this.id = id;
        this.contratoId = contratoId;
        this.monto = monto;
        this.numeroDePago = numeroDePago;
        this.fecha = fecha;
        this.contrato = contrato;
    }

    public Pago(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContratoId() {
        return contratoId;
    }

    public void setContratoId(int contratoId) {
        this.contratoId = contratoId;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public int getNumeroDePago() {
        return numeroDePago;
    }

    public void setNumeroDePago(int numeroDePago) {
        this.numeroDePago = numeroDePago;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }
}
