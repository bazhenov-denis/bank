package org.example.bank.service;

import org.example.bank.domain.User;
import org.example.bank.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void registerUser_shouldCreateNewUser() {
        String username = "testuser";
        String password = "123456";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User createdUser = userService.registerUser(username, password);

        assertNotNull(createdUser);
        assertEquals(username, createdUser.getUsername());
        assertNotEquals(password, createdUser.getPassword()); // пароль должен быть зашифрован
        assertTrue(passwordEncoder.matches(password, createdUser.getPassword()));

        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_shouldThrowExceptionIfUserExists() {
        String username = "existingUser";
        String password = "secret";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userService.registerUser(username, password));

        assertEquals("Пользователь с таким именем уже существует", ex.getMessage());
    }

    @Test
    void findByUsername_shouldReturnUserIfExists() {
        String username = "admin";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User found = userService.findByUsername(username);

        assertNotNull(found);
        assertEquals(username, found.getUsername());
    }

    @Test
    void findByUsername_shouldReturnNullIfNotExists() {
        String username = "notfound";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        User found = userService.findByUsername(username);

        assertNull(found);
    }
}
