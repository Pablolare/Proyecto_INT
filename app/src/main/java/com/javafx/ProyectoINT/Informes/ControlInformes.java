package com.javafx.ProyectoINT.Informes;

import java.io.File;
import java.io.IOException;
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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
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

    private Connection conexion;

    @FXML
    void initialize() {
        System.out.println("Informes");
        // Inicializar la conexión a la base de datos
        this.conexion = ConexionBD.getConnection();
    }

    @FXML
    void PagAtras(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/PaginaPrincipal.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    // Método para generar el informe de Usuario
    @FXML
    void buttonInforme1(ActionEvent event) {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("Parametro", "%" + mititulo.getText() + "%");
        lanzaInforme("/Informes/LrVolley-Usuario.jasper", parametros);
    }

    // Método para generar el informe de Gráfica
    @FXML
    void buttonInforme2(ActionEvent event) {
        Map<String, Object> parametros = new HashMap<>();
        lanzaInforme("/Informes/LrVolley-Grafica.jasper", parametros);
    }

    // Método para generar el informe de Entrenamientos
    @FXML
    void buttonInforme3(ActionEvent event) {
        Map<String, Object> parametros = new HashMap<>();
        lanzaInforme("/Informes/LrVolley-Entrenamientos.jasper", parametros);
    }

    // Efectos hover para el botón Atrás
    @FXML
    void onMouseEntered(MouseEvent event) {
        Button btn = (Button) event.getSource();
        btn.setStyle("-fx-background-color: #334155; -fx-text-fill: white; -fx-font-size: 24px; -fx-padding: 5 15; -fx-cursor: hand; -fx-border-color: #64748b; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8;");
    }

    @FXML
    void onMouseExited(MouseEvent event) {
        Button btn = (Button) event.getSource();
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 24px; -fx-padding: 5 15; -fx-cursor: hand; -fx-border-color: #475569; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8;");
    }

    // Efectos hover para los botones de informes
    @FXML
    void onMouseEnteredButton(MouseEvent event) {
        Button btn = (Button) event.getSource();
        if (btn == buttonInformeN) {
            btn.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 25; -fx-cursor: hand; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-weight: bold;");
        } else if (btn == buttonInformeG) {
            btn.setStyle("-fx-background-color: #7c3aed; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 25; -fx-cursor: hand; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-weight: bold;");
        } else if (btn == buttonInformeE) {
            btn.setStyle("-fx-background-color: #059669; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 25; -fx-cursor: hand; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-weight: bold;");
        }
    }

    @FXML
    void onMouseExitedButton(MouseEvent event) {
        Button btn = (Button) event.getSource();
        if (btn == buttonInformeN) {
            btn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 25; -fx-cursor: hand; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-weight: bold;");
        } else if (btn == buttonInformeG) {
            btn.setStyle("-fx-background-color: #8b5cf6; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 25; -fx-cursor: hand; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-weight: bold;");
        } else if (btn == buttonInformeE) {
            btn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 25; -fx-cursor: hand; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-weight: bold;");
        }
    }

    private void lanzaInforme(String rutaInf, Map<String, Object> param) {
        System.out.println("=== INICIO DEBUG ===");
        System.out.println("Directorio de trabajo actual: " + System.getProperty("user.dir"));
        System.out.println("¿Existe carpeta Informes?: " + new File("Informes").exists());
        System.out.println("Ruta absoluta esperada: " + new File("Informes").getAbsolutePath());
        System.out.println("Intentando cargar: " + rutaInf);
        
        try {
            // Verificar que el recurso existe
            java.io.InputStream stream = getClass().getResourceAsStream(rutaInf);
            System.out.println("Stream: " + (stream != null ? "ENCONTRADO" : "NULL"));
            
            if (stream == null) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Archivo no encontrado");
                alert.setContentText("No se pudo encontrar el archivo:\n" + rutaInf);
                alert.showAndWait();
                return;
            }
            
            // Intentar cargar el JasperReport
            JasperReport report = (JasperReport) JRLoader.loadObject(stream);
            System.out.println("JasperReport cargado: " + (report != null ? "OK" : "NULL"));
            
            try {
                System.out.println("Generando informe con conexión: " + this.conexion);
                JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, this.conexion);

                if (!jasperPrint.getPages().isEmpty()) {
                    
                    // Crear carpeta para guardar PDFs si no existe
                    File directorio = new File("Informes");
                    if (!directorio.exists()) {
                        directorio.mkdirs();
                        System.out.println("✓ Carpeta Informes creada para guardar PDFs");
                    }
                    
                    String nombreBase = rutaInf.substring(rutaInf.lastIndexOf('/') + 1, rutaInf.lastIndexOf('.'));
                    
                    String pdfOutputPath = "Informes" + File.separator + nombreBase + "informe.pdf";
                    JasperExportManager.exportReportToPdfFile(jasperPrint, pdfOutputPath);
                    System.out.println("PDF generado: " + pdfOutputPath);

                    String outputHtmlFile = "Informes" + File.separator + nombreBase + "informe.html";
                    JasperExportManager.exportReportToHtmlFile(jasperPrint, outputHtmlFile);
                    System.out.println("HTML generado: " + outputHtmlFile);

                    // Siempre mostrar incrustado
                    wv.getEngine().load(new File(outputHtmlFile).toURI().toString());
                    
                } else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Información");
                    alert.setHeaderText("Alerta de Informe");
                    alert.setContentText("El informe no generó páginas (sin resultados en la consulta)");
                    alert.showAndWait();
                }

            } catch (JRException e) {
                System.out.println("Error JasperReports al generar: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al generar el informe: " + e.getMessage());
            }
        } catch (JRException ex) {
            System.out.println("Error al cargar el informe: " + ex.getMessage());
            ex.printStackTrace();
            
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al cargar el archivo .jasper");
            alert.setContentText("Detalles: " + ex.getMessage() + "\n\n" +
                            "Es posible que el archivo .jasper esté corrupto o sea incompatible.\n" +
                            "Intenta recompilar el archivo .jrxml en JasperSoft Studio.");
            alert.showAndWait();
        }
        System.out.println("=== FIN DEBUG ===");
    }
}