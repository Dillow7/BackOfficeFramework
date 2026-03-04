package com.hotel.backoffice;

import com.hotel.backoffice.model.Reservation;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDao {
    private static final String INSERT_SQL =
        "INSERT INTO reservation (id_client, nb_passager, date_heure_arrive, id_hotel) VALUES (?, ?, ?, ?)";

    private static final String UPDATE_SQL =
        "UPDATE reservation SET id_client = ?, nb_passager = ?, date_heure_arrive = ?, id_hotel = ? WHERE id = ?";

    private static final String DELETE_SQL =
        "DELETE FROM reservation WHERE id = ?";

    public void insert(String idClient, int nbPassager, Timestamp dateHeureArrive, int idHotel) throws SQLException {
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            ps.setString(1, idClient);
            ps.setInt(2, nbPassager);
            ps.setTimestamp(3, dateHeureArrive);
            ps.setInt(4, idHotel);
            ps.executeUpdate();
        }
    }

    public List<Reservation> findAll() throws SQLException {
        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM reservation ORDER BY date_heure_arrive DESC")) {
            return mapReservations(rs);
        }
    }

    public Reservation findById(int id) throws SQLException {
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM reservation WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Reservation r = new Reservation();
                    r.setId(rs.getInt("id"));
                    r.setIdClient(rs.getString("id_client"));
                    r.setNbPassager(rs.getInt("nb_passager"));
                    r.setDateHeureArrive(rs.getTimestamp("date_heure_arrive").toLocalDateTime());
                    r.setIdHotel(rs.getInt("id_hotel"));
                    return r;
                }
                return null;
            }
        }
    }

    public void update(int id, String idClient, int nbPassager, Timestamp dateHeureArrive, int idHotel) throws SQLException {
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, idClient);
            ps.setInt(2, nbPassager);
            ps.setTimestamp(3, dateHeureArrive);
            ps.setInt(4, idHotel);
            ps.setInt(5, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Reservation> findByDate(LocalDate date) throws SQLException {
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT * FROM reservation WHERE DATE(date_heure_arrive) = ? ORDER BY date_heure_arrive DESC")) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                return mapReservations(rs);
            }
        }
    }

    private List<Reservation> mapReservations(ResultSet rs) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        while (rs.next()) {
            Reservation r = new Reservation();
            r.setId(rs.getInt("id"));
            r.setIdClient(rs.getString("id_client"));
            r.setNbPassager(rs.getInt("nb_passager"));
            r.setDateHeureArrive(rs.getTimestamp("date_heure_arrive").toLocalDateTime());
            r.setIdHotel(rs.getInt("id_hotel"));
            reservations.add(r);
        }
        return reservations;
    }
}
