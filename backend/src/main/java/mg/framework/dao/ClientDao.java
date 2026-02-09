package mg.framework.dao;

import mg.framework.entity.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientDao {
    public List<Client> findAll() throws SQLException {
        String sql = "SELECT id, nom, prenom, telephone, email FROM client ORDER BY id";
        List<Client> clients = new ArrayList<>();

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                clients.add(mapRow(rs));
            }
        }

        return clients;
    }

    public Optional<Client> findById(String id) throws SQLException {
        String sql = "SELECT id, nom, prenom, telephone, email FROM client WHERE id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }

        return Optional.empty();
    }

    public Client save(Client client) throws SQLException {
        String sql = "INSERT INTO client(id, nom, prenom, telephone, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, client.getId());
            statement.setString(2, client.getNom());
            statement.setString(3, client.getPrenom());
            statement.setString(4, client.getTelephone());
            statement.setString(5, client.getEmail());
            statement.executeUpdate();
        }

        return client;
    }

    public boolean update(Client client) throws SQLException {
        String sql = "UPDATE client SET nom = ?, prenom = ?, telephone = ?, email = ? WHERE id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, client.getNom());
            statement.setString(2, client.getPrenom());
            statement.setString(3, client.getTelephone());
            statement.setString(4, client.getEmail());
            statement.setString(5, client.getId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM client WHERE id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    private Client mapRow(ResultSet rs) throws SQLException {
        return new Client(
                rs.getString("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("telephone"),
                rs.getString("email")
        );
    }
}
