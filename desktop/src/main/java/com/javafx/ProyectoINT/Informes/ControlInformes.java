package com.javafx.ProyectoINT.Informes;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import com.javafx.ProyectoINT.ConexionBD;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

public class ControlInformes {

    @FXML
    private WebView wv;

    @FXML
    private TextField mititulo;

    @FXML
    private Button buttonInformeN;

    @FXML
    private Button buttonInformeG;

    @FXML
    private Button buttonInformeE;

    @FXML
    private CheckBox checkCombo;

    private Connection conexion;

    @FXML
    void initialize() {
        System.out.println("Informes");
        this.conexion = ConexionBD.getInstancia().getConexion();

        checkCombo.selectedProperty().addListener((observable, valorAnt, valorAct) -> {
            mititulo.setDisable(valorAct);
        });
        mititulo.setDisable(true);
    }

    @FXML
    void PagAtras(ActionEvent event) throws IOException {
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

    @FXML
    void buttonInforme1(ActionEvent event) {
        Map<String, Object> parametros = new HashMap<>();
        if (checkCombo.isSelected()) {
            lanzaInforme("/Informes/LrVolley-Usuario.jasper", parametros, 0);
        } else {
            parametros.put("Parametro", "%" + mititulo.getText() + "%");
            lanzaInforme("/Informes/LrVolley-Usuario.jasper", parametros, 1);
        }
    }

    @FXML
    void buttonInforme2(ActionEvent event) {
        Map<String, Object> parametros = new HashMap<>();
        if (checkCombo.isSelected()) {
            lanzaInforme("/Informes/LrVolley-Grafica.jrxml", parametros, 0);
        } else {
            lanzaInforme("/Informes/LrVolley-Grafica.jrxml", parametros, 1);
        }
    }

    @FXML
    void buttonInforme3(ActionEvent event) {
        Map<String, Object> parametros = new HashMap<>();
        if (checkCombo.isSelected()) {
            lanzaInforme("/Informes/LrVolley-Entrenamientos.jasper", parametros, 0);
        } else {
            parametros.put("Parametro", "%" + mititulo.getText() + "%");
            lanzaInforme("/Informes/LrVolley-Entrenamientos.jasper", parametros, 1);
        }
    }

    @FXML
    void onMouseEntered(MouseEvent event) {
        Button btn = (Button) event.getSource();
        btn.setStyle(
                "-fx-background-color: #334155; -fx-text-fill: white; -fx-font-size: 24px; -fx-padding: 5 15; -fx-cursor: hand; -fx-border-color: #64748b; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8;");
    }

    @FXML
    void onMouseExited(MouseEvent event) {
        Button btn = (Button) event.getSource();
        btn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 24px; -fx-padding: 5 15; -fx-cursor: hand; -fx-border-color: #475569; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8;");
    }

    @FXML
    void onMouseEnteredButton(MouseEvent event) {
        Button btn = (Button) event.getSource();
        if (btn == buttonInformeN) {
            btn.setStyle(
                    "-fx-background-color: #2563eb; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 25; -fx-cursor: hand; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-weight: bold;");
        } else if (btn == buttonInformeG) {
            btn.setStyle(
                    "-fx-background-color: #7c3aed; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 25; -fx-cursor: hand; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-weight: bold;");
        } else if (btn == buttonInformeE) {
            btn.setStyle(
                    "-fx-background-color: #059669; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 25; -fx-cursor: hand; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-weight: bold;");
        }
    }

    @FXML
    void onMouseExitedButton(MouseEvent event) {
        Button btn = (Button) event.getSource();
        if (btn == buttonInformeN) {
            btn.setStyle(
                    "-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 25; -fx-cursor: hand; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-weight: bold;");
        } else if (btn == buttonInformeG) {
            btn.setStyle(
                    "-fx-background-color: #8b5cf6; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 25; -fx-cursor: hand; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-weight: bold;");
        } else if (btn == buttonInformeE) {
            btn.setStyle(
                    "-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 25; -fx-cursor: hand; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-weight: bold;");
        }
    }

    private String getAppBasePath() {
        try {
            // Obtener la ubicación del JAR o del directorio de clases
            java.net.URL location = getClass().getProtectionDomain().getCodeSource().getLocation();
            File file = new File(location.toURI());

            // Si es un JAR, devolver el directorio padre
            if (file.isFile()) {
                return file.getParentFile().getAbsolutePath();
            }
            // Si es un directorio (en desarrollo), devolver el directorio del proyecto
            return file.getAbsolutePath();
        } catch (Exception e) {
            // Fallback: usar el directorio de trabajo actual
            return System.getProperty("user.dir");
        }
    }

    private void lanzaInforme(String rutaInf, Map<String, Object> param, int tipo) {
        try {
            JasperReport report;
            if (rutaInf.endsWith(".jrxml")) {
                report = JasperCompileManager.compileReport(getClass().getResourceAsStream(rutaInf));
            } else {
                report = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream(rutaInf));
            }
            try {
                JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, this.conexion);

                if (!jasperPrint.getPages().isEmpty()) {
                    String basePath = getAppBasePath();
                    System.out.println("Base path: " + basePath);

                    File informesDir = new File(basePath, "Informes");
                    System.out.println("Informes dir: " + informesDir.getAbsolutePath());

                    if (!informesDir.exists()) {
                        boolean created = informesDir.mkdirs();
                        System.out.println("Carpeta creada: " + created);
                        if (!created) {
                            // Si no se puede crear en basePath, usar directorio temporal del usuario
                            informesDir = new File(System.getProperty("user.home"), "LrVolley_Informes");
                            informesDir.mkdirs();
                            System.out.println("Usando carpeta alternativa: " + informesDir.getAbsolutePath());
                        }
                    }

                    String nombreBase = rutaInf.substring(rutaInf.lastIndexOf('/') + 1, rutaInf.lastIndexOf('.')) + "informe";

                    String pdfOutputPath = new File(informesDir, nombreBase + ".pdf").getAbsolutePath();
                    JasperExportManager.exportReportToPdfFile(jasperPrint, pdfOutputPath);
                    System.out.println("PDF generado: " + pdfOutputPath);

                    String outputHtmlFile = new File(informesDir, nombreBase + ".html").getAbsolutePath();
                    JasperExportManager.exportReportToHtmlFile(jasperPrint, outputHtmlFile);
                    System.out.println("HTML generado: " + outputHtmlFile);

                    if (tipo == 0) {
                        wv.getEngine().load(new File(outputHtmlFile).toURI().toString());
                    } else {
                        WebView wvnuevo = new WebView();
                        wvnuevo.getEngine().load(new File(outputHtmlFile).toURI().toString());
                        StackPane stackPane = new StackPane(wvnuevo);
                        Scene scene = new Scene(stackPane, 900, 600);
                        Stage stage = new Stage();
                        stage.setTitle("Informe en HTML");
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setResizable(true);
                        stage.setScene(scene);
                        stage.show();
                    }
                } else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Información");
                    alert.setHeaderText("Alerta de Informe");
                    alert.setContentText("La búsqueda " + mititulo.getText() + " no generó páginas");
                    alert.showAndWait();
                }

            } catch (JRException e) {
                System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error al generar el informe: " + e.getMessage());
            }
        } catch (JRException ex) {
            System.out.println(ex.getMessage());
        }
    }
}