package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.dto.AuthenticationRequestDTO;
import br.edu.fatecsjc.lgnspringapi.dto.AuthenticationResponseDTO;
import br.edu.fatecsjc.lgnspringapi.dto.RegisterRequestDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Token;
import br.edu.fatecsjc.lgnspringapi.entity.User;
import br.edu.fatecsjc.lgnspringapi.enums.Role;
import br.edu.fatecsjc.lgnspringapi.enums.TokenType;
import br.edu.fatecsjc.lgnspringapi.repository.TokenRepository;
import br.edu.fatecsjc.lgnspringapi.repository.UserRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.PrintWriter;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        RegisterRequestDTO requestObj = new RegisterRequestDTO();
        requestObj.setFirstname("Joao");
        requestObj.setLastname("Arruda");
        requestObj.setEmail("joaoarruda@example.com");
        requestObj.setPassword("password");
        requestObj.setRole(Role.USER);

        User user = User.builder()
                .firstName("Joao")
                .lastName("Arruda")
                .email("joaoarruda@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");

        AuthenticationResponseDTO responseObj = authenticationService.register(requestObj);

        assertEquals("jwtToken", responseObj.getAccessToken());
        assertEquals("refreshToken", responseObj.getRefreshToken());
        verify(userRepository, times(1)).save(any(User.class));
        verify(tokenRepository, times(1)).save(any());
    }

    @Test
    void testAuthenticate() {
        AuthenticationRequestDTO requestObj = new AuthenticationRequestDTO();
        requestObj.setEmail("joaoarruda@example.com");
        requestObj.setPassword("password");

        User user = User.builder()
                .firstName("Joao")
                .lastName("Arruda")
                .email("joaoarruda@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");

        AuthenticationResponseDTO responseObj = authenticationService.authenticate(requestObj);

        assertEquals("jwtToken", responseObj.getAccessToken());
        assertEquals("refreshToken", responseObj.getRefreshToken());
        verify(authenticationManager, times(1)).authenticate(any());
        verify(tokenRepository, times(1)).save(any());
    }

    @Test
    void testRefreshToken() throws Exception {
        String refreshToken = "refreshToken";
        String accessToken = "newAccessToken";
        String userEmail = "joaoarruda@example.com";

        User user = User.builder()
                .firstName("Joao")
                .lastName("Arruda")
                .email(userEmail)
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + refreshToken);
        when(jwtService.extractUsername(refreshToken)).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(refreshToken, user)).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn(accessToken);

        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ServletOutputStream servletOutputStream = new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener listener) {
                // Implement the logic for setting the WriteListener if needed.
            }

            @Override
            public void write(int b) {
                outputStream.write(b);
            }
        };
        when(response.getOutputStream()).thenReturn(servletOutputStream);

        authenticationService.refreshToken(request, response);

        verify(jwtService, times(1)).generateToken(user);
        verify(tokenRepository, times(1)).save(any());
    }

    @Test
    void testRefreshToken_NoAuthHeader() throws Exception {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ServletOutputStream servletOutputStream = new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener listener) {
                // Implement the logic for setting the WriteListener if needed.
            }

            @Override
            public void write(int b) {
                outputStream.write(b);
            }
        };
        when(response.getOutputStream()).thenReturn(servletOutputStream);

        authenticationService.refreshToken(request, response);

        verify(jwtService, times(0)).extractUsername(any());
        verify(userRepository, times(0)).findByEmail(any());
        verify(jwtService, times(0)).isTokenValid(any(), any());
        verify(jwtService, times(0)).generateToken(any());
        verify(tokenRepository, times(0)).save(any());
    }

    @Test
    void testRevokeAllUserTokens() {
        User user = User.builder()
                .id(1L)
                .firstName("Joao")
                .lastName("Arruda")
                .email("joaoarruda@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        Token token1 = Token.builder()
                .user(user)
                .token("token1")
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        Token token2 = Token.builder()
                .user(user)
                .token("token2")
                .tokenType(TokenType.BEARER)
                .expired(true)
                .revoked(true)
                .build();

        Token token3 = Token.builder()
                .user(user)
                .token("token3")
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(true)
                .build();

        when(tokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(List.of(token1, token2, token3));

        authenticationService.revokeAllUserTokens(user);

        verify(tokenRepository, times(1)).saveAll(anyList());
        assertTrue(token1.isExpired());
        assertTrue(token1.isRevoked());
        assertTrue(token2.isExpired());
        assertTrue(token2.isRevoked());
        assertTrue(token3.isExpired());
        assertTrue(token3.isRevoked());
    }

    @Test
    void testRevokeAllUserTokens_NoValidTokens() {
        User user = User.builder()
                .id(1L)
                .firstName("Joao")
                .lastName("Arruda")
                .email("joaoarruda@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(tokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(List.of());

        authenticationService.revokeAllUserTokens(user);
        verify(tokenRepository, times(0)).saveAll(anyList());

    }

    @Test
    void testRefreshToken_InvalidAuthHeader() throws Exception {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("InvalidHeader");

        authenticationService.refreshToken(request, response);

        verify(jwtService, times(0)).extractUsername(any());
        verify(userRepository, times(0)).findByEmail(any());
        verify(jwtService, times(0)).isTokenValid(any(), any());
        verify(jwtService, times(0)).generateToken(any());
        verify(tokenRepository, times(0)).save(any());
    }

    @Test
    void testRefreshToken_ValidAuthHeader_InvalidUser() {
        String refreshToken = "refreshToken";
        String userEmail = "joaoarruda@example.com";

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + refreshToken);
        when(jwtService.extractUsername(refreshToken)).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            authenticationService.refreshToken(request, response);
        });

        verify(jwtService, times(1)).extractUsername(refreshToken);
        verify(userRepository, times(1)).findByEmail(userEmail);
        verify(jwtService, times(0)).isTokenValid(any(), any());
        verify(jwtService, times(0)).generateToken(any());
        verify(tokenRepository, times(0)).save(any());
    }

    @Test
    void testRefreshToken_NullEmail() throws Exception {
        String refreshToken = "refreshToken";

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + refreshToken);
        when(jwtService.extractUsername(refreshToken)).thenReturn(null);

        authenticationService.refreshToken(request, response);

        verify(userRepository, times(0)).findByEmail(any());
        verify(jwtService, times(0)).isTokenValid(any(), any());
        verify(jwtService, times(0)).generateToken(any());
        verify(tokenRepository, times(0)).save(any());
    }

    @Test
    void testRefreshToken_InvalidToken() throws Exception {
        String refreshToken = "refreshToken";
        String userEmail = "joaoarruda@example.com";

        User user = User.builder()
                .firstName("Joao")
                .lastName("Arruda")
                .email(userEmail)
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + refreshToken);
        when(jwtService.extractUsername(refreshToken)).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(refreshToken, user)).thenReturn(false);

        authenticationService.refreshToken(request, response);

        verify(jwtService, times(1)).extractUsername(refreshToken);
        verify(userRepository, times(1)).findByEmail(userEmail);
        verify(jwtService, times(1)).isTokenValid(refreshToken, user);
        verify(jwtService, times(0)).generateToken(any());
        verify(tokenRepository, times(0)).save(any());
    }

}

