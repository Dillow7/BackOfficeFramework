package com.hotel.backoffice.model;

public class Vehicule {
    private int id;
    private String reference;
    private int nbPlace;
    private String typeCarburant; // Diesel, Essence, Hybride, ES

    public Vehicule() {}

    public Vehicule(int id, String reference, int nbPlace, String typeCarburant) {
        this.id = id;
        this.reference = reference;
        this.nbPlace = nbPlace;
        this.typeCarburant = typeCarburant;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public int getNbPlace() { return nbPlace; }
    public void setNbPlace(int nbPlace) { this.nbPlace = nbPlace; }

    public String getTypeCarburant() { return typeCarburant; }
    public void setTypeCarburant(String typeCarburant) { this.typeCarburant = typeCarburant; }
}
