package com.levelUp.level_up_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_producto;

    @Column(length = 600, nullable = false)
    private String image;

    @Column(length = 200, nullable = false)
    private String nombre;

    @Column(length = 300,nullable = false)
    private String descripcion;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal precio;

    @Column(length = 80, nullable = false)
    private String categoria;

    @Column(nullable = false)
    private Boolean disponible;
}
