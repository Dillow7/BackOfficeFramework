package com.hotel.backoffice;

import com.hotel.backoffice.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDao {

    public List<Client> findAll() throws SQLException {
        String sql = "SELECT id, nom, prenom, telephone, email FROM client ORDER BY id";
        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            List<Client> clients = new ArrayList<>();
            while (rs.next()) {
                clients.add(mapClient(rs));
            }
            return clients;
        }
    }

    public Client findById(String id) throws SQLException {
        String sql = "SELECT id, nom, prenom, telephone, email FROM client WHERE id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapClient(rs);
                }
                return null;
            }
        }
    }

    public void insert(Client client) throws SQLException {
        String sql = "INSERT INTO client (id, nom, prenom, telephone, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, client.getId());
            ps.setString(2, client.getNom());
            ps.setString(3, client.getPrenom());
            ps.setString(4, client.getTelephone());
            ps.setString(5, client.getEmail());
            ps.executeUpdate();
        }
    }

    private Client mapClient(ResultSet rs) throws SQLException {
        Client c = new Client();
        c.setId(rs.getString("id"));
        c.setNom(rs.getString("nom"));
        c.setPrenom(rs.getString("prenom"));
        c.setTelephone(rs.getString("telephone"));
        c.setEmail(rs.getString("email"));
        return c;
    }
}
