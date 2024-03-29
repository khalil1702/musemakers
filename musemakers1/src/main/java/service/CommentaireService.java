package service;

import entities.Commentaire;
import entities.Reclamation;
import utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentaireService implements IService<Commentaire>{
    private Connection cnx;
    private Statement ste;
    private PreparedStatement pst;

    public CommentaireService() {
        cnx = DataSource.getInstance().getCnx();
    }

   @Override
    public void ajouter(Commentaire c)throws SQLException {
        String requete = "INSERT INTO commentaire (idCom, idRec, DateCom, ContenuCom) VALUES (?, ?, ?, ?)";

            pst = cnx.prepareStatement(requete);
            pst.setInt(1, c.getIdCom());
            pst.setInt(2, c.getReclamation().getIdRec());
            pst.setDate(3, new java.sql.Date(c.getDateCom().getTime()));
            pst.setString(4, c.getContenuCom());
            pst.executeUpdate();
            System.out.println("Commentaire ajouté !");

    }

/*
    @Override
    public void modifier(Commentaire c)throws SQLException {
        String requete = "UPDATE commentaire SET idRec = ?, DateCom = ?, ContenuCom = ? WHERE idCom = ?";


            pst = cnx.prepareStatement(requete);
            pst.setInt(1, c.getReclamation().getIdRec());
            pst.setDate(2, new java.sql.Date(c.getDateCom().getTime()));
            pst.setString(3, c.getContenuCom());
            pst.setInt(4, c.getIdCom());
            pst.executeUpdate();
            System.out.println("Commentaire modifié !");

    }
*/


    @Override
    public void modifier(Commentaire c) throws SQLException {


        PreparedStatement ps = cnx.prepareStatement("UPDATE commentaire SET idRec = ?, DateCom = ?, ContenuCom = ? WHERE idCom = ?");
        ps.setInt(1, c.getReclamation().getIdRec());
        ps.setDate(2, new java.sql.Date(c.getDateCom().getTime()));
        ps.setString(3, c.getContenuCom());
        ps.setInt(4, c.getIdCom());

        ps.executeUpdate();
        System.out.println("Reclamation modifiée!");
    }

    @Override
    public void supprimer(int id) throws SQLException{
        String requete = "DELETE FROM commentaire WHERE idCom = ?";

            pst = cnx.prepareStatement(requete);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Commentaire supprimé !");

    }

    @Override
    public Commentaire getOneById(int id) throws SQLException{
        Commentaire c = null;
        String requete = "SELECT * FROM commentaire WHERE idCom = ?";

            pst = cnx.prepareStatement(requete);
            pst.setInt(1, id);
            ResultSet rst = pst.executeQuery();
            if (rst.next()) {
                c = new Commentaire();
                c.setIdCom(rst.getInt("idCom"));
                // Vous devez récupérer l'objet Reclamation associé à partir de la base de données
                c.setDateCom(rst.getDate("DateCom"));
                c.setContenuCom(rst.getString("ContenuCom"));
            }

        return c;
    }
    @Override
    public List<Commentaire> getAll() throws SQLException {
        List<Commentaire> commentaires = new ArrayList<>();
        String requete = "SELECT * FROM commentaire";

        ste = cnx.createStatement();
        ResultSet rst = ste.executeQuery(requete);
        while (rst.next()) {
            Commentaire c = new Commentaire();
            c.setIdCom(rst.getInt("idCom"));
            // Vous devez récupérer l'objet Reclamation associé à partir de la base de données
            c.setDateCom(rst.getDate("DateCom"));
            c.setContenuCom(rst.getString("ContenuCom"));
            commentaires.add(c);
        }

        return commentaires;
    }




}
