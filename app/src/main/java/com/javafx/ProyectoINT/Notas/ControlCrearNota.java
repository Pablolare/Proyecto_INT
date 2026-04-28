package com.javafx.ProyectoINT.Notas;

import java.io.IOException;

import com.javafx.ProyectoINT.InicioSesion.ControlInicioSesion;
import com.javafx.ProyectoINT.modelos.Entrenamiento;
import com.javafx.ProyectoINT.modelos.EntrenamientoDAO;
import com.javafx.ProyectoINT.modelos.Nota;
import com.javafx.ProyectoINT.modelos.NotaDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControlCrearNota {

    public static Nota notaEditar = null;

    @FXML private Label lblTituloPantalla;
    @FXML private TextField txTitulo;
    @FXML private TextArea txContenido;
    @FXML private ComboBox<String> cmbEntreno;

    @FXML
    void initialize() {
        cargarEntrenos();

        if (notaEditar != null) {
            lblTituloPantalla.setText("Editar Nota");
            txTitulo.setText(notaEditar.getTitulo());
            txContenido.setText(notaEditar.getContenido());
            cmbEntreno.setValue(notaEditar.getNombreEntreno() != null ? notaEditar.getNombreEntreno() : "Sin entrenamiento");
        } else {
            lblTituloPantalla.setText("Nueva Nota");
        }
    }

    private void cargarEntrenos() {
        cmbEntreno.getItems().clear();
        cmbEntreno.getItems().add("Sin entrenamiento");
        for (Entrenamiento e : new EntrenamientoDAO().obtenerEntrenosAgrupadosUsuario(ControlInicioSesion.usuarioLogueadoId)) {
            cmbEntreno.getItems().add(e.getNombre_entreno());
        }
        cmbEntreno.setValue("Sin entrenamiento");
    }

    @FXML
    void Guardar(ActionEvent event) throws IOException {
        String titulo = txTitulo.getText();
        if (titulo == null || titulo.trim().isEmpty()) {
            new Alert(AlertType.WARNING, "El título es obligatorio").showAndWait();
            return;
        }

        Integer idEntreno = obtenerIdEntreno();
        NotaDAO dao = new NotaDAO();

        if (notaEditar == null) {
            dao.insertarNota(new Nota(ControlInicioSesion.usuarioLogueadoId, idEntreno, titulo, txContenido.getText()));
        } else {
            notaEditar.setTitulo(titulo);
            notaEditar.setContenido(txContenido.getText());
            notaEditar.setId_entreno(idEntreno);
            dao.actualizarNota(notaEditar);
        }

        volverANotas(event);
    }

    @FXML
    void Cancelar(ActionEvent event) throws IOException {
        volverANotas(event);
    }

    private void volverANotas(ActionEvent event) throws IOException {
        notaEditar = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/Notas.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        boolean maximizado = stage.isMaximized();
        double w = stage.getWidth(), h = stage.getHeight();
        double x = stage.getX(), y = stage.getY();
        stage.setScene(new Scene(root));
        if (maximizado) { stage.setMaximized(true); } else { stage.setWidth(w); stage.setHeight(h); stage.setX(x); stage.setY(y); }
        stage.show();
    }

    private Integer obtenerIdEntreno() {
        String nombre = cmbEntreno.getValue();
        if (nombre == null || nombre.equals("Sin entrenamiento")) return null;
        return new NotaDAO().obtenerPrimerIdEntrenoPorNombre(nombre, ControlInicioSesion.usuarioLogueadoId);
    }
}
