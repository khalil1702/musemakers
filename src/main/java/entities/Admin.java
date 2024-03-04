package entities;

public class Admin extends User {


    public Admin(String nom_user, String prenom_user, String email, String mdp) {
        super(nom_user, prenom_user, email, mdp);
    }

    public Admin(int id_user, String nom_user, String prenom_user, String email, String mdp) {
        super(id_user, nom_user, prenom_user, email, mdp);
    }

    public Admin() {

    }
// Admin specific methods
}
