package com.lrvoley.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Entrenamientos")
public class Entrenamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entreno")
    private int idEntreno;

    @Column(name = "id_usuario")
    private int idUsuario;

    @Column(name = "id_ejer")
    private int idEjer;

    @Column(name = "nombre_entreno")
    private String nombreEntreno;

    private int repeticiones;
    private int fallos;
    private int aciertos;
    private boolean completado;

    public Entrenamiento() {}

    public int getIdEntreno() { return idEntreno; }
    public void setIdEntreno(int idEntreno) { this.idEntreno = idEntreno; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public int getIdEjer() { return idEjer; }
    public void setIdEjer(int idEjer) { this.idEjer = idEjer; }
    public String getNombreEntreno() { return nombreEntreno; }
    public void setNombreEntreno(String nombreEntreno) { this.nombreEntreno = nombreEntreno; }
    public int getRepeticiones() { return repeticiones; }
    public void setRepeticiones(int repeticiones) { this.repeticiones = repeticiones; }
    public int getFallos() { return fallos; }
    public void setFallos(int fallos) { this.fallos = fallos; }
    public int getAciertos() { return aciertos; }
    public void setAciertos(int aciertos) { this.aciertos = aciertos; }
    public boolean isCompletado() { return completado; }
    public void setCompletado(boolean completado) { this.completado = completado; }
}
