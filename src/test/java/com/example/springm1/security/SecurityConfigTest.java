package com.example.springm1.security;

import com.example.springm1.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class SecurityConfigTest {

    @Test
    public void testPasswordEncoder() {
        SecurityConfig securityConfig = new SecurityConfig();
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        
        assertNotNull(passwordEncoder);
        assert(passwordEncoder instanceof BCryptPasswordEncoder);
    }
    
    @Test
    public void testAuthenticationManager() throws Exception {
        SecurityConfig securityConfig = new SecurityConfig();
        HttpSecurity httpSecurity = mock(HttpSecurity.class);
        
        // This test just verifies that the configuration class exists and can be instantiated
        assertNotNull(securityConfig);
    }
} 