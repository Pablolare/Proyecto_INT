package com.javafx.ProyectoINT.modelos;

import com.javafx.ProyectoINT.ApiCliente;
import javafx.collections.ObservableList;

public class UsuarioDAO {

    public boolean insertarUsuario(Usuario usuario) {
        return ApiCliente.insertarUsuario(usuario);
    }

    public boolean actualizarUsuario(Usuario usuario) {
        return ApiCliente.actualizarUsuario(usuario);
    }

    public boolean borrarUsuario(int id_usuario) {
        return ApiCliente.borrarUsuario(id_usuario);
    }

    public ObservableList<Usuario> cargarUsuariosDesdeBD() {
        return ApiCliente.cargarUsuarios();
    }
}
