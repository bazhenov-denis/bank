package org.example.bank.api;

import org.example.bank.DTO.JwtResponse;
import org.example.bank.DTO.LoginRequest;
import org.example.bank.DTO.UserRegistrationDto;
import org.example.bank.domain.User;
import org.example.bank.security.JwtUtils;
import org.example.bank.security.UserDetailsImpl;
import org.example.bank.service.UserService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Создаем аутентификационный токен из данных запроса
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

        // Пытаемся аутентифицировать пользователя
        Authentication authentication = authenticationManager.authenticate(authToken);

        // Если аутентификация успешна, устанавливаем контекст безопасности
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Приводим principal к вашему кастомному классу
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Генерация JWT-токена
        String token = jwtUtils.generateJwtToken(authentication);
        logger.info("{} Пользователь {} успешно аутентифицирован{}", token,  userDetails.getId(), userDetails.getUsername());

        // Создаем объект ответа с токеном и данными пользователя
        JwtResponse jwtResponse = new JwtResponse(token, userDetails.getId(), userDetails.getUsername());

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserRegistrationDto registrationDto) {
        User createdUser = userService.registerUser(registrationDto.getUsername(), registrationDto.getPassword());
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(org.springframework.security.core.Authentication authentication) {
        return ResponseEntity.ok(authentication.getPrincipal());
    }
}
