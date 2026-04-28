package com.javafx.ProyectoINT.modelos;

import com.javafx.ProyectoINT.ConexionBD;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ObjetivoDAO {

    public boolean insertarObjetivo(Objetivo objetivo) {
        String sql = "INSERT INTO Objetivo (id_usuario, descripcion, meta_aciertos, meta_repeticiones, cumplido) VALUES (?, ?, ?, ?, ?)";

        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, objetivo.getId_usuario());
            stmt.setString(2, objetivo.getDescripcion());
            if (objetivo.getMeta_aciertos() == null) {
                stmt.setNull(3, Types.INTEGER);
            } else {
                stmt.setInt(3, objetivo.getMeta_aciertos());
            }
            if (objetivo.getMeta_repeticiones() == null) {
                stmt.setNull(4, Types.INTEGER);
            } else {
                stmt.setInt(4, objetivo.getMeta_repeticiones());
            }
            stmt.setBoolean(5, objetivo.isCumplido());
            stmt.executeUpdate();
            stmt.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al insertar objetivo: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarObjetivo(Objetivo objetivo) {
        String sql = "UPDATE Objetivo SET descripcion=?, meta_aciertos=?, meta_repeticiones=?, cumplido=? WHERE id_objetivo=?";

        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, objetivo.getDescripcion());
            if (objetivo.getMeta_aciertos() == null) {
                stmt.setNull(2, Types.INTEGER);
            } else {
                stmt.setInt(2, objetivo.getMeta_aciertos());
            }
            if (objetivo.getMeta_repeticiones() == null) {
                stmt.setNull(3, Types.INTEGER);
            } else {
                stmt.setInt(3, objetivo.getMeta_repeticiones());
            }
            stmt.setBoolean(4, objetivo.isCumplido());
            stmt.setInt(5, objetivo.getId_objetivo());
            stmt.executeUpdate();
            stmt.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar objetivo: " + e.getMessage());
            return false;
        }
    }

    public boolean borrarObjetivo(int id_objetivo) {
        String sql = "DELETE FROM Objetivo WHERE id_objetivo=?";

        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_objetivo);
            stmt.executeUpdate();
            stmt.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al borrar objetivo: " + e.getMessage());
            return false;
        }
    }

    public ObservableList<Objetivo> cargarObjetivosUsuario(int id_usuario) {
        ObservableList<Objetivo> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Objetivo WHERE id_usuario=? ORDER BY fecha_creacion DESC";

        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_usuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int metaAc = rs.getInt("meta_aciertos");
                Integer metaAcVal = rs.wasNull() ? null : metaAc;
                int metaRep = rs.getInt("meta_repeticiones");
                Integer metaRepVal = rs.wasNull() ? null : metaRep;

                lista.add(new Objetivo(
                        rs.getInt("id_objetivo"),
                        rs.getInt("id_usuario"),
                        rs.getString("descripcion"),
                        metaAcVal,
                        metaRepVal,
                        rs.getBoolean("cumplido"),
                        rs.getString("fecha_creacion")));
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Error al obtener objetivos: " + e.getMessage());
        }

        return lista;
    }
}
