package com.example.kidzbraindb.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        // 5 peticiones por minuto
        Refill refill = Refill.intervally(5, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(5, refill);
        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // solo aplicamos el límite a las rutas más atacables (login, registro, recuperar password)
        if (path.contains("/login") || path.contains("/registro") || path.contains("/reset-request")) {

            // obtenemos la IP de quien hace la petición
            String ip = request.getRemoteAddr();
            Bucket bucket = cache.computeIfAbsent(ip, k -> createNewBucket());

            // intentamos gastar 1 ficha
            if (bucket.tryConsume(1)) {
                filterChain.doFilter(request, response);
            } else {
                // nohay fichas :(
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value()); // error 429
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Demasiadas peticiones. Intenta más tarde.\"}");
                return;
            }
        } else {
            // para otras rutas
            filterChain.doFilter(request, response);
        }
    }
}