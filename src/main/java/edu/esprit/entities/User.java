package edu.esprit.entities;

import java.util.Date;
import java.util.Objects;

public class User {
    private static User loggedInUser;

    private int id_user;
    private String nom_user;
    private String prenom_user;
    private String email ;
    private String mdp ;
    private String role;

    public User(String email, String role) {
        this.email=email;
        this.role=role;
    }


    public User() {
    }

    public int getId_user() {
        return id_user;
    }

    public String getNom_user() {
        return nom_user;
    }

    public String getPrenom_user() {
        return prenom_user;
    }

    public String getEmail() {
        return email;
    }

    public String getMdp() {
        return mdp;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public void setNom_user(String nom_user) {
        this.nom_user = nom_user;
    }

    public void setPrenom_user(String prenom_user) {
        this.prenom_user = prenom_user;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }



    public User(int id_user, String nom_user, String prenom_user, String email, String mdp) {//admin
        this.id_user = id_user;
        this.nom_user = nom_user;
        this.prenom_user = prenom_user;
        this.email = email;
        this.mdp = mdp;

    }

    public User(String nom_user, String prenom_user, String email, String mdp) {//user
        this.nom_user = nom_user;
        this.prenom_user = prenom_user;
        this.email = email;
        this.mdp = mdp;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id_user == user.id_user && Objects.equals(nom_user, user.nom_user) && Objects.equals(prenom_user, user.prenom_user) && Objects.equals(email, user.email) && Objects.equals(mdp, user.mdp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_user, nom_user, prenom_user, email, mdp);
    }

    @Override
    public String toString() {
        return "User{" +
                "nom_user='" + nom_user + '\'' +
                ", prenom_user='" + prenom_user + '\'' +
                ", email='" + email + '\'' +
                ", mdp='" + mdp + '\'' +
                '}';
    }


}