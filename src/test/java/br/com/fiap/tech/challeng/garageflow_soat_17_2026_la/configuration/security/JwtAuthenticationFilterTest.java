package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.configuration.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldContinueChainWithoutAuthenticatingWhenNoAuthorizationHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldContinueChainWithoutAuthenticatingWhenHeaderIsNotBearer() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Basic dXNlcjpwYXNz");

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldAuthenticateWhenTokenIsValid() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        when(jwtService.isTokenValid("valid-token")).thenReturn(true);
        when(jwtService.extractEmail("valid-token")).thenReturn("admin@garageflow.com");
        when(jwtService.extractRole("valid-token")).thenReturn("ADMIN");

        filter.doFilterInternal(request, response, filterChain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals("admin@garageflow.com", authentication.getPrincipal());
        assertTrue(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWhenTokenIsInvalid() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid-token");
        when(jwtService.isTokenValid("invalid-token")).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotOverrideExistingAuthentication() throws Exception {
        UsernamePasswordAuthenticationToken existing =
                new UsernamePasswordAuthenticationToken("someone-else", null);
        SecurityContextHolder.getContext().setAuthentication(existing);

        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        when(jwtService.isTokenValid("valid-token")).thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        assertEquals(existing, SecurityContextHolder.getContext().getAuthentication());
        verify(jwtService, never()).extractEmail(anyString());
        verify(filterChain).doFilter(request, response);
    }
}
