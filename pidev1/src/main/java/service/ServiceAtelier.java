package service;

import entities.Atelier;
import entities.Cour;
import utils.DataSource;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ServiceAtelier implements IService<Atelier>{


    Connection  conn= DataSource.getInstance().getCnx();

    @Override
    public void ajouter(Atelier atelier) {
        String requete ="insert into atelier ( id_cours, dateDebut_atelier, dateFin_atelier, lien_atelier) VALUES (?, ?,?,?)";
        try {
            PreparedStatement pst = conn.prepareStatement(requete);
            pst.setInt(1, 6); // Supposons que l'id_cours soit la clé étrangère
            pst.setString(2,  atelier.getDateDebut_atelier().toString());
            pst.setString(3,  atelier.getDateFin_atelier().toString());
            pst.setString(4, atelier.getLien());
            pst.executeUpdate();
            System.out.println("Atelier ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'atelier : " + e.getMessage());
        }
    }

   /* @Override
    public void modifier(Atelier p) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateDebutStr = sdf.format(p.getDateDebut_atelier());
        String dateFinStr = sdf.format(p.getDateFin_atelier());

        String requete = "UPDATE atelier SET id_cours=?, dateDebut_atelier=?, dateFin_atelier=?, lien_atelier=? WHERE id_atelier=?";
        try {
            PreparedStatement pst = conn.prepareStatement(requete);
            pst.setInt(1, p.getCour().getId_cours()); // Correction ici
            pst.setString(2, dateDebutStr);
            pst.setString(3, dateFinStr);
            pst.setString(4, p.getLien());
            pst.setInt(5, p.getId_atelier()); // Correction ici

            pst.executeUpdate();
            System.out.println("Atelier modifié avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de l'atelier : " + e.getMessage());
        }
        }*/

    @Override
    public void modifier(Atelier p) {
        String requete = "UPDATE atelier SET id_cours = ?, dateDebut_atelier = ?, dateFin_atelier = ?, lien_atelier = ? WHERE id_atelier = ?";

        try (
             PreparedStatement pstmt = conn.prepareStatement(requete)) {


            pstmt.setObject(1, p.getCour().getId_cours()); // Vous devrez peut-être convertir `Cour` en un type approprié
            pstmt.setString(2, p.getDateDebut_atelier().toString());
            pstmt.setString(3, p.getDateFin_atelier().toString());
            pstmt.setString(4, p.getLien());
            pstmt.setInt(5, p.getId_atelier());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    @Override
    public void supprimer(int id) {
        try {
            String requete = "DELETE  FROM atelier WHERE id_atelier=?";
            PreparedStatement pst = conn.prepareStatement(requete);
            pst.setInt(1, id);

            pst.executeUpdate();
            System.out.println("atelier supprimé!");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.println("atelier non supprimé!");
        }

    }

    @Override
    public Atelier getOneById(int id) {
        Atelier atelier = null;
        String query = "SELECT * FROM atelier WHERE id_atelier = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                // Récupérer les données de l'atelier à partir du ResultSet
                int id_cours = rs.getInt("id_cours");
                LocalDate dateDebut = rs.getDate("dateDebut_atelier").toLocalDate();
                LocalDate dateFin = rs.getDate("dateFin_atelier").toLocalDate();
                String lien = rs.getString("lien_atelier");
                // Créer une nouvelle instance de Atelier
                atelier = new Atelier(id, new Cour(id_cours), dateDebut, dateFin, lien);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return atelier;
    }



    @Override
    public Set<Atelier> getAll() {
        Set<Atelier> ateliers = new HashSet<>();

        String requete = "SELECT * FROM atelier"; // Requête SQL pour récupérer tous les ateliers
        try {
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(requete);
            while (res.next()) {
                int id_atelier = res.getInt("id_atelier");
                int id_cours = res.getInt("id_cours"); // Remplacez par le nom de la colonne dans votre table
                Cour c = new Cour();
                c.setId_cours(id_cours);
                LocalDate dateDebut_atelier = res.getDate("dateDebut_atelier").toLocalDate(); // Remplacez par le nom de la colonne
                LocalDate dateFin_atelier = res.getDate("dateFin_atelier").toLocalDate(); // Remplacez par le nom de la colonne
                String lien_atelier = res.getString("lien_atelier"); // Remplacez par le nom de la colonne

                Atelier a = new Atelier(id_atelier, c, dateDebut_atelier, dateFin_atelier, lien_atelier);
                ateliers.add(a);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des ateliers : " + e.getMessage());
        }

        return ateliers;
    }



    public List<Atelier> trierParlieuAscendant() {
        List<Atelier> ateliers = new ArrayList<>(getAll());
        return ateliers.stream()
                .sorted(Comparator.comparing(Atelier::getDateDebut_atelier))
                .collect(Collectors.toList());
    }

    public List<Atelier> trierParlieuDescendant() {
        List<Atelier> ateliers = new ArrayList<>(getAll());
        return ateliers.stream()
                .sorted(Comparator.comparing(Atelier::getDateDebut_atelier).reversed())
                .collect(Collectors.toList());
    }
    public List<Atelier> trierParDateDebutAscendant() {
        List<Atelier> ateliers = new ArrayList<>(getAll());
        return ateliers.stream()
                .sorted(Comparator.comparing(Atelier::getDateFin_atelier))
                .collect(Collectors.toList());
    }
    public List<Atelier> trierParDateDebutDescendant() {
        List<Atelier> ateliers = new ArrayList<>(getAll());
        return ateliers.stream()
                .sorted(Comparator.comparing(Atelier::getDateFin_atelier))
                .collect(Collectors.toList());
    }

    public List<Atelier> trierParDateFinAscendant() {
        List<Atelier> ateliers = new ArrayList<>(getAll());
        return ateliers.stream()
                .sorted(Comparator.comparing(Atelier::getDateFin_atelier).reversed())
                .collect(Collectors.toList());
    }
    public List<Atelier> trierParDateFinDescendant() {
        List<Atelier> ateliers = new ArrayList<>(getAll());
        return ateliers.stream()
                .sorted(Comparator.comparing(Atelier::getDateFin_atelier).reversed())
                .collect(Collectors.toList());
    }

}













