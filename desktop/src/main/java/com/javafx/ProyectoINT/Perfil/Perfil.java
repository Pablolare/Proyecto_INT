package com.javafx.ProyectoINT.Perfil;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Perfil extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primeraEscena) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/FXML_Proyecto/Perfil.fxml"));
        Scene scene = new Scene(root);
        primeraEscena.setScene(scene);
        primeraEscena.setTitle("LrVoley - Pagina perfil");
        primeraEscena.show();
        
        
    }   
}
