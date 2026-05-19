package com.javafx.ProyectoINT.modelos;

import com.javafx.ProyectoINT.ApiCliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EntrenamientoDAO {

    public boolean insertarEntrenamiento(Entrenamiento entrenamiento) {
        return ApiCliente.insertarEntrenamiento(entrenamiento);
    }

    public boolean actualizarEntrenamiento(Entrenamiento entrenamiento) {
        return ApiCliente.actualizarEntrenamiento(entrenamiento);
    }

    public boolean borrarEntrenamiento(int id_entreno) {
        return ApiCliente.borrarEntrenamiento(id_entreno);
    }

    public boolean borrarEntrenamientoPorEjercicio(String nombreEntreno, int idEjer, int idUsuario) {
        return ApiCliente.borrarEntrenamientoPorEjercicio(nombreEntreno, idEjer, idUsuario);
    }

    public ObservableList<Entrenamiento> cargarEntrenamientosDesdeBD() {
        return ApiCliente.cargarEntrenamientos();
    }

    public ObservableList<Entrenamiento> obtenerEntrenamientosPorNombre(String nombreEntreno, int idUsuario) {
        return ApiCliente.obtenerEntrenamientosPorNombre(nombreEntreno, idUsuario);
    }

    public ObservableList<Entrenamiento> obtenerEntrenosAgrupadosUsuario(int idUsuario) {
        ObservableList<Entrenamiento> todos = ApiCliente.obtenerEntrenamientosUsuario(idUsuario);
        Map<String, List<Entrenamiento>> grupos = new LinkedHashMap<>();
        for (Entrenamiento e : todos) {
            grupos.computeIfAbsent(e.getNombre_entreno(), k -> new ArrayList<>()).add(e);
        }
        ObservableList<Entrenamiento> resultado = FXCollections.observableArrayList();
        for (Map.Entry<String, List<Entrenamiento>> entry : grupos.entrySet()) {
            List<Entrenamiento> grupo = entry.getValue();
            int totalAciertos = 0, totalFallos = 0;
            boolean completado = true;
            String descripcion = grupo.get(0).getDescripcion();
            for (Entrenamiento e : grupo) {
                totalAciertos += e.getAciertos();
                totalFallos += e.getFallos();
                if (!e.isCompletado()) completado = false;
            }
            Entrenamiento agrupado = new Entrenamiento(0, idUsuario, 0, entry.getKey(),
                    descripcion, totalFallos, totalAciertos, completado);
            agrupado.setNumEjercicios(grupo.size());
            resultado.add(agrupado);
        }
        return resultado;
    }

    public boolean borrarEntrenamientoPorNombre(String nombreEntreno, int idUsuario) {
        return ApiCliente.borrarEntrenamientoPorNombre(nombreEntreno, idUsuario);
    }

    public List<DatosProgresion> obtenerProgresionUsuario(int idUsuario) {
        return ApiCliente.obtenerProgresionUsuario(idUsuario);
    }

    public ObservableList<Entrenamiento> obtenerEntrenamientosUsuario(int idUsuario) {
        return ApiCliente.obtenerEntrenamientosUsuario(idUsuario);
    }

    public EstadisticasUsuario obtenerEstadisticas(int idUsuario) {
        return ApiCliente.obtenerEstadisticas(idUsuario);
    }

    // ==================== Clases de datos internas (mantienen la misma interfaz) ====================

    public static class DatosProgresion {
        private final int numeroEntreno;
        private final int aciertos;
        private final int fallos;
        private final String nombreEntreno;

        public DatosProgresion(int numeroEntreno, int aciertos, int fallos, String nombreEntreno) {
            this.numeroEntreno = numeroEntreno;
            this.aciertos = aciertos;
            this.fallos = fallos;
            this.nombreEntreno = nombreEntreno;
        }

        public int getNumeroEntreno() { return numeroEntreno; }
        public int getAciertos() { return aciertos; }
        public int getFallos() { return fallos; }
        public String getNombreEntreno() { return nombreEntreno; }
    }

    public static class EstadisticasUsuario {
        private final int totalEntrenos;
        private final int totalAciertos;
        private final int totalFallos;
        private final double promedioAciertos;
        private final double promedioFallos;

        public EstadisticasUsuario(int totalEntrenos, int totalAciertos, int totalFallos,
                                   double promedioAciertos, double promedioFallos) {
            this.totalEntrenos = totalEntrenos;
            this.totalAciertos = totalAciertos;
            this.totalFallos = totalFallos;
            this.promedioAciertos = promedioAciertos;
            this.promedioFallos = promedioFallos;
        }

        public int getTotalEntrenos() { return totalEntrenos; }
        public int getTotalAciertos() { return totalAciertos; }
        public int getTotalFallos() { return totalFallos; }
        public double getPromedioAciertos() { return promedioAciertos; }
        public double getPromedioFallos() { return promedioFallos; }

        public double getPorcentajeAciertos() {
            int total = totalAciertos + totalFallos;
            return total > 0 ? (totalAciertos * 100.0 / total) : 0;
        }
    }
}
