package com.javafx.ProyectoINT.Perfil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.javafx.ProyectoINT.ConexionBD;
import com.javafx.ProyectoINT.InicioSesion.ControlInicioSesion;
import com.javafx.ProyectoINT.modelos.Usuario;
import com.javafx.ProyectoINT.modelos.UsuarioDAO;

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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControlPerfil {
    private ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtRol;

    @FXML
    private TableView<Usuario> tablaPerfil;
    @FXML
    private TableColumn<Usuario, Integer> colId_usuario;
    @FXML
    private TableColumn<Usuario, String> colNombre;
    @FXML
    private TableColumn<Usuario, String> colApellido;
    @FXML
    private TableColumn<Usuario, String> colLogin;
    @FXML
    private TableColumn<Usuario, String> colContraseña;
    @FXML
    private TableColumn<Usuario, String> colRol;
    @FXML
    private TableColumn<Usuario, String> colCorreo;

    @FXML
    void initialize() {
        colId_usuario.setCellValueFactory(new PropertyValueFactory<>("id_usuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        colContraseña.setCellValueFactory(new PropertyValueFactory<>("contraseña"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        tablaPerfil.setItems(listaUsuarios);
        cargarUsuariosDesdeBD();

        // Auto-rellenar campos con el usuario actual buscando en la base de datos
        cargarDatosUsuarioActual();
    }

    private void cargarDatosUsuarioActual() {
        int idUsuario = ControlInicioSesion.usuarioLogueadoId;

        if (idUsuario == -1) {
            System.out.println("No hay usuario logueado");
            return;
        }

        // Buscar el usuario en la base de datos
        try {
            Connection conexion = ConexionBD.getConnection();
            String consulta = "SELECT * FROM Usuario WHERE id_usuario = ?";
            PreparedStatement statement = conexion.prepareStatement(consulta);
            statement.setInt(1, idUsuario);
            ResultSet resultado = statement.executeQuery();

            if (resultado.next()) {
                txtNombre.setText(resultado.getString("nombre"));
                txtApellido.setText(resultado.getString("apellido"));
                txtCorreo.setText(resultado.getString("correo"));
                txtRol.setText(resultado.getString("rol"));
            }

            resultado.close();
            statement.close();
        } catch (Exception e) {
            System.out.println("Error al cargar datos del usuario: " + e.getMessage());
        }
    }

    @FXML
    void Aceptar(ActionEvent event) throws IOException {
        int idUsuario = ControlInicioSesion.usuarioLogueadoId;

        if (idUsuario == -1) {
            System.out.println("No hay usuario en sesión");
            return;
        }

        // Crear objeto Usuario con los datos actualizados
        // Primero necesitamos obtener el login y contraseña actuales de la BD
        try {
            Connection conexion = ConexionBD.getConnection();
            String consulta = "SELECT login, contraseña FROM Usuario WHERE id_usuario = ?";
            PreparedStatement statement = conexion.prepareStatement(consulta);
            statement.setInt(1, idUsuario);
            ResultSet resultado = statement.executeQuery();

            if (resultado.next()) {
                String login = resultado.getString("login");
                String contraseña = resultado.getString("contraseña");

                // Crear usuario con los datos de los campos
                Usuario usuarioActualizado = new Usuario(
                        idUsuario,
                        txtNombre.getText(),
                        txtApellido.getText(),
                        login,
                        contraseña,
                        txtRol.getText(),
                        txtCorreo.getText());

                // Guardar cambios en la base de datos
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                if (usuarioDAO.actualizarUsuario(usuarioActualizado)) {
                    System.out.println("Perfil actualizado exitosamente");
                } else {
                    System.out.println("Error al actualizar el perfil");
                }
            }

            resultado.close();
            statement.close();
        } catch (Exception e) {
            System.out.println("Error al actualizar perfil: " + e.getMessage());
        }

        // Volver a la página principal
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/PaginaPrincipal.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void cargarUsuariosDesdeBD() {
        try {
            Connection conexion = ConexionBD.getConnection();
            String consulta = "SELECT * FROM Usuario";
            PreparedStatement statment = conexion.prepareStatement(consulta);
            ResultSet resultado = statment.executeQuery();

            listaUsuarios.clear();

            while (resultado.next()) {
                Usuario usuario = new Usuario(
                        resultado.getInt("id_usuario"),
                        resultado.getString("nombre"),
                        resultado.getString("apellido"),
                        resultado.getString("login"),
                        resultado.getString("contraseña"),
                        resultado.getString("rol"),
                        resultado.getString("correo"));
                listaUsuarios.add(usuario);
            }

            resultado.close();
            statment.close();

        } catch (Exception e) {
            System.out.println("Error al cargar usuarios: " + e.getMessage());
        }
    }

    @FXML
    void BorrarCuenta(ActionEvent event) throws IOException {
        Usuario seleccionado = tablaPerfil.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            System.out.println("Por favor selecciona un usuario para borrar");
            return;
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        if (usuarioDAO.borrarUsuario(seleccionado.getId_usuario())) {
            System.out.println("Usuario eliminado exitosamente");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/InicioSesion.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("No se pudo eliminar el usuario");
        }
    }

    @FXML
    void CerrarSesion(ActionEvent event) throws IOException {
        System.out.println("Cerrando sesión...");
        // Resetear el ID del usuario logueado
        ControlInicioSesion.usuarioLogueadoId = -1;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/InicioSesion.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void VolverPaginaPrincipal(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/PaginaPrincipal.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
