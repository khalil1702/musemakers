package entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

public class Commentaire {
    private String userNom;

    private int idCom;
    private Reclamation reclamation;  // Référence à l'objet Reclamation
    private Date DateCom;
    private String ContenuCom;
    private User user;  // Remplacez 'int idU' par 'User user'


    public Commentaire() {
    }

    public Commentaire(Reclamation reclamation, Date dateCom, String contenuCom) {
        this.reclamation = reclamation;
        DateCom = dateCom;
        ContenuCom = contenuCom;
    }

    // Constructeur
    public Commentaire(int idCom, Reclamation reclamation, Date DateCom, String ContenuCom) {
        this.idCom = idCom;
        this.reclamation = reclamation;
        this.DateCom = DateCom;
        this.ContenuCom = ContenuCom;
    }

    // Getters
    public int getIdCom() {
        return idCom;
    }

    public Reclamation getReclamation() {
        return reclamation;
    }

    public int getIdRec() {
        return reclamation.getIdRec();  // Obtenir idRec via l'objet Reclamation
    }

    public Date getDateCom() {
        return DateCom;
    }

    public String getContenuCom() {
        return ContenuCom;
    }

    // Setters
    public void setIdCom(int idCom) {
        this.idCom = idCom;
    }

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
    }

    public void setDateCom(Date DateCom) {
        this.DateCom = DateCom;
    }

    public void setContenuCom(String ContenuCom) {
        this.ContenuCom = ContenuCom;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return " Nom du client : " + reclamation.getUser().getNom_user() + '\n' +
                " reclamation : " + reclamation.getDescriRec() + '\n' +
                " Commentaire : " + ContenuCom + '\n' +
                " Date : " + DateCom ;

    }

    public String getUserNom() {
        return userNom;
    }

    public void setUserNom(String userNom) {
        this.userNom = userNom;
    }

    // Ajoutez un accesseur pour la propriété activiteNom
    public StringProperty userNomProperty() {
        return new SimpleStringProperty(getUserNom());
    }
}