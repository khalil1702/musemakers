package edu.esprit.services;

import edu.esprit.entities.Exposition;
import edu.esprit.utils.DataSource;

import java.sql.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

public class ServiceExposition implements IService<Exposition> {
    Connection cnx = DataSource.getInstance().getCnx();
    @Override
    public void ajouter(Exposition exp) throws SQLException {
        String req = "INSERT INTO `exposition` (nom, Date_debut, Heure_debut, Date_fin, Heure_fin, Description, Theme, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, exp.getNom());
            ps.setDate(2, exp.getDateDebut());
            ps.setTime(3, exp.getHeure_debut());
            ps.setDate(4, exp.getDateFin());
            ps.setTime(5, exp.getHeure_fin());
            ps.setString(6, exp.getDescription());
            ps.setString(7, exp.getTheme());
            ps.setString(8, exp.getImage());
            ps.executeUpdate();
            System.out.println("Exposition ajoutée avec succès!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Exposition exp) throws SQLException {
        String req = "UPDATE exposition SET nom=?, Date_debut=?, Heure_debut=?, Date_fin=?, Heure_fin=?, Description=?, Theme=?, image=? WHERE id_exposition=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, exp.getNom());
            ps.setDate(2, exp.getDateDebut());
            ps.setTime(3, exp.getHeure_debut());
            ps.setDate(4, exp.getDateFin());
            ps.setTime(5, exp.getHeure_fin());
            ps.setString(6, exp.getDescription());
            ps.setString(7, exp.getTheme());
            ps.setString(8, exp.getImage());
            ps.setInt(9, exp.getId());
            int line_tomodify = ps.executeUpdate();
            if (line_tomodify > 0) {
                System.out.println("Exposition modifiée !");
            } else {
                System.out.println("Exposition avec l'ID " + exp.getId() + " n'existe pas !");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM exposition WHERE id_exposition=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            int line_todelete = ps.executeUpdate();
            if (line_todelete > 0) {
                System.out.println("Exposition supprimée avec succès!");
            } else {
                System.out.println("Exposition avec l'ID " + id + " n'existe pas!");

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Exposition getOneById(int id) throws SQLException {
        String req = "SELECT * FROM exposition WHERE id_exposition=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet res = ps.executeQuery();

            if (res.next()) {
                String nom = res.getString("nom");
                Date dateDebut = res.getDate("Date_debut");
                Time heureDebut = res.getTime("Heure_debut");
                Date dateFin = res.getDate("Date_fin");
                Time heureFin = res.getTime("Heure_fin");
                String description = res.getString("Description");
                String theme = res.getString("Theme");
                String image = res.getString("image");
                System.out.println("Exposition trouvée !");
                return new Exposition(id, nom, dateDebut, dateFin, heureDebut, heureFin, description, theme, image);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }


    @Override
    public Set<Exposition> getAll() throws SQLException {
        Set<Exposition> expositions = new HashSet<>();
        String req = "SELECT * FROM exposition";
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()) {
                int id = res.getInt(1);
                String nom = res.getString(2);
                Date dateDebut = res.getDate("Date_debut");
                Time heureDebut = res.getTime("Heure_debut");
                Date dateFin = res.getDate("Date_fin");
                Time heureFin = res.getTime("Heure_fin");
                String description = res.getString("Description");
                String theme = res.getString("Theme");
                String image = res.getString("image");
                Exposition exp = new Exposition(id, nom, dateDebut, dateFin, heureDebut, heureFin, description, theme, image);
                expositions.add(exp);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return expositions;
    }



    public Set<Exposition> chercherParThemeOuNom(String theme, String nom) throws SQLException {
        Set<Exposition> result = new HashSet<>();
        String req = "SELECT * FROM exposition WHERE Theme LIKE ? AND nom LIKE ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, "%" + theme + "%");
            ps.setString(2, "%" + nom + "%");
            ResultSet res = ps.executeQuery();

            while (res.next()) {
                try {
                    int id = res.getInt(1);
                    String expositionNom = res.getString(2);
                    Date dateDebut = res.getDate("Date_debut");
                    Time heureDebut = res.getTime("Heure_debut");
                    Date dateFin = res.getDate("Date_fin");
                    Time heureFin = res.getTime("Heure_fin");
                    String description = res.getString("Description");
                    String themeE = res.getString("Theme");
                    String image = res.getString("image");



                    Exposition exp = new Exposition(id, expositionNom, dateDebut, dateFin, heureDebut, heureFin, description, themeE, image);
                    result.add(exp);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    private Date tryGetDate(ResultSet resultSet, int columnIndex) throws SQLException {
        try {
            return resultSet.getDate(columnIndex);
        } catch (SQLException e) {
            // Log or print a message to indicate the issue
            System.out.println("Error getting Date from ResultSet: " + e.getMessage());
            return null; // Return null as a placeholder for the Date
        }
    }



}
