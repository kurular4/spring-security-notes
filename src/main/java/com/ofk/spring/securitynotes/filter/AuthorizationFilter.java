package com.ofk.spring.securitynotes.filter;

import com.ofk.spring.securitynotes.config.JwtConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthorizationFilter extends OncePerRequestFilter {
    private JwtConstant jwtConstant;

    public AuthorizationFilter(JwtConstant jwtConstant) {
        this.jwtConstant = jwtConstant;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(httpServletRequest);

        if (token != null && validateToken(token)) {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtConstant.getSecretKey().getBytes()))
                    .parseClaimsJws(token);

            Set<? extends GrantedAuthority> authorities = ((List<Map<String, String>>) claimsJws
                    .getBody()
                    .get("authorities"))
                    .stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toSet());

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(
                            claimsJws.getBody().getSubject(), null, authorities));
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    boolean validateToken(String token) throws JwtException {
        Jws<Claims> claims = Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(jwtConstant.getSecretKey().getBytes())).parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new Date());
    }

    String getUsername(String token) {
        return Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(jwtConstant.getSecretKey().getBytes())).parseClaimsJws(token).getBody().getSubject();
    }

    String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(jwtConstant.getHeader());

        if (bearerToken != null && bearerToken.startsWith(jwtConstant.getPrefix())) {
            return bearerToken.substring(7);
        } else return null;
    }
}
