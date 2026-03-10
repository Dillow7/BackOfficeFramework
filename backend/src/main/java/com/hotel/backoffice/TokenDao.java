package com.hotel.backoffice;

import com.hotel.backoffice.model.Token;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class TokenDao {
    public Optional<Token> findByToken(String tokenValue) throws SQLException {
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM token WHERE token = ?")) {
            ps.setString(1, tokenValue);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Token token = new Token();
                    token.setId(rs.getInt("id"));
                    token.setToken(rs.getString("token"));
                    token.setDateExpiration(rs.getTimestamp("date_expiration").toLocalDateTime());
                    return Optional.of(token);
                }
                return Optional.empty();
            }
        }
    }

    public boolean isTokenValid(String tokenValue) throws SQLException {
        Optional<Token> tokenOpt = findByToken(tokenValue);
        if (tokenOpt.isPresent()) {
            Token token = tokenOpt.get();
            return token.getDateExpiration().isAfter(LocalDateTime.now());
        }
        return false;
    }
}
