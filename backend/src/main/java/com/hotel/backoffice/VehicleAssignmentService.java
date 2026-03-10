package com.hotel.backoffice;

import com.hotel.backoffice.model.AssignmentReport;
import com.hotel.backoffice.model.Hotel;
import com.hotel.backoffice.model.Reservation;
import com.hotel.backoffice.model.TransferAssignment;
import com.hotel.backoffice.model.Vehicule;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class VehicleAssignmentService {
    private static final double DEFAULT_VITESSE_MOYENNE_KMH = 40.0;
    private static final int DEFAULT_TEMPS_ATTENTE_MAX_MINUTES = 30;
    private static final boolean DEFAULT_ACTIVER_BUFFER_DEPART = false;
    private static final int DEFAULT_BUFFER_DEPART_MINUTES = 0;

    private final ReservationDao reservationDao = new ReservationDao();
    private final VehiculeDao vehiculeDao = new VehiculeDao();
    private final HotelDao hotelDao = new HotelDao();
    private final DistanceDao distanceDao = new DistanceDao();

    public AssignmentReport buildDailyReport(LocalDate date) throws SQLException {
        double vitesseMoyenne = resolveDoubleEnv("VITESSE_MOYENNE_KMH", DEFAULT_VITESSE_MOYENNE_KMH);
        int tempsAttenteMax = resolveIntEnv("TEMPS_ATTENTE_MAX_MINUTES", DEFAULT_TEMPS_ATTENTE_MAX_MINUTES);
        boolean bufferDepartActif = resolveBooleanEnv("ACTIVER_BUFFER_DEPART", DEFAULT_ACTIVER_BUFFER_DEPART);
        int bufferDepartMinutes = resolveIntEnv("BUFFER_DEPART_MINUTES", DEFAULT_BUFFER_DEPART_MINUTES);

        List<Reservation> reservations = reservationDao.findByDate(date);
        reservations.sort(Comparator.comparing(Reservation::getDateHeureArrive, Comparator.nullsLast(LocalDateTime::compareTo)));

        List<Vehicule> vehicules = vehiculeDao.findAll();
        List<Hotel> hotels = hotelDao.findAll();
        Map<Integer, Hotel> hotelsById = mapHotelsById(hotels);
        List<Hotel> airports = findAirports(hotels);
        Map<String, Double> distancesKm = distanceDao.findAllDistancesKm();

        AssignmentReport report = new AssignmentReport();
        report.setDate(date);
        report.setVitesseMoyenneKmh(vitesseMoyenne);
        report.setTempsAttenteMaxMinutes(tempsAttenteMax);
        report.setBufferDepartActif(bufferDepartActif);
        report.setBufferDepartMinutes(bufferDepartMinutes);

        int maxVehicleCapacity = findMaxVehicleCapacity(vehicules);
        List<AssignmentCandidate> pending = new ArrayList<>();

        for (Reservation reservation : reservations) {
            TransferAssignment assignment = buildBaseAssignment(reservation, hotelsById);
            Hotel hotel = hotelsById.get(reservation.getIdHotel());

            if (hotel == null) {
                markUnassigned(report, assignment, "Hôtel introuvable");
                continue;
            }
            if (hotel.isAeroport()) {
                markUnassigned(report, assignment, "Le lieu de destination est déjà un aéroport");
                continue;
            }
            if (isBlank(hotel.getCode())) {
                markUnassigned(report, assignment, "Code lieu manquant pour l'hôtel");
                continue;
            }
            if (airports.isEmpty()) {
                markUnassigned(report, assignment, "Aucun aéroport configuré (hotel.aeroport)");
                continue;
            }
            if (reservation.getDateHeureArrive() == null) {
                markUnassigned(report, assignment, "Date d'arrivée avion manquante");
                continue;
            }

            AirportChoice nearestAirport = chooseNearestAirport(hotel.getCode(), airports, distancesKm);
            if (nearestAirport == null) {
                markUnassigned(report, assignment, "Distance introuvable entre aéroport et hôtel");
                continue;
            }

            if (reservation.getNbPassager() > maxVehicleCapacity) {
                markUnassigned(report, assignment, "Pas de véhicule avec capacité suffisante");
                continue;
            }

            long directTravelMinutes = computeTravelMinutes(nearestAirport.distanceKm, vitesseMoyenne);
            LocalDateTime flightArrival = reservation.getDateHeureArrive();
            LocalDateTime plannedDeparture = flightArrival.plusMinutes(bufferDepartActif ? bufferDepartMinutes : 0L);

            assignment.setAeroportCode(nearestAirport.airportCode);
            assignment.setDistanceKm(nearestAirport.distanceKm);
            assignment.setDureeTrajetMinutes(directTravelMinutes);

            AssignmentCandidate candidate = new AssignmentCandidate();
            candidate.reservationId = reservation.getId();
            candidate.hotelCode = hotel.getCode();
            candidate.airportCode = nearestAirport.airportCode;
            candidate.nbPassager = reservation.getNbPassager();
            candidate.readyAt = plannedDeparture;
            candidate.airportToHotelDistanceKm = nearestAirport.distanceKm;
            candidate.assignment = assignment;
            pending.add(candidate);
        }

        if (vehicules.isEmpty()) {
            for (AssignmentCandidate candidate : pending) {
                markUnassigned(report, candidate.assignment, "Aucun véhicule disponible");
            }
            return report;
        }

        List<VehicleState> vehicleStates = new ArrayList<>();
        for (Vehicule vehicule : vehicules) {
            VehicleState state = new VehicleState();
            state.vehicule = vehicule;
            state.availableAt = LocalDateTime.MIN;
            state.currentAirportCode = null;
            vehicleStates.add(state);
        }

        int nextTrajetId = 1;
        while (!pending.isEmpty()) {
            TripPlan bestTrip = chooseBestTrip(pending, vehicleStates, distancesKm, vitesseMoyenne, tempsAttenteMax);
            if (bestTrip == null) {
                for (AssignmentCandidate candidate : pending) {
                    markUnassigned(report, candidate.assignment,
                        "Conflit d'horaires (temps d'attente max: " + tempsAttenteMax + " min)");
                }
                pending.clear();
                break;
            }

            bestTrip.trajetId = nextTrajetId++;
            applyTrip(bestTrip, pending, report);
        }

        report.getAssigned().sort(
            Comparator
                .comparing(TransferAssignment::getHeureDepartAeroport, Comparator.nullsLast(LocalDateTime::compareTo))
                .thenComparing(TransferAssignment::getTrajetId, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(TransferAssignment::getOrdreDepot, Comparator.nullsLast(Integer::compareTo))
        );

        return report;
    }

    private TripPlan chooseBestTrip(
        List<AssignmentCandidate> pending,
        List<VehicleState> vehicleStates,
        Map<String, Double> distancesKm,
        double vitesseMoyenne,
        int tempsAttenteMax
    ) {
        TripPlan best = null;

        List<String> airportsWithPending = airportsWithPendingReservations(pending);
        for (VehicleState state : vehicleStates) {
            List<String> candidateAirports = new ArrayList<>();
            if (!isBlank(state.currentAirportCode)) {
                candidateAirports.add(state.currentAirportCode);
            } else {
                candidateAirports.addAll(airportsWithPending);
            }

            for (String airportCode : candidateAirports) {
                TripPlan candidatePlan = buildTripPlanForVehicleAirport(
                    pending,
                    state,
                    airportCode,
                    distancesKm,
                    vitesseMoyenne,
                    tempsAttenteMax
                );
                if (candidatePlan == null) {
                    continue;
                }
                if (best == null || compareTripPlans(candidatePlan, best) < 0) {
                    best = candidatePlan;
                }
            }
        }

        return best;
    }

    private TripPlan buildTripPlanForVehicleAirport(
        List<AssignmentCandidate> pending,
        VehicleState state,
        String airportCode,
        Map<String, Double> distancesKm,
        double vitesseMoyenne,
        int tempsAttenteMax
    ) {
        int capacity = state.vehicule.getNbPlace();

        List<AssignmentCandidate> airportPending = new ArrayList<>();
        for (AssignmentCandidate candidate : pending) {
            if (airportCode.equals(candidate.airportCode) && candidate.nbPassager <= capacity) {
                airportPending.add(candidate);
            }
        }
        if (airportPending.isEmpty()) {
            return null;
        }

        LocalDateTime earliestReady = null;
        for (AssignmentCandidate candidate : airportPending) {
            if (earliestReady == null || candidate.readyAt.isBefore(earliestReady)) {
                earliestReady = candidate.readyAt;
            }
        }

        LocalDateTime departure = maxDateTime(earliestReady, state.availableAt);

        List<AssignmentCandidate> readyCandidates = new ArrayList<>();
        for (AssignmentCandidate candidate : airportPending) {
            if (!candidate.readyAt.isAfter(departure)) {
                readyCandidates.add(candidate);
            }
        }
        if (readyCandidates.isEmpty()) {
            return null;
        }

        readyCandidates.sort(
            Comparator
                .comparing((AssignmentCandidate c) -> c.readyAt)
                .thenComparingDouble(c -> c.airportToHotelDistanceKm)
                .thenComparingInt(c -> c.reservationId)
        );

        List<AssignmentCandidate> selected = new ArrayList<>();
        int totalPassengers = 0;
        for (AssignmentCandidate candidate : readyCandidates) {
            if (selected.isEmpty()) {
                selected.add(candidate);
                totalPassengers += candidate.nbPassager;
                continue;
            }
            if (totalPassengers + candidate.nbPassager <= capacity) {
                selected.add(candidate);
                totalPassengers += candidate.nbPassager;
            }
        }

        if (selected.isEmpty()) {
            return null;
        }

        List<AssignmentCandidate> route = orderRoute(selected, distancesKm);

        Map<Integer, LocalDateTime> arrivalByReservationId = new HashMap<>();
        String currentCode = airportCode;
        LocalDateTime currentTime = departure;

        for (AssignmentCandidate candidate : route) {
            Double legDistance = DistanceDao.findSymmetricDistanceKm(distancesKm, currentCode, candidate.hotelCode);
            if (legDistance == null) {
                if (airportCode.equals(currentCode)) {
                    legDistance = candidate.airportToHotelDistanceKm;
                } else {
                    legDistance = DistanceDao.findSymmetricDistanceKm(distancesKm, airportCode, candidate.hotelCode);
                }
            }
            if (legDistance == null) {
                return null;
            }

            long legMinutes = computeTravelMinutes(legDistance, vitesseMoyenne);
            currentTime = currentTime.plusMinutes(legMinutes);
            arrivalByReservationId.put(candidate.reservationId, currentTime);
            currentCode = candidate.hotelCode;
        }

        Double returnDistance = DistanceDao.findSymmetricDistanceKm(distancesKm, currentCode, airportCode);
        if (returnDistance == null && !route.isEmpty()) {
            returnDistance = route.get(route.size() - 1).airportToHotelDistanceKm;
        }
        if (returnDistance == null) {
            return null;
        }

        long returnMinutes = computeTravelMinutes(returnDistance, vitesseMoyenne);
        LocalDateTime nextVehicleAvailableAt = currentTime.plusMinutes(returnMinutes + tempsAttenteMax);

        TripPlan tripPlan = new TripPlan();
        tripPlan.vehicleState = state;
        tripPlan.airportCode = airportCode;
        tripPlan.departure = departure;
        tripPlan.selectedCandidates = selected;
        tripPlan.route = route;
        tripPlan.totalPassengers = totalPassengers;
        tripPlan.arrivalByReservationId = arrivalByReservationId;
        tripPlan.nextVehicleAvailableAt = nextVehicleAvailableAt;
        return tripPlan;
    }

    private void applyTrip(TripPlan tripPlan, List<AssignmentCandidate> pending, AssignmentReport report) {
        int ordreDepot = 1;
        for (AssignmentCandidate candidate : tripPlan.route) {
            TransferAssignment assignment = candidate.assignment;
            assignment.setVehiculeId(tripPlan.vehicleState.vehicule.getId());
            assignment.setVehiculeReference(tripPlan.vehicleState.vehicule.getReference());
            assignment.setVehiculeNbPlace(tripPlan.vehicleState.vehicule.getNbPlace());
            assignment.setVehiculeTypeCarburant(tripPlan.vehicleState.vehicule.getTypeCarburant());
            assignment.setHeureDepartAeroport(tripPlan.departure);
            assignment.setHeureArriveeHotel(tripPlan.arrivalByReservationId.get(candidate.reservationId));
            assignment.setTrajetId(tripPlan.trajetId);
            assignment.setOrdreDepot(ordreDepot++);
            assignment.setPassagersTrajet(tripPlan.totalPassengers);
            assignment.setNbReservationsTrajet(tripPlan.route.size());
            report.getAssigned().add(assignment);
        }

        pending.removeAll(tripPlan.selectedCandidates);
        tripPlan.vehicleState.availableAt = tripPlan.nextVehicleAvailableAt;
        tripPlan.vehicleState.currentAirportCode = tripPlan.airportCode;
    }

    private int compareTripPlans(TripPlan left, TripPlan right) {
        int compare = left.departure.compareTo(right.departure);
        if (compare != 0) {
            return compare;
        }

        compare = Integer.compare(right.route.size(), left.route.size());
        if (compare != 0) {
            return compare;
        }

        compare = Integer.compare(left.vehicleState.vehicule.getNbPlace() - left.totalPassengers,
            right.vehicleState.vehicule.getNbPlace() - right.totalPassengers);
        if (compare != 0) {
            return compare;
        }

        compare = Integer.compare(isDiesel(left.vehicleState.vehicule) ? 0 : 1, isDiesel(right.vehicleState.vehicule) ? 0 : 1);
        if (compare != 0) {
            return compare;
        }

        compare = Integer.compare(left.vehicleState.vehicule.getNbPlace(), right.vehicleState.vehicule.getNbPlace());
        if (compare != 0) {
            return compare;
        }

        return Integer.compare(left.vehicleState.vehicule.getId(), right.vehicleState.vehicule.getId());
    }

    private List<AssignmentCandidate> orderRoute(List<AssignmentCandidate> selected, Map<String, Double> distancesKm) {
        List<AssignmentCandidate> ordered = new ArrayList<>();
        if (selected.isEmpty()) {
            return ordered;
        }

        AssignmentCandidate first = selected.get(0);
        ordered.add(first);
        if (selected.size() == 1) {
            return ordered;
        }

        List<AssignmentCandidate> others = new ArrayList<>(selected.subList(1, selected.size()));
        others.sort((left, right) -> {
            double leftDistance = resolveDistanceToFirst(first, left, distancesKm);
            double rightDistance = resolveDistanceToFirst(first, right, distancesKm);
            int compare = Double.compare(leftDistance, rightDistance);
            if (compare != 0) {
                return compare;
            }
            compare = left.readyAt.compareTo(right.readyAt);
            if (compare != 0) {
                return compare;
            }
            return Integer.compare(left.reservationId, right.reservationId);
        });

        ordered.addAll(others);
        return ordered;
    }

    private double resolveDistanceToFirst(
        AssignmentCandidate first,
        AssignmentCandidate candidate,
        Map<String, Double> distancesKm
    ) {
        Double distance = DistanceDao.findSymmetricDistanceKm(distancesKm, first.hotelCode, candidate.hotelCode);
        if (distance != null) {
            return distance;
        }
        return candidate.airportToHotelDistanceKm;
    }

    private TransferAssignment buildBaseAssignment(Reservation reservation, Map<Integer, Hotel> hotelsById) {
        TransferAssignment assignment = new TransferAssignment();
        assignment.setReservationId(reservation.getId());
        assignment.setIdClient(reservation.getIdClient());
        assignment.setNbPassager(reservation.getNbPassager());
        assignment.setIdHotel(reservation.getIdHotel());
        assignment.setHeureArriveeHotel(reservation.getDateHeureArrive());

        Hotel hotel = hotelsById.get(reservation.getIdHotel());
        if (hotel != null) {
            assignment.setHotelNom(hotel.getNom());
            assignment.setHotelCode(hotel.getCode());
        }
        return assignment;
    }

    private void markUnassigned(AssignmentReport report, TransferAssignment assignment, String motif) {
        assignment.setMotif(motif);
        report.getUnassigned().add(assignment);
    }

    // Méthodes utilitaires
    private AirportChoice chooseNearestAirport(String hotelCode, List<Hotel> airports, Map<String, Double> distancesKm) {
        AirportChoice choice = null;
        for (Hotel airport : airports) {
            if (isBlank(airport.getCode())) {
                continue;
            }
            Double distance = DistanceDao.findSymmetricDistanceKm(distancesKm, airport.getCode(), hotelCode);
            if (distance == null) {
                continue;
            }

            if (choice == null
                || distance < choice.distanceKm
                || (distance.equals(choice.distanceKm) && airport.getCode().compareTo(choice.airportCode) < 0)) {
                choice = new AirportChoice();
                choice.airportCode = airport.getCode();
                choice.distanceKm = distance;
            }
        }
        return choice;
    }

    // Trouve la capacité maximale parmi les véhicules
    private int findMaxVehicleCapacity(List<Vehicule> vehicules) {
        int max = 0;
        for (Vehicule vehicule : vehicules) {
            if (vehicule.getNbPlace() > max) {
                max = vehicule.getNbPlace();
            }
        }
        return max;
    }

    private Map<Integer, Hotel> mapHotelsById(List<Hotel> hotels) {
        Map<Integer, Hotel> map = new HashMap<>();
        for (Hotel hotel : hotels) {
            map.put(hotel.getIdHotel(), hotel);
        }
        return map;
    }

    private List<Hotel> findAirports(List<Hotel> hotels) {
        List<Hotel> airports = new ArrayList<>();
        for (Hotel hotel : hotels) {
            if (hotel.isAeroport()) {
                airports.add(hotel);
            }
        }
        return airports;
    }

    // Trouve la liste des codes aéroport présents dans les réservations en attente
    private List<String> airportsWithPendingReservations(List<AssignmentCandidate> pending) {
        List<String> airports = new ArrayList<>();
        for (AssignmentCandidate candidate : pending) {
            if (!airports.contains(candidate.airportCode)) {
                airports.add(candidate.airportCode);
            }
        }
        return airports;
    }

    private long computeTravelMinutes(double distanceKm, double vitesseMoyenneKmh) {
        double minutes = (distanceKm / vitesseMoyenneKmh) * 60.0;
        return Math.max(1L, (long) Math.ceil(minutes));
    }

    private LocalDateTime maxDateTime(LocalDateTime left, LocalDateTime right) {
        return left.isAfter(right) ? left : right;
    }

    private boolean isDiesel(Vehicule vehicule) {
        return vehicule.getTypeCarburant() != null
            && "diesel".equals(vehicule.getTypeCarburant().trim().toLowerCase(Locale.ROOT));
    }

    private static double resolveDoubleEnv(String envName, double defaultValue) {
        String raw = System.getenv(envName);
        if (raw == null || raw.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            double parsed = Double.parseDouble(raw.trim());
            return parsed > 0 ? parsed : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static int resolveIntEnv(String envName, int defaultValue) {
        String raw = System.getenv(envName);
        if (raw == null || raw.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            int parsed = Integer.parseInt(raw.trim());
            return parsed >= 0 ? parsed : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static boolean resolveBooleanEnv(String envName, boolean defaultValue) {
        String raw = System.getenv(envName);
        if (raw == null || raw.trim().isEmpty()) {
            return defaultValue;
        }
        String value = raw.trim().toLowerCase(Locale.ROOT);
        if ("true".equals(value) || "1".equals(value) || "yes".equals(value) || "on".equals(value)) {
            return true;
        }
        if ("false".equals(value) || "0".equals(value) || "no".equals(value) || "off".equals(value)) {
            return false;
        }
        return defaultValue;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static class AssignmentCandidate {
        private int reservationId;
        private String airportCode;
        private String hotelCode;
        private int nbPassager;
        private LocalDateTime readyAt;
        private double airportToHotelDistanceKm;
        private TransferAssignment assignment;
    }

    private static class VehicleState {
        private Vehicule vehicule;
        private LocalDateTime availableAt;
        private String currentAirportCode;
    }

    private static class TripPlan {
        private int trajetId;
        private VehicleState vehicleState;
        private String airportCode;
        private LocalDateTime departure;
        private List<AssignmentCandidate> selectedCandidates;
        private List<AssignmentCandidate> route;
        private int totalPassengers;
        private Map<Integer, LocalDateTime> arrivalByReservationId;
        private LocalDateTime nextVehicleAvailableAt;
    }

    private static class AirportChoice {
        private String airportCode;
        private Double distanceKm;
    }
}
