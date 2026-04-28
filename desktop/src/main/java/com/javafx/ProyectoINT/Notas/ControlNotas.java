package com.javafx.ProyectoINT.Notas;

import java.io.IOException;

import com.javafx.ProyectoINT.InicioSesion.ControlInicioSesion;
import com.javafx.ProyectoINT.modelos.Nota;
import com.javafx.ProyectoINT.modelos.NotaDAO;

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

public class ControlNotas {

    @FXML private TextField txtBuscar;
    @FXML private TableView<Nota> tablaNotas;
    @FXML private TableColumn<Nota, Integer> colId;
    @FXML private TableColumn<Nota, String> colTitulo;
    @FXML private TableColumn<Nota, String> colEntreno;
    @FXML private TableColumn<Nota, String> colFecha;

    private ObservableList<Nota> listaBase = FXCollections.observableArrayList();
    private FilteredList<Nota> listaFiltrada;

    @FXML
    void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_nota"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colEntreno.setCellValueFactory(new PropertyValueFactory<>("nombreEntreno"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        listaFiltrada = new FilteredList<>(listaBase, p -> true);
        SortedList<Nota> listaSorted = new SortedList<>(listaFiltrada);
        listaSorted.comparatorProperty().bind(tablaNotas.comparatorProperty());
        tablaNotas.setItems(listaSorted);

        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> {
            listaFiltrada.setPredicate(nota -> {
                if (newVal == null || newVal.trim().isEmpty()) return true;
                String f = newVal.toLowerCase();
                return nota.getTitulo().toLowerCase().contains(f)
                    || (nota.getNombreEntreno() != null && nota.getNombreEntreno().toLowerCase().contains(f));
            });
        });

        cargarNotas();
    }

    private void cargarNotas() {
        listaBase.clear();
        listaBase.addAll(new NotaDAO().cargarNotasUsuario(ControlInicioSesion.usuarioLogueadoId));
    }

    @FXML
    void NuevaNota(ActionEvent event) throws IOException {
        ControlCrearNota.notaEditar = null;
        navegarACrearNota(event);
    }

    @FXML
    void Editar(ActionEvent event) throws IOException {
        Nota seleccionada = tablaNotas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            new Alert(AlertType.WARNING, "Selecciona una nota para editar").showAndWait();
            return;
        }
        ControlCrearNota.notaEditar = seleccionada;
        navegarACrearNota(event);
    }

    private void navegarACrearNota(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/CrearNota.fxml"));
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
    void Borrar(ActionEvent event) {
        Nota seleccionada = tablaNotas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            new Alert(AlertType.WARNING, "Selecciona una nota para borrar").showAndWait();
            return;
        }
        Alert confirmacion = new Alert(AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar borrado");
        confirmacion.setHeaderText("Borrar nota '" + seleccionada.getTitulo() + "'");
        confirmacion.setContentText("¿Estás seguro?");
        confirmacion.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                if (new NotaDAO().borrarNota(seleccionada.getId_nota())) {
                    cargarNotas();
                } else {
                    new Alert(AlertType.ERROR, "No se pudo borrar la nota").showAndWait();
                }
            }
        });
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
}
