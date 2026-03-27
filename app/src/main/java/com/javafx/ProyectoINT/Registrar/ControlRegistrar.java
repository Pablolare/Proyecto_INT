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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error de validación");
            alert.setHeaderText("Campo vacío");
            alert.setContentText("El nombre no puede estar vacío");
            alert.showAndWait();
            return;
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error de validación");
            alert.setHeaderText("Campo vacío");
            alert.setContentText("El apellido no puede estar vacío");
            alert.showAndWait();
            return;
        }
        if (contraseña == null || contraseña.trim().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error de validación");
            alert.setHeaderText("Campo vacío");
            alert.setContentText("La contraseña no puede estar vacía");
            alert.showAndWait();
            return;
        }
        if (correo == null || correo.trim().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error de validación");
            alert.setHeaderText("Campo vacío");
            alert.setContentText("El correo no puede estar vacío");
            alert.showAndWait();
            return;
        }
        if (rol == null || rol.trim().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error de validación");
            alert.setHeaderText("Campo vacío");
            alert.setContentText("El rol no puede estar vacío");
            alert.showAndWait();
            return;
        }

        String login = generarLogin(nombre, apellido);

        if (!contraseña.equals(confirmarContraseña)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error de validación");
            alert.setHeaderText("Contraseñas no coinciden");
            alert.setContentText("Las contraseñas no coinciden");
            alert.showAndWait();
            return;
        }

        Usuario usuario = new Usuario(nombre, apellido, login, contraseña, rol, correo);

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        if (usuarioDAO.insertarUsuario(usuario)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText("Registro exitoso");
            alert.setContentText("Usuario registrado exitosamente: " + nombre);
            alert.showAndWait();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/InicioSesion.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            boolean maximizado = stage.isMaximized();
            double w = stage.getWidth(), h = stage.getHeight();
            double x = stage.getX(), y = stage.getY();
            stage.setScene(new Scene(root));
            if (maximizado) { stage.setMaximized(true); } else { stage.setWidth(w); stage.setHeight(h); stage.setX(x); stage.setY(y); }
            stage.show();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al registrar");
            alert.setContentText("No se pudo registrar el usuario");
            alert.showAndWait();
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
        boolean maximizado = stage.isMaximized();
        double w = stage.getWidth(), h = stage.getHeight();
        double x = stage.getX(), y = stage.getY();
        stage.setScene(new Scene(root));
        if (maximizado) { stage.setMaximized(true); } else { stage.setWidth(w); stage.setHeight(h); stage.setX(x); stage.setY(y); }
        stage.show();
    }
}
