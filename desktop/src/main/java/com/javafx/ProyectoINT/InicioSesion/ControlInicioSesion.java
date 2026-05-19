package com.javafx.ProyectoINT.InicioSesion;

import java.io.IOException;

import com.javafx.ProyectoINT.ApiCliente;
import com.javafx.ProyectoINT.SessionManager;
import com.javafx.ProyectoINT.modelos.Usuario;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControlInicioSesion {

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
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Campos vacíos");
            alert.setHeaderText("Faltan datos");
            alert.setContentText("Por favor completa todos los campos");
            alert.showAndWait();
            return;
        }

        Usuario usuario = ApiCliente.login(loginOCorreo, contraseña);

        if (usuario != null) {
            usuarioLogueadoId = usuario.getId_usuario();
            SessionManager.iniciarSesion(usuario.getId_usuario(), usuario.getRol());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/PaginaPrincipal.fxml"));
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
            alert.setTitle("Error de inicio de sesión");
            alert.setHeaderText("Credenciales incorrectas");
            alert.setContentText("Usuario o contraseña incorrectos");
            alert.showAndWait();
        }
    }

    @FXML
    void Registrarse(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/Registrar.fxml"));
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
