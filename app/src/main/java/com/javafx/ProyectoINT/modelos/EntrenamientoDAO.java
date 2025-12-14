package com.javafx.ProyectoINT.modelos;

import com.javafx.ProyectoINT.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EntrenamientoDAO {

    public boolean insertarEntrenamiento(Entrenamiento entrenamiento) {
        String sql = "INSERT INTO Entrenamientos (id_usuario, id_ejer, nombre_entreno, repeticiones, fallos, aciertos, completado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = ConexionBD.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, entrenamiento.getId_usuario());
            stmt.setInt(2, entrenamiento.getId_ejer());
            stmt.setString(3, entrenamiento.getNombre_entreno());
            stmt.setInt(4, entrenamiento.getRepeticiones());
            stmt.setInt(5, entrenamiento.getFallos());
            stmt.setInt(6, entrenamiento.getAciertos());
            stmt.setBoolean(7, entrenamiento.isCompletado());

            stmt.executeUpdate();
            stmt.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al insertar entrenamiento: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarEntrenamiento(Entrenamiento entrenamiento) {
        String sql = "UPDATE Entrenamientos SET id_usuario=?, id_ejer=?, nombre_entreno=?, repeticiones=?, fallos=?, aciertos=?, completado=? WHERE id_entreno=?";

        try {
            Connection conn = ConexionBD.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, entrenamiento.getId_usuario());
            stmt.setInt(2, entrenamiento.getId_ejer());
            stmt.setString(3, entrenamiento.getNombre_entreno());
            stmt.setInt(4, entrenamiento.getRepeticiones());
            stmt.setInt(5, entrenamiento.getFallos());
            stmt.setInt(6, entrenamiento.getAciertos());
            stmt.setBoolean(7, entrenamiento.isCompletado());
            stmt.setInt(8, entrenamiento.getId_entreno());

            stmt.executeUpdate();
            stmt.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar entrenamiento: " + e.getMessage());
            return false;
        }
    }

    public boolean borrarEntrenamiento(int id_entreno) {
        String sql = "DELETE FROM Entrenamientos WHERE id_entreno=?";

        try {
            Connection conn = ConexionBD.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_entreno);

            stmt.executeUpdate();
            stmt.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al borrar entrenamiento: " + e.getMessage());
            return false;
        }
    }

    public ObservableList<Entrenamiento> cargarEntrenamientosDesdeBD() {
        ObservableList<Entrenamiento> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Entrenamientos";

        try {
            Connection conn = ConexionBD.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Entrenamiento entrenamiento = new Entrenamiento(
                        rs.getInt("id_entreno"),
                        rs.getInt("id_usuario"),
                        rs.getInt("id_ejer"),
                        rs.getString("nombre_entreno"),
                        rs.getInt("repeticiones"),
                        rs.getInt("fallos"),
                        rs.getInt("aciertos"),
                        rs.getBoolean("completado"));
                lista.add(entrenamiento);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Error al obtener entrenamientos: " + e.getMessage());
        }

        return lista;
    }

    // ==================== MÉTODOS NUEVOS PARA EL GRÁFICO ====================
    
    /**
     * Clase interna para representar datos de progresión
     */
    public static class DatosProgresion {
        private int numeroEntreno;
        private int aciertos;
        private int fallos;
        private String nombreEntreno;
        
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
    
    /**
     * Obtiene la progresión de aciertos y fallos de un usuario específico
     */
    public List<DatosProgresion> obtenerProgresionUsuario(int idUsuario) {
        List<DatosProgresion> progresion = new ArrayList<>();
        String sql = "SELECT id_entreno, aciertos, fallos, nombre_entreno " +
                     "FROM Entrenamientos " +
                     "WHERE id_usuario = ? " +
                     "ORDER BY id_entreno ASC";
        
        try {
            Connection conn = ConexionBD.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();
            
            int contador = 1;
            while (rs.next()) {
                progresion.add(new DatosProgresion(
                    contador++,
                    rs.getInt("aciertos"),
                    rs.getInt("fallos"),
                    rs.getString("nombre_entreno")
                ));
            }
            
            rs.close();
            pstmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener progresión: " + e.getMessage());
            e.printStackTrace();
        }
        
        return progresion;
    }
    
    /**
     * Obtiene todos los entrenamientos de un usuario
     */
    public ObservableList<Entrenamiento> obtenerEntrenamientosUsuario(int idUsuario) {
        ObservableList<Entrenamiento> entrenamientos = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Entrenamientos WHERE id_usuario = ? ORDER BY id_entreno ASC";
        
        try {
            Connection conn = ConexionBD.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                entrenamientos.add(new Entrenamiento(
                    rs.getInt("id_entreno"),
                    rs.getInt("id_usuario"),
                    rs.getInt("id_ejer"),
                    rs.getString("nombre_entreno"),
                    rs.getInt("repeticiones"),
                    rs.getInt("fallos"),
                    rs.getInt("aciertos"),
                    rs.getBoolean("completado")
                ));
            }
            
            rs.close();
            pstmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener entrenamientos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return entrenamientos;
    }
    
    /**
     * Calcula estadísticas generales del usuario
     */
    public EstadisticasUsuario obtenerEstadisticas(int idUsuario) {
        String sql = "SELECT " +
                     "COUNT(*) as total_entrenos, " +
                     "SUM(aciertos) as total_aciertos, " +
                     "SUM(fallos) as total_fallos, " +
                     "AVG(aciertos) as promedio_aciertos, " +
                     "AVG(fallos) as promedio_fallos " +
                     "FROM Entrenamientos WHERE id_usuario = ?";
        
        try {
            Connection conn = ConexionBD.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                EstadisticasUsuario stats = new EstadisticasUsuario(
                    rs.getInt("total_entrenos"),
                    rs.getInt("total_aciertos"),
                    rs.getInt("total_fallos"),
                    rs.getDouble("promedio_aciertos"),
                    rs.getDouble("promedio_fallos")
                );
                
                rs.close();
                pstmt.close();
                return stats;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener estadísticas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Clase para estadísticas del usuario
     */
    public static class EstadisticasUsuario {
        private int totalEntrenos;
        private int totalAciertos;
        private int totalFallos;
        private double promedioAciertos;
        private double promedioFallos;
        
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