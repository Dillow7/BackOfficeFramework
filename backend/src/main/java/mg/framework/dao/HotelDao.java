package mg.framework.dao;

import mg.framework.entity.Hotel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HotelDao {
    public List<Hotel> findAll() throws SQLException {
        String sql = "SELECT id, nom FROM hotel ORDER BY id";
        List<Hotel> hotels = new ArrayList<>();

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                hotels.add(mapRow(rs));
            }
        }

        return hotels;
    }

    public Optional<Hotel> findById(int id) throws SQLException {
        String sql = "SELECT id, nom FROM hotel WHERE id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }

        return Optional.empty();
    }

    public Hotel save(Hotel hotel) throws SQLException {
        String sql = "INSERT INTO hotel(nom) VALUES (?)";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, hotel.getNom());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    hotel.setId(rs.getInt(1));
                }
            }
        }

        return hotel;
    }

    public boolean update(Hotel hotel) throws SQLException {
        String sql = "UPDATE hotel SET nom = ? WHERE id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hotel.getNom());
            statement.setInt(2, hotel.getId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM hotel WHERE id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    private Hotel mapRow(ResultSet rs) throws SQLException {
        return new Hotel(rs.getInt("id"), rs.getString("nom"));
    }
}
