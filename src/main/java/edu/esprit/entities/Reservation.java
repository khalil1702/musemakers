package edu.esprit.entities;

import java.net.ProtocolFamily;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class Reservation {
    private int idReservation;
    private Timestamp dateReser;
    private int ticketsNumber;
    private int accessByAdmin;

    // Foreign key
    private Exposition exposition;
    private User client;

    public Reservation() {

    }

    public Reservation(int idReservation, Timestamp dateReser, int ticketsNumber, int accessByAdmin, Exposition exposition, User client) {
        this.idReservation = idReservation;
        this.dateReser = dateReser;
        this.ticketsNumber = ticketsNumber;
        this.accessByAdmin = accessByAdmin;
        this.exposition = exposition;
        this.client = client;
    }

    public Reservation(Timestamp dateReser, int ticketsNumber, int accessByAdmin, Exposition exposition, User client) {
        this.dateReser = dateReser;
        this.ticketsNumber = ticketsNumber;
        this.accessByAdmin = accessByAdmin;
        this.exposition = exposition;
        this.client = client;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public Timestamp getDateReser() {
        return dateReser;
    }

    public void setDateReser(Timestamp dateReser) {
        this.dateReser = dateReser;
    }

    public int getTicketsNumber() {
        return ticketsNumber;
    }

    public void setTicketsNumber(int ticketsNumber) {
        if (ticketsNumber > 0) {
            this.ticketsNumber = ticketsNumber;
        } else {
            System.out.println("Erreur : Le nombre de tickets doit être positif.");
        }
    }

    public int getAccessByAdmin() {

        return accessByAdmin;
    }

    public void setAccessByAdmin(int accessByAdmin) {
        this.accessByAdmin = accessByAdmin;
    }

    public Exposition getExposition() {
        return exposition;
    }

    public void setExposition(Exposition exposition) {
        this.exposition = exposition;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation that)) return false;
        return getIdReservation() == that.getIdReservation();
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "idReservation=" + idReservation +
                ", dateReser=" + dateReser +
                ", ticketsNumber=" + ticketsNumber +
                ", statutReservation=" + accessByAdmin +
                ", exposition=" + exposition +
                ", client=" + client +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdReservation());
    }

//
public String getExpositionNom() {
    return exposition.getNom();
}

    public String getExpositionTheme() {
        return exposition.getTheme();
    }

    public String getUserEmail() {
        return client.getEmail();
    }
    public Date getExpositionDateD() {
        return exposition.getDateDebut();
    }
    public Date getExpositionDateF() {
        return exposition.getDateFin();
    }

    public Time getHeureDebutExpo(){
        return exposition.getHeure_debut();
    }

    public Time getHeureFinExpo(){
        return exposition.getHeure_fin();
    }


}
