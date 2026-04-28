package com.javafx.ProyectoINT.CrearEjercicio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CrearEjercicio extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primeraEscena) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML_Proyecto/CrearEjercicio.fxml"));

        Scene scene = new Scene(root);
        primeraEscena.setScene(scene);
        primeraEscena.setTitle("LrVoley - Pagina crear ejercicio");
        primeraEscena.show();
        
        
    }
}
