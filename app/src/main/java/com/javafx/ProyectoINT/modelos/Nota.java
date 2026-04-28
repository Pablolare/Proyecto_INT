package com.javafx.ProyectoINT.modelos;

public class Nota {

    private int id_nota;
    private int id_usuario;
    private Integer id_entreno; // nullable
    private String titulo;
    private String contenido;
    private String fecha;
    private String nombreEntreno; // solo para display, no se guarda en BD

    public Nota(int id_nota, int id_usuario, Integer id_entreno, String titulo, String contenido, String fecha) {
        this.id_nota = id_nota;
        this.id_usuario = id_usuario;
        this.id_entreno = id_entreno;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fecha = fecha;
    }

    public Nota(int id_usuario, Integer id_entreno, String titulo, String contenido) {
        this.id_usuario = id_usuario;
        this.id_entreno = id_entreno;
        this.titulo = titulo;
        this.contenido = contenido;
    }

    public int getId_nota() { return id_nota; }
    public int getId_usuario() { return id_usuario; }
    public Integer getId_entreno() { return id_entreno; }
    public String getTitulo() { return titulo; }
    public String getContenido() { return contenido; }
    public String getFecha() { return fecha; }

    public void setId_nota(int id_nota) { this.id_nota = id_nota; }
    public void setId_usuario(int id_usuario) { this.id_usuario = id_usuario; }
    public void setId_entreno(Integer id_entreno) { this.id_entreno = id_entreno; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getNombreEntreno() { return nombreEntreno; }
    public void setNombreEntreno(String nombreEntreno) { this.nombreEntreno = nombreEntreno; }
}
