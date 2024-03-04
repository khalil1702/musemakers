package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccueilArtiste {
    @FXML
    private Button compteid;
    @FXML
    private Button logoutid ;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/musemakers";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public void initialize() {
        compteid.setOnAction(event -> {
            try {
                // Charger l'interface AfficherClientNV.fxml
                Parent root = FXMLLoader.load(getClass().getResource("/ModifierPassword3.fxml"));
                Stage stage = (Stage) compteid.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        logoutid.setOnAction(event -> {
            try {
                // Mettre à jour le statut de tous les utilisateurs
                updateAllUserStatusToNull();

                // Charger l'interface loginAdmin.fxml
                Parent root = FXMLLoader.load(getClass().getResource("/LoginAdmin.fxml"));
                Stage stage = (Stage) logoutid.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });}
    public void updateAllUserStatusToNull() {
        String req = "UPDATE user SET status = NULL";
        Connection conn = null;
        try {
            // Chargez le pilote JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Créez la connexion
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            PreparedStatement pst = conn.prepareStatement(req);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Status updated successfully for all users");
            } else {
                System.out.println("No users found in the database");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error updating status for all users");
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    }

