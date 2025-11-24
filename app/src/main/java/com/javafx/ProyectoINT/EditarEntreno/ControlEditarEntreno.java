package com.javafx.ProyectoINT.EditarEntreno;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.javafx.ProyectoINT.ConexionBD;
import com.javafx.ProyectoINT.modelos.Entrenamiento;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControlEditarEntreno {
    
    @FXML
    private TextField Aciertos;

    @FXML
    private CheckBox Completado;

    @FXML
    private TextField Fallos;

    @FXML
    private TextField Nombre;

    @FXML
    private TextField Repeticiones;

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
    void Aceptar(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/Entrenos.fxml"));
        Parent root = loader.load();    
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void Actualizar(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/EditarEntreno.fxml"));
        Parent root = loader.load();    
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void EditarEjercicios(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/EditarEjercicios.fxml"));
        Parent root = loader.load();    
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void cargarEjerciciosDesdeBD() {
        try {
            Connection conexion = ConexionBD.getInstancia().getConexion();
            String consulta = "SELECT * FROM Entrenamientos";
            PreparedStatement statment = conexion.prepareStatement(consulta);
            ResultSet resultado = statment.executeQuery();
            
            listaEntrenamientos.clear();
            
            while (resultado.next()) {
                Entrenamiento entrenamiento = new Entrenamiento(
                    resultado.getInt("id_entreno"),
                    resultado.getInt("id_usuario"),
                    resultado.getInt("id_ejer"),
                    resultado.getString("nombre_entreno"),
                    resultado.getInt("repeticiones"),
                    resultado.getInt("fallos"),
                    resultado.getInt("aciertos"),
                    resultado.getBoolean("completado")
                );
                listaEntrenamientos.add(entrenamiento);
            }
            
            resultado.close();
            statment.close();
            
        } catch (Exception e) {
            System.out.println("Error al cargar ejercicios: " + e.getMessage());
        }
    }
}
