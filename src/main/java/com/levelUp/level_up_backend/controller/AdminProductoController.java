package com.levelUp.level_up_backend.controller;

import com.levelUp.level_up_backend.model.Producto;
import com.levelUp.level_up_backend.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

// --- ENDPOINTS PRIVADOS PARA ROL ADMIN ---
@RestController
@RequestMapping("/admin/productos")
@Tag(name = "3. Admin - Productos", description = "Endpoints protegidos (Rol ADMIN) para gestionar el catálogo de productos.")
@SecurityRequirement(name = "bearerAuth")
public class AdminProductoController {

    @Autowired
    private ProductoService productoService;

    @Operation(
            summary = "Crear un nuevo producto",
            description = "Añade un nuevo producto a la base de datos. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto creado exitosamente."),
            @ApiResponse(responseCode = "400", description = "Datos del producto inválidos."),
            @ApiResponse(responseCode = "401", description = "No autorizado (Token no válido o sin rol ADMIN)."),
            @ApiResponse(responseCode = "403", description = "Acceso denegado (Rol incorrecto).")
    })
    @PostMapping
    public Producto createProducto(@RequestBody Producto producto) {
        return productoService.createProducto(producto);
    }

    @Operation(
            summary = "Actualizar un producto existente",
            description = "Actualiza los detalles de un producto usando su ID. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado con ese ID."),
            @ApiResponse(responseCode = "401", description = "No autorizado."),
            @ApiResponse(responseCode = "403", description = "Acceso denegado.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody Producto productoDetails) {
        try {
            Producto updatedProducto = productoService.updateProducto(id, productoDetails);
            return ResponseEntity.ok(updatedProducto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Eliminar un producto",
            description = "Elimina un producto de la base de datos usando su ID. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "240", description = "Producto eliminado exitosamente."), // 204 No Content
            @ApiResponse(responseCode = "404", description = "Producto no encontrado con ese ID."),
            @ApiResponse(responseCode = "401", description = "No autorizado."),
            @ApiResponse(responseCode = "403", description = "Acceso denegado.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        try {
            productoService.deleteProducto(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}