package com.javafx.ProyectoINT.EditarEjercicios;

import java.io.IOException;

import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import com.javafx.ProyectoINT.ListarEjercicios.ControlListarEjercicios;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControlEditarEjercicios {
    @FXML
    private TextField txFinalidad;

    @FXML
    private TextField txNombre;

    @FXML
    private TextField txTipo;

    private ObservableList<Ejercicios> listaEjercicios = FXCollections.observableArrayList();
    @FXML
    private TableView<Ejercicios> tablaEditarEjercicios;
    @FXML
    private TableColumn<Ejercicios, Integer> colId;
    @FXML
    private TableColumn<Ejercicios, String> colNombreEjer;
    @FXML
    private TableColumn<Ejercicios, String> colTipo;
    @FXML
    private TableColumn<Ejercicios, String> colFinalidad;

    private ValidationSupport validationSupport;

    @FXML
    void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_ejer"));
        colNombreEjer.setCellValueFactory(new PropertyValueFactory<>("nombre_ejer"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colFinalidad.setCellValueFactory(new PropertyValueFactory<>("finalidad"));
        tablaEditarEjercicios.setItems(listaEjercicios);
        cargarEjerciciosDesdeBD();
        configurarValidaciones();

        tablaEditarEjercicios.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        txNombre.setText(newSelection.getNombre_ejer());
                        txTipo.setText(newSelection.getTipo());
                        txFinalidad.setText(newSelection.getFinalidad());
                    }
                });

        if (ControlListarEjercicios.ejercicioSeleccionadoId >= 0) {
            for (Ejercicios ej : listaEjercicios) {
                if (ej.getId_ejer() == ControlListarEjercicios.ejercicioSeleccionadoId) {
                    tablaEditarEjercicios.getSelectionModel().select(ej);
                    break;
                }
            }
            ControlListarEjercicios.ejercicioSeleccionadoId = -1;
        }
    }

    private void configurarValidaciones() {
        validationSupport = new ValidationSupport();

        Validator<String> nombreValidator = (control, value) -> {
            if (value == null || value.trim().isEmpty()) {
                return ValidationResult.fromError(control, "El nombre es obligatorio");
            }
            return null;
        };

        Validator<String> tipoValidator = (control, value) -> {
            if (value == null || value.trim().isEmpty()) {
                return ValidationResult.fromError(control, "El tipo es obligatorio");
            }
            return null;
        };

        Validator<String> finalidadValidator = (control, value) -> {
            if (value == null || value.trim().isEmpty()) {
                return ValidationResult.fromError(control, "La finalidad es obligatoria");
            }
            return null;
        };

        validationSupport.registerValidator(txNombre, nombreValidator);
        validationSupport.registerValidator(txTipo, tipoValidator);
        validationSupport.registerValidator(txFinalidad, finalidadValidator);
    }

    @FXML
    void Actualizar(ActionEvent event) {
        if (validationSupport.isInvalid()) {
            return;
        }

        Ejercicios seleccionado = tablaEditarEjercicios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Sin selección");
            alert.setHeaderText("Ningún ejercicio seleccionado");
            alert.setContentText("Selecciona un ejercicio de la tabla para actualizarlo");
            alert.showAndWait();
            return;
        }
        seleccionado.setNombre_ejer(txNombre.getText());
        seleccionado.setTipo(txTipo.getText());
        seleccionado.setFinalidad(txFinalidad.getText());

        EjerciciosDAO dao = new EjerciciosDAO();
        dao.actualizarEjercicio(seleccionado);
        cargarEjerciciosDesdeBD();
    }

    @FXML
    void Borrar(ActionEvent event) {
        Ejercicios seleccionado = tablaEditarEjercicios.getSelectionModel().getSelectedItem();
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
                    txNombre.clear();
                    txTipo.clear();
                    txFinalidad.clear();
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
    void Aceptar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/ListarEjercicios.fxml"));
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
        listaEjercicios.addAll(dao.cargarEjerciciosDesdeBD());
    }

    @FXML
    void PagAtras(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/ListarEjercicios.fxml"));
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
