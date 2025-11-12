package com.levelUp.level_up_backend.service;

import com.levelUp.level_up_backend.dto.LoginRequest;
import com.levelUp.level_up_backend.dto.RegisterRequest;
import com.levelUp.level_up_backend.model.Usuario;
import com.levelUp.level_up_backend.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registrarUsuario(RegisterRequest datosRegistro) {

        // validar si el email ya existe
        if (usuarioRepository.existsByEmail(datosRegistro.getEmail())) {
            throw new RuntimeException("El email ya está en uso.");
        }
        //mapeo del DTO al nuevo usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombres(datosRegistro.getNombres());
        nuevoUsuario.setApellidos(datosRegistro.getApellidos());
        nuevoUsuario.setEmail(datosRegistro.getEmail());
        nuevoUsuario.setFechaNacimiento(datosRegistro.getFechaNacimiento());
        nuevoUsuario.setDireccion(datosRegistro.getDireccion());
        //codificacion de la contraseña
        String passwordHasheada = passwordEncoder.encode(datosRegistro.getPassword());
        nuevoUsuario.setPassword(passwordHasheada);

        //asignar rol USER por defecto
        if (datosRegistro.getRol() == null || datosRegistro.getRol().isEmpty()) {
            nuevoUsuario.setRol("USER");
        } else {
            nuevoUsuario.setRol(datosRegistro.getRol());
        }
        usuarioRepository.save(nuevoUsuario);
    }
    //validacion de credenciales para el login
    public Map<String, String> login(LoginRequest datosLogin) {

        //busqueda de usuario por su email
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(datosLogin.getEmail());

        //validacion de email y contraseña
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (passwordEncoder.matches(datosLogin.getPassword(), usuario.getPassword())) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Login exitoso");
                response.put("rol", usuario.getRol());
                response.put("email", usuario.getEmail());
                response.put("nombreUsuario", usuario.getNombres());

                return response;
            }
        }
        throw new RuntimeException("Credenciales incorrectas o el usuario no existe."); // en caso de fallar
    }

    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }
}