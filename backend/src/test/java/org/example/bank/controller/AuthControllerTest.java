package org.example.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bank.DTO.JwtResponse;
import org.example.bank.DTO.LoginRequest;
import org.example.bank.DTO.UserRegistrationDto;
import org.example.bank.api.AuthController;
import org.example.bank.domain.User;
import org.example.bank.security.JwtAuthenticationFilter;
import org.example.bank.security.JwtUtils;
import org.example.bank.security.UserDetailsImpl;
import org.example.bank.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {JwtAuthenticationFilter.class} // ИСКЛЮЧАЕМ
        )
)
@Import({AuthControllerTest.MockBeans.class, AuthControllerTest.NoSecurityConfig.class})
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserService userService;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockBeans {
        @Bean public UserService userService() { return Mockito.mock(UserService.class); }
        @Bean public AuthenticationManager authenticationManager() { return Mockito.mock(AuthenticationManager.class); }
        @Bean public JwtUtils jwtUtils() { return Mockito.mock(JwtUtils.class); }
    }

    @TestConfiguration
    static class NoSecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            return http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                    .build();
        }
    }

    @Test
    void register_shouldReturnCreatedUser() throws Exception {
        var dto = new UserRegistrationDto("testuser", "password123");
        var user = new User("testuser", "encodedPassword");
        user.setId(1L);

        when(userService.registerUser(dto.getUsername(), dto.getPassword())).thenReturn(user);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void login_shouldReturnJwtResponse() throws Exception {
        var user = new User("testuser", "encodedPassword");
        user.setId(1L);
        var userDetails = new UserDetailsImpl(user);
        var loginRequest = new LoginRequest("testuser", "password123");

        var mockAuth = Mockito.mock(Authentication.class);
        when(mockAuth.getPrincipal()).thenReturn(userDetails);
        when(authenticationManager.authenticate(any())).thenReturn(mockAuth);
        when(jwtUtils.generateJwtToken(any())).thenReturn("mocked-jwt-token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void getCurrentUser_shouldReturnPrincipal() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }
}
