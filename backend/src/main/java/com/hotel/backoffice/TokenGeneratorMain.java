package com.hotel.backoffice;

import com.hotel.backoffice.model.Token;
import java.time.LocalDateTime;
import java.util.UUID;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TokenGeneratorMain {
    public static void main(String[] args) {
        // Exemple de tokens à insérer
        insertToken(generateToken(), LocalDateTime.now().minusDays(1)); // expiré
        insertToken(generateToken(), LocalDateTime.now()); // expire maintenant
        insertToken(generateToken(), LocalDateTime.now().plusDays(1)); // valide
    }

    private static String generateToken() {
        // Génère un token UID de 16 caractères alphanumériques
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
    }

    private static void insertToken(String token, LocalDateTime expiration) {
        String url = "jdbc:mysql://localhost:3306/hotel"; // à adapter selon config
        String user = "root"; // à adapter
        String password = "root"; // à adapter
        String sql = "INSERT INTO token (token, date_expiration) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            stmt.setObject(2, expiration);
            stmt.executeUpdate();
            System.out.println("Token ajouté: " + token + " (expire le " + expiration + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
