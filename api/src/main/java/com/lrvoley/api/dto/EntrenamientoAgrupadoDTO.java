package com.lrvoley.api.dto;

public class EntrenamientoAgrupadoDTO {
    private String nombreEntreno;
    private int numEjercicios;
    private String descripcion;
    private int fallos;
    private int aciertos;
    private boolean completado;

    public EntrenamientoAgrupadoDTO(String nombreEntreno, int numEjercicios,
                                     String descripcion,
                                     int fallos, int aciertos, boolean completado) {
        this.nombreEntreno = nombreEntreno;
        this.numEjercicios = numEjercicios;
        this.descripcion = descripcion;
        this.fallos = fallos;
        this.aciertos = aciertos;
        this.completado = completado;
    }

    public String getNombreEntreno() { return nombreEntreno; }
    public int getNumEjercicios() { return numEjercicios; }
    public String getDescripcion() { return descripcion; }
    public int getFallos() { return fallos; }
    public int getAciertos() { return aciertos; }
    public boolean isCompletado() { return completado; }
}
