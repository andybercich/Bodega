package org.example.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.Entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {


    private final JwtProperties jwtProperties;

    @Autowired
    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }
    public String getToken (Usuario usuario){
        return getToken(new HashMap<>(), usuario);
    }
    private String getToken(Map<String,Object> extraClaims, Usuario usuario){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(usuario.getMail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    private Key getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getMailFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String mail = getMailFromToken(token);

        // Debug: mostrar mail extraído del token y el usuario actual
        System.out.println("Mail del token: " + mail);
        System.out.println("Mail del UserDetails: " + userDetails.getUsername());

        boolean expired = isTokenExpired(token);
        // Debug: ver si el token está expirado
        System.out.println("Token expirado? " + expired);

        boolean valid = mail.equals(userDetails.getUsername()) && !expired;
        // Debug final: resultado de validación
        System.out.println("Token válido? " + valid);

        return valid;
    }

    private boolean isTokenExpired(String token){
        Date exp = getExpiration(token);
        System.out.println("Expiración del token: " + exp); // debug
        return exp.before(new Date());
    }

    private Claims getAllClaimes(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token).getBody();
    }

    public <T> T getClaim (String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaimes(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token){
        return  getClaim(token, Claims::getExpiration);
    }
}
