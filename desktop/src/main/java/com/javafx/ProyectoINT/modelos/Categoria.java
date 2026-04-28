package com.javafx.ProyectoINT.modelos;

public class Categoria {

    private int id_categoria;
    private String nombre;
    private String descripcion;

    public Categoria(int id_categoria, String nombre, String descripcion) {
        this.id_categoria = id_categoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getId_categoria() { return id_categoria; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }

    public void setId_categoria(int id_categoria) { this.id_categoria = id_categoria; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() { return nombre; }
}
