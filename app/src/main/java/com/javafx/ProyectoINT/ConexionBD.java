package com.javafx.ProyectoINT;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBD {

    private static ConexionBD instancia;
    private Connection conexion;

    private static final String URL;
    private static final String USUARIO;
    private static final String PASSWORD;

    static {
        Properties props = new Properties();
        try (InputStream input = ConexionBD.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new IOException("No se pudo encontrar el archivo database.properties");
            }
            props.load(input);
            URL = props.getProperty("db.url");
            USUARIO = props.getProperty("db.user");
            PASSWORD = props.getProperty("db.password");
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar la configuración de la base de datos: " + e.getMessage(), e);
        }
    }

    private ConexionBD() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("Conexión exitosa a LrVoley");
        } catch (Exception e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }

    public static ConexionBD getInstancia() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    public Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                System.out.println("Conexión realizada");
            }
        } catch (SQLException e) {
            System.out.println("No se conectó con la base de datos, error: " + e.getMessage());
        }
        return conexion;
    }
}