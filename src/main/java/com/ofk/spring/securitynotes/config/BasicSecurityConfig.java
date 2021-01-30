package com.ofk.spring.securitynotes.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@Configuration
//@EnableWebSecurity
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Basic authentication
     * Every request authenticated with credentials
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/document/**").permitAll() // No authentication for specified paths
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }
}
