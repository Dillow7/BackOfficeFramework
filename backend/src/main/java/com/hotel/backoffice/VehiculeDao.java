package com.hotel.backoffice;

import com.hotel.backoffice.model.Vehicule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculeDao {
    private static final String INSERT_SQL =
        "INSERT INTO vehicule (reference, nb_place, type_carburant) VALUES (?, ?, ?)";

    private static final String UPDATE_SQL =
        "UPDATE vehicule SET reference = ?, nb_place = ?, type_carburant = ? WHERE id = ?";

    private static final String DELETE_SQL =
        "DELETE FROM vehicule WHERE id = ?";

    public void insert(String reference, int nbPlace, String typeCarburant) throws SQLException {
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            ps.setString(1, reference);
            ps.setInt(2, nbPlace);
            ps.setString(3, typeCarburant);
            ps.executeUpdate();
        }
    }

    public List<Vehicule> findAll() throws SQLException {
        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM vehicule ORDER BY id")) {
            return mapVehicules(rs);
        }
    }

    public Vehicule findById(int id) throws SQLException {
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM vehicule WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapVehicule(rs);
                }
                return null;
            }
        }
    }

    public void update(int id, String reference, int nbPlace, String typeCarburant) throws SQLException {
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, reference);
            ps.setInt(2, nbPlace);
            ps.setString(3, typeCarburant);
            ps.setInt(4, id);
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

    private List<Vehicule> mapVehicules(ResultSet rs) throws SQLException {
        List<Vehicule> vehicules = new ArrayList<>();
        while (rs.next()) {
            vehicules.add(mapVehicule(rs));
        }
        return vehicules;
    }

    private Vehicule mapVehicule(ResultSet rs) throws SQLException {
        Vehicule v = new Vehicule();
        v.setId(rs.getInt("id"));
        v.setReference(rs.getString("reference"));
        v.setNbPlace(rs.getInt("nb_place"));
        v.setTypeCarburant(rs.getString("type_carburant"));
        return v;
    }
}
