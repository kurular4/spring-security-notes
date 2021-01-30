package com.ofk.spring.securitynotes.service;

import com.ofk.spring.securitynotes.config.Role;
import com.ofk.spring.securitynotes.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple service, no actual database as it is not primary concern
 */
@Service
public class SimpleUserDetailsService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final List<User> users = new ArrayList<>();

    @Autowired
    public SimpleUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void initData() {
        users.add(new User(Role.ADMIN.getAuthorities(),
                "admin", passwordEncoder.encode("pass"),
                true, true, true, true));
        users.add(new User(Role.USER.getAuthorities(),
                "user", passwordEncoder.encode("pass"),
                true, true, true, true));
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return users.stream().filter(user -> user.getUsername().equals(s)).findFirst().orElseThrow(() -> new UsernameNotFoundException(s + " not found"));
    }
}
