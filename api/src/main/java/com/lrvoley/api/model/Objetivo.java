package com.lrvoley.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Objetivo")
public class Objetivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_objetivo")
    private int idObjetivo;

    @Column(name = "id_usuario")
    private int idUsuario;

    private String descripcion;

    @Column(name = "meta_aciertos")
    private Integer metaAciertos;

    @Column(name = "meta_repeticiones")
    private Integer metaRepeticiones;

    private boolean cumplido;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    public Objetivo() {}

    @jakarta.persistence.PrePersist
    public void prePersist() {
        if (fechaCreacion == null) fechaCreacion = LocalDateTime.now();
    }

    public int getIdObjetivo() { return idObjetivo; }
    public void setIdObjetivo(int idObjetivo) { this.idObjetivo = idObjetivo; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getMetaAciertos() { return metaAciertos; }
    public void setMetaAciertos(Integer metaAciertos) { this.metaAciertos = metaAciertos; }
    public Integer getMetaRepeticiones() { return metaRepeticiones; }
    public void setMetaRepeticiones(Integer metaRepeticiones) { this.metaRepeticiones = metaRepeticiones; }
    public boolean isCumplido() { return cumplido; }
    public void setCumplido(boolean cumplido) { this.cumplido = cumplido; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
