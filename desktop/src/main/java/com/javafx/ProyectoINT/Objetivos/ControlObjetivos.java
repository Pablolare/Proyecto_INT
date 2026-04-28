package com.javafx.ProyectoINT.Objetivos;

import java.io.IOException;

import com.javafx.ProyectoINT.InicioSesion.ControlInicioSesion;
import com.javafx.ProyectoINT.modelos.Objetivo;
import com.javafx.ProyectoINT.modelos.ObjetivoDAO;

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
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControlObjetivos {

    @FXML private TextField txDescripcion;
    @FXML private TextField txMetaAciertos;
    @FXML private TextField txMetaRepeticiones;
    @FXML private CheckBox chkCumplido;
    @FXML private TextField txtBuscar;

    @FXML private TableView<Objetivo> tablaObjetivos;
    @FXML private TableColumn<Objetivo, Integer> colId;
    @FXML private TableColumn<Objetivo, String> colDescripcion;
    @FXML private TableColumn<Objetivo, Integer> colMetaAciertos;
    @FXML private TableColumn<Objetivo, Integer> colMetaRepeticiones;
    @FXML private TableColumn<Objetivo, Boolean> colCumplido;
    @FXML private TableColumn<Objetivo, String> colFecha;

    private ObservableList<Objetivo> listaBase = FXCollections.observableArrayList();
    private FilteredList<Objetivo> listaFiltrada;

    @FXML
    void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_objetivo"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colMetaAciertos.setCellValueFactory(new PropertyValueFactory<>("meta_aciertos"));
        colMetaRepeticiones.setCellValueFactory(new PropertyValueFactory<>("meta_repeticiones"));
        colCumplido.setCellValueFactory(new PropertyValueFactory<>("cumplido"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha_creacion"));

        listaFiltrada = new FilteredList<>(listaBase, p -> true);
        SortedList<Objetivo> listaSorted = new SortedList<>(listaFiltrada);
        listaSorted.comparatorProperty().bind(tablaObjetivos.comparatorProperty());
        tablaObjetivos.setItems(listaSorted);

        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> {
            listaFiltrada.setPredicate(obj -> {
                if (newVal == null || newVal.trim().isEmpty()) return true;
                return obj.getDescripcion().toLowerCase().contains(newVal.toLowerCase());
            });
        });

        cargarObjetivos();

        tablaObjetivos.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        txDescripcion.setText(newVal.getDescripcion());
                        txMetaAciertos.setText(newVal.getMeta_aciertos() != null ? String.valueOf(newVal.getMeta_aciertos()) : "");
                        txMetaRepeticiones.setText(newVal.getMeta_repeticiones() != null ? String.valueOf(newVal.getMeta_repeticiones()) : "");
                        chkCumplido.setSelected(newVal.isCumplido());
                    }
                });
    }

    @FXML
    void Crear(ActionEvent event) {
        if (txDescripcion.getText().trim().isEmpty()) { mostrarAviso("La descripción es obligatoria"); return; }
        new ObjetivoDAO().insertarObjetivo(new Objetivo(ControlInicioSesion.usuarioLogueadoId, txDescripcion.getText(), parsearEntero(txMetaAciertos.getText()), parsearEntero(txMetaRepeticiones.getText())));
        cargarObjetivos();
        limpiarCampos();
    }

    @FXML
    void Actualizar(ActionEvent event) {
        if (txDescripcion.getText().trim().isEmpty()) { mostrarAviso("La descripción es obligatoria"); return; }
        Objetivo seleccionado = tablaObjetivos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) { mostrarAviso("Selecciona un objetivo de la tabla para actualizarlo"); return; }
        seleccionado.setDescripcion(txDescripcion.getText());
        seleccionado.setMeta_aciertos(parsearEntero(txMetaAciertos.getText()));
        seleccionado.setMeta_repeticiones(parsearEntero(txMetaRepeticiones.getText()));
        seleccionado.setCumplido(chkCumplido.isSelected());
        new ObjetivoDAO().actualizarObjetivo(seleccionado);
        cargarObjetivos();
    }

    @FXML
    void Borrar(ActionEvent event) {
        Objetivo seleccionado = tablaObjetivos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) { mostrarAviso("Selecciona un objetivo de la tabla para borrarlo"); return; }
        Alert confirmacion = new Alert(AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar borrado");
        confirmacion.setHeaderText("Borrar objetivo '" + seleccionado.getDescripcion() + "'");
        confirmacion.setContentText("¿Estás seguro?");
        confirmacion.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                if (new ObjetivoDAO().borrarObjetivo(seleccionado.getId_objetivo())) {
                    cargarObjetivos();
                    limpiarCampos();
                } else {
                    new Alert(AlertType.ERROR, "No se pudo borrar el objetivo").showAndWait();
                }
            }
        });
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

    private void cargarObjetivos() {
        listaBase.clear();
        listaBase.addAll(new ObjetivoDAO().cargarObjetivosUsuario(ControlInicioSesion.usuarioLogueadoId));
    }

    private Integer parsearEntero(String texto) {
        if (texto == null || texto.trim().isEmpty()) return null;
        try { return Integer.parseInt(texto.trim()); } catch (NumberFormatException e) { return null; }
    }

    private void limpiarCampos() {
        txDescripcion.clear();
        txMetaAciertos.clear();
        txMetaRepeticiones.clear();
        chkCumplido.setSelected(false);
    }

    private void mostrarAviso(String mensaje) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Sin selección");
        alert.setHeaderText("Ningún objetivo seleccionado");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
