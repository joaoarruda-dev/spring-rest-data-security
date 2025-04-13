package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.dto.ChangePasswordRequestDTO;
import br.edu.fatecsjc.lgnspringapi.entity.User;
import br.edu.fatecsjc.lgnspringapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    private Principal principal;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        User user = new User();
        user.setPassword("encodedCurrentPassword");
        principal = new UsernamePasswordAuthenticationToken(user, null);
    }

    @Test
    public void testChangePassword_Success() {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        user.setPassword("encodedCurrentPassword");

        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO();
        request.setCurrentPassword("currentPassword");
        request.setNewPassword("newPassword");
        request.setConfirmationPassword("newPassword");

        when(passwordEncoder.matches("currentPassword", "encodedCurrentPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        userService.changePassword(request, principal);

        verify(userRepository, times(1)).save(user);
        assertEquals("encodedNewPassword", user.getPassword());
    }

    @Test
    public void testChangePassword_WrongCurrentPassword() {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        user.setPassword("encodedCurrentPassword");

        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO();
        request.setCurrentPassword("wrongCurrentPassword");
        request.setNewPassword("newPassword");
        request.setConfirmationPassword("newPassword");

        when(passwordEncoder.matches("wrongCurrentPassword", "encodedCurrentPassword")).thenReturn(false);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userService.changePassword(request, principal);
        });

        assertEquals("Wrong password", exception.getMessage());
        verify(userRepository, times(0)).save(user);
    }

    @Test
    public void testChangePassword_PasswordsDoNotMatch() {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        user.setPassword("encodedCurrentPassword");

        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO();
        request.setCurrentPassword("currentPassword");
        request.setNewPassword("newPassword");
        request.setConfirmationPassword("differentNewPassword");

        when(passwordEncoder.matches("currentPassword", "encodedCurrentPassword")).thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userService.changePassword(request, principal);
        });

        assertEquals("Password are not the same", exception.getMessage());
        verify(userRepository, times(0)).save(user);
    }
}