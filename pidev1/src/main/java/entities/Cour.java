package entities;

import com.mysql.cj.conf.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import java.time.LocalDate;
import java.util.Date;

public class Cour {
    private int id_cours;
    private String titre_cours;
    private String description_cours;
    private LocalDate dateDebut_cours;
    private LocalDate dateFin_cours;
    private User user;

    public Cour() {
    }

    public Cour(int id_cours, String titre_cours, String description_cours, LocalDate dateDebut_cours, LocalDate dateFin_cours, User u) {
        this.id_cours = id_cours;
        this.titre_cours = titre_cours;
        this.description_cours = description_cours;
        this.dateDebut_cours = dateDebut_cours;
        this.dateFin_cours = dateFin_cours;
        this.user = u;
    }

    public Cour(String titre_cours, String description_cours, LocalDate dateDebut_cours, LocalDate dateFin_cours, User u) {
        this.titre_cours = titre_cours;
        this.description_cours = description_cours;
        this.dateDebut_cours = dateDebut_cours;
        this.dateFin_cours = dateFin_cours;
        this.user = u;
    }

    public Cour(int idCours) {
    }

    public int getId_cours() {
        return id_cours;
    }

    public void setId_cours(int id_cours) {
        this.id_cours = id_cours;
    }

    public String getTitre_cours() {
        return titre_cours;
    }

    public void setTitre_cours(String titre_cours) {
        this.titre_cours = titre_cours;
    }

    public String getDescription_cours() {
        return description_cours;
    }

    public void setDescription_cours(String description_cours) {
        this.description_cours = description_cours;
    }

    public LocalDate getDateDebut_cours() {
        return dateDebut_cours;
    }

    public void setDateDebut_cours(LocalDate dateDebut_cours) {
        this.dateDebut_cours = dateDebut_cours;
    }

    public LocalDate getDateFin_cours() {
        return dateFin_cours;
    }

    public void setDateFin_cours(LocalDate dateFin_cours) {
        this.dateFin_cours = dateFin_cours;
    }

    public int getId_user() {
        return user.getId_user();
    }

    public void setId_user(User u) {
        this.user = u;
    }

    @Override
    public String toString() {
        return "Cour{" +
                "id_cours=" + id_cours +
                ", titre_cours='" + titre_cours + '\'' +
                ", description_cours='" + description_cours + '\'' +
                ", dateDebut_cours=" + dateDebut_cours +
                ", dateFin_cours=" + dateFin_cours +

                '}';
    }

}



