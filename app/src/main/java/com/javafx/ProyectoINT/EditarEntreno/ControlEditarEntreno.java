package com.javafx.ProyectoINT.EditarEntreno;

import java.io.IOException;

import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

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
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControlEditarEntreno {

    @FXML
    private TextField Aciertos;

    @FXML
    private CheckBox Completado;

    @FXML
    private TextField Fallos;

    @FXML
    private TextField Nombre;

    @FXML
    private TextField Repeticiones;

    private ObservableList<Entrenamiento> listaEntrenamientos = FXCollections.observableArrayList();
    @FXML
    private TableView<Entrenamiento> tablaEntrenamientos;
    @FXML
    private TableColumn<Entrenamiento, Integer> colIdEntreno;
    @FXML
    private TableColumn<Entrenamiento, String> colNombreEntreno;
    @FXML
    private TableColumn<Entrenamiento, Integer> colRepeticiones;
    @FXML
    private TableColumn<Entrenamiento, Integer> colFallos;
    @FXML
    private TableColumn<Entrenamiento, Integer> colAciertos;
    @FXML
    private TableColumn<Entrenamiento, Boolean> colCompletado;

    private ValidationSupport validationSupport;

    @FXML
    void initialize() {
        colIdEntreno.setCellValueFactory(new PropertyValueFactory<>("id_entreno"));
        colNombreEntreno.setCellValueFactory(new PropertyValueFactory<>("nombre_entreno"));
        colRepeticiones.setCellValueFactory(new PropertyValueFactory<>("repeticiones"));
        colFallos.setCellValueFactory(new PropertyValueFactory<>("fallos"));
        colAciertos.setCellValueFactory(new PropertyValueFactory<>("aciertos"));
        colCompletado.setCellValueFactory(new PropertyValueFactory<>("completado"));
        tablaEntrenamientos.setItems(listaEntrenamientos);
        cargarEjerciciosDesdeBD();
        configurarValidaciones();
        tablaEntrenamientos.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        Nombre.setText(newSelection.getNombre_entreno());
                        Repeticiones.setText(String.valueOf(newSelection.getRepeticiones()));
                        Fallos.setText(String.valueOf(newSelection.getFallos()));
                        Aciertos.setText(String.valueOf(newSelection.getAciertos()));
                        Completado.setSelected(newSelection.isCompletado());
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

        Validator<String> numeroValidator = (control, value) -> {
            if (value == null || value.trim().isEmpty()) {
                return ValidationResult.fromError(control, "El campo es obligatorio");
            }
            try {
                Integer.parseInt(value.trim());
            } catch (NumberFormatException e) {
                return ValidationResult.fromError(control, "Debe ser un número entero");
            }
            return null;
        };

        validationSupport.registerValidator(Nombre, nombreValidator);
        validationSupport.registerValidator(Repeticiones, numeroValidator);
        validationSupport.registerValidator(Fallos, numeroValidator);
        validationSupport.registerValidator(Aciertos, numeroValidator);
    }

    @FXML
    void Aceptar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/Entrenos.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void Actualizar(ActionEvent event) {
        if (validationSupport.isInvalid()) {
            System.out.println("Por favor, corrija los errores en el formulario");
            return;
        }

        Entrenamiento seleccionado = tablaEntrenamientos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            System.out.println("Selecciona un entrenamiento para actualizar");
            return;
        }
        seleccionado.setNombre_entreno(Nombre.getText());
        seleccionado.setRepeticiones(Integer.parseInt(Repeticiones.getText()));
        seleccionado.setFallos(Integer.parseInt(Fallos.getText()));
        seleccionado.setAciertos(Integer.parseInt(Aciertos.getText()));
        seleccionado.setCompletado(Completado.isSelected());

        EntrenamientoDAO dao = new EntrenamientoDAO();
        dao.actualizarEntrenamiento(seleccionado);
        cargarEjerciciosDesdeBD();
    }

    @FXML
    void EditarEjercicios(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/EditarEjercicios.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void cargarEjerciciosDesdeBD() {
        EntrenamientoDAO dao = new EntrenamientoDAO();
        listaEntrenamientos.clear();
        listaEntrenamientos.addAll(dao.cargarEntrenamientosDesdeBD());
    }
}
