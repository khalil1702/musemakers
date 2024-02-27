package entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;
import java.util.Objects;

public class Reclamation {
    private String userNom;
    private int idRec;
    private User user;  // Remplacez 'int idU' par 'User user'
    private String descriRec;
    private Date DateRec;
    private String CategorieRec;
    private String StatutRec;

    public Reclamation() {
    }

    public Reclamation(String descriRec, String categorieRec, String statutRec) {
        this.idRec = idRec;
        this.user = user;
        this.descriRec = descriRec;

        CategorieRec = categorieRec;
        StatutRec = statutRec;
    }

    public Reclamation(User user, String descriRec, Date dateRec, String categorieRec, String statutRec) {
        this.user = user;
        this.descriRec = descriRec;
        DateRec = dateRec;
        CategorieRec = categorieRec;
        StatutRec = statutRec;
    }

    public Reclamation(int idRec, User user, String descriRec, Date dateRec, String categorieRec, String statutRec) {
        this.idRec = idRec;
        this.user = user;
        this.descriRec = descriRec;
        DateRec = dateRec;
        CategorieRec = categorieRec;
        StatutRec = statutRec;
    }

    /* public Reclamation(User userAdd, String text, String text1, String text2) {
        }*/
    public String getNom_user() {
        return this.user.getNom_user();
    }


    public Reclamation(String descriRec, Date dateRec, String categorieRec, String statutRec) {
        this.descriRec = descriRec;
        DateRec = dateRec;
        CategorieRec = categorieRec;
        StatutRec = statutRec;
    }

    public int getIdRec() {
        return idRec;
    }

    public User getUser() {
        return user;
    }

    public String getDescriRec() {
        return descriRec;
    }

    public Date getDateRec() {
        return DateRec;
    }

    public String getCategorieRec() {
        return CategorieRec;
    }

    public String getStatutRec() {
        return StatutRec;
    }

    public void setIdRec(int idRec) {
        this.idRec = idRec;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDescriRec(String descriRec) {
        this.descriRec = descriRec;
    }

    public void setDateRec(Date dateRec) {
        DateRec = dateRec;
    }

    public void setCategorieRec(String categorieRec) {
        CategorieRec = categorieRec;
    }

    public void setStatutRec(String statutRec) {
        StatutRec = statutRec;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "idRec=" + idRec +
                ", idU=" + user.getId_user() +
                ", descriRec=" + descriRec +
                ", DateRec=" + DateRec +
                ", CategorieRec=" + CategorieRec +
                ", StatutRec=" + StatutRec +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reclamation that)) return false;
        return getIdRec() == that.getIdRec() && Objects.equals(getStatutRec(), that.getStatutRec());
    }


    public String getUserNom() {
        return userNom;
    }
    public void setUserNom(String userNom) {
        this.userNom = userNom;
    }
    public StringProperty userNomProperty() {
        return new SimpleStringProperty(getUserNom());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getIdRec(), getStatutRec());
    }

    public void setUser(String nomUser) {
    }

    public String getUserNom1() {
        return user != null ? user.getNom_user() : null;
    }

    // Ajoutez un accesseur pour la propriété activiteNom
    public StringProperty userNom1Property() {
        return new SimpleStringProperty(getUserNom1());
    }
}