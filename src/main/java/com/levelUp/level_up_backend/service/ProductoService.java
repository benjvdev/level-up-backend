package com.levelUp.level_up_backend.service;

import com.levelUp.level_up_backend.model.Producto;
import com.levelUp.level_up_backend.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> getProductoById(Long id) {
        return productoRepository.findById(id);
    }

    public Producto createProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto updateProducto(Long id, Producto productoDetails) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));

        producto.setNombre(productoDetails.getNombre());
        producto.setDescripcion(productoDetails.getDescripcion());
        producto.setPrecio(productoDetails.getPrecio());
        producto.setImage(productoDetails.getImage());
        producto.setCategoria(productoDetails.getCategoria());
        producto.setDisponible(productoDetails.getDisponible());

        return productoRepository.save(producto);
    }

    public void deleteProducto(Long id) {
        // verificar si el producto existe antes de borrar
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con id: " + id);
        }
        productoRepository.deleteById(id);
    }
    //metodos para filtrar
    public List<Producto> getProductosByCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    public List<Producto> getProductosDisponibles() {
        return productoRepository.findByDisponible(true);
    }
    public List<Producto> searchProductos(String query) {
        return productoRepository.findByNombreContainingIgnoreCaseOrCategoriaContainingIgnoreCaseOrDescripcionContainingIgnoreCase(query, query,query);
    }
}
