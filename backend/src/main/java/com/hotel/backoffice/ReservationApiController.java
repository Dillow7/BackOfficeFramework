package com.hotel.backoffice;

import com.hotel.backoffice.model.ApiResponse;
import com.hotel.backoffice.model.Reservation;
import mg.framework.annotations.Controlleur;
import mg.framework.annotations.GetMapping;
import mg.framework.annotations.Json;
import mg.framework.annotations.RequestParam;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


// Contrôleur pour gérer les opérations liées aux réservations et à l'assignation des véhicules
@Controlleur
public class ReservationApiController {
    private final ReservationDao reservationDao = new ReservationDao();

    @GetMapping("/api/reservations")
    @Json
    public List<java.util.Map<String,Object>> list(@RequestParam("date") String date) {
        try {
            List<Reservation> reservations;
            if (date != null && !date.isEmpty()) {
                reservations = reservationDao.findByDate(LocalDate.parse(date));
            } else {
                reservations = reservationDao.findAll();
            }
            java.util.List<java.util.Map<String,Object>> out = new java.util.ArrayList<>();
            for (Reservation r : reservations) {
                java.util.Map<String,Object> m = new java.util.HashMap<>();
                m.put("id", r.getId());
                m.put("idClient", r.getIdClient());
                m.put("nbPassager", r.getNbPassager());
                m.put("dateHeureArrive", r.getDateHeureArrive() != null ? r.getDateHeureArrive().toString() : null);
                m.put("idHotel", r.getIdHotel());
                out.add(m);
            }
            return out;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }
}
