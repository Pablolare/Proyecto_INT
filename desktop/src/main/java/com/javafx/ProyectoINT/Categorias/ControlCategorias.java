package com.javafx.ProyectoINT.Categorias;

import java.io.IOException;

import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import com.javafx.ProyectoINT.modelos.Categoria;
import com.javafx.ProyectoINT.modelos.CategoriaDAO;

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

public class ControlCategorias {

    @FXML private TextField txNombre;
    @FXML private TextField txDescripcion;
    @FXML private TextField txtBuscar;

    @FXML private TableView<Categoria> tablaCategorias;
    @FXML private TableColumn<Categoria, Integer> colId;
    @FXML private TableColumn<Categoria, String> colNombre;
    @FXML private TableColumn<Categoria, String> colDescripcion;

    private ObservableList<Categoria> listaBase = FXCollections.observableArrayList();
    private FilteredList<Categoria> listaFiltrada;
    private ValidationSupport validationSupport;

    @FXML
    void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_categoria"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        listaFiltrada = new FilteredList<>(listaBase, p -> true);
        SortedList<Categoria> listaSorted = new SortedList<>(listaFiltrada);
        listaSorted.comparatorProperty().bind(tablaCategorias.comparatorProperty());
        tablaCategorias.setItems(listaSorted);

        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> {
            listaFiltrada.setPredicate(cat -> {
                if (newVal == null || newVal.trim().isEmpty()) return true;
                String filtro = newVal.toLowerCase();
                return cat.getNombre().toLowerCase().contains(filtro)
                    || (cat.getDescripcion() != null && cat.getDescripcion().toLowerCase().contains(filtro));
            });
        });

        cargarCategorias();
        configurarValidaciones();

        tablaCategorias.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        txNombre.setText(newVal.getNombre());
                        txDescripcion.setText(newVal.getDescripcion());
                    }
                });
    }

    private void configurarValidaciones() {
        validationSupport = new ValidationSupport();
        Validator<String> requerido = (control, value) -> {
            if (value == null || value.trim().isEmpty()) {
                return ValidationResult.fromError(control, "Campo obligatorio");
            }
            return null;
        };
        validationSupport.registerValidator(txNombre, requerido);
    }

    @FXML
    void Crear(ActionEvent event) {
        if (validationSupport.isInvalid()) return;
        CategoriaDAO dao = new CategoriaDAO();
        dao.insertarCategoria(new Categoria(txNombre.getText(), txDescripcion.getText()));
        cargarCategorias();
        limpiarCampos();
    }

    @FXML
    void Actualizar(ActionEvent event) {
        if (validationSupport.isInvalid()) return;
        Categoria seleccionada = tablaCategorias.getSelectionModel().getSelectedItem();
        if (seleccionada == null) { mostrarAviso("Selecciona una categoría de la tabla para actualizarla"); return; }
        seleccionada.setNombre(txNombre.getText());
        seleccionada.setDescripcion(txDescripcion.getText());
        new CategoriaDAO().actualizarCategoria(seleccionada);
        cargarCategorias();
    }

    @FXML
    void Borrar(ActionEvent event) {
        Categoria seleccionada = tablaCategorias.getSelectionModel().getSelectedItem();
        if (seleccionada == null) { mostrarAviso("Selecciona una categoría de la tabla para borrarla"); return; }
        Alert confirmacion = new Alert(AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar borrado");
        confirmacion.setHeaderText("Borrar categoría '" + seleccionada.getNombre() + "'");
        confirmacion.setContentText("¿Estás seguro?");
        confirmacion.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                if (new CategoriaDAO().borrarCategoria(seleccionada.getId_categoria())) {
                    cargarCategorias();
                    limpiarCampos();
                } else {
                    new Alert(AlertType.ERROR, "No se pudo borrar la categoría").showAndWait();
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

    private void cargarCategorias() {
        listaBase.clear();
        listaBase.addAll(new CategoriaDAO().cargarCategoriasDesdeBD());
    }

    private void limpiarCampos() { txNombre.clear(); txDescripcion.clear(); }

    private void mostrarAviso(String mensaje) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Sin selección");
        alert.setHeaderText("Ninguna categoría seleccionada");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
