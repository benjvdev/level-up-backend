package com.levelUp.level_up_backend.controller;

import com.levelUp.level_up_backend.model.Producto;
import com.levelUp.level_up_backend.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

// --- ENDPOINTS PÚBLICOS (GET) ---
@RestController
@RequestMapping("/productos")
@Tag(name = "1. Productos", description = "Endpoints públicos para consultar productos.")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Operation(
            summary = "Obtener todos los productos",
            description = "Devuelve una lista de todos los productos disponibles en la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos devuelta exitosamente.")
    })
    @GetMapping
    public List<Producto> getAllProductos() {
        return productoService.getAllProductos();
    }

    @Operation(
            summary = "Obtener un producto por su ID",
            description = "Busca y devuelve un producto específico usando su ID único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado y devuelto."),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado con ese ID.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        return productoService.getProductoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Obtener productos por categoría",
            description = "Devuelve una lista de todos los productos que coinciden con el nombre de la categoría."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos filtrada devuelta.")
    })
    @GetMapping("/categoria/{categoria}")
    public List<Producto> getProductosPorCategoria(@PathVariable String categoria) {
        return productoService.getProductosByCategoria(categoria);
    }

}
