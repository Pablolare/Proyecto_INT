package com.javafx.ProyectoINT.PagAñadirEntreno;

import java.io.IOException;

import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControlPagAñadirEntreno {
    private ObservableList<Ejercicios> listaEjercicios = FXCollections.observableArrayList();

    @FXML
    private TableView<Ejercicios> tablaEjerNuevoEntreno;
    @FXML
    private TableColumn<Ejercicios, String> colNombreEjer;
    @FXML
    private TableColumn<Ejercicios, String> colTipo;
    @FXML
    private TableColumn<Ejercicios, String> colFinalidad;

    @FXML
    private Label nombreEntreno;

    @FXML
    private TextField txtNuevoEntreno;

    private ValidationSupport validationSupport;

    @FXML
    void initialize() {
        colNombreEjer.setCellValueFactory(new PropertyValueFactory<>("nombre_ejer"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colFinalidad.setCellValueFactory(new PropertyValueFactory<>("finalidad"));
        tablaEjerNuevoEntreno.setItems(listaEjercicios);
        cargarEjerciciosDesdeBD();
        configurarValidaciones();

        txtNuevoEntreno.textProperty().addListener((obs, oldValue, newValue) -> {
            nombreEntreno.setText(newValue);
            cargarEjerciciosDesdeBD();
        });
    }

    private void configurarValidaciones() {
        validationSupport = new ValidationSupport();

        Validator<String> nombreValidator = (control, value) -> {
            if (value == null || value.trim().isEmpty()) {
                return ValidationResult.fromError(control, "El nombre del entrenamiento es obligatorio");
            }
            return null;
        };

        validationSupport.registerValidator(txtNuevoEntreno, nombreValidator);
    }

    @FXML
    void AgregarEjercicio(ActionEvent event) {
        if (validationSupport.isInvalid()) {
            System.out.println("Por favor, ingresa un nombre para el entrenamiento");
            return;
        }

        String nombreEntrenamiento = txtNuevoEntreno.getText();
        Ejercicios seleccionado = tablaEjerNuevoEntreno.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            System.out.println("Error: Selecciona un ejercicio para agregar");
            return;
        }

        int id_usuario = 1;

        Entrenamiento entrenamiento = new Entrenamiento(
                id_usuario,
                seleccionado.getId_ejer(),
                nombreEntrenamiento,
                0,
                0,
                0,
                false);

        EntrenamientoDAO dao = new EntrenamientoDAO();
        if (dao.insertarEntrenamiento(entrenamiento)) {
            System.out.println("Ejercicio añadido al entrenamiento: " + seleccionado.getNombre_ejer());
            cargarEjerciciosDesdeBD();
        } else {
            System.out.println("Error al añadir ejercicio al entrenamiento");
        }
    }

    @FXML
    void PagAtras(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/PaginaPrincipal.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void cargarEjerciciosDesdeBD() {
        EjerciciosDAO dao = new EjerciciosDAO();
        ObservableList<Ejercicios> todosEjercicios = dao.cargarEjerciciosDesdeBD();

        listaEjercicios.clear();

        String nombreEntrenamiento = txtNuevoEntreno.getText();

        if (nombreEntrenamiento == null || nombreEntrenamiento.trim().isEmpty()) {
            listaEjercicios.addAll(todosEjercicios);
            return;
        }

        EntrenamientoDAO entrenamientoDAO = new EntrenamientoDAO();
        ObservableList<Entrenamiento> entrenamientos = entrenamientoDAO.cargarEntrenamientosDesdeBD();

        for (Ejercicios ejercicio : todosEjercicios) {
            boolean yaEstaEnEntrenamiento = false;

            for (Entrenamiento entrenamiento : entrenamientos) {
                if (entrenamiento.getNombre_entreno().equals(nombreEntrenamiento)
                        && entrenamiento.getId_ejer() == ejercicio.getId_ejer()) {
                    yaEstaEnEntrenamiento = true;
                    break;
                }
            }

            if (!yaEstaEnEntrenamiento) {
                listaEjercicios.add(ejercicio);
            }
        }
    }
}
