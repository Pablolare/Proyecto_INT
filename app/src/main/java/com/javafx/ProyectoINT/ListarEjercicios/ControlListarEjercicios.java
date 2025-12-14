package com.javafx.ProyectoINT.ListarEjercicios;

import java.io.IOException;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControlListarEjercicios {

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
            System.out.println("Selecciona un ejercicio para borrar");
            return;
        }
        EjerciciosDAO dao = new EjerciciosDAO();
        dao.borrarEjercicio(seleccionado.getId_ejer());
        cargarEjerciciosDesdeBD();
    }

    @FXML
    void Editar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/EditarEjercicios.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void CrearEjercicio(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/CrearEjercicio.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void PagAtras(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/PagAñadirEntreno.fxml"));
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
}
