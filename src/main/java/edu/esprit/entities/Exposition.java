package edu.esprit.entities;


import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.sql.Date;
import java.util.Objects;

public class Exposition {
    private int id;
    private String nom;
    private Date dateDebut;
    private Date dateFin;
    private Time heure_debut;
    private Time heure_fin;
    private String description;
    private String theme;
    private String image;
    public Exposition(){}

    public Exposition(int id, String nom, Date dateDebut, Date dateFin, Time heure_debut, Time heure_fin, String description, String theme, String image) {
        this.id = id;
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.heure_debut = heure_debut;
        this.heure_fin = heure_fin;
        this.description = description;
        this.theme = theme;
        this.image = image;
    }

    public Exposition(String nom, Date dateDebut, Date dateFin, Time heure_debut, Time heure_fin, String description, String theme, String image) {
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.heure_debut = heure_debut;
        this.heure_fin = heure_fin;
        this.description = description;
        this.theme = theme;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {

        this.nom = nom;

    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;

    }
    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;

    }

    public Time getHeure_debut() {
        return heure_debut;
    }

    public void setHeure_debut(Time heure_debut) {
        this.heure_debut = heure_debut;
    }

    public Time getHeure_fin() {
        return heure_fin;
    }

    public void setHeure_fin(Time heure_fin) {
        this.heure_fin = heure_fin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exposition that)) return false;
        return getId() == that.getId();
    }

    @Override
    public String toString() {
        return "Exposition{" +
                "nom='" + nom + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", description='" + description + '\'' +
                ", theme='" + theme + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }


}
