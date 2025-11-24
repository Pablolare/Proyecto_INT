package com.javafx.ProyectoINT.EditarEjercicios;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.javafx.ProyectoINT.ConexionBD;
import com.javafx.ProyectoINT.modelos.Ejercicios;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControlEditarEjercicios {
    @FXML
    private TextArea txFinalidad;

    @FXML
    private TextField txNombre;

    @FXML
    private TextField txTipo;

    private ObservableList<Ejercicios> listaEjercicios = FXCollections.observableArrayList();
    @FXML
    private TableView<Ejercicios> tablaEditarEjercicios;
    @FXML
    private TableColumn<Ejercicios, String> colNombreEjer;
    @FXML
    private TableColumn<Ejercicios, String> colTipo;
    @FXML
    private TableColumn<Ejercicios, String> colFinalidad;

    @FXML
    void initialize() {
        colNombreEjer.setCellValueFactory(new PropertyValueFactory<>("nombre_ejer"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colFinalidad.setCellValueFactory(new PropertyValueFactory<>("finalidad"));
        tablaEditarEjercicios.setItems(listaEjercicios);
        cargarEjerciciosDesdeBD();
    }

    @FXML
    void Aceptar(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/EditarEntreno.fxml"));
        Parent root = loader.load();    
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    private void cargarEjerciciosDesdeBD() {
        try {
            Connection conexion = ConexionBD.getInstancia().getConexion();
            String consulta = "SELECT * FROM Ejercicio";
            PreparedStatement statment = conexion.prepareStatement(consulta);
            ResultSet resultado = statment.executeQuery();
            
            listaEjercicios.clear();
            
            while (resultado.next()) {
                Ejercicios ejercicio = new Ejercicios(
                    resultado.getInt("id_ejer"),
                    resultado.getString("nombre_ejer"),
                    resultado.getString("tipo"),
                    resultado.getString("finalidad")
                );
                listaEjercicios.add(ejercicio);
            }
            
            resultado.close();
            statment.close();
            
        } catch (Exception e) {
            System.out.println("Error al cargar ejercicios: " + e.getMessage());
        }
    }
}
