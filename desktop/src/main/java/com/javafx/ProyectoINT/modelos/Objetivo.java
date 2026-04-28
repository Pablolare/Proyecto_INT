package com.javafx.ProyectoINT.modelos;

public class Objetivo {

    private int id_objetivo;
    private int id_usuario;
    private String descripcion;
    private Integer meta_aciertos;     // nullable
    private Integer meta_repeticiones; // nullable
    private boolean cumplido;
    private String fecha_creacion;

    public Objetivo(int id_objetivo, int id_usuario, String descripcion,
                    Integer meta_aciertos, Integer meta_repeticiones, boolean cumplido, String fecha_creacion) {
        this.id_objetivo = id_objetivo;
        this.id_usuario = id_usuario;
        this.descripcion = descripcion;
        this.meta_aciertos = meta_aciertos;
        this.meta_repeticiones = meta_repeticiones;
        this.cumplido = cumplido;
        this.fecha_creacion = fecha_creacion;
    }

    public Objetivo(int id_usuario, String descripcion, Integer meta_aciertos, Integer meta_repeticiones) {
        this.id_usuario = id_usuario;
        this.descripcion = descripcion;
        this.meta_aciertos = meta_aciertos;
        this.meta_repeticiones = meta_repeticiones;
        this.cumplido = false;
    }

    public int getId_objetivo() { return id_objetivo; }
    public int getId_usuario() { return id_usuario; }
    public String getDescripcion() { return descripcion; }
    public Integer getMeta_aciertos() { return meta_aciertos; }
    public Integer getMeta_repeticiones() { return meta_repeticiones; }
    public boolean isCumplido() { return cumplido; }
    public String getFecha_creacion() { return fecha_creacion; }

    public void setId_objetivo(int id_objetivo) { this.id_objetivo = id_objetivo; }
    public void setId_usuario(int id_usuario) { this.id_usuario = id_usuario; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setMeta_aciertos(Integer meta_aciertos) { this.meta_aciertos = meta_aciertos; }
    public void setMeta_repeticiones(Integer meta_repeticiones) { this.meta_repeticiones = meta_repeticiones; }
    public void setCumplido(boolean cumplido) { this.cumplido = cumplido; }
    public void setFecha_creacion(String fecha_creacion) { this.fecha_creacion = fecha_creacion; }
}
