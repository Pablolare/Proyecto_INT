package com.javafx.ProyectoINT.PaginaPrincipal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PaginaPrincipal extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primeraEscena) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/FXML_Proyecto/PaginaPrincipal.fxml"));

        Scene scene = new Scene(root);
        primeraEscena.setScene(scene);
        primeraEscena.setTitle("LrVoley - Pagina principal");
        primeraEscena.show();
        
        
    }
}