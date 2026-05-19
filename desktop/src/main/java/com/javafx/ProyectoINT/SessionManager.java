package com.javafx.ProyectoINT;

public class SessionManager {

    private static int idUsuario = -1;
    private static String rol = "";

    public static void iniciarSesion(int id, String rolUsuario) {
        idUsuario = id;
        rol = rolUsuario;
    }

    public static void cerrarSesion() {
        idUsuario = -1;
        rol = "";
    }

    public static int getIdUsuario() { return idUsuario; }
    public static String getRol() { return rol; }
    public static boolean esAdmin() { return "administrador".equals(rol); }
}
