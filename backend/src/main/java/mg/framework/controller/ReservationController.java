package mg.framework.controller;

import mg.framework.entity.Reservation;
import mg.framework.dao.ReservationDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ReservationController {
    
    private final ReservationDao reservationDao = new ReservationDao();
    
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        log.info("GET /api/reservations - Récupération de toutes les réservations");
        try {
            List<Reservation> reservations = reservationDao.findAll();
            return ResponseEntity.ok(reservations);
        } catch (SQLException e) {
            log.error("Erreur DB: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable Integer id) {
        log.info("GET /api/reservations/{} - Récupération de la réservation", id);
        try {
            Optional<Reservation> reservation = reservationDao.findById(id);
            if (reservation.isPresent()) {
                return ResponseEntity.ok(reservation.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"Réservation non trouvée\"}");
        } catch (SQLException e) {
            log.error("Erreur DB: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createReservation(@Valid @RequestBody Reservation reservation) {
        log.info("POST /api/reservations - Création d'une nouvelle réservation pour le client: {}", reservation.getIdClient());
        try {
            Reservation createdReservation = reservationDao.save(reservation);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
        } catch (SQLException e) {
            log.error("Erreur DB: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReservation(@PathVariable Integer id, @Valid @RequestBody Reservation reservationDetails) {
        log.info("PUT /api/reservations/{} - Modification de la réservation", id);
        try {
            reservationDetails.setId(id);
            boolean updated = reservationDao.update(reservationDetails);
            if (updated) {
                return ResponseEntity.ok(reservationDetails);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"Réservation non trouvée\"}");
        } catch (SQLException e) {
            log.error("Erreur DB: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Integer id) {
        log.info("DELETE /api/reservations/{} - Suppression de la réservation", id);
        try {
            Optional<Reservation> reservation = reservationDao.findById(id);
            if (reservation.isPresent()) {
                reservationDao.delete(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"Réservation non trouvée\"}");
        } catch (SQLException e) {
            log.error("Erreur DB: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
