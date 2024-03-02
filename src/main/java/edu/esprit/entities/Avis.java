package edu.esprit.entities;

import java.sql.Date;
public class Avis {

    private int idAvis;
    private String commentaire;

    private int note;

    private Integer likes;
    private Integer dislikes;

    private boolean favoris;
    // Foreign key
    private Oeuvre oeuvre;
    private User client;



    public Avis(){
    }

    public Avis(int idAvis,String commentaire, int note, Oeuvre oeuvre, User client, Integer likes, Integer dislikes ,boolean favoris) {
        this.idAvis = idAvis;
        this.commentaire = commentaire;

        this.note = note;
        this.oeuvre = oeuvre;
        this.client = client;
        this.likes = likes;
        this.dislikes = dislikes;
        this.favoris = favoris;
    }

    public Avis(String commentaire,int note, Oeuvre oeuvre, User client, Integer likes, Integer dislikes,boolean favoris) {
        this.commentaire = commentaire;

        this.note = note;
        this.oeuvre = oeuvre;
        this.client = client;
        this.likes = likes;
        this.dislikes = dislikes;
        this.favoris = favoris;
    }

    public int getIdAvis() {
        return idAvis;
    }

    public void setIdAvis(int idAvis) {
        this.idAvis = idAvis;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }


    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public Oeuvre getOeuvre() {
        return oeuvre;
    }

    public void setOeuvre(Oeuvre oeuvre) {
        this.oeuvre = oeuvre;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    // Getters et setters pour dislikes
    public Integer getDislikes() {
        return dislikes;
    }

    public void setDislikes(Integer dislikes) {
        this.dislikes = dislikes;
    }

    public boolean getFavoris() {
        return favoris;
    }

    public void setFavoris(boolean favoris) {
        this.favoris = favoris;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Avis that)) return false;
        return getIdAvis() == that.getIdAvis();
    }

    @Override
    public String toString() {
        return "Avis{" +
                "commentaire='" + commentaire + '\'' +
                ", note=" + note +
                ", oeuvre=" + oeuvre +
                ", User=" + client.getNom_user() +
                '}';
    }



}
