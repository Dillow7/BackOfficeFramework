package com.hotel.backoffice;

import com.hotel.backoffice.model.ApiResponse;
import com.hotel.backoffice.model.Reservation;
import mg.framework.annotations.Controlleur;
import mg.framework.annotations.GetMapping;
import mg.framework.annotations.Json;
import mg.framework.annotations.PostMapping;
import mg.framework.annotations.RequestParam;

import java.sql.Timestamp;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// Contrôleur pour gérer les opérations liées aux réservations et à l'assignation des véhicules
@Controlleur
public class ReservationApiController {
    private final ReservationDao reservationDao = new ReservationDao();
    private final TokenDao tokenDao = new TokenDao();

    @GetMapping("/api/reservations")
    @Json
    public List<Map<String,Object>> list(@RequestParam("date") String date) {
        try {
            List<Reservation> reservations;
            if (date != null && !date.isEmpty()) {
                reservations = reservationDao.findByDate(LocalDate.parse(date));
            } else {
                reservations = reservationDao.findAll();
            }
            List<Map<String,Object>> out = new ArrayList<>();
            for (Reservation r : reservations) {
                out.add(toMap(r));
            }
            return out;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @GetMapping("/api/reservations/by-id")
    @Json
    public Map<String, Object> getById(@RequestParam("id") String id) {
        try {
            int reservationId = Integer.parseInt(id);
            Reservation r = reservationDao.findById(reservationId);
            if (r == null) {
                Map<String, Object> err = new HashMap<>();
                err.put("error", "Réservation non trouvée");
                return err;
            }
            return toMap(r);
        } catch (Exception e) {
            Map<String, Object> err = new HashMap<>();
            err.put("error", "Requête invalide");
            return err;
        }
    }

    @PostMapping("/api/reservations")
    @Json
    public ApiResponse<Map<String, Object>> create(
            @RequestParam("token") String token,
            @RequestParam("id_client") String idClient,
            @RequestParam("nb_passager") String nbPassager,
            @RequestParam("date_heure_arrive") String dateHeureArrive,
            @RequestParam("id_hotel") String idHotel
    ) {
        try {
            if (token == null || token.isEmpty() || !tokenDao.isTokenValid(token)) {
                return new ApiResponse<>("error", 401, Map.of("error", "Token invalide ou expiré"));
            }

            int nb = Integer.parseInt(nbPassager);
            int hid = Integer.parseInt(idHotel);
            LocalDateTime ldt = LocalDateTime.parse(dateHeureArrive, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            reservationDao.insert(idClient, nb, Timestamp.valueOf(ldt), hid);
            return new ApiResponse<>("success", 201, Map.of("message", "Réservation créée"));
        } catch (Exception e) {
            return new ApiResponse<>("error", 400, Map.of("error", "Requête invalide"));
        }
    }

    @PostMapping("/api/reservations/update")
    @Json
    public ApiResponse<Map<String, Object>> update(
            @RequestParam("token") String token,
            @RequestParam("id") String id,
            @RequestParam("id_client") String idClient,
            @RequestParam("nb_passager") String nbPassager,
            @RequestParam("date_heure_arrive") String dateHeureArrive,
            @RequestParam("id_hotel") String idHotel
    ) {
        try {
            if (token == null || token.isEmpty() || !tokenDao.isTokenValid(token)) {
                return new ApiResponse<>("error", 401, Map.of("error", "Token invalide ou expiré"));
            }
            int rid = Integer.parseInt(id);
            int nb = Integer.parseInt(nbPassager);
            int hid = Integer.parseInt(idHotel);
            LocalDateTime ldt = LocalDateTime.parse(dateHeureArrive, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            reservationDao.update(rid, idClient, nb, Timestamp.valueOf(ldt), hid);
            return new ApiResponse<>("success", 200, Map.of("message", "Réservation mise à jour"));
        } catch (Exception e) {
            return new ApiResponse<>("error", 400, Map.of("error", "Requête invalide"));
        }
    }

    @PostMapping("/api/reservations/delete")
    @Json
    public ApiResponse<Map<String, Object>> delete(
            @RequestParam("token") String token,
            @RequestParam("id") String id
    ) {
        try {
            if (token == null || token.isEmpty() || !tokenDao.isTokenValid(token)) {
                return new ApiResponse<>("error", 401, Map.of("error", "Token invalide ou expiré"));
            }
            int rid = Integer.parseInt(id);
            reservationDao.delete(rid);
            return new ApiResponse<>("success", 200, Map.of("message", "Réservation supprimée"));
        } catch (Exception e) {
            return new ApiResponse<>("error", 400, Map.of("error", "Requête invalide"));
        }
    }

    private Map<String, Object> toMap(Reservation r) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", r.getId());
        m.put("idClient", r.getIdClient());
        m.put("nbPassager", r.getNbPassager());
        m.put("dateHeureArrive", r.getDateHeureArrive() != null ? r.getDateHeureArrive().toString() : null);
        m.put("idHotel", r.getIdHotel());
        return m;
    }
}
