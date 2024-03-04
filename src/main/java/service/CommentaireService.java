package service;

import entities.Commentaire;
import entities.Reclamation;
import entities.User;
import utils.DataSource;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

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
            Reclamation r = new Reclamation();
            ReclamationService rs = new ReclamationService();
            r=rs.getOneById(rst.getInt("IdRec"));
            c.setReclamation(r);
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
        ReclamationService rs = new ReclamationService();

        ste = cnx.createStatement();
        ResultSet rst = ste.executeQuery(requete);
        while (rst.next()) {
            Commentaire c = new Commentaire();
            c.setIdCom(rst.getInt("idCom"));
            Reclamation rec = new Reclamation();
            rec = rs.getOneById(rst.getInt("idRec"));
            c.setReclamation(rec);
            // Vous devez récupérer l'objet Reclamation associé à partir de la base de données
            c.setDateCom(rst.getDate("DateCom"));
            c.setContenuCom(rst.getString("ContenuCom"));
            commentaires.add(c);
        }

        return commentaires;
    }

    public List<Commentaire> getCommentairesForReclamation(Reclamation reclamation) throws SQLException {
        // Utilisez une requête SQL pour récupérer les commentaires associés à la réclamation donnée
        String query = "SELECT * FROM commentaire WHERE idRec = ?";
        List<Commentaire> commentaires = new ArrayList<>();

        PreparedStatement preparedStatement = cnx.prepareStatement(query);
        preparedStatement.setInt(1, reclamation.getIdRec());
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Commentaire commentaire = new Commentaire();
                commentaire.setIdCom(resultSet.getInt("idCom"));
                commentaire.setReclamation(reclamation);
                commentaire.setContenuCom(resultSet.getString("ContenuCom"));
                commentaire.setDateCom(resultSet.getDate("DateCom"));
                // Ajoutez le commentaire à la liste
                commentaires.add(commentaire);
            }
        }

        return commentaires;
    }

    public Map<String, Integer> countCommentsByCategory() throws SQLException {
        Map<String, Integer> commentCountByCategory = new HashMap<>();
        String query = "SELECT r.CategorieRec, COUNT(c.idCom) as count " +
                "FROM commentaire c " +
                "JOIN reclamation r ON c.idRec = r.idRec " +
                "GROUP BY r.CategorieRec";

        try (PreparedStatement statement = cnx.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String category = resultSet.getString("CategorieRec");
                int count = resultSet.getInt("count");
                commentCountByCategory.put(category, count);
            }
        }
        return commentCountByCategory;
    }
    public List<Commentaire> trierParNomUserAscendant() throws SQLException {
        List<Commentaire> commentaires = getAll();
        return commentaires.stream()
                .sorted(Comparator.comparing(c -> c.getReclamation().getUser().getNom_user()))
                .collect(Collectors.toList());
    }

    public List<Commentaire> trierParNomUserDescendant() throws SQLException {
        List<Commentaire> commentaires = getAll();
        return commentaires.stream()
                .sorted(Comparator.comparing(c -> c.getReclamation().getUser().getNom_user(), Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public List<Commentaire> trierParDateAscendant() throws SQLException {
        List<Commentaire> commentaires = getAll();
        return commentaires.stream()
                .sorted(Comparator.comparing(Commentaire::getDateCom))
                .collect(Collectors.toList());
    }

    public List<Commentaire> trierParDateDescendant() throws SQLException {
        List<Commentaire> commentaires = getAll();
        return commentaires.stream()
                .sorted(Comparator.comparing(Commentaire::getDateCom, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public List<Commentaire> trierParReclamationAscendant() throws SQLException {
        List<Commentaire> commentaires = getAll();
        return commentaires.stream()
                .sorted(Comparator.comparing(c -> c.getReclamation().getIdRec()))
                .collect(Collectors.toList());
    }

    public List<Commentaire> trierParReclamationDescendant() throws SQLException {
        List<Commentaire> commentaires = getAll();
        return commentaires.stream()
                .sorted(Comparator.comparing(c -> c.getReclamation().getIdRec(), Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public List<Commentaire> trierParContenuAscendant() throws SQLException {
        List<Commentaire> commentaires = getAll();
        return commentaires.stream()
                .sorted(Comparator.comparing(Commentaire::getContenuCom))
                .collect(Collectors.toList());
    }

    public List<Commentaire> trierParContenuDescendant() throws SQLException {
        List<Commentaire> commentaires = getAll();
        return commentaires.stream()
                .sorted(Comparator.comparing(Commentaire::getContenuCom, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}