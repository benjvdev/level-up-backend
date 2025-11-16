package com.levelUp.level_up_backend.service;

import com.levelUp.level_up_backend.dto.LoginRequest;
import com.levelUp.level_up_backend.dto.RegisterRequest;
import com.levelUp.level_up_backend.model.Usuario;
import com.levelUp.level_up_backend.repository.UsuarioRepository;
import com.levelUp.level_up_backend.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    //registro
    public void registrarUsuario(RegisterRequest datosRegistro) {
        if (usuarioRepository.existsByEmail(datosRegistro.getEmail())) {
            throw new RuntimeException("El email ya está en uso.");
        }
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombres(datosRegistro.getNombres());
        nuevoUsuario.setApellidos(datosRegistro.getApellidos());
        nuevoUsuario.setEmail(datosRegistro.getEmail());
        nuevoUsuario.setFechaNacimiento(datosRegistro.getFechaNacimiento());
        nuevoUsuario.setDireccion(datosRegistro.getDireccion());
        nuevoUsuario.setPassword(passwordEncoder.encode(datosRegistro.getPassword()));
        nuevoUsuario.setRol(
                datosRegistro.getRol() == null || datosRegistro.getRol().isEmpty()
                        ? "USER"
                        : datosRegistro.getRol()
        );
        usuarioRepository.save(nuevoUsuario);
    }
    //login
    public Map<String, Object> login(LoginRequest datosLogin) {
        try {
            //validar credenciales
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            datosLogin.getEmail(),
                            datosLogin.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("email o contraseña incorrectos");
        }
        // generar el token JWT
        final UserDetails userDetails = userDetailsService.loadUserByUsername(datosLogin.getEmail());
        final String token = jwtUtil.generateToken(userDetails);

        //obtener usuario para retornar datos
        Usuario usuario = usuarioRepository.findByEmail(datosLogin.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado después del login"));

        //payload que el AuthContext espera
        Map<String, Object> userPayload = new HashMap<>();
        userPayload.put("rol", usuario.getRol());
        userPayload.put("nombreUsuario", usuario.getNombres());
        userPayload.put("email", usuario.getEmail());

        //respuesta final (anidada)
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", userPayload); //anidamos el usuario

        return response;
    }

    //eliminar usuario
    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("El usuario no existe.");
        }
        usuarioRepository.deleteById(id);
    }
    // encontrar usuario por su email
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }
}