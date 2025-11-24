package com.javafx.ProyectoINT.modelos;

public class Usuario  {

    private int id_usuario; 
    private String nombre;
    private String apellido; 
    private String login; 
    private String contraseña; 
    private String rol; 
    private String correo;

    public Usuario(int id_usuario, String nombre, String apellido, String login, String contraseña, String rol, String correo){
        
        this.id_usuario=id_usuario;
        this.nombre=nombre;
        this.apellido=apellido;
        this.login=login;
        this.contraseña=contraseña;
        this.rol=rol;
        this.correo=correo;
        
    }

    public int getId_usuario() {
        return this.id_usuario;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public String getLogin() {
        return this.login;
    }

    public String getContraseña() {
        return this.contraseña;
    }

    public String getRol() {
        return this.rol;
    }

    public String getCorreo() {
        return this.correo;
    }




    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
