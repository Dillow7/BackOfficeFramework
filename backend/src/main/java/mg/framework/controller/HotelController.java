package mg.framework.controller;

import mg.framework.entity.Hotel;
import mg.framework.dao.HotelDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/hotels")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class HotelController {
    
    private final HotelDao hotelDao = new HotelDao();
    
    @GetMapping
    public ResponseEntity<List<Hotel>> getAllHotels() {
        log.info("GET /api/hotels - Récupération de tous les hôtels");
        try {
            List<Hotel> hotels = hotelDao.findAll();
            return ResponseEntity.ok(hotels);
        } catch (SQLException e) {
            log.error("Erreur DB: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getHotelById(@PathVariable Integer id) {
        log.info("GET /api/hotels/{} - Récupération de l'hôtel", id);
        try {
            Optional<Hotel> hotel = hotelDao.findById(id);
            if (hotel.isPresent()) {
                return ResponseEntity.ok(hotel.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"Hôtel non trouvé\"}");
        } catch (SQLException e) {
            log.error("Erreur DB: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createHotel(@Valid @RequestBody Hotel hotel) {
        log.info("POST /api/hotels - Création d'un nouvel hôtel: {}", hotel.getNom());
        try {
            Hotel createdHotel = hotelDao.save(hotel);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdHotel);
        } catch (SQLException e) {
            log.error("Erreur DB: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateHotel(@PathVariable Integer id, @Valid @RequestBody Hotel hotelDetails) {
        log.info("PUT /api/hotels/{} - Modification de l'hôtel", id);
        try {
            hotelDetails.setId(id);
            boolean updated = hotelDao.update(hotelDetails);
            if (updated) {
                return ResponseEntity.ok(hotelDetails);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"Hôtel non trouvé\"}");
        } catch (SQLException e) {
            log.error("Erreur DB: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHotel(@PathVariable Integer id) {
        log.info("DELETE /api/hotels/{} - Suppression de l'hôtel", id);
        try {
            Optional<Hotel> hotel = hotelDao.findById(id);
            if (hotel.isPresent()) {
                hotelDao.delete(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"Hôtel non trouvé\"}");
        } catch (SQLException e) {
            log.error("Erreur DB: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
