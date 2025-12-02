package com.javafx.ProyectoINT;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionBD {

    private static ConexionBD instancia;
    private Connection conexion;

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/LrVoley";
    private static final String USUARIO = "admin";
    private static final String PASSWORD = "rWLbek73dokz";

    private ConexionBD() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("Conexión exitosa a LrVoley");

        } catch (Exception e) {
            System.out.println(" Error de conexión: " + e.getMessage());
        }
    }

    public static ConexionBD getInstancia() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    public Connection getConexion() {
        return conexion;
    }
}
