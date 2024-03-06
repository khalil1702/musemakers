package edu.esprit.services;

import edu.esprit.entities.Exposition;
import edu.esprit.entities.User;
import edu.esprit.utils.DataSource;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class ServicePersonne implements IService<User> {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/musemakers";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    Connection cnx= DataSource.getInstance().getCnx();
    @Override
    public void ajouter(User p) {

    }

    @Override
    public void modifier(User p) {

    }

    @Override
    public void supprimer(int id) {

    }

    @Override
    public User getOneById(int id) {

        String req = "SELECT * FROM user WHERE id_user=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id); //  the value for the placeholder
            ResultSet res = ps.executeQuery(); // Execution ta3 prepared statement

            if (res.next()) {
                String nom_user = res.getString("nom_user");
                String prenom_user = res.getString("prenom_user");
                String email = res.getString("email");
                String mdp = res.getString("mdp");
                int num_tel = res.getInt("num_tel");
                Date date_de_naissance = res.getDate("date_de_naissance");
                String cartepro = res.getString("cartepro");
                String role = res.getString("role");
                String status = res.getString("status");

                System.out.println("personne mijoud !");
                return new User(id,nom_user,prenom_user,email,mdp);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    public Set<User> getAll()throws SQLException {
        Set<User> personnes = new HashSet<>();

        String req = "Select * from user";
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()){
                int id = res.getInt(1);
                String nom = res.getString("nom_user");
                String prenom = res.getString(3);
                String email = res.getString(4);
                String mdp = res.getString(5);
                int num_tel = res.getInt(6);
                Date d_naissance = res.getDate("date_de_naissance");
                String carte_pro = res.getString(8);
                String role = res.getString(9);
                User p = new User(id,nom,prenom,email,mdp);
                personnes.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return personnes;
    }

    public int getConnectedUserId() throws SQLException {
        int userId = -1;
        String sql = "SELECT id_user FROM user WHERE status = 'connected'";

        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();  // Add this line to execute the query

            if (rs.next()) {
                userId = rs.getInt("id_user");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return userId;
    }





    }



