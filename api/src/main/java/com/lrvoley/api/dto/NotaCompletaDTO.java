package com.lrvoley.api.dto;

public class NotaCompletaDTO {
    private int idNota;
    private int idUsuario;
    private Integer idEntreno;
    private String titulo;
    private String contenido;
    private String fecha;
    private String nombreEntreno;

    public NotaCompletaDTO(int idNota, int idUsuario, Integer idEntreno,
                           String titulo, String contenido, String fecha, String nombreEntreno) {
        this.idNota = idNota;
        this.idUsuario = idUsuario;
        this.idEntreno = idEntreno;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fecha = fecha;
        this.nombreEntreno = nombreEntreno;
    }

    public int getIdNota() { return idNota; }
    public int getIdUsuario() { return idUsuario; }
    public Integer getIdEntreno() { return idEntreno; }
    public String getTitulo() { return titulo; }
    public String getContenido() { return contenido; }
    public String getFecha() { return fecha; }
    public String getNombreEntreno() { return nombreEntreno; }
}
