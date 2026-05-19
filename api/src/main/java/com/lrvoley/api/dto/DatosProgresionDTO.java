package com.lrvoley.api.dto;

public class DatosProgresionDTO {
    private int numeroEntreno;
    private int aciertos;
    private int fallos;
    private String nombreEntreno;

    public DatosProgresionDTO(int numeroEntreno, int aciertos, int fallos, String nombreEntreno) {
        this.numeroEntreno = numeroEntreno;
        this.aciertos = aciertos;
        this.fallos = fallos;
        this.nombreEntreno = nombreEntreno;
    }

    public int getNumeroEntreno() { return numeroEntreno; }
    public int getAciertos() { return aciertos; }
    public int getFallos() { return fallos; }
    public String getNombreEntreno() { return nombreEntreno; }
}
