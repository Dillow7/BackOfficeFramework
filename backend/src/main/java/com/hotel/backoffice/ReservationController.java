package com.hotel.backoffice;

import com.hotel.backoffice.model.AssignmentReport;
import mg.framework.annotations.Controlleur;
import mg.framework.annotations.GetMapping;
import mg.framework.annotations.PostMapping;
import mg.framework.annotations.RequestParam;
import mg.framework.model.ModelView;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


// Contrôleur pour gérer les opérations liées aux réservations et à l'assignation des véhicules
@Controlleur
public class ReservationController {
    private final ReservationDao reservationDao = new ReservationDao();
    private final VehicleAssignmentService vehicleAssignmentService = new VehicleAssignmentService();

    @GetMapping("/reservations")
    public ModelView list(
        @RequestParam("date") String dateValue,
        @RequestParam("wait_time_minutes") String waitTimeMinutes
    ) {
        ModelView mv = new ModelView("/WEB-INF/jsp/reservations.jsp");

        LocalDate selectedDate = LocalDate.now();
        if (dateValue != null && !dateValue.trim().isEmpty()) {
            try {
                selectedDate = LocalDate.parse(dateValue.trim());
            } catch (Exception e) {
                mv.addAttribute("error", "Date invalide. Format attendu: YYYY-MM-DD");
            }
        }

        try {
            Integer waitOverride = null;
            if (waitTimeMinutes != null && !waitTimeMinutes.trim().isEmpty()) {
                waitOverride = Integer.parseInt(waitTimeMinutes.trim());
            }

            AssignmentReport report = vehicleAssignmentService.buildDailyReport(selectedDate, waitOverride, null);
            mv.addAttribute("selectedDate", selectedDate.toString());
            mv.addAttribute("vitesseMoyenne", report.getVitesseMoyenneKmh());
            mv.addAttribute("tempsAttenteMax", report.getTempsAttenteMaxMinutes());
            mv.addAttribute("bufferActif", report.isBufferDepartActif());
            mv.addAttribute("bufferMinutes", report.getBufferDepartMinutes());
            mv.addAttribute("waitTimeMinutes", waitOverride);
            mv.addAttribute("totalReservations", report.getTotalReservations());
            mv.addAttribute("totalTrajets", report.getTotalTrajets());
            mv.addAttribute("assignedCount", report.getAssigned().size());
            mv.addAttribute("unassignedCount", report.getUnassigned().size());
            mv.addAttribute("assignations", report.getAssigned());
            mv.addAttribute("nonAssignees", report.getUnassigned());
        } catch (Exception e) {
            mv.addAttribute("error",
                "Erreur lors du calcul d'assignation: " + e.getMessage()
                    + ". Vérifiez la migration SQL Sprint 3 (hotel.code, hotel.aeroport, table distance).");
        }

        return mv;
    }

    @GetMapping("/reservations/new")
    public ModelView form() {
        return new ModelView("/WEB-INF/jsp/reservation-form.jsp");
    }

    @PostMapping("/reservations/new")
    public ModelView submit(
        @RequestParam("id_client") String idClient,
        @RequestParam("nb_passager") String nbPassager,
        @RequestParam("date_heure_arrive") String dateHeureArriveeAvion,
        @RequestParam("id_hotel") String idHotel
    ) {
        ModelView mv = new ModelView("/WEB-INF/jsp/reservation-form.jsp");

        if (idClient == null || idClient.length() != 4) {
            mv.addAttribute("error", "id_client doit contenir 4 caractères");
            return mv;
        }

        try {
            int nb = Integer.parseInt(nbPassager);
            int hotel = Integer.parseInt(idHotel);
            LocalDateTime ldt = LocalDateTime.parse(dateHeureArriveeAvion, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime dateCreation = LocalDateTime.now(); // Date de création de la réservation
            reservationDao.insert(idClient, nb, Timestamp.valueOf(ldt), Timestamp.valueOf(dateCreation), hotel);
            mv.addAttribute("success", "Réservation enregistrée");
        } catch (Exception e) {
            mv.addAttribute("error", "Erreur lors de l'enregistrement: " + e.getMessage());
        }

        return mv;
    }
}
