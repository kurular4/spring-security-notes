package com.ofk.spring.securitynotes.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ofk.spring.securitynotes.config.JwtConstant;
import com.ofk.spring.securitynotes.dto.UserAuthenticationData;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtConstant jwtConstant;

    public AuthenticationFilter(AuthenticationManager authenticationManager, JwtConstant jwtConstant) {
        this.authenticationManager = authenticationManager;
        this.jwtConstant = jwtConstant;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            UserAuthenticationData userAuthenticationData = mapper.readValue(request.getInputStream(), UserAuthenticationData.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userAuthenticationData.getUsername(), userAuthenticationData.getPassword());
            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Date now = new Date();
        Date validity = new Date(now.getTime() + TimeUnit.DAYS.toMillis(jwtConstant.getExpirationInDays()));

        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(Keys.hmacShaKeyFor(jwtConstant.getSecretKey().getBytes()))
                .compact();
        response.addHeader(jwtConstant.getHeader(), jwtConstant.getPrefix() + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
