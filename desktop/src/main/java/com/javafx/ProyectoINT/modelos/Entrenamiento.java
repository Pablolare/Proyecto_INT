package com.javafx.ProyectoINT.modelos;

public class Entrenamiento {

    private int id_entreno;
    private int id_usuario;
    private int id_ejer;
    private String nombre_entreno;
    private String descripcion;
    private int fallos;
    private int aciertos;
    private boolean completado;
    private int numEjercicios;

    public Entrenamiento(int id_entreno, int id_usuario, int id_ejer, String nombre_entreno,
                         String descripcion, int fallos, int aciertos, boolean completado) {
        this.id_entreno = id_entreno;
        this.id_usuario = id_usuario;
        this.id_ejer = id_ejer;
        this.nombre_entreno = nombre_entreno;
        this.descripcion = descripcion;
        this.fallos = fallos;
        this.aciertos = aciertos;
        this.completado = completado;
    }

    public Entrenamiento(int id_usuario, int id_ejer, String nombre_entreno,
                         String descripcion, int fallos, int aciertos, boolean completado) {
        this.id_usuario = id_usuario;
        this.id_ejer = id_ejer;
        this.nombre_entreno = nombre_entreno;
        this.descripcion = descripcion;
        this.fallos = fallos;
        this.aciertos = aciertos;
        this.completado = completado;
    }

    public int getId_entreno() { return id_entreno; }
    public void setId_entreno(int id_entreno) { this.id_entreno = id_entreno; }
    public int getId_usuario() { return id_usuario; }
    public void setId_usuario(int id_usuario) { this.id_usuario = id_usuario; }
    public int getId_ejer() { return id_ejer; }
    public void setId_ejer(int id_ejer) { this.id_ejer = id_ejer; }
    public String getNombre_entreno() { return nombre_entreno; }
    public void setNombre_entreno(String nombre_entreno) { this.nombre_entreno = nombre_entreno; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getFallos() { return fallos; }
    public void setFallos(int fallos) { this.fallos = fallos; }
    public int getAciertos() { return aciertos; }
    public void setAciertos(int aciertos) { this.aciertos = aciertos; }
    public boolean isCompletado() { return completado; }
    public void setCompletado(boolean completado) { this.completado = completado; }
    public int getNumEjercicios() { return numEjercicios; }
    public void setNumEjercicios(int numEjercicios) { this.numEjercicios = numEjercicios; }
}
