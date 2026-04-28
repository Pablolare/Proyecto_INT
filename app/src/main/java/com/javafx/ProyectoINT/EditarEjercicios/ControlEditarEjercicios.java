package com.javafx.ProyectoINT.EditarEjercicios;

import java.io.IOException;
import java.util.List;

import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import com.javafx.ProyectoINT.ListarEjercicios.ControlListarEjercicios;
import com.javafx.ProyectoINT.modelos.Categoria;
import com.javafx.ProyectoINT.modelos.CategoriaDAO;
import com.javafx.ProyectoINT.modelos.Ejercicios;
import com.javafx.ProyectoINT.modelos.EjerciciosDAO;

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
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControlEditarEjercicios {

    @FXML private TextField txFinalidad;
    @FXML private TextField txNombre;
    @FXML private TextField txTipo;
    @FXML private TextField txtBuscar;
    @FXML private ListView<Categoria> listaCategorias;

    private ObservableList<Ejercicios> listaBase = FXCollections.observableArrayList();
    private FilteredList<Ejercicios> listaFiltrada;
    @FXML private TableView<Ejercicios> tablaEditarEjercicios;
    @FXML private TableColumn<Ejercicios, Integer> colId;
    @FXML private TableColumn<Ejercicios, String> colNombreEjer;
    @FXML private TableColumn<Ejercicios, String> colTipo;
    @FXML private TableColumn<Ejercicios, String> colFinalidad;

    private ValidationSupport validationSupport;

    @FXML
    void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_ejer"));
        colNombreEjer.setCellValueFactory(new PropertyValueFactory<>("nombre_ejer"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colFinalidad.setCellValueFactory(new PropertyValueFactory<>("finalidad"));

        listaFiltrada = new FilteredList<>(listaBase, p -> true);
        SortedList<Ejercicios> listaSorted = new SortedList<>(listaFiltrada);
        listaSorted.comparatorProperty().bind(tablaEditarEjercicios.comparatorProperty());
        tablaEditarEjercicios.setItems(listaSorted);

        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> {
            listaFiltrada.setPredicate(ej -> {
                if (newVal == null || newVal.trim().isEmpty()) return true;
                String f = newVal.toLowerCase();
                return ej.getNombre_ejer().toLowerCase().contains(f)
                    || ej.getTipo().toLowerCase().contains(f)
                    || (ej.getFinalidad() != null && ej.getFinalidad().toLowerCase().contains(f));
            });
        });

        cargarEjerciciosDesdeBD();
        cargarCategorias();
        configurarValidaciones();

        tablaEditarEjercicios.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        txNombre.setText(newSelection.getNombre_ejer());
                        txTipo.setText(newSelection.getTipo());
                        txFinalidad.setText(newSelection.getFinalidad());
                        cargarCategoriasDeEjercicio(newSelection.getId_ejer());
                    }
                });

        if (ControlListarEjercicios.ejercicioSeleccionadoId >= 0) {
            for (Ejercicios ej : listaBase) {
                if (ej.getId_ejer() == ControlListarEjercicios.ejercicioSeleccionadoId) {
                    tablaEditarEjercicios.getSelectionModel().select(ej);
                    break;
                }
            }
            ControlListarEjercicios.ejercicioSeleccionadoId = -1;
        }
    }

    private void cargarCategorias() {
        CategoriaDAO dao = new CategoriaDAO();
        listaCategorias.setItems(dao.cargarCategoriasDesdeBD());
        listaCategorias.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void cargarCategoriasDeEjercicio(int idEjer) {
        CategoriaDAO dao = new CategoriaDAO();
        ObservableList<Categoria> asignadas = dao.obtenerCategoriasPorEjercicio(idEjer);
        listaCategorias.getSelectionModel().clearSelection();
        for (int i = 0; i < listaCategorias.getItems().size(); i++) {
            Categoria cat = listaCategorias.getItems().get(i);
            for (Categoria asignada : asignadas) {
                if (cat.getId_categoria() == asignada.getId_categoria()) {
                    listaCategorias.getSelectionModel().select(i);
                    break;
                }
            }
        }
    }

    private void configurarValidaciones() {
        validationSupport = new ValidationSupport();

        Validator<String> nombreValidator = (control, value) -> {
            if (value == null || value.trim().isEmpty()) {
                return ValidationResult.fromError(control, "El nombre es obligatorio");
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

        validationSupport.registerValidator(txNombre, nombreValidator);
        validationSupport.registerValidator(txTipo, tipoValidator);
        validationSupport.registerValidator(txFinalidad, finalidadValidator);
    }

    @FXML
    void Actualizar(ActionEvent event) {
        if (validationSupport.isInvalid()) return;

        Ejercicios seleccionado = tablaEditarEjercicios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Sin selección");
            alert.setHeaderText("Ningún ejercicio seleccionado");
            alert.setContentText("Selecciona un ejercicio de la tabla para actualizarlo");
            alert.showAndWait();
            return;
        }
        seleccionado.setNombre_ejer(txNombre.getText());
        seleccionado.setTipo(txTipo.getText());
        seleccionado.setFinalidad(txFinalidad.getText());

        EjerciciosDAO dao = new EjerciciosDAO();
        dao.actualizarEjercicio(seleccionado);

        List<Categoria> categoriasSeleccionadas = listaCategorias.getSelectionModel().getSelectedItems();
        CategoriaDAO catDao = new CategoriaDAO();
        catDao.asignarCategoriasAEjercicio(seleccionado.getId_ejer(), categoriasSeleccionadas);

        cargarEjerciciosDesdeBD();
    }

    @FXML
    void Borrar(ActionEvent event) {
        Ejercicios seleccionado = tablaEditarEjercicios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Sin selección");
            alert.setHeaderText("Ningún ejercicio seleccionado");
            alert.setContentText("Selecciona un ejercicio de la tabla para borrarlo");
            alert.showAndWait();
            return;
        }

        Alert confirmacion = new Alert(AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar borrado");
        confirmacion.setHeaderText("Borrar ejercicio '" + seleccionado.getNombre_ejer() + "'");
        confirmacion.setContentText("¿Estás seguro de que quieres borrar este ejercicio?");

        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                EjerciciosDAO dao = new EjerciciosDAO();
                if (dao.borrarEjercicio(seleccionado.getId_ejer())) {
                    cargarEjerciciosDesdeBD();
                    txNombre.clear();
                    txTipo.clear();
                    txFinalidad.clear();
                    listaCategorias.getSelectionModel().clearSelection();
                } else {
                    Alert error = new Alert(AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText("Error al borrar");
                    error.setContentText("No se pudo borrar el ejercicio");
                    error.showAndWait();
                }
            }
        });
    }

    @FXML
    void Aceptar(ActionEvent event) throws IOException {
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

    private void cargarEjerciciosDesdeBD() {
        EjerciciosDAO dao = new EjerciciosDAO();
        listaBase.clear();
        listaBase.addAll(dao.cargarEjerciciosDesdeBD());
    }

    @FXML
    void PagAtras(ActionEvent event) throws IOException {
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
}
