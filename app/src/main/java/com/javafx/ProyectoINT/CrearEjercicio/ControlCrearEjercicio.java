package com.javafx.ProyectoINT.CrearEjercicio;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControlCrearEjercicio {
        
    @FXML
    private Button btnCrear;

    @FXML
    private TextField txtFinalidad;

    @FXML
    private TextField txtNombreEjercicio;

    @FXML
    private TextField txtTipo;

    @FXML
    void Creacion(ActionEvent event) {

    }

    @FXML
    void PagAtras(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/AgregarEjercicio.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
