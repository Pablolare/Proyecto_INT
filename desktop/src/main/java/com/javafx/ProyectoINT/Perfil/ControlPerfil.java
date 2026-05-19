package com.javafx.ProyectoINT.Perfil;

import java.io.IOException;

import com.javafx.ProyectoINT.ApiCliente;
import com.javafx.ProyectoINT.InicioSesion.ControlInicioSesion;
import com.javafx.ProyectoINT.modelos.Usuario;
import com.javafx.ProyectoINT.modelos.UsuarioDAO;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControlPerfil {

    private ObservableList<Usuario> listaBase = FXCollections.observableArrayList();
    private FilteredList<Usuario> listaFiltrada;
    private Usuario usuarioActual;

    @FXML private TextField txtBuscar;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtRol;

    @FXML private TableView<Usuario> tablaPerfil;
    @FXML private TableColumn<Usuario, Integer> colId_usuario;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colApellido;
    @FXML private TableColumn<Usuario, String> colLogin;
    @FXML private TableColumn<Usuario, String> colContraseña;
    @FXML private TableColumn<Usuario, String> colRol;
    @FXML private TableColumn<Usuario, String> colCorreo;

    @FXML
    void initialize() {
        colId_usuario.setCellValueFactory(new PropertyValueFactory<>("id_usuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        colContraseña.setCellValueFactory(new PropertyValueFactory<>("contraseña"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));

        listaFiltrada = new FilteredList<>(listaBase, p -> true);
        SortedList<Usuario> listaSorted = new SortedList<>(listaFiltrada);
        listaSorted.comparatorProperty().bind(tablaPerfil.comparatorProperty());
        tablaPerfil.setItems(listaSorted);

        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> {
            listaFiltrada.setPredicate(u -> {
                if (newVal == null || newVal.trim().isEmpty()) return true;
                String f = newVal.toLowerCase();
                return u.getNombre().toLowerCase().contains(f)
                    || u.getApellido().toLowerCase().contains(f)
                    || u.getLogin().toLowerCase().contains(f)
                    || (u.getCorreo() != null && u.getCorreo().toLowerCase().contains(f));
            });
        });

        cargarUsuariosDesdeBD();
        cargarDatosUsuarioActual();
    }

    private void cargarDatosUsuarioActual() {
        int idUsuario = ControlInicioSesion.usuarioLogueadoId;
        if (idUsuario == -1) return;

        usuarioActual = ApiCliente.obtenerUsuarioPorId(idUsuario);
        if (usuarioActual != null) {
            txtNombre.setText(usuarioActual.getNombre());
            txtApellido.setText(usuarioActual.getApellido());
            txtCorreo.setText(usuarioActual.getCorreo());
            txtRol.setText(usuarioActual.getRol());
        }
    }

    @FXML
    void Aceptar(ActionEvent event) throws IOException {
        if (usuarioActual == null) return;

        Usuario usuarioActualizado = new Usuario(
                usuarioActual.getId_usuario(),
                txtNombre.getText(),
                txtApellido.getText(),
                usuarioActual.getLogin(),
                usuarioActual.getContraseña(),
                txtRol.getText(),
                txtCorreo.getText());

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        if (usuarioDAO.actualizarUsuario(usuarioActualizado)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText("Perfil actualizado");
            alert.setContentText("Tu perfil ha sido actualizado exitosamente");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al actualizar");
            alert.setContentText("No se pudo actualizar el perfil");
            alert.showAndWait();
        }

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

    private void cargarUsuariosDesdeBD() {
        listaBase.clear();
        listaBase.addAll(new UsuarioDAO().cargarUsuariosDesdeBD());
    }

    @FXML
    void BorrarCuenta(ActionEvent event) throws IOException {
        Usuario seleccionado = tablaPerfil.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("No hay selección");
            alert.setContentText("Por favor selecciona un usuario para borrar");
            alert.showAndWait();
            return;
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        if (usuarioDAO.borrarUsuario(seleccionado.getId_usuario())) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText("Usuario eliminado");
            alert.setContentText("El usuario ha sido eliminado exitosamente");
            alert.showAndWait();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/InicioSesion.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            boolean maximizado = stage.isMaximized();
            double w = stage.getWidth(), h = stage.getHeight();
            double x = stage.getX(), y = stage.getY();
            stage.setScene(new Scene(root));
            if (maximizado) { stage.setMaximized(true); } else { stage.setWidth(w); stage.setHeight(h); stage.setX(x); stage.setY(y); }
            stage.show();
        } else {
            new Alert(AlertType.ERROR, "No se pudo eliminar el usuario").showAndWait();
        }
    }

    @FXML
    void CerrarSesion(ActionEvent event) throws IOException {
        ControlInicioSesion.usuarioLogueadoId = -1;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/InicioSesion.fxml"));
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
    void VolverPaginaPrincipal(ActionEvent event) throws IOException {
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
}
