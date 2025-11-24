package com.javafx.ProyectoINT.Perfil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.javafx.ProyectoINT.ConexionBD;
import com.javafx.ProyectoINT.modelos.Usuario;

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

public class ControlPerfil{
    private ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();
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
        cargarEjerciciosDesdeBD();
    }

    @FXML
    void Aceptar(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/PaginaPrincipal.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void cargarEjerciciosDesdeBD() {
        try {
            Connection conexion = ConexionBD.getInstancia().getConexion();
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
                    resultado.getString("correo")
                );
                listaUsuarios.add(usuario);
            }
            
            resultado.close();
            statment.close();
            
        } catch (Exception e) {
            System.out.println("Error al cargar ejercicios: " + e.getMessage());
        }
    }

    @FXML
    void BorrarCuenta(ActionEvent event) {

    }

    @FXML
    void CerrarSesion(ActionEvent event) {

    }
}






