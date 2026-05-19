package com.javafx.ProyectoINT.EditarEntreno;

import java.io.IOException;

import com.javafx.ProyectoINT.Entrenos.ControlEntrenos;
import com.javafx.ProyectoINT.InicioSesion.ControlInicioSesion;
import com.javafx.ProyectoINT.modelos.Ejercicios;
import com.javafx.ProyectoINT.modelos.EjerciciosDAO;
import com.javafx.ProyectoINT.modelos.Entrenamiento;
import com.javafx.ProyectoINT.modelos.EntrenamientoDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControlEditarEntreno {

    @FXML private TextField txtNombre;
    @FXML private TextArea txtDescripcion;

    private ObservableList<Ejercicios> listaEjercicios = FXCollections.observableArrayList();
    @FXML private TableView<Ejercicios> tablaEjercicios;
    @FXML private TableColumn<Ejercicios, String> colNombreEjer;
    @FXML private TableColumn<Ejercicios, String> colTipo;
    @FXML private TableColumn<Ejercicios, String> colFinalidad;

    private ObservableList<Entrenamiento> entrenamientosEnBD = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        colNombreEjer.setCellValueFactory(new PropertyValueFactory<>("nombre_ejer"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colFinalidad.setCellValueFactory(new PropertyValueFactory<>("finalidad"));
        tablaEjercicios.setItems(listaEjercicios);
        txtNombre.setText(ControlEntrenos.entrenoSeleccionado);
        cargarDatosEntreno();
    }

    @FXML
    void Actualizar(ActionEvent event) {
        String nombre = txtNombre.getText();
        if (nombre == null || nombre.trim().isEmpty()) {
            mostrarError("Nombre vacío", "El nombre del entrenamiento no puede estar vacío");
            return;
        }

        String descripcion = txtDescripcion.getText() != null ? txtDescripcion.getText().trim() : "";
        EntrenamientoDAO dao = new EntrenamientoDAO();
        boolean ok = true;
        for (Entrenamiento e : entrenamientosEnBD) {
            e.setNombre_entreno(nombre.trim());
            e.setDescripcion(descripcion);
            if (!dao.actualizarEntrenamiento(e)) ok = false;
        }

        if (ok) {
            ControlEntrenos.entrenoSeleccionado = nombre.trim();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Actualizado");
            alert.setHeaderText("Entrenamiento actualizado");
            alert.setContentText("Los cambios se han guardado correctamente.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText("Actualización parcial");
            alert.setContentText("Algunos registros no se pudieron actualizar.");
            alert.showAndWait();
        }
    }

    @FXML
    void Aceptar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/Entrenos.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        boolean maximizado = stage.isMaximized();
        double w = stage.getWidth(), h = stage.getHeight();
        double x = stage.getX(), y = stage.getY();
        stage.setScene(new Scene(root));
        if (maximizado) { stage.setMaximized(true); } else { stage.setWidth(w); stage.setHeight(h); stage.setX(x); stage.setY(y); }
        stage.show();
    }

    @FXML
    void EditarEjercicios(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/EditarEjercicios.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        boolean maximizado = stage.isMaximized();
        double w = stage.getWidth(), h = stage.getHeight();
        double x = stage.getX(), y = stage.getY();
        stage.setScene(new Scene(root));
        if (maximizado) { stage.setMaximized(true); } else { stage.setWidth(w); stage.setHeight(h); stage.setX(x); stage.setY(y); }
        stage.show();
    }

    private void cargarDatosEntreno() {
        String nombreEntreno = ControlEntrenos.entrenoSeleccionado;
        int idUsuario = ControlInicioSesion.usuarioLogueadoId;

        EntrenamientoDAO entrenamientoDAO = new EntrenamientoDAO();
        ObservableList<Entrenamiento> entrenos = entrenamientoDAO.obtenerEntrenamientosPorNombre(nombreEntreno, idUsuario);
        entrenamientosEnBD.setAll(entrenos);

        if (!entrenos.isEmpty()) {
            Entrenamiento primero = entrenos.get(0);
            txtDescripcion.setText(primero.getDescripcion() != null ? primero.getDescripcion() : "");
        }

        EjerciciosDAO ejerciciosDAO = new EjerciciosDAO();
        listaEjercicios.clear();
        listaEjercicios.addAll(ejerciciosDAO.obtenerEjerciciosPorEntreno(nombreEntreno, idUsuario));
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
