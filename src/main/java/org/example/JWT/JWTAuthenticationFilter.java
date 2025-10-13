package org.example.JWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // Rutas de usuario
        if ("/usuario/registrarUsuario".equals(path) && "POST".equals(method)) return true;
        if ("/usuario/login".equals(path) && "POST".equals(method)) return true;
        if ("/usuario/validarMail".equals(path) && "POST".equals(method)) return true;

        // Productos: todos GET son públicos
        if (path.startsWith("/producto") && "GET".equals(method)) return true;

        // Webhook de Mercado Pago
        if ("/webhook".equals(path) && "POST".equals(method)) return true;

        // Imagenes: solo GET público
        if (path.startsWith("/imagen") && "GET".equals(method)) return true;

        // Contacto/Email
        if ("/contacto/sendContactEmail".equals(path) && "POST".equals(method)) return true;

        // Descuento: solo GET público
        if (path.startsWith("/descuento") && "GET".equals(method)) return true;

        // Categorías: solo GET público
        if ("/categorias".equals(path) && "GET".equals(method)) return true;

        // Artículos: solo GET público
        if ("/articulos".equals(path) && "GET".equals(method)) return true;

        // Lo demás se filtra normalmente
        return false;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = getTokenFromRequest(request);
        final String mail;
        if(token == null){
            filterChain.doFilter(request,response);
            return;
        }
        mail = jwtService.getMailFromToken(token);
        if (mail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(mail);
            if (jwtService.isTokenValid(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer")){
            return authHeader.substring(7);
        }
        return null;
    }

}
