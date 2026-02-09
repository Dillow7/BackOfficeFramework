package mg.framework.controller;

import mg.framework.entity.Client;
import mg.framework.dao.ClientDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ClientController {
    
    private final ClientDao clientDao = new ClientDao();
    
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        log.info("GET /api/clients - Récupération de tous les clients");
        try {
            List<Client> clients = clientDao.findAll();
            return ResponseEntity.ok(clients);
        } catch (SQLException e) {
            log.error("Erreur DB: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable String id) {
        log.info("GET /api/clients/{} - Récupération du client", id);
        try {
            Optional<Client> client = clientDao.findById(id);
            if (client.isPresent()) {
                return ResponseEntity.ok(client.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"Client non trouvé\"}");
        } catch (SQLException e) {
            log.error("Erreur DB: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createClient(@Valid @RequestBody Client client) {
        log.info("POST /api/clients - Création d'un nouveau client: {} {}", client.getNom(), client.getPrenom());
        try {
            Client createdClient = clientDao.save(client);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
        } catch (SQLException e) {
            log.error("Erreur DB: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@PathVariable String id, @Valid @RequestBody Client clientDetails) {
        log.info("PUT /api/clients/{} - Modification du client", id);
        try {
            clientDetails.setId(id);
            boolean updated = clientDao.update(clientDetails);
            if (updated) {
                return ResponseEntity.ok(clientDetails);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"Client non trouvé\"}");
        } catch (SQLException e) {
            log.error("Erreur DB: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable String id) {
        log.info("DELETE /api/clients/{} - Suppression du client", id);
        try {
            Optional<Client> client = clientDao.findById(id);
            if (client.isPresent()) {
                clientDao.delete(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"Client non trouvé\"}");
        } catch (SQLException e) {
            log.error("Erreur DB: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
