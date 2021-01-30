package com.ofk.spring.securitynotes.config;

import com.ofk.spring.securitynotes.filter.AuthenticationFilter;
import com.ofk.spring.securitynotes.filter.AuthorizationFilter;
import com.ofk.spring.securitynotes.service.SimpleUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class JwtSecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final SimpleUserDetailsService userDetailsService;
    private final JwtConstant jwtConstant;

    @Autowired
    public JwtSecurityConfig(PasswordEncoder passwordEncoder, SimpleUserDetailsService userDetailsService, JwtConstant jwtConstant) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtConstant = jwtConstant;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new AuthenticationFilter(authenticationManager(), jwtConstant))
                .addFilterAfter(new AuthorizationFilter(jwtConstant), AuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.DELETE, "/document/*").hasAnyRole(Role.ADMIN.name())
                .antMatchers(HttpMethod.GET, "/document/*").hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                .anyRequest()
                .authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(getAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider getAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

//    @Bean
//    @Override
//    protected UserDetailsService userDetailsService() {
//        return new InMemoryUserDetailsManager(
//                User.builder()
//                        .username("test_admin")
//                        .password(passwordEncoder.encode("abc123"))
//                        .roles(Role.ADMIN.name())
//                        .build(),
//                User.builder()
//                        .username("test_user")
//                        .password(passwordEncoder.encode("abc123"))
//                        .roles(Role.USER.name())
//                        .build());
//    }
}
