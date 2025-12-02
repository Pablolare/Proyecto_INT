package com.javafx.ProyectoINT.Entrenos;

import java.io.IOException;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControlEntrenos {

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
    }

    @FXML
    void Borrar(ActionEvent event) {
        Entrenamiento seleccionado = tablaEntrenamientos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            System.out.println("Selecciona un entrenamiento para borrar");
            return;
        }
        EntrenamientoDAO dao = new EntrenamientoDAO();
        dao.borrarEntrenamiento(seleccionado.getId_entreno());
        cargarEjerciciosDesdeBD();
    }

    @FXML
    void Editar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/EditarEntreno.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void AgregarEjercicio(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/AgregarEjercicio.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
        EntrenamientoDAO dao = new EntrenamientoDAO();
        listaEntrenamientos.clear();
        listaEntrenamientos.addAll(dao.cargarEntrenamientosDesdeBD());
    }
}