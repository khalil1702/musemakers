package entities;

import java.util.Date;

public class Client extends User {
    private Date date_de_naissance;
    private int num_tel;

    @Override

    public String toString() {
        return "Nom : " + getNom_user() + "\n" +
                "Prénom : " + getPrenom_user() + "\n" +
                "Email : " + getEmail() + "\n" +
                "Mot de passe : " + getMdp() + "\n" +
                "Numéro de téléphone : " + num_tel + "\n" +
                "Date de naissance : " + date_de_naissance;
    }


    public Date getDate_de_naissance() {
        return date_de_naissance;
    }

    public void setDate_de_naissance(Date date_de_naissance) {
        this.date_de_naissance = date_de_naissance;
    }

    public int getNum_tel() {
        return num_tel;
    }

    public void setNum_tel(int num_tel) {
        this.num_tel = num_tel;
    }

    public Client() {
    }

    public Client(int id_user, String nom_user, String prenom_user, String email, String mdp, Date date_de_naissance, int num_tel) {
        super(id_user, nom_user, prenom_user, email, mdp);
        this.date_de_naissance = date_de_naissance;
        this.num_tel = num_tel;
    }

    public Client(String nom_user, String prenom_user, String email, String mdp, int num_tel, Date date_de_naissance) {
        super(nom_user, prenom_user, email, mdp);
        this.num_tel = num_tel;
        this.date_de_naissance = date_de_naissance;
    }

    // getters and setters for date_de_naissance, num_tel
}
