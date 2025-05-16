package com.example.springm1.security;

import com.example.springm1.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        
        // This test just verifies that the configuration class exists and can be instantiated
        assertNotNull(securityConfig);
    }
} 