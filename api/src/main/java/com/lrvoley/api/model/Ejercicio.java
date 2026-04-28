package com.lrvoley.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Ejercicio")
public class Ejercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ejer")
    private int idEjer;

    @Column(name = "nombre_ejer")
    private String nombreEjer;

    private String tipo;

    @Column(columnDefinition = "TEXT")
    private String finalidad;

    public Ejercicio() {}

    public int getIdEjer() { return idEjer; }
    public void setIdEjer(int idEjer) { this.idEjer = idEjer; }
    public String getNombreEjer() { return nombreEjer; }
    public void setNombreEjer(String nombreEjer) { this.nombreEjer = nombreEjer; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getFinalidad() { return finalidad; }
    public void setFinalidad(String finalidad) { this.finalidad = finalidad; }
}
