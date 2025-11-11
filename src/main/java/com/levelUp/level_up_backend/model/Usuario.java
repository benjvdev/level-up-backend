package com.levelUp.level_up_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;

    @Column(length = 60,nullable = false)
    private String nombres;

    @Column(length = 60,nullable = true)
    private String apellidos;

    @JsonIgnore
    @Column(length = 80, nullable = false)
    private String password;

    @Column (length = 60, nullable = false)
    private String email;

    @Column (length = 40, nullable = false)
    private String rol;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(length = 60, nullable = true)
    private String direccion;
}
