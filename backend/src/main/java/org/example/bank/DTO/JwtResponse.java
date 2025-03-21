package org.example.bank.DTO;

public class JwtResponse {
    private final String token;
    private final Long id;
    private final String username;
    // Если необходимо, можно добавить дополнительные поля, например, роли

    public JwtResponse(String token, Long id, String username) {
        this.token = token;
        this.id = id;
        this.username = username;
    }

    // Геттеры
    public String getToken() {
        return token;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
