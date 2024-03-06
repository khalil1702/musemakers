package service;

import entities.Cour;
import entities.CoursFavoris;
import entities.User;
import utils.DataSource;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class ServiceCoursFavoris {
    private Connection conn;

    public ServiceCoursFavoris() {
        conn = DataSource.getInstance().getCnx();
    }

    public void ajouter(CoursFavoris p) {

        String requete = "insert into coursfavoris (CourID,likeByUser) values (?,?)";
        try {
            PreparedStatement pst = conn.prepareStatement(requete);
            pst.setInt(1, p.getCour().getId_cours());
            pst.setInt(2, p.getUser().getId_user());
            pst.executeUpdate();
            System.out.println("Cours Favoris ajouté!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void modifier(CoursFavoris p) {

        String requete = "UPDATE coursfavoris SET CourID = ?,likeByUser = ? WHERE CoursFavID= ?";
        try {
            PreparedStatement pst = conn.prepareStatement(requete);
            pst.setInt(1, p.getCour().getId_cours());
            pst.setInt(2, p.getUser().getId_user());
            pst.setInt(3, p.getCourFavoris_ID());

            pst.executeUpdate();
            System.out.println("Cours Favoris modifié !");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void supprimer(int id) {
        try {


            String deleteQuery = "DELETE FROM coursfavoris WHERE CoursFavID=? AND CourID = ? AND likeByUser = ?";
            PreparedStatement deletePst = conn.prepareStatement(deleteQuery);
            deletePst.setInt(1, id);

            int rowsAffected = deletePst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cours Favoris supprimé!");
            } else {
                System.out.println("Aucun cours Favoris supprimé. Vérifiez l'ID du cours.");
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.println("Cours Favoris non supprimé!");
        }
    }

    public Set<CoursFavoris> getAllByUser(User user) {
        Set<CoursFavoris> cours = new HashSet<>();

        String requete = "SELECT * FROM coursfavoris WHERE likeByUser = " + user.getId_user();
        try {
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(requete);
            while (res.next()) {
                int id_coursFav = res.getInt(1);
                int IdCour = res.getInt(2);
                int likedby = res.getInt(3);

                ServiceCour su  = new ServiceCour();
                Cour cx = su.getOneById(IdCour);
                CoursFavoris c = new CoursFavoris(id_coursFav,user,cx);
                // Create Cour object with initialized User object

                cours.add(c);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return cours;
    }
}
