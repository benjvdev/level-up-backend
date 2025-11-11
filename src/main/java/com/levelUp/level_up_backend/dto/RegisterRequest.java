package com.levelUp.level_up_backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RegisterRequest {
    private String nombres;
    private String apellidos;
    private String email;
    private String password;
    private String rol;
    private LocalDate fechaNacimiento;
    private String direccion;
}
