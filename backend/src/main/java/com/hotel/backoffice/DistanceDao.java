package com.hotel.backoffice;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;


// DAO pour gérer les opérations liées aux distances entre aéroports
public class DistanceDao {
    public Map<String, Double> findAllDistancesKm() throws SQLException {
        String sql = "SELECT code_from, code_to, valeur_km FROM distance";
        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            Map<String, Double> distances = new HashMap<>();
            while (rs.next()) {
                String codeFrom = normalizeCode(rs.getString("code_from"));
                String codeTo = normalizeCode(rs.getString("code_to"));
                double distanceKm = rs.getDouble("valeur_km");
                distances.put(key(codeFrom, codeTo), distanceKm);
            }
            return distances;
        }
    }

    public static Double findSymmetricDistanceKm(Map<String, Double> distances, String fromCode, String toCode) {
        String from = normalizeCode(fromCode);
        String to = normalizeCode(toCode);
        if (from == null || to == null) {
            return null;
        }
        if (from.equals(to)) {
            return 0.0;
        }

        Double direct = distances.get(key(from, to));
        Double reverse = distances.get(key(to, from));
        if (direct == null) {
            return reverse;
        }
        if (reverse == null) {
            return direct;
        }
        return Math.min(direct, reverse);
    }

    private static String key(String from, String to) {
        return from + "->" + to;
    }

    private static String normalizeCode(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim().toUpperCase();
        return normalized.isEmpty() ? null : normalized;
    }
}
