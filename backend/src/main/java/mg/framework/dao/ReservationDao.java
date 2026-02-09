package mg.framework.dao;

import mg.framework.entity.Reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReservationDao {
    public List<Reservation> findAll() throws SQLException {
        String sql = "SELECT id, idClient, nbPassager, dateHeureArrive, idHotel FROM reservation ORDER BY id";
        List<Reservation> reservations = new ArrayList<>();

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                reservations.add(mapRow(rs));
            }
        }

        return reservations;
    }

    public Optional<Reservation> findById(int id) throws SQLException {
        String sql = "SELECT id, idClient, nbPassager, dateHeureArrive, idHotel FROM reservation WHERE id = ?";

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

    public Reservation save(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO reservation(idClient, nbPassager, dateHeureArrive, idHotel) VALUES (?, ?, ?, ?)";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, reservation.getIdClient());
            statement.setInt(2, reservation.getNbPassager());
            statement.setTimestamp(3, Timestamp.valueOf(reservation.getDateHeureArrive()));
            statement.setInt(4, reservation.getIdHotel());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    reservation.setId(rs.getInt(1));
                }
            }
        }

        return reservation;
    }

    public boolean update(Reservation reservation) throws SQLException {
        String sql = "UPDATE reservation SET idClient = ?, nbPassager = ?, dateHeureArrive = ?, idHotel = ? WHERE id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, reservation.getIdClient());
            statement.setInt(2, reservation.getNbPassager());
            statement.setTimestamp(3, Timestamp.valueOf(reservation.getDateHeureArrive()));
            statement.setInt(4, reservation.getIdHotel());
            statement.setInt(5, reservation.getId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM reservation WHERE id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    private Reservation mapRow(ResultSet rs) throws SQLException {
        return new Reservation(
                rs.getInt("id"),
                rs.getString("idClient"),
                rs.getInt("nbPassager"),
                rs.getTimestamp("dateHeureArrive").toLocalDateTime(),
                rs.getInt("idHotel")
        );
    }
}
