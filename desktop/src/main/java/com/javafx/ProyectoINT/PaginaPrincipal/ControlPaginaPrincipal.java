package com.javafx.ProyectoINT.PaginaPrincipal;

import java.io.IOException;

import com.javafx.ProyectoINT.InicioSesion.ControlInicioSesion;
import com.javafx.ProyectoINT.SessionManager;
import com.javafx.ProyectoINT.modelos.Entrenamiento;
import com.javafx.ProyectoINT.modelos.EntrenamientoDAO;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ControlPaginaPrincipal {

    @FXML
    private Label LbUltimoEntreno;

    private ObservableList<Entrenamiento> listaEntrenamientos = FXCollections.observableArrayList();
    @FXML
    private TableView<Entrenamiento> TablaUltimoEntreno;
    @FXML
    private TableColumn<Entrenamiento, String> colNombreEntreno;
    @FXML
    private TableColumn<Entrenamiento, Integer> colNumEjercicios;
    @FXML
    private TableColumn<Entrenamiento, String> colDescripcion;

    @FXML
    private Button btnAñadirEntreno;

    @FXML
    private Button btnEntrenos;

    @FXML
    private Button btnHistorial;

    @FXML
    private Button btnInformes;

    @FXML
    private Button btnObjetivos;

    @FXML
    private Button btnGestionEjercicios;

    @FXML
    void initialize() {
        colNombreEntreno.setCellValueFactory(new PropertyValueFactory<>("nombre_entreno"));
        colNumEjercicios.setCellValueFactory(new PropertyValueFactory<>("numEjercicios"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        TablaUltimoEntreno.setItems(listaEntrenamientos);
        cargarEjerciciosDesdeBD();

        boolean esAdmin = SessionManager.esAdmin();
        btnGestionEjercicios.setVisible(esAdmin);
        btnGestionEjercicios.setManaged(esAdmin);

        Timeline autoRefresh = new Timeline(new KeyFrame(Duration.seconds(10), e -> cargarEjerciciosDesdeBD()));
        autoRefresh.setCycleCount(Timeline.INDEFINITE);
        autoRefresh.play();
    }

    @FXML
    void PagAñadirEntreno(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/PagAñadirEntreno.fxml"));
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
    void PagEntrenos(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/Entrenos.fxml"));
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
    void PagHistorial(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/Historial.fxml"));
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
    void PagInformes(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/Informes.fxml"));
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
    void PagObjetivos(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/Objetivos.fxml"));
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
    void Perfil(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/Perfil.fxml"));
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
    void PagGestionEjercicios(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/CrearEjercicio.fxml"));
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
        EntrenamientoDAO dao = new EntrenamientoDAO();
        listaEntrenamientos.clear();
        listaEntrenamientos.addAll(dao.obtenerEntrenosAgrupadosUsuario(ControlInicioSesion.usuarioLogueadoId));
    }
}
