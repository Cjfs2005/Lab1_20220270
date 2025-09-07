package com.example.lab1_20220270;

public class ResultadoPartida {
    private String estado;
    private int tiempo;
    private String fecha;
    private int numeroJuego;

    public ResultadoPartida(String estado, int tiempo, String fecha, int numeroJuego) {
        this.estado = estado;
        this.tiempo = tiempo;
        this.fecha = fecha;
        this.numeroJuego = numeroJuego;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getNumeroJuego() {
        return numeroJuego;
    }

    public void setNumeroJuego(int numeroJuego) {
        this.numeroJuego = numeroJuego;
    }
}
