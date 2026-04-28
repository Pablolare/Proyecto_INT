package com.javafx.ProyectoINT.Entrenos;

import java.io.IOException;

import com.javafx.ProyectoINT.InicioSesion.ControlInicioSesion;
import com.javafx.ProyectoINT.modelos.Entrenamiento;
import com.javafx.ProyectoINT.modelos.EntrenamientoDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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

public class ControlEntrenos {

    public static String entrenoSeleccionado = "";

    private ObservableList<Entrenamiento> listaBase = FXCollections.observableArrayList();
    private FilteredList<Entrenamiento> listaFiltrada;

    @FXML private TextField txtBuscar;
    @FXML
    private TableView<Entrenamiento> tablaEntrenamientos;
    @FXML
    private TableColumn<Entrenamiento, String> colNombreEntreno;
    @FXML
    private TableColumn<Entrenamiento, Integer> colNumEjercicios;
    @FXML
    private TableColumn<Entrenamiento, Integer> colRepeticiones;
    @FXML
    private TableColumn<Entrenamiento, Integer> colFallos;
    @FXML
    private TableColumn<Entrenamiento, Integer> colAciertos;
    @FXML
    private TableColumn<Entrenamiento, Boolean> colCompletado;

    @FXML
    void initialize() {
        colNombreEntreno.setCellValueFactory(new PropertyValueFactory<>("nombre_entreno"));
        colNumEjercicios.setCellValueFactory(new PropertyValueFactory<>("numEjercicios"));
        colRepeticiones.setCellValueFactory(new PropertyValueFactory<>("repeticiones"));
        colFallos.setCellValueFactory(new PropertyValueFactory<>("fallos"));
        colAciertos.setCellValueFactory(new PropertyValueFactory<>("aciertos"));
        colCompletado.setCellValueFactory(new PropertyValueFactory<>("completado"));

        listaFiltrada = new FilteredList<>(listaBase, p -> true);
        SortedList<Entrenamiento> listaSorted = new SortedList<>(listaFiltrada);
        listaSorted.comparatorProperty().bind(tablaEntrenamientos.comparatorProperty());
        tablaEntrenamientos.setItems(listaSorted);

        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> {
            listaFiltrada.setPredicate(e -> {
                if (newVal == null || newVal.trim().isEmpty()) return true;
                return e.getNombre_entreno().toLowerCase().contains(newVal.toLowerCase());
            });
        });

        cargarEntrenosDesdeBD();
    }


    @FXML
    void Borrar(ActionEvent event) {
        Entrenamiento seleccionado = tablaEntrenamientos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Sin selección");
            alert.setHeaderText("Ningún entrenamiento seleccionado");
            alert.setContentText("Selecciona un entrenamiento de la tabla para borrarlo");
            alert.showAndWait();
            return;
        }

        Alert confirmacion = new Alert(AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar borrado");
        confirmacion.setHeaderText("Borrar entrenamiento '" + seleccionado.getNombre_entreno() + "'");
        confirmacion.setContentText("Se borrarán todos los ejercicios asociados a este entrenamiento. ¿Estás seguro?");

        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                EntrenamientoDAO dao = new EntrenamientoDAO();
                if (dao.borrarEntrenamientoPorNombre(seleccionado.getNombre_entreno(),
                        ControlInicioSesion.usuarioLogueadoId)) {
                    cargarEntrenosDesdeBD();
                } else {
                    Alert error = new Alert(AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText("Error al borrar");
                    error.setContentText("No se pudo borrar el entrenamiento");
                    error.showAndWait();
                }
            }
        });
    }


    @FXML
    void Editar(ActionEvent event) throws IOException {
        Entrenamiento seleccionado = tablaEntrenamientos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Sin selección");
            alert.setHeaderText("Ningún entrenamiento seleccionado");
            alert.setContentText("Selecciona un entrenamiento de la tabla para editarlo");
            alert.showAndWait();
            return;
        }
        entrenoSeleccionado = seleccionado.getNombre_entreno();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/EditarEntreno.fxml"));
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
    void CrearEjercicio(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/CrearEjercicio.fxml"));
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
    void ListarEjercicios(ActionEvent event) throws IOException {
        Entrenamiento seleccionado = tablaEntrenamientos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Sin selección");
            alert.setHeaderText("Ningún entrenamiento seleccionado");
            alert.setContentText("Selecciona un entrenamiento de la tabla para ver sus ejercicios");
            alert.showAndWait();
            return;
        }
        entrenoSeleccionado = seleccionado.getNombre_entreno();
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

    @FXML
    void PagNotas(ActionEvent event) throws IOException {
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

    @FXML
    void PagCategorias(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/Categorias.fxml"));
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

    private void cargarEntrenosDesdeBD() {
        EntrenamientoDAO dao = new EntrenamientoDAO();
        listaBase.clear();
        listaBase.addAll(dao.obtenerEntrenosAgrupadosUsuario(ControlInicioSesion.usuarioLogueadoId));
    }
}
