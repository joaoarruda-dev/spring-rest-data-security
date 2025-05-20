package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.repository.TokenRepository;
import br.edu.fatecsjc.lgnspringapi.entity.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class LogoutServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private LogoutService logoutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogoutWithValidToken() {
        String token = "Bearer validToken";
        Token storedToken = new Token();
        when(request.getHeader("Authorization")).thenReturn(token);
        when(tokenRepository.findByToken("validToken")).thenReturn(Optional.of(storedToken));

        logoutService.logout(request, response, authentication);

        verify(tokenRepository).findByToken("validToken");
        verify(tokenRepository).save(storedToken);
        assertTrue(storedToken.isExpired());
        assertTrue(storedToken.isRevoked());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testLogoutWithInvalidToken() {
        String token = "Bearer invalidToken";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(tokenRepository.findByToken("invalidToken")).thenReturn(Optional.empty());

        logoutService.logout(request, response, authentication);

        verify(tokenRepository).findByToken("invalidToken");
        verify(tokenRepository, never()).save(any());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testLogoutWithoutAuthorizationHeader() {
        when(request.getHeader("Authorization")).thenReturn(null);

        logoutService.logout(request, response, authentication);

        verify(tokenRepository, never()).findByToken(any());
        verify(tokenRepository, never()).save(any());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}