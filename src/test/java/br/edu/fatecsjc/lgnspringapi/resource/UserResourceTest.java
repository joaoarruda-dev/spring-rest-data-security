package br.edu.fatecsjc.lgnspringapi.resource;

import br.edu.fatecsjc.lgnspringapi.dto.ChangePasswordRequestDTO;
import br.edu.fatecsjc.lgnspringapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

class UserResourceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserResource userResource;

    @Mock
    private Principal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testChangePassword() {
        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO();
        doNothing().when(userService).changePassword(any(ChangePasswordRequestDTO.class), any(Principal.class));

        ResponseEntity<?> response = userResource.changePassword(request, principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}