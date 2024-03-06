package service;

import entities.Reclamation;
import entities.User;
import utils.DataSource;
import utils.FilterBadWord;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

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
        pst.setInt(1, r.getUser().getId_user());  // Utilisez 'r.getUser().getId_user()' au lieu de 'r.getIdU()'
        pst.setString(2,FilterBadWord.filter(r.getDescriRec()));
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
        ps.setString(2, FilterBadWord.filter(r.getDescriRec()));
        ps.setDate(3, new java.sql.Date(r.getDateRec().getTime()));
        ps.setString(4, r.getCategorieRec());
        ps.setString(5, r.getStatutRec());
        ps.setInt(6, r.getIdRec());
        ps.executeUpdate();
        System.out.println("Reclamation modifiée!");

    }



    @Override
    public void supprimer(int idRec) throws SQLException {
        try {
            // Supprimer d'abord les enregistrements liés de la table commentaire
            String requeteSuppressionCommentaires = "DELETE FROM commentaire WHERE idRec=?";
            PreparedStatement pstSuppressionCommentaires = cnx.prepareStatement(requeteSuppressionCommentaires);
            pstSuppressionCommentaires.setInt(1, idRec);
            pstSuppressionCommentaires.executeUpdate();

            // Une fois les commentaires liés supprimés, supprimer l'enregistrement de la table Reclamation
            String requeteSuppressionReclamation = "DELETE FROM Reclamation WHERE idRec=?";
            PreparedStatement pstSuppressionReclamation = cnx.prepareStatement(requeteSuppressionReclamation);
            pstSuppressionReclamation.setInt(1, idRec);
            pstSuppressionReclamation.executeUpdate();

            System.out.println("Reclamation supprimée!");
        } catch (SQLException ex) {
            // Gérer les exceptions
            ex.printStackTrace(); // ou enregistrez l'erreur
            throw ex; // relancez l'exception pour la gérer à un niveau supérieur si nécessaire
        }
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


    public Map<String, Integer> countReclamationsByCategory() throws SQLException {
        Map<String, Integer> categoryCounts = new HashMap<>();
        String req = "SELECT CategorieRec, COUNT(*) AS count FROM Reclamation GROUP BY CategorieRec";
        try (PreparedStatement ps = cnx.prepareStatement(req);
             ResultSet rst = ps.executeQuery()) {
            while (rst.next()) {
                String category = rst.getString("CategorieRec");
                int count = rst.getInt("count");
                categoryCounts.put(category, count);
            }
        }
        return categoryCounts;
    }

    public Map<String, Integer> countReclamationsByStatus() throws SQLException {
        Map<String, Integer> statusCounts = new HashMap<>();
        String req = "SELECT StatutRec, COUNT(*) AS count FROM Reclamation GROUP BY StatutRec";
        try (PreparedStatement ps = cnx.prepareStatement(req);
             ResultSet rst = ps.executeQuery()) {
            while (rst.next()) {
                String status = rst.getString("StatutRec");
                int count = rst.getInt("count");
                statusCounts.put(status, count);
            }
        }
        return statusCounts;
    }

    public Map<String, Integer> getCommentCountByCategory() throws SQLException {
        Map<String, Integer> commentCountByCategory = new HashMap<>();
        String query = "SELECT CategorieRec, COUNT(*) as count FROM commentaire GROUP BY CategorieRec";

        Statement statement = cnx.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            commentCountByCategory.put(resultSet.getString("CategorieRec"), resultSet.getInt("count"));
        }
        return commentCountByCategory;
    }
    public List<Reclamation> trierParNomUserAscendant() throws SQLException {
        List<Reclamation> reclamations = getAll();
        return reclamations.stream()
                .sorted(Comparator.comparing(r -> r.getUser().getNom_user()))
                .collect(Collectors.toList());
    }

    public List<Reclamation> trierParNomUserDescendant() throws SQLException {
        List<Reclamation> reclamations = getAll();
        return reclamations.stream()
                .sorted(Comparator.comparing(r -> r.getUser().getNom_user(), Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public List<Reclamation> trierParDescriptionAscendant() throws SQLException {
        List<Reclamation> reclamations = getAll();
        return reclamations.stream()
                .sorted(Comparator.comparing(Reclamation::getDescriRec))
                .collect(Collectors.toList());
    }

    public List<Reclamation> trierParDescriptionDescendant() throws SQLException {
        List<Reclamation> reclamations = getAll();
        return reclamations.stream()
                .sorted(Comparator.comparing(Reclamation::getDescriRec, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public List<Reclamation> trierParStatutAscendant() throws SQLException {
        List<Reclamation> reclamations = getAll();
        return reclamations.stream()
                .sorted(Comparator.comparing(Reclamation::getStatutRec))
                .collect(Collectors.toList());
    }

    public List<Reclamation> trierParStatutDescendant() throws SQLException {
        List<Reclamation> reclamations = getAll();
        return reclamations.stream()
                .sorted(Comparator.comparing(Reclamation::getStatutRec, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public List<Reclamation> trierParDateReclamationAscendant() throws SQLException {
        List<Reclamation> reclamations = getAll();
        return reclamations.stream()
                .sorted(Comparator.comparing(Reclamation::getDateRec))
                .collect(Collectors.toList());
    }

    public List<Reclamation> trierParDateReclamationDescendant() throws SQLException {
        List<Reclamation> reclamations = getAll();
        return reclamations.stream()
                .sorted(Comparator.comparing(Reclamation::getDateRec, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

}