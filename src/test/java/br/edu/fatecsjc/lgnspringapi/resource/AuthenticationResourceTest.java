package br.edu.fatecsjc.lgnspringapi.resource;

import br.edu.fatecsjc.lgnspringapi.dto.AuthenticationRequestDTO;
import br.edu.fatecsjc.lgnspringapi.dto.AuthenticationResponseDTO;
import br.edu.fatecsjc.lgnspringapi.dto.RegisterRequestDTO;
import br.edu.fatecsjc.lgnspringapi.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class AuthenticationResourceTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationResource authenticationResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        AuthenticationResponseDTO responseDTO = new AuthenticationResponseDTO();
        when(authenticationService.register(any(RegisterRequestDTO.class))).thenReturn(responseDTO);

        ResponseEntity<AuthenticationResponseDTO> response = authenticationResource.register(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void testAuthenticate() {
        AuthenticationRequestDTO request = new AuthenticationRequestDTO();
        AuthenticationResponseDTO responseDTO = new AuthenticationResponseDTO();
        when(authenticationService.authenticate(any(AuthenticationRequestDTO.class))).thenReturn(responseDTO);

        ResponseEntity<AuthenticationResponseDTO> response = authenticationResource.authenticate(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void testRefreshToken() throws IOException {
        doNothing().when(authenticationService).refreshToken(any(HttpServletRequest.class), any(HttpServletResponse.class));

        ResponseEntity<Void> responseEntity = authenticationResource.refreshToken(null, null);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}