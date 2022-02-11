package com.example.catalogservice.security.jwt;

import com.example.catalogservice.security.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${jwt.expiresIn}")
    private int jwtExpirationMs;

    @Value("${jwt.secretKey}")
    private String jwtSecret;

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).
                getBody().getSubject();
    }


    private List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        String role = (String) Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get("role");
        List<String> rolesStrings = new ArrayList<>();
        rolesStrings.add(role);
        return rolesStrings.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public String getToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (headerAuth != null && headerAuth.startsWith("Bearer")) {
            return headerAuth.substring(7);
        }
        return null;
    }


    public UserDetailsImpl getUserDetailsFromToken(String token) {
        String email = getEmailFromJwtToken(token);
        List<GrantedAuthority> authorities = getAuthoritiesFromToken(token);
        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setUsername(email);
        userDetails.setAuthorities(authorities);
        return userDetails;
    }
}
