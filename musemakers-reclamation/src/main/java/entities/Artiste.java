package entities;
import java.util.Date;

public class Artiste extends User {
    private String cartepro;
    private Date date_de_naissance;
    private int num_tel;

    public Artiste() {
    }

    public Artiste(int id_user, String nom_user, String prenom_user, String email, String mdp, String cartepro, Date date_de_naissance, int num_tel) {
        super(id_user, nom_user, prenom_user, email, mdp);
        this.cartepro = cartepro;
        this.date_de_naissance = date_de_naissance;
        this.num_tel = num_tel;
    }

    public Artiste(String nom_user, String prenom_user, String email, String mdp, int num_tel, Date date_de_naissance, String cartepro) {
        super(nom_user, prenom_user, email, mdp);
        this.num_tel = num_tel;
        this.date_de_naissance = date_de_naissance;
        this.cartepro = cartepro;
    }
    public String toString() {
        return "Artiste{" +
                " nom_user='" + getNom_user()  +
                ", prenom_user='" + getPrenom_user() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", mdp='" + getMdp() + '\'' +
                ", num_tel=" + num_tel +
                ", date_de_naissance=" + date_de_naissance +
                ", cartepro='" + cartepro + '\'' +
                '}';
    }
    public String getCartepro() {
        return cartepro;
    }

    public void setCartepro(String cartepro) {
        this.cartepro = cartepro;
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
}