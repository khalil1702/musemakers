package edu.esprit.services;

import edu.esprit.entities.Oeuvre;

import edu.esprit.utils.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.*;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

public class ServiceOeuvre implements IService<Oeuvre>  {
    Connection cnx = DataSource.getInstance().getCnx();

    @Override
    public void ajouter(Oeuvre o) {
        String req = "INSERT INTO `oeuvre` (nom_oeuvre, categorie_oeuvre, prix_oeuvre, datecreation, description, image) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, o.getNom());
            ps.setString(2, o.getCategorie());
            ps.setFloat(3, o.getPrix());
            ps.setDate(4, o.getDateCreation());
            ps.setString(5, o.getDescription());
            ps.setString(6, o.getImage());
            ps.executeUpdate();
            System.out.println("Oeuvre added succesfully !");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Oeuvre o) {
        String req = "UPDATE oeuvre SET nom_oeuvre=?, categorie_oeuvre=?, prix_oeuvre=?, datecreation=?, description=?, image=? WHERE id_oeuvre=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, o.getNom());
            ps.setString(2, o.getCategorie());
            ps.setFloat(3, o.getPrix());
            ps.setDate(4, o.getDateCreation());
            ps.setString(5, o.getDescription());
            ps.setString(6, o.getImage());
            ps.setInt(7, o.getId());
            ps.executeUpdate();
            System.out.println("Oeuvre modifiée avec succès !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM oeuvre WHERE id_oeuvre=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Oeuvre supprimée avec succès !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public Oeuvre getOneById(int id) {
        String req = "SELECT * FROM oeuvre WHERE id_oeuvre=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id); //  the value for the placeholder
            ResultSet res = ps.executeQuery(); // Execution of prepared statement

            if (res.next()) {
                String nom = res.getString("nom_oeuvre");
                String categorie = res.getString("categorie_oeuvre");
                float prix = res.getFloat("prix_oeuvre");
                Date dateCreation = res.getDate("datecreation");
                String description = res.getString("description");
                String image = res.getString("image");
                Oeuvre oeuvre = new Oeuvre(id, nom, categorie, prix, dateCreation, description, image);
                System.out.println("Oeuvre retrouvée !");
                System.out.println(oeuvre.toString()); // Utilisation de la méthode toString
                return oeuvre;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Set<Oeuvre> getAll() {
        Set<Oeuvre> oeuvres = new HashSet<>();
        String req = "SELECT * FROM oeuvre";
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()) {
                int id = res.getInt("id_oeuvre");
                String nom = res.getString("nom_oeuvre");
                String categorie = res.getString("categorie_oeuvre");
                float prix = res.getFloat("prix_oeuvre");
                Date dateCreation = res.getDate("datecreation");
                String description = res.getString("description");
                String image = res.getString("image");
                Oeuvre oeuvre = new Oeuvre(id, nom, categorie, prix, dateCreation, description, image);
                oeuvres.add(oeuvre);
                System.out.println(oeuvre.toString());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return oeuvres;
    }


    public void afficherStatistiques() {
        // Créer les données pour les diagrammes
        ObservableList<PieChart.Data> categorieData = FXCollections.observableArrayList();
        ObservableList<PieChart.Data> prixData = FXCollections.observableArrayList();

        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM oeuvre");

            Map<String, Integer> categorieCount = new HashMap<>();
            Map<String, Integer> prixCount = new HashMap<>();
            while (res.next()) {
                String categorie = res.getString("categorie_oeuvre");
                float prix = res.getFloat("prix_oeuvre");

                categorieCount.put(categorie, categorieCount.getOrDefault(categorie, 0) + 1);

                String prixCategorie;
                if (prix > 5000) {
                    prixCategorie = "+5000";
                } else if (prix > 1000) {
                    prixCategorie = "+1000";
                } else {
                    prixCategorie = "-1000";
                }
                prixCount.put(prixCategorie, prixCount.getOrDefault(prixCategorie, 0) + 1);
            }

            for (Map.Entry<String, Integer> entry : categorieCount.entrySet()) {
                categorieData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }
            for (Map.Entry<String, Integer> entry : prixCount.entrySet()) {
                prixData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Créer les diagrammes
        final PieChart categorieChart = new PieChart(categorieData);
        categorieChart.setTitle("les Statistiques par Categorie");

        final PieChart prixChart = new PieChart(prixData);
        prixChart.setTitle("les Statistiques par prix");

        // Créer la boîte de dialogue
        Dialog<Void> dialog = new Dialog<>();
        dialog.initStyle(StageStyle.UTILITY);
        HBox hbox = new HBox(categorieChart, prixChart);
        dialog.getDialogPane().setContent(hbox);
        // Ajouter un bouton de fermeture
        ButtonType closeButton = new ButtonType("Fermer");
        dialog.getDialogPane().getButtonTypes().add(closeButton);
        dialog.showAndWait();
    }
    public void triParNom(List<Oeuvre> oeuvres, boolean ascendant) {
        oeuvres.sort(Comparator.comparing(Oeuvre::getNom));
        if (!ascendant) {
            oeuvres.sort(Comparator.comparing(Oeuvre::getNom).reversed());
        }
    }

    public void triParDateCreation(List<Oeuvre> oeuvres, boolean ascendant) {
        oeuvres.sort(Comparator.comparing(Oeuvre::getDateCreation));
        if (!ascendant) {
            oeuvres.sort(Comparator.comparing(Oeuvre::getDateCreation).reversed());
        }
    }

    public void triParPrix(List<Oeuvre> oeuvres, boolean ascendant) {
        oeuvres.sort(Comparator.comparing(Oeuvre::getPrix));
        if (!ascendant) {
            oeuvres.sort(Comparator.comparing(Oeuvre::getPrix).reversed());
        }
    }


    public Set<Oeuvre> chercherParCategorieOuNom(String categorieoeuvre, String nomoeuvre) throws SQLException {
        Set<Oeuvre> result = new HashSet<>();
        String req = "SELECT * FROM oeuvre WHERE LOWER(categorie_oeuvre) LIKE ? AND LOWER(nom_oeuvre) LIKE ?";

        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, "%" + categorieoeuvre.toLowerCase() + "%");
            ps.setString(2, "%" + nomoeuvre.toLowerCase() + "%");
            ResultSet res = ps.executeQuery();

            // Convertir le ResultSet en une collection de données
            List<Oeuvre> oeuvres = new ArrayList<>();
            while (res.next()) {
                int id = res.getInt("id_oeuvre");
                String nom = res.getString("nom_oeuvre");
                String categorie = res.getString("categorie_oeuvre");
                float prix = res.getFloat("prix_oeuvre");
                Date dateCreation = res.getDate("datecreation");
                String description = res.getString("description");
                String image = res.getString("image");

                Oeuvre o = new Oeuvre(id ,nom,categorie,prix,dateCreation,description,image);
                oeuvres.add(o);
            }

            // Utiliser des streams pour filtrer et collecter les résultats
            result = oeuvres.stream()
                    .filter(oeuvre -> oeuvre.getCategorie().toLowerCase().contains(categorieoeuvre.toLowerCase()) && oeuvre.getNom().toLowerCase().contains(nomoeuvre.toLowerCase()))
                    .collect(Collectors.toSet());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

}



