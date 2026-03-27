package com.javafx.ProyectoINT.CrearEjercicio;

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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControlCrearEjercicio {

    @FXML
    private Button btnCrear;

    @FXML
    private TextField txtFinalidad;

    @FXML
    private TextField txtNombreEjercicio;

    @FXML
    private TextField txtTipo;

    @FXML
    private TableView<Ejercicios> tablaEjercicios;

    @FXML
    private TableColumn<Ejercicios, String> colNombre;

    @FXML
    private TableColumn<Ejercicios, String> colTipo;

    @FXML
    private TableColumn<Ejercicios, String> colFinalidad;

    private ObservableList<Ejercicios> listaEjercicios = FXCollections.observableArrayList();
    private ValidationSupport validationSupport;

    @FXML
    void initialize() {
        configurarValidaciones();
        configurarTabla();
        cargarEjercicios();
    }

    private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre_ejer"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colFinalidad.setCellValueFactory(new PropertyValueFactory<>("finalidad"));
        tablaEjercicios.setItems(listaEjercicios);
    }

    private void cargarEjercicios() {
        EjerciciosDAO dao = new EjerciciosDAO();
        listaEjercicios.clear();
        listaEjercicios.addAll(dao.cargarEjerciciosDesdeBD());
    }

    private void configurarValidaciones() {
        validationSupport = new ValidationSupport();

        Validator<String> nombreValidator = (control, value) -> {
            if (value == null || value.trim().isEmpty()) {
                return ValidationResult.fromError(control, "El nombre del ejercicio es obligatorio");
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

        validationSupport.registerValidator(txtNombreEjercicio, nombreValidator);
        validationSupport.registerValidator(txtTipo, tipoValidator);
        validationSupport.registerValidator(txtFinalidad, finalidadValidator);
    }

    @FXML
    void Creacion(ActionEvent event) {
        if (validationSupport.isInvalid()) {
            System.out.println("Por favor, corrija los errores en el formulario antes de guardar");
            return;
        }

        Ejercicios ejercicio = new Ejercicios(txtNombreEjercicio.getText(), txtTipo.getText(), txtFinalidad.getText());
        EjerciciosDAO dao = new EjerciciosDAO();
        dao.insertarEjercicio(ejercicio);

        // Actualizar la tabla
        cargarEjercicios();

        // Limpiar campos
        txtNombreEjercicio.clear();
        txtTipo.clear();
        txtFinalidad.clear();
    }

    @FXML
    void Cerrar(ActionEvent event) throws IOException {
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
}
