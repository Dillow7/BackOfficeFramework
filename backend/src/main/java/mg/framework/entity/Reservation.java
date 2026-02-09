package mg.framework.entity;

import java.time.LocalDateTime;

public class Reservation {
    private Integer id;
    private String idClient;
    private int nbPassager;
    private LocalDateTime dateHeureArrive;
    private Integer idHotel;

    public Reservation() {
    }

    public Reservation(Integer id, String idClient, int nbPassager, LocalDateTime dateHeureArrive, Integer idHotel) {
        this.id = id;
        this.idClient = idClient;
        this.nbPassager = nbPassager;
        this.dateHeureArrive = dateHeureArrive;
        this.idHotel = idHotel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public LocalDateTime getDateHeureArrive() {
        return dateHeureArrive;
    }

    public void setDateHeureArrive(LocalDateTime dateHeureArrive) {
        this.dateHeureArrive = dateHeureArrive;
    }

    public Integer getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(Integer idHotel) {
        this.idHotel = idHotel;
    }
}
