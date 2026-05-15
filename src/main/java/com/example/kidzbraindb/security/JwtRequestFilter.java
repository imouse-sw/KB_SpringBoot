package com.example.kidzbraindb.security;

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
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // le quitamos la palabra "Bearer " para dejar solo el código
            try {
                username = jwtUtil.extractUsername(jwt); // sacamos el correo del código
            } catch (Exception e) {
                System.out.println("El token es inválido o ya expiró.");
            }
        }

        // si trae correo y aún no le hemos dado acceso en esta petición, lo revisamos
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // buscamos al usuario en la BD
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // verificamos matemáticamente que el token no sea pirata y no haya caducado
            if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                // le damos su pulsera de acceso (Authentication)
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Le avisamos a Spring Security que este usuario ya puede pasar
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        // deja que la petición avance
        filterChain.doFilter(request, response);
    }
}
