package com.javafx.ProyectoINT.Historial;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.javafx.ProyectoINT.InicioSesion.ControlInicioSesion;
import com.javafx.ProyectoINT.modelos.Ejercicios;
import com.javafx.ProyectoINT.modelos.EjerciciosDAO;
import com.javafx.ProyectoINT.modelos.Entrenamiento;
import com.javafx.ProyectoINT.modelos.EntrenamientoDAO;
import com.javafx.ProyectoINT.modelos.EntrenamientoDAO.DatosProgresion;
import com.javafx.ProyectoINT.modelos.EntrenamientoDAO.EstadisticasUsuario;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.control.TextField;
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
    private TableColumn<Entrenamiento, Integer> colAciertos;
    
    @FXML
    private TableColumn<Entrenamiento, Integer> colFallos;
    
    @FXML
    private TableColumn<Entrenamiento, Boolean> colCompletado;
    
    @FXML
    private TextField txtBuscar;

    private int idUsuarioActual;
    private EntrenamientoDAO dao;
    private Map<Integer, String> mapaEjer = new HashMap<>();
    private ObservableList<Entrenamiento> listaBase = FXCollections.observableArrayList();
    private FilteredList<Entrenamiento> listaFiltrada;
    
    @FXML
    void initialize() {
        dao = new EntrenamientoDAO();
        new EjerciciosDAO().cargarEjerciciosDesdeBD()
                .forEach(e -> mapaEjer.put(e.getId_ejer(), e.getNombre_ejer()));

        colId.setVisible(false);
        colIdUsuario.setVisible(false);
        colCompletado.setVisible(false);

        colNombre.setCellValueFactory(cellData -> {
            Entrenamiento e = cellData.getValue();
            String nombreEjer = mapaEjer.getOrDefault(e.getId_ejer(), "Ejercicio " + e.getId_ejer());
            return new javafx.beans.property.SimpleStringProperty(nombreEjer + " (" + e.getNombre_entreno() + ")");
        });
        colNombre.setText("Ejercicio (Entrenamiento)");
        colAciertos.setCellValueFactory(new PropertyValueFactory<>("aciertos"));
        colAciertos.setStyle("-fx-text-fill: #4CAF50; -fx-alignment: CENTER;");
        colFallos.setCellValueFactory(new PropertyValueFactory<>("fallos"));
        colFallos.setStyle("-fx-text-fill: #f44336; -fx-alignment: CENTER;");

        TableColumn<Entrenamiento, String> colEfectividad = new TableColumn<>("Efectividad %");
        colEfectividad.setCellValueFactory(cellData -> {
            Entrenamiento e = cellData.getValue();
            int total = e.getAciertos() + e.getFallos();
            String val = total > 0
                    ? String.format("%.1f%%", e.getAciertos() * 100.0 / total)
                    : "—";
            return new javafx.beans.property.SimpleStringProperty(val);
        });
        colEfectividad.setStyle("-fx-text-fill: #60a5fa; -fx-alignment: CENTER;");
        tablaHistorial.getColumns().add(colEfectividad);

        listaFiltrada = new FilteredList<>(listaBase, p -> true);
        SortedList<Entrenamiento> listaSorted = new SortedList<>(listaFiltrada);
        listaSorted.comparatorProperty().bind(tablaHistorial.comparatorProperty());
        tablaHistorial.setItems(listaSorted);

        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> {
            listaFiltrada.setPredicate(e -> {
                if (newVal == null || newVal.trim().isEmpty()) return true;
                String lower = newVal.toLowerCase();
                String nombreEjer = mapaEjer.getOrDefault(e.getId_ejer(), "").toLowerCase();
                return e.getNombre_entreno().toLowerCase().contains(lower) || nombreEjer.contains(lower);
            });
        });

        tablaHistorial.setStyle("-fx-background-color: #1e293b;");
        tablaHistorial.setRowFactory(tv -> {
            javafx.scene.control.TableRow<Entrenamiento> row = new javafx.scene.control.TableRow<>();
            row.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
            return row;
        });

        lineChart.setCreateSymbols(true);
        lineChart.setLegendVisible(true);

        idUsuarioActual = ControlInicioSesion.usuarioLogueadoId;
        cargarDatosCompletos();
    }
    public void setUsuario(int idUsuario) {
        this.idUsuarioActual = idUsuario;
        cargarDatosCompletos();
    }
    
    private void cargarDatosCompletos() {
        cargarGraficoProgresion();
        cargarEstadisticas();
        cargarTablaHistorial();
    }

    private void cargarGraficoProgresion() {
        lineChart.getData().clear();
        
        List<DatosProgresion> progresion = dao.obtenerProgresionUsuario(idUsuarioActual);
        
        if (progresion.isEmpty()) {
            System.out.println("No hay datos de entrenamiento para este usuario");
            return;
        }
        
        XYChart.Series<String, Number> serieAciertos = new XYChart.Series<>();
        serieAciertos.setName("Aciertos");
        
        XYChart.Series<String, Number> serieFallos = new XYChart.Series<>();
        serieFallos.setName("Fallos");
        
        for (DatosProgresion dato : progresion) {
            String etiqueta = "E" + dato.getNumeroEntreno();
            serieAciertos.getData().add(new XYChart.Data<>(etiqueta, dato.getAciertos()));
            serieFallos.getData().add(new XYChart.Data<>(etiqueta, dato.getFallos()));
        }
        
        lineChart.getData().addAll(serieAciertos, serieFallos);
        
        aplicarEstilosGrafico();
    }

    private void aplicarEstilosGrafico() {
        javafx.application.Platform.runLater(() -> {
            try {
                if (!lineChart.getData().isEmpty() && lineChart.getData().get(0).getNode() != null) {
                    lineChart.getData().get(0).getNode().setStyle(
                        "-fx-stroke: #4CAF50; -fx-stroke-width: 3px;"
                    );
                }
                
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

    private void cargarTablaHistorial() {
        listaBase.clear();
        dao.obtenerEntrenamientosUsuario(idUsuarioActual).stream()
                .filter(Entrenamiento::isCompletado)
                .forEach(listaBase::add);
    }

    @FXML
    void actualizarGrafico(ActionEvent event) {
        cargarDatosCompletos();
        System.out.println("Datos actualizados correctamente");
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
}