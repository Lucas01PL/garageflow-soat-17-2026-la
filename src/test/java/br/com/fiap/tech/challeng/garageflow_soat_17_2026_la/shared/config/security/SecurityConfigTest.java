package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.config.security;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.infrastructure.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SecurityConfigTest {

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @InjectMocks
    private SecurityConfig securityConfig;

    @Test
    void shouldCreateBCryptPasswordEncoder() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();

        assertNotNull(encoder);
        assertInstanceOf(BCryptPasswordEncoder.class, encoder);
    }

    @Test
    void shouldCreateAuthenticationProviderWithUserDetailsService() {
        DaoAuthenticationProvider provider = securityConfig.authenticationProvider();

        assertNotNull(provider);
    }

    @Test
    void shouldDelegateAuthenticationManagerCreationToConfiguration() throws Exception {
        AuthenticationConfiguration configuration = mock(AuthenticationConfiguration.class);
        AuthenticationManager expectedManager = mock(AuthenticationManager.class);
        when(configuration.getAuthenticationManager()).thenReturn(expectedManager);

        AuthenticationManager result = securityConfig.authenticationManager(configuration);

        assertSame(expectedManager, result);
    }

    @Test
    void shouldBuildSecurityFilterChain() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class, Answers.RETURNS_SELF);
        DefaultSecurityFilterChain expectedChain = mock(DefaultSecurityFilterChain.class);
        when(http.build()).thenReturn(expectedChain);

        SecurityFilterChain result = securityConfig.securityFilterChain(http);

        assertSame(expectedChain, result);
    }
}
