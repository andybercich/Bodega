package org.example.Config;

import lombok.RequiredArgsConstructor;
import org.example.JWT.JWTAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authRequest ->
                        authRequest

                                //Solo admin
                                .requestMatchers(HttpMethod.PUT, "/activate/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/**").hasAuthority("ADMIN")

                                //Usuario Controller
                                .requestMatchers(HttpMethod.POST, "/usuario/registrarUsuario").permitAll()
                                .requestMatchers(HttpMethod.POST, "/usuario/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/usuario/validarMail").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/usuario/favorite/**").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/usuario/deleteFavorite/**").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/usuario/favorites").authenticated()
                                .requestMatchers(HttpMethod.GET, "/usuario/favorites").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/usuario/dataUser").authenticated()
                                //Solo admin
                                .requestMatchers(HttpMethod.GET, "/usuario/paginados").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/usuario/admin").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/usuario/admin/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/usuario/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/usuario/**").hasAuthority("ADMIN")





                                //Producto
                                .requestMatchers(HttpMethod.GET, "/producto/**").permitAll()


                                //Solo admin
                                .requestMatchers(HttpMethod.PATCH, "/producto/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/producto/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/producto/**").hasAuthority("ADMIN")


                                //Mercado Pago
                                .requestMatchers(HttpMethod.POST, "/api/mercadopago").authenticated()
                                .requestMatchers(HttpMethod.POST, "/webhook").permitAll()



                                //Imagen
                                .requestMatchers(HttpMethod.GET, "/imagen/**").permitAll()


                                .requestMatchers(HttpMethod.PATCH, "/imagen/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/imagen/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/imagen/**").hasAuthority("ADMIN")


                                //Email controller
                                .requestMatchers(HttpMethod.POST, "/contacto/sendContactEmail").permitAll()
                                .requestMatchers(HttpMethod.POST, "/contacto/**").authenticated()


                                //Direcciones
                                .requestMatchers(HttpMethod.GET, "/direcciones").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/direcciones/**").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/direcciones/**").authenticated()
                                .requestMatchers(HttpMethod.PATCH, "/direcciones/**").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/direcciones/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/direcciones/**").authenticated()


                                //Descuentos
                                .requestMatchers(HttpMethod.GET, "/descuento/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/descuento/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/descuento/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/descuento/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/descuento/**").hasAuthority("ADMIN")




                                //Compra
                                .requestMatchers(HttpMethod.GET, "/compra/user").authenticated()
                                .requestMatchers(HttpMethod.POST, "/compra").authenticated()
                                .requestMatchers(HttpMethod.GET, "/compra/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/compra/**").hasAuthority("ADMIN")



                                //Codigo descuento
                                .requestMatchers(HttpMethod.POST, "/codDescuento/aplicarCodigo").authenticated()
                                .requestMatchers(HttpMethod.POST, "/codDescuento/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/codDescuento/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/codDescuento/**").hasAuthority("ADMIN")


                                //Categorias
                                .requestMatchers(HttpMethod.GET, "/categorias/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/categorias/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/categorias", "/categorias/**").hasAuthority("ADMIN")


                                //Articulos
                                .requestMatchers(HttpMethod.GET, "/articulos/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/articulos/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/articulos/**").hasAuthority("ADMIN")

                                .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
