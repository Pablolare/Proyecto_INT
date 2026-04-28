package com.javafx.ProyectoINT.ListarEjercicios;

import java.io.IOException;

import com.javafx.ProyectoINT.Entrenos.ControlEntrenos;
import com.javafx.ProyectoINT.InicioSesion.ControlInicioSesion;
import com.javafx.ProyectoINT.modelos.Ejercicios;
import com.javafx.ProyectoINT.modelos.EjerciciosDAO;

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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControlListarEjercicios {

    public static int ejercicioSeleccionadoId = -1;

    private ObservableList<Ejercicios> listaEjercicios = FXCollections.observableArrayList();
    @FXML
    private TableView<Ejercicios> tablaEjercicios;
    @FXML
    private TableColumn<Ejercicios, Integer> colId;
    @FXML
    private TableColumn<Ejercicios, String> colNombre;
    @FXML
    private TableColumn<Ejercicios, String> colTipo;
    @FXML
    private TableColumn<Ejercicios, String> colFinalidad;

    @FXML
    void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_ejer"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre_ejer"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colFinalidad.setCellValueFactory(new PropertyValueFactory<>("finalidad"));
        tablaEjercicios.setItems(listaEjercicios);
        cargarEjerciciosDesdeBD();
    }

    @FXML
    void Borrar(ActionEvent event) {
        Ejercicios seleccionado = tablaEjercicios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Sin selección");
            alert.setHeaderText("Ningún ejercicio seleccionado");
            alert.setContentText("Selecciona un ejercicio de la tabla para borrarlo");
            alert.showAndWait();
            return;
        }

        Alert confirmacion = new Alert(AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar borrado");
        confirmacion.setHeaderText("Borrar ejercicio '" + seleccionado.getNombre_ejer() + "'");
        confirmacion.setContentText("¿Estás seguro de que quieres borrar este ejercicio?");

        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                EjerciciosDAO dao = new EjerciciosDAO();
                if (dao.borrarEjercicio(seleccionado.getId_ejer())) {
                    cargarEjerciciosDesdeBD();
                } else {
                    Alert error = new Alert(AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText("Error al borrar");
                    error.setContentText("No se pudo borrar el ejercicio");
                    error.showAndWait();
                }
            }
        });
    }

    @FXML
    void Editar(ActionEvent event) throws IOException {
        Ejercicios seleccionado = tablaEjercicios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Sin selección");
            alert.setHeaderText("Ningún ejercicio seleccionado");
            alert.setContentText("Selecciona un ejercicio de la tabla para editarlo");
            alert.showAndWait();
            return;
        }
        ejercicioSeleccionadoId = seleccionado.getId_ejer();
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

    @FXML
    void PagAtras(ActionEvent event) throws IOException {
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

    private void cargarEjerciciosDesdeBD() {
        EjerciciosDAO dao = new EjerciciosDAO();
        listaEjercicios.clear();
        String nombreEntreno = ControlEntrenos.entrenoSeleccionado;
        int idUsuario = ControlInicioSesion.usuarioLogueadoId;
        if (nombreEntreno != null && !nombreEntreno.isEmpty()) {
            listaEjercicios.addAll(dao.obtenerEjerciciosPorEntreno(nombreEntreno, idUsuario));
        } else {
            listaEjercicios.addAll(dao.cargarEjerciciosDesdeBD());
        }
    }
}
