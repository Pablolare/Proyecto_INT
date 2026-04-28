package com.javafx.ProyectoINT.PagAñadirEntreno;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControlPagAñadirEntreno {
    private ObservableList<Ejercicios> listaEjercicios = FXCollections.observableArrayList();
    private ObservableList<Ejercicios> listaEjerciciosEntreno = FXCollections.observableArrayList();

    @FXML
    private TableView<Ejercicios> tablaEjerNuevoEntreno;
    @FXML
    private TableColumn<Ejercicios, String> colNombreEjer;
    @FXML
    private TableColumn<Ejercicios, String> colTipo;
    @FXML
    private TableColumn<Ejercicios, String> colFinalidad;

    @FXML
    private TableView<Ejercicios> tablaEjerDelEntreno;
    @FXML
    private TableColumn<Ejercicios, String> colNombreEjerEntreno;
    @FXML
    private TableColumn<Ejercicios, String> colTipoEntreno;
    @FXML
    private TableColumn<Ejercicios, String> colFinalidadEntreno;

    @FXML
    private TextField txtNuevoEntreno;

    @FXML
    private Label lblValidacion;

    @FXML
    private Button btnGuardarEntreno;

    @FXML
    void initialize() {
        colNombreEjer.setCellValueFactory(new PropertyValueFactory<>("nombre_ejer"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colFinalidad.setCellValueFactory(new PropertyValueFactory<>("finalidad"));
        tablaEjerNuevoEntreno.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tablaEjerNuevoEntreno.setItems(listaEjercicios);

        colNombreEjerEntreno.setCellValueFactory(new PropertyValueFactory<>("nombre_ejer"));
        colTipoEntreno.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colFinalidadEntreno.setCellValueFactory(new PropertyValueFactory<>("finalidad"));
        tablaEjerDelEntreno.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tablaEjerDelEntreno.setItems(listaEjerciciosEntreno);

        cargarEjerciciosDesdeBD();
    }

    @FXML
    void AgregarEjercicio(ActionEvent event) {
        ObservableList<Ejercicios> seleccionados = tablaEjerNuevoEntreno.getSelectionModel().getSelectedItems();

        if (seleccionados == null || seleccionados.isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Sin selección");
            alert.setHeaderText("Ningún ejercicio seleccionado");
            alert.setContentText(
                    "Selecciona uno o varios ejercicios de la tabla para agregarlos al entrenamiento (mantén Ctrl para seleccionar varios)");
            alert.showAndWait();
            return;
        }

        List<Ejercicios> copiaSeleccionados = new ArrayList<>(seleccionados);
        listaEjerciciosEntreno.addAll(copiaSeleccionados);
        listaEjercicios.removeAll(copiaSeleccionados);
        tablaEjerNuevoEntreno.getSelectionModel().clearSelection();
        ocultarValidacion();
    }

    @FXML
    void QuitarEjercicio(ActionEvent event) {
        ObservableList<Ejercicios> seleccionados = tablaEjerDelEntreno.getSelectionModel().getSelectedItems();

        if (seleccionados == null || seleccionados.isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Sin selección");
            alert.setHeaderText("Ningún ejercicio seleccionado");
            alert.setContentText("Selecciona uno o varios ejercicios de la tabla del entreno para quitarlos");
            alert.showAndWait();
            return;
        }

        List<Ejercicios> copiaSeleccionados = new ArrayList<>(seleccionados);
        listaEjercicios.addAll(copiaSeleccionados);
        listaEjerciciosEntreno.removeAll(copiaSeleccionados);
        tablaEjerDelEntreno.getSelectionModel().clearSelection();
    }

    @FXML
    void GuardarEntreno(ActionEvent event) throws IOException {
        String nombreEntrenamiento = txtNuevoEntreno.getText();
        if (nombreEntrenamiento == null || nombreEntrenamiento.trim().isEmpty()) {
            mostrarValidacion("El nombre del entrenamiento es obligatorio");
            return;
        }

        if (listaEjerciciosEntreno.isEmpty()) {
            mostrarValidacion("Debes añadir al menos un ejercicio al entrenamiento");
            return;
        }

        int idUsuario = ControlInicioSesion.usuarioLogueadoId;
        EntrenamientoDAO dao = new EntrenamientoDAO();
        int insertados = 0;
        boolean huboError = false;

        for (Ejercicios ejercicio : listaEjerciciosEntreno) {
            Entrenamiento entrenamiento = new Entrenamiento(
                    idUsuario,
                    ejercicio.getId_ejer(),
                    nombreEntrenamiento.trim(),
                    0, 0, 0, false);

            if (dao.insertarEntrenamiento(entrenamiento)) {
                insertados++;
            } else {
                huboError = true;
            }
        }

        if (huboError && insertados == 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al guardar");
            alert.setContentText(
                    "No se pudo guardar el entrenamiento. Es posible que ya exista un entrenamiento con ese nombre.");
            alert.showAndWait();
            return;
        }

        if (huboError) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Guardado parcial");
            alert.setHeaderText("Algunos ejercicios no se guardaron");
            alert.setContentText(
                    "Se guardaron " + insertados + " de " + listaEjerciciosEntreno.size() + " ejercicios.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Entrenamiento guardado");
            alert.setHeaderText("Guardado correctamente");
            alert.setContentText("El entrenamiento '" + nombreEntrenamiento.trim()
                    + "' se ha creado con " + insertados + " ejercicio(s).");
            alert.showAndWait();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/PaginaPrincipal.fxml"));
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
    void PagAtras(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/PaginaPrincipal.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        boolean maximizado = stage.isMaximized();
        double w = stage.getWidth(), h = stage.getHeight();
        double x = stage.getX(), y = stage.getY();
        stage.setScene(new Scene(root));
        if (maximizado) { stage.setMaximized(true); } else { stage.setWidth(w); stage.setHeight(h); stage.setX(x); stage.setY(y); }
        stage.show();
    }

    private void cargarEjerciciosDesdeBD() {
        EjerciciosDAO dao = new EjerciciosDAO();
        ObservableList<Ejercicios> todosEjercicios = dao.cargarEjerciciosDesdeBD();
        listaEjercicios.clear();
        listaEjercicios.addAll(todosEjercicios);
    }

    private void mostrarValidacion(String mensaje) {
        lblValidacion.setText(mensaje);
        lblValidacion.setVisible(true);
        lblValidacion.setManaged(true);
    }

    private void ocultarValidacion() {
        lblValidacion.setText("");
        lblValidacion.setVisible(false);
        lblValidacion.setManaged(false);
    }
}
