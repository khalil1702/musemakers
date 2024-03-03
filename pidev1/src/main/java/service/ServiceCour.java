package service;

import entities.Cour;
import entities.User;
import utils.DataSource;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class ServiceCour implements IService<Cour> {
    private Connection conn;

    public ServiceCour() {
        conn = DataSource.getInstance().getCnx();
    }

    @Override
    public void ajouter(Cour p) {
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // String dateDebutStr = sdf.format(p.getDateDebut_cours());
        // String dateFinStr = sdf.format(p.getDateFin_cours());
        System.out.printf(p.getDateDebut_cours().toString());
        String requete = "insert into cours (titre_cours,descri_cours,dateDebut_cours,dateFin_cours,id_user) values (?,?,?,?,?)";
        try {
            PreparedStatement pst = conn.prepareStatement(requete);
            pst.setString(1, p.getTitre_cours());
            pst.setString(2, p.getDescription_cours());
            pst.setString(3, p.getDateDebut_cours().toString());
            pst.setString(4, p.getDateFin_cours().toString());
            pst.setInt(5, 1);
            pst.executeUpdate();
            System.out.println("Cours ajouté!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modifier(Cour p) {

        String dateDebutStr = p.getDateDebut_cours().toString();
        String dateFinStr = p.getDateFin_cours().toString();
        String requete = "UPDATE cours SET titre_cours=?, descri_cours=?, dateDebut_cours=?, dateFin_cours=?, id_user=? WHERE id_cours=?";
        try {
            PreparedStatement pst = conn.prepareStatement(requete);
            pst.setString(1, p.getTitre_cours());
            pst.setString(2, p.getDescription_cours());
            pst.setString(3, dateDebutStr);
            pst.setString(4, dateFinStr);
            pst.setInt(5, p.getId_user());
            pst.setInt(6, p.getId_cours());
            pst.executeUpdate();
            System.out.println("Cours modifié !");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void supprimer(int id) {

        try {
            // Vérifie d'abord s'il y a des ateliers liés à ce cours
            String checkQuery = "SELECT COUNT(*) FROM atelier WHERE id_cours=?";
            PreparedStatement checkPst = conn.prepareStatement(checkQuery);
            checkPst.setInt(1, id);
            ResultSet rs = checkPst.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Ce cours ne peut pas être supprimé car il est lié à des ateliers.");
                return;
            }

            // S'il n'y a pas d'ateliers liés, on peut supprimer le cours
            String deleteQuery = "DELETE FROM cours WHERE id_cours=?";
            PreparedStatement deletePst = conn.prepareStatement(deleteQuery);
            deletePst.setInt(1, id);

            int rowsAffected = deletePst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cours supprimé!");
            } else {
                System.out.println("Aucun cours supprimé. Vérifiez l'ID du cours.");
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.println("Cours non supprimé!");
        }
        /*
        try {
            String deleteQuery = "DELETE FROM cours WHERE id_cours=?";
            PreparedStatement deletePst = conn.prepareStatement(deleteQuery);
            deletePst.setInt(1, id);

            int rowsAffected = deletePst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cours supprimé!");
            } else {
                System.out.println("Aucun cours supprimé. Vérifiez l'ID du cours.");
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.println("Cours non supprimé!");*/
    }


    @Override
    public Cour getOneById(int id) {
        Cour r = new Cour();
        String req = "SELECT * FROM cours WHERE id_cours = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                r.setId_cours(rst.getInt("id_cours"));
                r.setTitre_cours(rst.getString("titre_cours"));
                r.setDescription_cours(rst.getString("descri_cours"));
                r.setDateDebut_cours(rst.getDate("dateDebut_cours").toLocalDate());
                r.setDateFin_cours(rst.getDate("DateFin_cours").toLocalDate());
                User u = new User();

                u.setId_user(rst.getInt("id_user"));

                r.setId_user(u);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return r;
    }

    @Override
    public Set<Cour> getAll() {
        Set<Cour> cours = new HashSet<>();

        String requete = "SELECT * FROM cours";
        try {
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(requete);
            while (res.next()) {
                int id_cours = res.getInt(1);
                String titre_cours = res.getString(2);
                String description_cours = res.getString(3);
                LocalDate dateDebut_cours = res.getDate(4).toLocalDate();
                LocalDate dateFin_cours = res.getDate(5).toLocalDate();

                // Retrieve user ID from the database
                int userId = res.getInt(6);

                // Initialize a User object with the retrieved ID
                User u = new User();
                u.setId_user(userId);

                // Create Cour object with initialized User object
                Cour r = new Cour(id_cours, titre_cours, description_cours, dateDebut_cours, dateFin_cours, u);
                cours.add(r);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return cours;
    }
    public Set<Cour> getAllDESC() {
        Set<Cour> cours = new HashSet<>();

        String requete = "SELECT * FROM cours ORDER BY descri_cours, titre_cours DESC";
        try {
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(requete);
            while (res.next()) {
                int id_cours = res.getInt(1);
                String titre_cours = res.getString(2);
                String description_cours = res.getString(3);
                LocalDate dateDebut_cours = res.getDate(4).toLocalDate();
                LocalDate dateFin_cours = res.getDate(5).toLocalDate();

                // Retrieve user ID from the database
                int userId = res.getInt(6);

                // Initialize a User object with the retrieved ID
                User u = new User();
                u.setId_user(userId);

                // Create Cour object with initialized User object
                Cour r = new Cour(id_cours, titre_cours, description_cours, dateDebut_cours, dateFin_cours, u);
                cours.add(r);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return cours;
    }
    //
    public Set<Cour> chercherParDesc(String Desc, int tri)
    {
        Set<Cour> cours = new HashSet<>();
        String requete;
        switch(tri)
        {
            case 1:
            {
                requete = "SELECT * FROM cours WHERE descri_cours LIKE ? ORDER BY descri_cours DESC";
                break;
            }
            default:
            {
                requete = "SELECT * FROM cours WHERE descri_cours LIKE ? ORDER BY descri_cours ASC";
                break;
            }
        }

        try {
            PreparedStatement pst = conn.prepareStatement(requete);
            pst.setString(1, "%" + Desc + "%");
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                int id_cours = res.getInt(1);
                String titre_cours = res.getString(2);
                String description_cours = res.getString(3);
                LocalDate dateDebut_cours = res.getDate(4).toLocalDate();
                LocalDate dateFin_cours = res.getDate(5).toLocalDate();

                // Retrieve user ID from the database
                int userId = res.getInt(6);

                // Initialize a User object with the retrieved ID
                User u = new User();
                u.setId_user(userId);

                // Create Cour object with initialized User object
                Cour r = new Cour(id_cours, titre_cours, description_cours, dateDebut_cours, dateFin_cours, u);
                cours.add(r);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return cours;
    }
    public Set<Cour> chercherParTitre(String titre, int tri) {


        Set<Cour> cours = new HashSet<>();
        String requete;
        switch(tri)
        {
            case 1:
            {
                requete = "SELECT * FROM cours WHERE titre_cours LIKE ? ORDER BY titre_cours DESC";
                break;
            }
            default:
            {
                requete = "SELECT * FROM cours WHERE titre_cours LIKE ? ORDER BY titre_cours ASC";
                break;
            }
        }
        try {
            PreparedStatement pst = conn.prepareStatement(requete);
            pst.setString(1, "%" + titre + "%");
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                int id_cours = res.getInt(1);
                String titre_cours = res.getString(2);
                String description_cours = res.getString(3);
                LocalDate dateDebut_cours = res.getDate(4).toLocalDate();
                LocalDate dateFin_cours = res.getDate(5).toLocalDate();

                // Retrieve user ID from the database
                int userId = res.getInt(6);

                // Initialize a User object with the retrieved ID
                User u = new User();
                u.setId_user(userId);

                // Create Cour object with initialized User object
                Cour r = new Cour(id_cours, titre_cours, description_cours, dateDebut_cours, dateFin_cours, u);
                cours.add(r);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return cours;
    }

    public Set<Cour> chercherParTitreOuDescription(String titre, int tri) {


            Set<Cour> cours = new HashSet<>();
            String requete;
            switch(tri)
            {
                case 1:
                {
                    requete = "SELECT * FROM cours WHERE titre_cours LIKE ? OR descri_cours LIKE ? ORDER BY titre_cours,descri_cours DESC";
                    break;
                }
                default:
                {
                    requete = "SELECT * FROM cours WHERE titre_cours LIKE ? OR descri_cours LIKE ? ORDER BY titre_cours,descri_cours ASC";
                    break;
                }
            }
            try {
                PreparedStatement pst = conn.prepareStatement(requete);
                pst.setString(1, "%" + titre + "%");
                pst.setString(2, "%" + titre + "%");
                ResultSet res = pst.executeQuery();
                while (res.next()) {
                    int id_cours = res.getInt(1);
                    String titre_cours = res.getString(2);
                    String description_cours = res.getString(3);
                    LocalDate dateDebut_cours = res.getDate(4).toLocalDate();
                    LocalDate dateFin_cours = res.getDate(5).toLocalDate();

                    // Retrieve user ID from the database
                    int userId = res.getInt(6);

                    // Initialize a User object with the retrieved ID
                    User u = new User();
                    u.setId_user(userId);

                    // Create Cour object with initialized User object
                    Cour r = new Cour(id_cours, titre_cours, description_cours, dateDebut_cours, dateFin_cours, u);
                    cours.add(r);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return cours;
        }

    }


