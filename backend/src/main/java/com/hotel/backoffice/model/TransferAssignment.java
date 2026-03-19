package com.hotel.backoffice.model;

import java.time.LocalDateTime;

public class TransferAssignment {
    private int reservationId;
    private String idClient;
    private int nbPassager;

    private int idHotel;
    private String hotelNom;
    private String hotelCode;
    private String aeroportCode;

    private LocalDateTime heureDepartAeroport;
    private LocalDateTime heureArriveeHotel;
    private double distanceKm;
    private long dureeTrajetMinutes;

    private Integer vehiculeId;
    private String vehiculeReference;
    private Integer vehiculeNbPlace;
    private String vehiculeTypeCarburant;
    private Integer trajetId;
    private Integer ordreDepot;
    private Integer passagersTrajet;
    private Integer nbReservationsTrajet;
    private String motif;
    private Long tempsAttenteEstimeMinutes;
    private String detailsTrajet;
    private Double kmParcourusTrajet;
    private LocalDateTime heureArriveeTrajet;

    public boolean isAssigned() {
        return vehiculeId != null;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public int getNbPassager() {
        return nbPassager;
    }

    public void setNbPassager(int nbPassager) {
        this.nbPassager = nbPassager;
    }

    public int getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(int idHotel) {
        this.idHotel = idHotel;
    }

    public String getHotelNom() {
        return hotelNom;
    }

    public void setHotelNom(String hotelNom) {
        this.hotelNom = hotelNom;
    }

    public String getHotelCode() {
        return hotelCode;
    }

    public void setHotelCode(String hotelCode) {
        this.hotelCode = hotelCode;
    }

    public String getAeroportCode() {
        return aeroportCode;
    }

    public void setAeroportCode(String aeroportCode) {
        this.aeroportCode = aeroportCode;
    }

    public LocalDateTime getHeureDepartAeroport() {
        return heureDepartAeroport;
    }

    public void setHeureDepartAeroport(LocalDateTime heureDepartAeroport) {
        this.heureDepartAeroport = heureDepartAeroport;
    }

    public LocalDateTime getHeureArriveeHotel() {
        return heureArriveeHotel;
    }

    public void setHeureArriveeHotel(LocalDateTime heureArriveeHotel) {
        this.heureArriveeHotel = heureArriveeHotel;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public long getDureeTrajetMinutes() {
        return dureeTrajetMinutes;
    }

    public void setDureeTrajetMinutes(long dureeTrajetMinutes) {
        this.dureeTrajetMinutes = dureeTrajetMinutes;
    }

    public Integer getVehiculeId() {
        return vehiculeId;
    }

    public void setVehiculeId(Integer vehiculeId) {
        this.vehiculeId = vehiculeId;
    }

    public String getVehiculeReference() {
        return vehiculeReference;
    }

    public void setVehiculeReference(String vehiculeReference) {
        this.vehiculeReference = vehiculeReference;
    }

    public Integer getVehiculeNbPlace() {
        return vehiculeNbPlace;
    }

    public void setVehiculeNbPlace(Integer vehiculeNbPlace) {
        this.vehiculeNbPlace = vehiculeNbPlace;
    }

    public String getVehiculeTypeCarburant() {
        return vehiculeTypeCarburant;
    }

    public void setVehiculeTypeCarburant(String vehiculeTypeCarburant) {
        this.vehiculeTypeCarburant = vehiculeTypeCarburant;
    }

    public Integer getTrajetId() {
        return trajetId;
    }

    public void setTrajetId(Integer trajetId) {
        this.trajetId = trajetId;
    }

    public Integer getOrdreDepot() {
        return ordreDepot;
    }

    public void setOrdreDepot(Integer ordreDepot) {
        this.ordreDepot = ordreDepot;
    }

    public Integer getPassagersTrajet() {
        return passagersTrajet;
    }

    public void setPassagersTrajet(Integer passagersTrajet) {
        this.passagersTrajet = passagersTrajet;
    }

    public Integer getNbReservationsTrajet() {
        return nbReservationsTrajet;
    }

    public void setNbReservationsTrajet(Integer nbReservationsTrajet) {
        this.nbReservationsTrajet = nbReservationsTrajet;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Long getTempsAttenteEstimeMinutes() {
        return tempsAttenteEstimeMinutes;
    }

    public void setTempsAttenteEstimeMinutes(Long tempsAttenteEstimeMinutes) {
        this.tempsAttenteEstimeMinutes = tempsAttenteEstimeMinutes;
    }

    public String getDetailsTrajet() {
        return detailsTrajet;
    }

    public void setDetailsTrajet(String detailsTrajet) {
        this.detailsTrajet = detailsTrajet;
    }

    public Double getKmParcourusTrajet() {
        return kmParcourusTrajet;
    }

    public void setKmParcourusTrajet(Double kmParcourusTrajet) {
        this.kmParcourusTrajet = kmParcourusTrajet;
    }

    public LocalDateTime getHeureArriveeTrajet() {
        return heureArriveeTrajet;
    }

    public void setHeureArriveeTrajet(LocalDateTime heureArriveeTrajet) {
        this.heureArriveeTrajet = heureArriveeTrajet;
    }
}
