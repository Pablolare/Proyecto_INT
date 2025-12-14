package com.javafx.ProyectoINT.Historial;

import java.io.IOException;
import java.util.List;

import com.javafx.ProyectoINT.modelos.Entrenamiento;
import com.javafx.ProyectoINT.modelos.EntrenamientoDAO;
import com.javafx.ProyectoINT.modelos.EntrenamientoDAO.DatosProgresion;
import com.javafx.ProyectoINT.modelos.EntrenamientoDAO.EstadisticasUsuario;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControlHistorial {
    
    // Gráfico
    @FXML
    private LineChart<String, Number> lineChart;
    
    @FXML
    private CategoryAxis xAxis;
    
    @FXML
    private NumberAxis yAxis;
    
    // Labels de estadísticas
    @FXML
    private Label lblTotalEntrenos;
    
    @FXML
    private Label lblTotalAciertos;
    
    @FXML
    private Label lblTotalFallos;
    
    @FXML
    private Label lblPorcentaje;
    
    // Tabla
    @FXML
    private TableView<Entrenamiento> tablaHistorial;
    
    @FXML
    private TableColumn<Entrenamiento, Integer> colId;
    
    @FXML
    private TableColumn<Entrenamiento, Integer> colIdUsuario;
    
    @FXML
    private TableColumn<Entrenamiento, String> colNombre;
    
    @FXML
    private TableColumn<Entrenamiento, Integer> colRepeticiones;
    
    @FXML
    private TableColumn<Entrenamiento, Integer> colAciertos;
    
    @FXML
    private TableColumn<Entrenamiento, Integer> colFallos;
    
    @FXML
    private TableColumn<Entrenamiento, Boolean> colCompletado;
    
    private int idUsuarioActual;
    private EntrenamientoDAO dao;
    
    @FXML
void initialize() {
    dao = new EntrenamientoDAO();
    
    // Configurar columnas
    colId.setCellValueFactory(new PropertyValueFactory<>("id_entreno"));
    colIdUsuario.setCellValueFactory(new PropertyValueFactory<>("id_usuario"));
    colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre_entreno"));
    colRepeticiones.setCellValueFactory(new PropertyValueFactory<>("repeticiones"));
    colAciertos.setCellValueFactory(new PropertyValueFactory<>("aciertos"));
    colFallos.setCellValueFactory(new PropertyValueFactory<>("fallos"));
    colCompletado.setCellValueFactory(new PropertyValueFactory<>("completado"));
    
    // FORZAR estilo en CADA CELDA (esto lo soluciona 100%)
    tablaHistorial.setStyle("-fx-background-color: #1e293b;");
    
    colId.setStyle("-fx-text-fill: white; -fx-alignment: CENTER;");
    colIdUsuario.setStyle("-fx-text-fill: white; -fx-alignment: CENTER;");
    colNombre.setStyle("-fx-text-fill: white;");
    colRepeticiones.setStyle("-fx-text-fill: white; -fx-alignment: CENTER;");
    colAciertos.setStyle("-fx-text-fill: #4CAF50; -fx-alignment: CENTER;");
    colFallos.setStyle("-fx-text-fill: #f44336; -fx-alignment: CENTER;");
    colCompletado.setStyle("-fx-text-fill: white; -fx-alignment: CENTER;");
    
    // Aplicar rowFactory para forzar el estilo en cada fila
    tablaHistorial.setRowFactory(tv -> {
        javafx.scene.control.TableRow<Entrenamiento> row = new javafx.scene.control.TableRow<>();
        row.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        return row;
    });
    
    // Configuración del gráfico
    lineChart.setCreateSymbols(true);
    lineChart.setLegendVisible(true);
    
    idUsuarioActual = 1;
    cargarDatosCompletos();
}
    
    /**
     * Método público para establecer el usuario desde otra vista
     * Llama a este método desde la vista de login/menu para pasar el usuario correcto
     */
    public void setUsuario(int idUsuario) {
        this.idUsuarioActual = idUsuario;
        cargarDatosCompletos();
    }
    
    /**
     * Carga todos los datos: gráfico, estadísticas y tabla
     */
    private void cargarDatosCompletos() {
        cargarGraficoProgresion();
        cargarEstadisticas();
        cargarTablaHistorial();
    }
    
    /**
     * Carga el gráfico de progresión
     */
    private void cargarGraficoProgresion() {
        // Limpiar datos anteriores
        lineChart.getData().clear();
        
        // Obtener datos de progresión
        List<DatosProgresion> progresion = dao.obtenerProgresionUsuario(idUsuarioActual);
        
        if (progresion.isEmpty()) {
            System.out.println("No hay datos de entrenamiento para este usuario");
            return;
        }
        
        // Crear series para el gráfico
        XYChart.Series<String, Number> serieAciertos = new XYChart.Series<>();
        serieAciertos.setName("Aciertos");
        
        XYChart.Series<String, Number> serieFallos = new XYChart.Series<>();
        serieFallos.setName("Fallos");
        
        // Llenar las series con datos
        for (DatosProgresion dato : progresion) {
            String etiqueta = "E" + dato.getNumeroEntreno();
            serieAciertos.getData().add(new XYChart.Data<>(etiqueta, dato.getAciertos()));
            serieFallos.getData().add(new XYChart.Data<>(etiqueta, dato.getFallos()));
        }
        
        // Añadir las series al gráfico
        lineChart.getData().addAll(serieAciertos, serieFallos);
        
        // Aplicar estilos personalizados
        aplicarEstilosGrafico();
    }
    
    /**
     * Aplica estilos CSS a las líneas del gráfico
     */
    private void aplicarEstilosGrafico() {
        // Esperar un frame para que el gráfico se renderice
        javafx.application.Platform.runLater(() -> {
            try {
                // La primera serie (Aciertos) en verde
                if (!lineChart.getData().isEmpty() && lineChart.getData().get(0).getNode() != null) {
                    lineChart.getData().get(0).getNode().setStyle(
                        "-fx-stroke: #4CAF50; -fx-stroke-width: 3px;"
                    );
                }
                
                // La segunda serie (Fallos) en rojo
                if (lineChart.getData().size() > 1 && lineChart.getData().get(1).getNode() != null) {
                    lineChart.getData().get(1).getNode().setStyle(
                        "-fx-stroke: #f44336; -fx-stroke-width: 3px;"
                    );
                }
            } catch (Exception e) {
                System.err.println("Error aplicando estilos al gráfico: " + e.getMessage());
            }
        });
    }
    
    /**
     * Carga las estadísticas generales
     */
    private void cargarEstadisticas() {
        EstadisticasUsuario stats = dao.obtenerEstadisticas(idUsuarioActual);
        
        if (stats != null) {
            lblTotalEntrenos.setText(String.valueOf(stats.getTotalEntrenos()));
            lblTotalAciertos.setText(String.valueOf(stats.getTotalAciertos()));
            lblTotalFallos.setText(String.valueOf(stats.getTotalFallos()));
            lblPorcentaje.setText(String.format("%.1f%%", stats.getPorcentajeAciertos()));
        } else {
            lblTotalEntrenos.setText("0");
            lblTotalAciertos.setText("0");
            lblTotalFallos.setText("0");
            lblPorcentaje.setText("0%");
        }
    }
    
    /**
     * Carga la tabla con el historial completo de entrenamientos
     */
    private void cargarTablaHistorial() {
         System.out.println("========== CARGANDO TABLA ==========");
    ObservableList<Entrenamiento> entrenamientos = dao.obtenerEntrenamientosUsuario(idUsuarioActual);
    System.out.println("📊 Total entrenamientos obtenidos: " + entrenamientos.size());
    
    // Imprimir cada entrenamiento
    for (Entrenamiento e : entrenamientos) {
        System.out.println("  ID: " + e.getId_entreno() + 
                          " | Usuario: " + e.getId_usuario() + 
                          " | Nombre: " + e.getNombre_entreno() + 
                          " | Reps: " + e.getRepeticiones() +
                          " | Aciertos: " + e.getAciertos() + 
                          " | Fallos: " + e.getFallos() +
                          " | Completado: " + e.isCompletado());
    }
    
    tablaHistorial.setItems(entrenamientos);
    tablaHistorial.refresh();
    System.out.println("✅ Items asignados a la tabla: " + tablaHistorial.getItems().size());
    }
    
    /**
     * Actualiza todos los datos del gráfico y la tabla
     */
    @FXML
    void actualizarGrafico(ActionEvent event) {
        cargarDatosCompletos();
        System.out.println("✅ Datos actualizados correctamente");
    }
    
    /**
     * Volver a la página anterior
     */
    @FXML
    void PagAtras(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Proyecto/PaginaPrincipal.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}