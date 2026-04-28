package com.lrvoley.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Nota")
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nota")
    private int idNota;

    @Column(name = "id_usuario")
    private int idUsuario;

    @Column(name = "id_entreno")
    private Integer idEntreno;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String contenido;

    private LocalDateTime fecha;

    public Nota() {}

    @jakarta.persistence.PrePersist
    public void prePersist() {
        if (fecha == null) fecha = LocalDateTime.now();
    }

    public int getIdNota() { return idNota; }
    public void setIdNota(int idNota) { this.idNota = idNota; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public Integer getIdEntreno() { return idEntreno; }
    public void setIdEntreno(Integer idEntreno) { this.idEntreno = idEntreno; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}
