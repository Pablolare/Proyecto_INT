package com.javafx.ProyectoINT.EditarEjercicios;

import java.io.IOException;

import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

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
            System.out.println("Por favor, corrija los errores en el formulario");
            return;
        }

        Ejercicios seleccionado = tablaEditarEjercicios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            System.out.println("Selecciona un ejercicio para actualizar");
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
            System.out.println("Selecciona un ejercicio para borrar");
            return;
        }
        EjerciciosDAO dao = new EjerciciosDAO();
        dao.borrarEjercicio(seleccionado.getId_ejer());
        cargarEjerciciosDesdeBD();
    }

    @FXML
    void Aceptar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/ListarEjercicios.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
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
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
