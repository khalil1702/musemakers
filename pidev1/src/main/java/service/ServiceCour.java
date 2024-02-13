package service;

import entities.Cour;
import entities.User;
import utils.DataSource;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

public class ServiceCour implements IService<Cour> {
    private Connection conn ;

    public ServiceCour() {
        conn= DataSource.getInstance().getCnx();
    }

    @Override
    public void ajouter(Cour p) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateDebutStr = sdf.format(p.getDateDebut_cours());
        String dateFinStr = sdf.format(p.getDateFin_cours());
        String requete="insert into cours (titre_cours,descri_cours,dateDebut_cours,dateFin_cours,id_user) values (?,?,?,?,?)";
        try {
            PreparedStatement pst = conn.prepareStatement(requete);
            pst.setString(1, p.getTitre_cours());
            pst.setString(2, p.getDescription_cours());
            pst.setString(3, dateDebutStr);
            pst.setString(4, dateFinStr);
            pst.setInt(5,p.getId_user());
            pst.executeUpdate();
            System.out.println("Cours ajouté!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modifier(Cour p) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateDebutStr = sdf.format(p.getDateDebut_cours());
        String dateFinStr = sdf.format(p.getDateFin_cours());
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
            String requete = "DELETE  FROM cours WHERE id_cours=?";
            PreparedStatement pst = conn.prepareStatement(requete);
            pst.setInt(1, id);

            pst.executeUpdate();
            System.out.println("Cours supprimé!");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.println("Cours non supprimé!");
        }

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
                r.setDateDebut_cours(rst.getDate("dateDebut_cours"));
                r.setDateFin_cours(rst.getDate("DateFin_cours"));
                User u = new User();

                u.setId_user(rst.getInt("id_user"));
                System.out.println(u.getId_user());
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

        String requete = "Select * from cours";
        try {
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(requete);
            while (res.next()){
                int id_cours = res.getInt(1);
                String titre_cours = res.getString(2);
                String description_cours = res.getString(3);

                java.util.Date dateDebut_cours = res.getDate(4);
                java.util.Date dateFin_cours = res.getDate(5);
                User u= new User();
                u.setId_user(res.getInt(6));





                Cour r= new Cour(id_cours,titre_cours,description_cours,dateDebut_cours,dateFin_cours,u);
                cours.add(r);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return cours;

    }
}
