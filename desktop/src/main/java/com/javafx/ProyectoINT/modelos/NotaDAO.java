package com.javafx.ProyectoINT.modelos;

import com.javafx.ProyectoINT.ConexionBD;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NotaDAO {

    public boolean insertarNota(Nota nota) {
        String sql = "INSERT INTO Nota (id_usuario, id_entreno, titulo, contenido) VALUES (?, ?, ?, ?)";

        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, nota.getId_usuario());
            if (nota.getId_entreno() == null) {
                stmt.setNull(2, Types.INTEGER);
            } else {
                stmt.setInt(2, nota.getId_entreno());
            }
            stmt.setString(3, nota.getTitulo());
            stmt.setString(4, nota.getContenido());
            stmt.executeUpdate();
            stmt.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al insertar nota: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarNota(Nota nota) {
        String sql = "UPDATE Nota SET id_entreno=?, titulo=?, contenido=? WHERE id_nota=?";

        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            if (nota.getId_entreno() == null) {
                stmt.setNull(1, Types.INTEGER);
            } else {
                stmt.setInt(1, nota.getId_entreno());
            }
            stmt.setString(2, nota.getTitulo());
            stmt.setString(3, nota.getContenido());
            stmt.setInt(4, nota.getId_nota());
            stmt.executeUpdate();
            stmt.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar nota: " + e.getMessage());
            return false;
        }
    }

    public boolean borrarNota(int id_nota) {
        String sql = "DELETE FROM Nota WHERE id_nota=?";

        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_nota);
            stmt.executeUpdate();
            stmt.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al borrar nota: " + e.getMessage());
            return false;
        }
    }

    public ObservableList<Nota> cargarNotasUsuario(int id_usuario) {
        ObservableList<Nota> lista = FXCollections.observableArrayList();
        String sql = "SELECT n.*, e.nombre_entreno FROM Nota n " +
                     "LEFT JOIN Entrenamientos e ON n.id_entreno = e.id_entreno " +
                     "WHERE n.id_usuario=? ORDER BY n.fecha DESC";

        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_usuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int idEntreno = rs.getInt("id_entreno");
                Integer idEntrenoVal = rs.wasNull() ? null : idEntreno;
                Nota nota = new Nota(
                        rs.getInt("id_nota"),
                        rs.getInt("id_usuario"),
                        idEntrenoVal,
                        rs.getString("titulo"),
                        rs.getString("contenido"),
                        rs.getString("fecha"));
                nota.setNombreEntreno(rs.getString("nombre_entreno"));
                lista.add(nota);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Error al obtener notas: " + e.getMessage());
        }

        return lista;
    }

    public Integer obtenerPrimerIdEntrenoPorNombre(String nombreEntreno, int idUsuario) {
        String sql = "SELECT MIN(id_entreno) as id FROM Entrenamientos WHERE nombre_entreno=? AND id_usuario=?";
        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombreEntreno);
            stmt.setInt(2, idUsuario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                return rs.wasNull() ? null : id;
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener id_entreno por nombre: " + e.getMessage());
        }
        return null;
    }
}
