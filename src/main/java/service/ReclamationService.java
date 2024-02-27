package service;

import entities.Reclamation;
import entities.User;
import utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService implements IService<Reclamation>{
    private Connection cnx ;
    private Statement ste ;
    PreparedStatement ps;
    private PreparedStatement pst ;

    public ReclamationService() {
        cnx= DataSource.getInstance().getCnx();
    }



    public void ajouter(Reclamation r) throws SQLException{

        if (r == null || r.getUser() == null) {
            // Handle the case where the reclamation or user is null
            System.out.println("Reclamation or associated user is null. Cannot add to database.");
            return; // or throw an exception
        }
        String requete = " insert into reclamation (idU,descriRec,DateRec,CategorieRec,StatutRec) values (?,?,?,?,?)" ;

            pst=cnx.prepareStatement(requete);
        pst.setInt(1,r.getUser().getId_user());  // Utilisez 'r.getUser().getId_user()' au lieu de 'r.getIdU()'
            pst.setString(2,r.getDescriRec());
            pst.setDate(3, new java.sql.Date(r.getDateRec().getTime()));
            pst.setString(4,r.getCategorieRec());
            pst.setString(5,r.getStatutRec());
            pst.executeUpdate();
            System.out.println("Reclamation ajoutée!");

    }

    @Override
    public void modifier(Reclamation r) throws SQLException{

            PreparedStatement ps = cnx.prepareStatement("UPDATE reclamation SET idU = ?, descriRec = ?, DateRec = ?, CategorieRec = ?, StatutRec = ? WHERE idRec = ?");
            ps.setInt(1, r.getUser().getId_user());  // Utilisez 'r.getUser().getId_user()' au lieu de 'r.getIdU()'
            ps.setString(2, r.getDescriRec());
            ps.setDate(3, new java.sql.Date(r.getDateRec().getTime()));
            ps.setString(4, r.getCategorieRec());
            ps.setString(5, r.getStatutRec());
            ps.setInt(6, r.getIdRec());
            ps.executeUpdate();
            System.out.println("Reclamation modifiée!");

    }



    @Override
    public void supprimer(int idRec ) throws SQLException {

            String requete = "DELETE  FROM Reclamation WHERE idRec=?";
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setInt(1, idRec);
            pst.executeUpdate();
            System.out.println("Reclamation supprimée!");

    }

    @Override
    public Reclamation getOneById(int id)throws SQLException {
        Reclamation r = new Reclamation();
        String req = "SELECT * FROM Reclamation WHERE idRec = ?";

        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, id);
        ResultSet rst = ps.executeQuery();
        while (rst.next()) {
            User u = new User();
            ServiceUser us = new ServiceUser();
            u=us.getOneById(rst.getInt("IdU"));
            r.setUser(u);  // Utilisez 'user.setId_user(rst.getInt("IdU"))' au lieu de 'r.setIdRec(rst.getInt("IdU"))'
              // Ajoutez cette ligne
            r.setIdRec(rst.getInt("idRec"));
            r.setDescriRec(rst.getString("DescriRec"));
            r.setDateRec(rst.getDate("dateRec"));
            r.setCategorieRec(rst.getString("categorieRec"));
            r.setStatutRec(rst.getString("statutRec"));
        }
return  r;
    }




    @Override
    public List<Reclamation> getAll() throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String req = "SELECT * FROM Reclamation";

        PreparedStatement ps = cnx.prepareStatement(req);
        ResultSet rst = ps.executeQuery();
        while (rst.next()) {
            Reclamation r = new Reclamation();
            ServiceUser us = new ServiceUser();
            /*User user = new User();
            user.setId_user(rst.getInt("IdU"));  // Utilisez 'user.setId_user(rst.getInt("IdU"))' au lieu de 'r.setIdRec(rst.getInt("IdU"))'
            r.setUser(user);  // Ajoutez cette ligne*/
            User u = new User();
            u = us.getOneById(rst.getInt("idU"));
            r.setUser(u);
            r.setIdRec(rst.getInt("idRec"));
            r.setDescriRec(rst.getString("DescriRec"));
            r.setDateRec(rst.getDate("dateRec"));
            r.setCategorieRec(rst.getString("categorieRec"));
            r.setStatutRec(rst.getString("statutRec"));
            reclamations.add(r);
        }

        return reclamations;
    }


}
