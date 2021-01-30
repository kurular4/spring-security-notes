package com.ofk.spring.securitynotes.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "security.jwt.token")
@Configuration
public class JwtConstant {
    private String secretKey;
    private int expirationInDays;
    private String prefix;
    private String header;

    public JwtConstant() {
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public int getExpirationInDays() {
        return expirationInDays;
    }

    public void setExpirationInDays(int expirationInDays) {
        this.expirationInDays = expirationInDays;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
