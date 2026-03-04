package com.hotel.backoffice.model;

public class Hotel {
    private int idHotel;
    private String nom;
    private String code;
    private boolean aeroport;

    public int getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(int idHotel) {
        this.idHotel = idHotel;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isAeroport() {
        return aeroport;
    }

    public void setAeroport(boolean aeroport) {
        this.aeroport = aeroport;
    }
}
