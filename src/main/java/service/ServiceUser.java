package service;


import entities.Admin;
import entities.Artiste;
import entities.Client;
import entities.User;
import utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser implements IService<User> {

    Connection cnx = DataSource.getInstance().getCnx();


    @Override
    public void ajouter(User p) {
        if (!p.getEmail().contains("@")) {
            System.out.println("Erreur : L'email doit contenir un '@'.");
            return;
        }
        String role;
        if (p instanceof Admin) {
            role = "Admin";
        } else if (p instanceof Client) {
            role = "Client";
        } else if (p instanceof Artiste) {
            role = "Artiste";
        } else {
            role = "User";
        }

        String sql = "INSERT INTO user (id_user, nom_user, prenom_user, email, mdp, num_tel, date_de_naissance, cartepro, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setInt(1, p.getId_user());
            pstmt.setString(2, p.getNom_user());
            pstmt.setString(3, p.getPrenom_user());
            pstmt.setString(4, p.getEmail());
            pstmt.setString(5, p.getMdp());
            if (p instanceof Client) {
                pstmt.setInt(6, ((Client) p).getNum_tel());
                pstmt.setDate(7, new java.sql.Date(((Client) p).getDate_de_naissance().getTime()));
                pstmt.setNull(8, java.sql.Types.VARCHAR);
            } else if (p instanceof Artiste) {
                pstmt.setInt(6, ((Artiste) p).getNum_tel());
                pstmt.setDate(7, new java.sql.Date(((Artiste) p).getDate_de_naissance().getTime()));
                pstmt.setString(8, ((Artiste) p).getCartepro());
            } else {
                pstmt.setNull(6, java.sql.Types.INTEGER);
                pstmt.setNull(7, java.sql.Types.DATE);
                pstmt.setNull(8, java.sql.Types.VARCHAR);
            }
            pstmt.setString(9, role);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public  void modifier(User p) {
        String role;
        if (p instanceof Admin) {
            role = "Admin";
        } else if (p instanceof Client) {
            role = "Client";
        } else if (p instanceof Artiste) {
            role = "Artiste";
        } else {
            role = "User";
        }

        String sql = "UPDATE user SET nom_user = ?, prenom_user = ?, email = ?, mdp = ?, num_tel = ?, date_de_naissance = ?, cartepro = ?, role = ? WHERE id_user = ?";

        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setString(1, p.getNom_user());
            pstmt.setString(2, p.getPrenom_user());
            pstmt.setString(3, p.getEmail());
            pstmt.setString(4, p.getMdp());
            if (p instanceof Client) {
                pstmt.setInt(5, ((Client) p).getNum_tel());
                pstmt.setDate(6, new java.sql.Date(((Client) p).getDate_de_naissance().getTime()));
                pstmt.setNull(7, java.sql.Types.VARCHAR);
            } else if (p instanceof Artiste) {
                pstmt.setInt(5, ((Artiste) p).getNum_tel());
                pstmt.setDate(6, new java.sql.Date(((Artiste) p).getDate_de_naissance().getTime()));
                pstmt.setString(7, ((Artiste) p).getCartepro());
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
                pstmt.setNull(6, java.sql.Types.DATE);
                pstmt.setNull(7, java.sql.Types.VARCHAR);
            }
            pstmt.setString(8, role);
            pstmt.setInt(9, p.getId_user());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User updated successfully!");
            } else {
                System.out.println("No user found with the given id.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM user WHERE id_user = ?";

        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("No user found with the given id.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public User getOneById(int id) {
        User user = null;
        String sql = "SELECT * FROM user WHERE id_user = ?";

        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                if ("Admin".equals(role)) {
                    user = new Admin();
                    // Ajoutez ici les autres attributs spécifiques à Admin si nécessaire
                } else if ("Client".equals(role)) {
                    user = new Client();
                    ((Client) user).setNum_tel(rs.getInt("num_tel"));
                    ((Client) user).setDate_de_naissance(rs.getDate("date_de_naissance"));
                } else if ("Artiste".equals(role)) {
                    user = new Artiste();
                    ((Artiste) user).setNum_tel(rs.getInt("num_tel"));
                    ((Artiste) user).setDate_de_naissance(rs.getDate("date_de_naissance"));
                    ((Artiste) user).setCartepro(rs.getString("cartepro"));
                } else {
                    user = new User();
                }
                user.setId_user(rs.getInt("id_user"));
                user.setNom_user(rs.getString("nom_user"));
                user.setPrenom_user(rs.getString("prenom_user"));
                user.setEmail(rs.getString("email"));
                user.setMdp(rs.getString("mdp"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return user; }


    /* @Override
     public Set<User> getAll() {
         Set<User> users = new HashSet<>();
         String sql = "SELECT * FROM user";

         try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
             ResultSet rs = pstmt.executeQuery();

             while (rs.next()) {
                 User user;
                 String role = rs.getString("role");
                 if ("Admin".equals(role)) {
                     user = new Admin();
                     // Ajoutez ici les autres attributs spécifiques à Admin si nécessaire
                 } else if ("Client".equals(role)) {
                     user = new Client();
                     ((Client) user).setNum_tel(rs.getInt("num_tel"));
                     ((Client) user).setDate_de_naissance(rs.getDate("date_de_naissance"));
                 } else if ("Artiste".equals(role)) {
                     user = new Artiste();
                     ((Artiste) user).setNum_tel(rs.getInt("num_tel"));
                     ((Artiste) user).setDate_de_naissance(rs.getDate("date_de_naissance"));
                     ((Artiste) user).setCartepro(rs.getString("cartepro"));
                 } else {
                     user = new User();
                 }
                 user.setNom_user(rs.getString("nom_user"));
                 user.setPrenom_user(rs.getString("prenom_user"));
                 user.setEmail(rs.getString("email"));
                 user.setMdp(rs.getString("mdp"));

                 users.add(user);
             }
         } catch (SQLException e) {
             System.out.println(e.getMessage());
         }

         return users;    }*/
    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user";

        try (PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                User user;
                String role = rs.getString("role");
                if ("Admin".equals(role)) {
                    user = new Admin();
                    // Ajoutez ici les autres attributs spécifiques à Admin si nécessaire
                } else if ("Client".equals(role)) {
                    user = new Client();
                    ((Client) user).setNum_tel(rs.getInt("num_tel"));
                    ((Client) user).setDate_de_naissance(rs.getDate("date_de_naissance"));
                } else if ("Artiste".equals(role)) {
                    user = new Artiste();
                    ((Artiste) user).setNum_tel(rs.getInt("num_tel"));
                    ((Artiste) user).setDate_de_naissance(rs.getDate("date_de_naissance"));
                    ((Artiste) user).setCartepro(rs.getString("cartepro"));
                } else {
                    user = new User();
                }
                user.setNom_user(rs.getString("nom_user"));
                user.setPrenom_user(rs.getString("prenom_user"));
                user.setEmail(rs.getString("email"));
                user.setMdp(rs.getString("mdp"));

                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return users;
    }

}