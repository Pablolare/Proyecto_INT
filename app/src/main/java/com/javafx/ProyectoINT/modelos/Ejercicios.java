package com.javafx.ProyectoINT.modelos;

public class Ejercicios {
    private int id_ejer;
    private String nombre_ejer;
    private String tipo;
    private String finalidad;

    public Ejercicios( int id_ejer, String nombre_ejer, String tipo, String finalidad){
        this.id_ejer=id_ejer;
        this.nombre_ejer=nombre_ejer;
        this.tipo=tipo;
        this.finalidad=finalidad;
    }

    public int getId_ejer() {
        return id_ejer;
    }

    public String getNombre_ejer() {
        return nombre_ejer;
    }

    public String getTipo() {
        return tipo;
    }

    public String getFinalidad() {
        return finalidad;
    }




    public void setId_ejer(int id_ejer) {
        this.id_ejer = id_ejer;
    }

    public void setNombre_ejer(String nombre_ejer) {
        this.nombre_ejer = nombre_ejer;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setFinalidad(String finalidad) {
        this.finalidad = finalidad;
    }
}
