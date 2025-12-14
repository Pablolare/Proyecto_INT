package com.javafx.ProyectoINT.InicioSesion;

import java.io.IOException;

import com.javafx.ProyectoINT.ConexionBD;
import com.javafx.ProyectoINT.modelos.Usuario;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;

public class ControlInicioSesion {

    // Variable estática para guardar el ID del usuario que inició sesión
    public static int usuarioLogueadoId = -1;

    @FXML
    private javafx.scene.control.PasswordField txtContraseña;

    @FXML
    private TextField txtUsuarioCorreo;

    @FXML
    void IniciarSesion(ActionEvent event) throws IOException {
        String loginOCorreo = txtUsuarioCorreo.getText();
        String contraseña = txtContraseña.getText();

        if (loginOCorreo.isEmpty() || contraseña.isEmpty()) {
            System.out.println("Por favor completa todos los campos");
            return;
        }

        Usuario usuario = buscarUsuario(loginOCorreo);

        if (usuario != null && usuario.getContraseña().equals(contraseña)) {
            System.out.println("Inicio de sesión exitoso: " + usuario.getNombre());

            // Guardar el ID del usuario que inició sesión
            usuarioLogueadoId = usuario.getId_usuario();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/PaginaPrincipal.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Usuario o contraseña incorrectos");
        }
    }

    private Usuario buscarUsuario(String loginOCorreo) {
        String sql = "SELECT * FROM Usuario WHERE login = ? OR correo = ?";

        try {
            Connection conn = ConexionBD.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, loginOCorreo);
            stmt.setString(2, loginOCorreo);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("login"),
                        rs.getString("contraseña"),
                        rs.getString("rol"),
                        rs.getString("correo"));
                rs.close();
                stmt.close();
                return usuario;
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Error al buscar usuario: " + e.getMessage());
        }

        return null;
    }

    @FXML
    void Registrarse(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/Registrar.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
