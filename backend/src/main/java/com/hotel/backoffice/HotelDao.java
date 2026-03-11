package com.hotel.backoffice;

import com.hotel.backoffice.model.Hotel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HotelDao {
    public List<Hotel> findAll() throws SQLException {
        String sql = "SELECT id_hotel, nom, code, aeroport FROM hotel ORDER BY id_hotel";
        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            List<Hotel> hotels = new ArrayList<>();
            while (rs.next()) {
                Hotel h = new Hotel();
                h.setIdHotel(rs.getInt("id_hotel"));
                h.setNom(rs.getString("nom"));
                h.setCode(rs.getString("code"));
                h.setAeroport(rs.getBoolean("aeroport"));
                hotels.add(h);
            }
            return hotels;
        }
    }

    public Hotel findById(int idHotel) throws SQLException {
        String sql = "SELECT id_hotel, nom, code, aeroport FROM hotel WHERE id_hotel = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idHotel);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Hotel h = new Hotel();
                    h.setIdHotel(rs.getInt("id_hotel"));
                    h.setNom(rs.getString("nom"));
                    h.setCode(rs.getString("code"));
                    h.setAeroport(rs.getBoolean("aeroport"));
                    return h;
                }
                return null;
            }
        }
    }
}
