package com.javafx.ProyectoINT.Registrar;

import java.io.IOException;

import com.javafx.ProyectoINT.modelos.Usuario;
import com.javafx.ProyectoINT.modelos.UsuarioDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControlRegistrar {
    @FXML
    private TextField txtConfirmarContraseña;

    @FXML
    private TextField txtContraseña;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtNombreUsuario;

    @FXML
    private TextField txtRol;

    @FXML
    void CrearCuenta(ActionEvent event) throws IOException {
        String nombre = txtNombreUsuario.getText();
        String apellido = txtApellidos.getText();
        String contraseña = txtContraseña.getText();
        String rol = txtRol.getText();
        String correo = txtCorreo.getText();
        String confirmarContraseña = txtConfirmarContraseña.getText();

        // DEBUG: Imprimir valores capturados
        System.out.println("================================");
        System.out.println("Nombre: '" + nombre + "'");
        System.out.println("Apellido: '" + apellido + "'");
        System.out.println("Contraseña: '" + contraseña + "'");
        System.out.println("Rol: '" + rol + "'");
        System.out.println("Correo: '" + correo + "'");
        System.out.println("================================");

        // Validar que los campos no estén vacíos
        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("El nombre no puede estar vacío");
            return;
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            System.out.println("El apellido no puede estar vacío");
            return;
        }
        if (contraseña == null || contraseña.trim().isEmpty()) {
            System.out.println("La contraseña no puede estar vacía");
            return;
        }
        if (correo == null || correo.trim().isEmpty()) {
            System.out.println("El correo no puede estar vacío");
            return;
        }
        if (rol == null || rol.trim().isEmpty()) {
            System.out.println("El rol no puede estar vacío");
            return;
        }

        String login = generarLogin(nombre, apellido);

        if (!contraseña.equals(confirmarContraseña)) {
            System.out.println("Las contraseñas no coinciden");
            return;
        }

        Usuario usuario = new Usuario(nombre, apellido, login, contraseña, rol, correo);

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        if (usuarioDAO.insertarUsuario(usuario)) {
            System.out.println("Usuario registrado exitosamente: " + nombre);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/PaginaPrincipal.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("No se pudo registrar el usuario");
        }
    }

    private String generarLogin(String nombre, String apellido) {
        String login = (nombre + apellido).toLowerCase().replaceAll("\\s", "");
        return login;
    }

    @FXML
    void InicioSesion(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/InicioSesion.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
