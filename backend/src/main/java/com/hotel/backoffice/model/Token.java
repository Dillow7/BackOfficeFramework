package com.hotel.backoffice.model;

import java.time.LocalDateTime;

public class Token {
    private int id;
    private String token;
    private LocalDateTime dateExpiration;

    public Token() {}

    public Token(int id, String token, LocalDateTime dateExpiration) {
        this.id = id;
        this.token = token;
        this.dateExpiration = dateExpiration;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public LocalDateTime getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(LocalDateTime dateExpiration) { this.dateExpiration = dateExpiration; }
}
