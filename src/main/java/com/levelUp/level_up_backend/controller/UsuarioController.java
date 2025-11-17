package com.levelUp.level_up_backend.controller;

import com.levelUp.level_up_backend.dto.LoginRequest;
import com.levelUp.level_up_backend.dto.RegisterRequest;
import com.levelUp.level_up_backend.model.Usuario;
import com.levelUp.level_up_backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "2. Autenticación y Usuarios", description = "Endpoints para registro, login y gestión de perfiles.")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Crea una nueva cuenta de usuario. El rol 'USER' se asigna por defecto."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente."),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud (ej. el email ya está en uso)")
    })
    @PostMapping("/registro")
    public ResponseEntity<String> registrar(@RequestBody RegisterRequest datosRegistro) {
        try {
            usuarioService.registrarUsuario(datosRegistro);
            return ResponseEntity.ok("Usuario registrado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(
            summary = "Iniciar sesión (Login)",
            description = "Autentica al usuario y devuelve un token JWT junto con los datos del usuario."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso. Devuelve token y datos de usuario."),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas (email o contraseña inválidos).")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest datosLogin) {
        try {
            Map<String, Object> response = usuarioService.login(datosLogin);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @Operation(
            summary = "Obtener el perfil del usuario actual",
            description = "Devuelve los datos del usuario autenticado. Requiere un token JWT en la cabecera 'Authorization: Bearer <token>'.",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Datos del perfil del usuario."),
            @ApiResponse(responseCode = "403", description = "No autorizado (token no válido o no proporcionado)."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado en la base de datos.")
    })
    @GetMapping("/perfil")
    public ResponseEntity<?> getMiPerfil(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "No autorizado"));
        }

        String userEmail = authentication.getName();

        try {
            Usuario usuario = usuarioService.findByEmail(userEmail);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }
}
