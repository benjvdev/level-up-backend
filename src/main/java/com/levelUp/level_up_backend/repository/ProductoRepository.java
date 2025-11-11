package com.levelUp.level_up_backend.repository;

import com.levelUp.level_up_backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoria(String categoria);

    List<Producto> findByNombreContaining(String nombre);

    List<Producto> findByDisponible(Boolean disponible);
}
