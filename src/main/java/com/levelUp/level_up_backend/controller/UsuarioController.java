package com.levelUp.level_up_backend.controller;

import com.levelUp.level_up_backend.dto.LoginRequest;
import com.levelUp.level_up_backend.dto.RegisterRequest;
import com.levelUp.level_up_backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    public ResponseEntity<String> registrar(@RequestBody RegisterRequest datosRegistro) {
        try {
            usuarioService.registrarUsuario(datosRegistro);
            return ResponseEntity.ok("Usuario registrado correctamente.");
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

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
}
