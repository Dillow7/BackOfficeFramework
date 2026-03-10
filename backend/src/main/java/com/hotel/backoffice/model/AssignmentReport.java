package com.hotel.backoffice.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AssignmentReport {
    private LocalDate date;
    private double vitesseMoyenneKmh;
    private int tempsAttenteMaxMinutes;
    private boolean bufferDepartActif;
    private int bufferDepartMinutes;
    private List<TransferAssignment> assigned = new ArrayList<>();
    private List<TransferAssignment> unassigned = new ArrayList<>();

    public int getTotalReservations() {
        return assigned.size() + unassigned.size();
    }

    public int getTotalTrajets() {
        java.util.Set<Integer> trajetIds = new java.util.HashSet<>();
        for (TransferAssignment assignment : assigned) {
            if (assignment.getTrajetId() != null) {
                trajetIds.add(assignment.getTrajetId());
            }
        }
        return trajetIds.size();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getVitesseMoyenneKmh() {
        return vitesseMoyenneKmh;
    }

    public void setVitesseMoyenneKmh(double vitesseMoyenneKmh) {
        this.vitesseMoyenneKmh = vitesseMoyenneKmh;
    }

    public int getTempsAttenteMaxMinutes() {
        return tempsAttenteMaxMinutes;
    }

    public void setTempsAttenteMaxMinutes(int tempsAttenteMaxMinutes) {
        this.tempsAttenteMaxMinutes = tempsAttenteMaxMinutes;
    }

    public boolean isBufferDepartActif() {
        return bufferDepartActif;
    }

    public void setBufferDepartActif(boolean bufferDepartActif) {
        this.bufferDepartActif = bufferDepartActif;
    }

    public int getBufferDepartMinutes() {
        return bufferDepartMinutes;
    }

    public void setBufferDepartMinutes(int bufferDepartMinutes) {
        this.bufferDepartMinutes = bufferDepartMinutes;
    }

    public List<TransferAssignment> getAssigned() {
        return assigned;
    }

    public void setAssigned(List<TransferAssignment> assigned) {
        this.assigned = assigned;
    }

    public List<TransferAssignment> getUnassigned() {
        return unassigned;
    }

    public void setUnassigned(List<TransferAssignment> unassigned) {
        this.unassigned = unassigned;
    }
}
