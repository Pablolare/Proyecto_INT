package com.javafx.ProyectoINT.modelos;

import com.javafx.ProyectoINT.ApiCliente;
import javafx.collections.ObservableList;
import java.util.List;

public class CategoriaDAO {

    public boolean insertarCategoria(Categoria categoria) {
        return ApiCliente.insertarCategoria(categoria);
    }

    public boolean actualizarCategoria(Categoria categoria) {
        return ApiCliente.actualizarCategoria(categoria);
    }

    public boolean borrarCategoria(int id_categoria) {
        return ApiCliente.borrarCategoria(id_categoria);
    }

    public ObservableList<Categoria> cargarCategoriasDesdeBD() {
        return ApiCliente.cargarCategorias();
    }

    public ObservableList<Categoria> obtenerCategoriasPorEjercicio(int id_ejer) {
        return ApiCliente.obtenerCategoriasPorEjercicio(id_ejer);
    }

    public boolean asignarCategoriasAEjercicio(int id_ejer, List<Categoria> categorias) {
        return ApiCliente.asignarCategoriasAEjercicio(id_ejer, categorias);
    }
}
