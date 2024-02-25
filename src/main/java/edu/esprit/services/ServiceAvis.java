package edu.esprit.services;

import edu.esprit.entities.Oeuvre;
import edu.esprit.entities.Avis;
import edu.esprit.entities.User;
import edu.esprit.utils.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.HBox;
import javafx.stage.StageStyle;

import java.sql.Date;
import java.util.*;

import java.sql.*;

public class ServiceAvis implements IService<Avis>{
    Connection cnx= DataSource.getInstance().getCnx();

    @Override
    public void ajouter(Avis p) {
        String req = "INSERT INTO avis (commentaire, date_experience, note, id_oeuvre, id_user,likes, dislikes) VALUES (?, ?, ?, ?, ?,?,?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, p.getCommentaire());
            ps.setDate(2, p.getDateExperience());
            ps.setInt(3, p.getNote());
            ps.setInt(4, p.getOeuvre().getId()); // Assuming Oeuvre has a getIdOeuvre method
            ps.setInt(5, p.getClient().getId_user());
            ps.setInt(6, p.getLikes()); // Assuming Avis has a getLikes method
            ps.setInt(7, p.getDislikes()); // Assuming Avis has a getDislikes method
            ps.executeUpdate();
            System.out.println("Avis ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Avis p) {
        String req = "UPDATE avis SET commentaire=?, date_experience=?, note=?, id_oeuvre=?, id_user=?, likes=?, dislikes=? WHERE id_avis=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, p.getCommentaire());
            ps.setDate(2, p.getDateExperience());
            ps.setInt(3, p.getNote());
            ps.setInt(4, p.getOeuvre().getId());
            ps.setInt(5, p.getClient().getId_user());
            ps.setInt(6, p.getLikes()); // Assuming Avis has a getLikes method
            ps.setInt(7, p.getDislikes()); // Assuming Avis has a getDislikes method
            ps.setInt(8, p.getIdAvis());
            ps.executeUpdate();
            System.out.println("Avis modifié avec succès !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM avis WHERE id_avis=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Avis supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Avis getOneById(int id) {

        String req = "SELECT * FROM avis WHERE id_avis=?";
        try {

            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                String commentaire = res.getString("commentaire");
                Date dateExperience = res.getDate("date_experience");
                int note = res.getInt("note");
                int idOeuvre = res.getInt("id_oeuvre");
                int idUser = res.getInt("id_user");
                int likes = res.getInt("likes");
                int dislikes = res.getInt("dislikes");

                ServiceOeuvre serviceOeuvre = new ServiceOeuvre();
                Oeuvre oeuvre = serviceOeuvre.getOneById(idOeuvre);
                ServicePersonne servicePersonne = new ServicePersonne();
                User user = servicePersonne.getOneById(idUser);

                Avis avis = new Avis(id, commentaire, dateExperience, note,oeuvre,user,likes,dislikes );
                System.out.println("avis retrouvée !");
                System.out.println(avis.toString()); // Utilisation de la méthode toString
                return avis;

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
       return null;
    }

    @Override
    public Set<Avis> getAll() {
        Set<Avis> avisSet = new HashSet<>();
        String req = "SELECT * FROM avis";
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()) {
                String commentaire = res.getString("commentaire");
                Date dateExperience = res.getDate("date_experience");
                int note = res.getInt("note");
                int idOeuvre = res.getInt("id_oeuvre");
                int idUser = res.getInt("id_user");
                int likes = res.getInt("likes");
                int dislikes = res.getInt("dislikes");

                ServiceOeuvre serviceOeuvre = new ServiceOeuvre();
                Oeuvre oeuvre = serviceOeuvre.getOneById(idOeuvre);
                ServicePersonne servicePersonne = new ServicePersonne();
                User user = servicePersonne.getOneById(idUser);

                Avis avis = new Avis(commentaire,dateExperience, note,oeuvre,user,likes,dislikes);
                avisSet.add(avis);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return avisSet;
    }

    public List<Avis> getAvisByUserId(int userId) {
        List<Avis> avisList = new ArrayList<>();
        String req = "SELECT * FROM avis WHERE id_user=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, userId);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                int id = res.getInt("id_avis");
                String commentaire = res.getString("commentaire");
                Date dateExperience = res.getDate("date_experience");
                int note = res.getInt("note");
                int idOeuvre = res.getInt("id_oeuvre");
                int likes = res.getInt("likes");
                int dislikes = res.getInt("dislikes");

                ServiceOeuvre serviceOeuvre = new ServiceOeuvre();
                Oeuvre oeuvre = serviceOeuvre.getOneById(idOeuvre);

                ServicePersonne servicePersonne = new ServicePersonne();
                User user = servicePersonne.getOneById(userId);

                Avis avis = new Avis(id, commentaire, dateExperience, note, oeuvre, user, likes, dislikes);
                avisList.add(avis);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return avisList;
    }
    public List<Avis> getAvisByOeuvre(Oeuvre oeuvre) {
        List<Avis> avisList = new ArrayList<>();
        String req = "SELECT * FROM avis WHERE id_oeuvre=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, oeuvre.getId());
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                int id = res.getInt("id_avis");
                String commentaire = res.getString("commentaire");
                Date dateExperience = res.getDate("date_experience");
                int note = res.getInt("note");
                int idUser = res.getInt("id_user");
                int likes = res.getInt("likes");
                int dislikes = res.getInt("dislikes");

                ServicePersonne servicePersonne = new ServicePersonne();
                User user = servicePersonne.getOneById(idUser);

                Avis avis = new Avis(id, commentaire, dateExperience, note, oeuvre, user, likes, dislikes);
                avisList.add(avis);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return avisList;
    }
    public void afficherStatistiquesAvis() {
        // Créer les données pour le diagramme
        ObservableList<PieChart.Data> avisData = FXCollections.observableArrayList();

        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM avis");

            Map<String, Integer> avisCount = new HashMap<>();
            while (res.next()) {
                int note = res.getInt("note");

                String avisCategorie;
                if (note <= 2) {
                    avisCategorie = "Négatif";
                } else if (note == 3) {
                    avisCategorie = "Neutre";
                } else {
                    avisCategorie = "Positif";
                }
                avisCount.put(avisCategorie, avisCount.getOrDefault(avisCategorie, 0) + 1);
            }

            for (Map.Entry<String, Integer> entry : avisCount.entrySet()) {
                avisData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Créer le diagramme
        final PieChart avisChart = new PieChart(avisData);
        avisChart.setTitle("Statistiques des avis");

        // Créer la boîte de dialogue
        Dialog<Void> dialog = new Dialog<>();
        dialog.initStyle(StageStyle.UTILITY);
        HBox hbox = new HBox(avisChart);
        dialog.getDialogPane().setContent(hbox);

        // Ajouter un bouton de fermeture
        ButtonType closeButton = new ButtonType("Fermer");
        dialog.getDialogPane().getButtonTypes().add(closeButton);

        dialog.showAndWait();
    }



}
