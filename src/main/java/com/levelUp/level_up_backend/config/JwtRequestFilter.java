package com.levelUp.level_up_backend.config;

import com.levelUp.level_up_backend.service.CustomUserDetailsService;
import com.levelUp.level_up_backend.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        //comprueba si el header existe y tiene el formato "Bearer <token>"
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Extrae el token
            try {
                username = jwtUtil.getUsernameFromToken(jwt);
            } catch (IllegalArgumentException e) {
                logger.warn("No se pudo obtener el username del token");
            } catch (ExpiredJwtException e) {
                logger.warn("El token ha expirado");
            }
        } else {
            // logger.warn("el header de autorización no empieza con 'Bearer '");
        }
        //si obtuvimos un username y NO hay una sesión de seguridad activa
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //cargamos los detalles del usuario desde la base de datos
            UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
            //validamos el token
            if (jwtUtil.validateToken(jwt, userDetails)) {
                //si el token es válido, creamos la autenticación
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //establecemos la autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        //continuamos con el resto de los filtros
        filterChain.doFilter(request, response);
    }
}
