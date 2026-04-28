package com.javafx.ProyectoINT.EditarEntreno;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.javafx.ProyectoINT.Entrenos.ControlEntrenos;
import com.javafx.ProyectoINT.InicioSesion.ControlInicioSesion;
import com.javafx.ProyectoINT.modelos.Ejercicios;
import com.javafx.ProyectoINT.modelos.EjerciciosDAO;
import com.javafx.ProyectoINT.modelos.Entrenamiento;
import com.javafx.ProyectoINT.modelos.EntrenamientoDAO;

import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControlEditarEntreno {

    @FXML
    private TextField Aciertos;

    @FXML
    private TextField Fallos;

    @FXML
    private TextField Nombre;

    @FXML
    private TextField Repeticiones;

    private ObservableList<Ejercicios> listaEjercicios = FXCollections.observableArrayList();
    @FXML
    private TableView<Ejercicios> tablaEjercicios;
    @FXML
    private TableColumn<Ejercicios, Integer> colIdEjer;
    @FXML
    private TableColumn<Ejercicios, String> colNombreEjer;
    @FXML
    private TableColumn<Ejercicios, String> colTipo;
    @FXML
    private TableColumn<Ejercicios, String> colFinalidad;
    @FXML
    private TableColumn<Ejercicios, Boolean> colCompletado;

    // Mapa para buscar el Entrenamiento correspondiente a cada ejercicio
    private Map<Integer, Entrenamiento> mapaEntrenamientos = new HashMap<>();

    @FXML
    void initialize() {
        colIdEjer.setCellValueFactory(new PropertyValueFactory<>("id_ejer"));
        colNombreEjer.setCellValueFactory(new PropertyValueFactory<>("nombre_ejer"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colFinalidad.setCellValueFactory(new PropertyValueFactory<>("finalidad"));

        // Columna Completado con checkbox editable
        colCompletado.setCellValueFactory(cellData -> {
            Ejercicios ejercicio = cellData.getValue();
            Entrenamiento entreno = mapaEntrenamientos.get(ejercicio.getId_ejer());
            SimpleBooleanProperty prop = new SimpleBooleanProperty(entreno != null && entreno.isCompletado());
            prop.addListener((obs, oldVal, newVal) -> {
                if (entreno != null) {
                    entreno.setCompletado(newVal);
                    EntrenamientoDAO dao = new EntrenamientoDAO();
                    dao.actualizarEntrenamiento(entreno);
                }
            });
            return prop;
        });
        colCompletado.setCellFactory(CheckBoxTableCell.forTableColumn(colCompletado));

        tablaEjercicios.setItems(listaEjercicios);

        Nombre.setText(ControlEntrenos.entrenoSeleccionado);

        cargarEjerciciosDelEntreno();

        tablaEjercicios.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        Entrenamiento entreno = mapaEntrenamientos.get(newSelection.getId_ejer());
                        if (entreno != null) {
                            Repeticiones.setText(String.valueOf(entreno.getRepeticiones()));
                            Fallos.setText(String.valueOf(entreno.getFallos()));
                            Aciertos.setText(String.valueOf(entreno.getAciertos()));
                        }
                    }
                });
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
    void Actualizar(ActionEvent event) {
        Ejercicios ejercicioSeleccionado = tablaEjercicios.getSelectionModel().getSelectedItem();
        if (ejercicioSeleccionado == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Sin selección");
            alert.setHeaderText("Ningún ejercicio seleccionado");
            alert.setContentText("Selecciona un ejercicio de la tabla para actualizar sus datos");
            alert.showAndWait();
            return;
        }

        String nombreTexto = Nombre.getText();
        String repeticionesTexto = Repeticiones.getText();
        String fallosTexto = Fallos.getText();
        String aciertosTexto = Aciertos.getText();

        if (nombreTexto == null || nombreTexto.trim().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error de validación");
            alert.setHeaderText("Campo vacío");
            alert.setContentText("El nombre del entrenamiento no puede estar vacío");
            alert.showAndWait();
            return;
        }

        int repeticiones, fallos, aciertos;
        try {
            repeticiones = Integer.parseInt(repeticionesTexto.trim());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error de validación");
            alert.setHeaderText("Valor inválido");
            alert.setContentText("Las repeticiones deben ser un número entero");
            alert.showAndWait();
            return;
        }
        try {
            fallos = Integer.parseInt(fallosTexto.trim());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error de validación");
            alert.setHeaderText("Valor inválido");
            alert.setContentText("Los fallos deben ser un número entero");
            alert.showAndWait();
            return;
        }
        try {
            aciertos = Integer.parseInt(aciertosTexto.trim());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error de validación");
            alert.setHeaderText("Valor inválido");
            alert.setContentText("Los aciertos deben ser un número entero");
            alert.showAndWait();
            return;
        }

        Entrenamiento entreno = mapaEntrenamientos.get(ejercicioSeleccionado.getId_ejer());
        if (entreno == null) {
            return;
        }

        entreno.setNombre_entreno(nombreTexto);
        entreno.setRepeticiones(repeticiones);
        entreno.setFallos(fallos);
        entreno.setAciertos(aciertos);

        EntrenamientoDAO dao = new EntrenamientoDAO();
        if (dao.actualizarEntrenamiento(entreno)) {
            ControlEntrenos.entrenoSeleccionado = nombreTexto;
            Nombre.setText(nombreTexto);
            cargarEjerciciosDelEntreno();
        }
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

    private void cargarEjerciciosDelEntreno() {
        String nombreEntreno = ControlEntrenos.entrenoSeleccionado;
        int idUsuario = ControlInicioSesion.usuarioLogueadoId;

        EjerciciosDAO ejerciciosDAO = new EjerciciosDAO();
        listaEjercicios.clear();
        listaEjercicios.addAll(ejerciciosDAO.obtenerEjerciciosPorEntreno(nombreEntreno, idUsuario));

        EntrenamientoDAO entrenamientoDAO = new EntrenamientoDAO();
        ObservableList<Entrenamiento> entrenamientos = entrenamientoDAO.obtenerEntrenamientosPorNombre(nombreEntreno, idUsuario);
        mapaEntrenamientos.clear();
        for (Entrenamiento e : entrenamientos) {
            mapaEntrenamientos.put(e.getId_ejer(), e);
        }
    }
}
