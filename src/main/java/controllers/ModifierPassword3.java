package controllers;

import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class ModifierPassword3 {
    @FXML
    private TextField oldPasswordField;

    @FXML
    private TextField newPasswordField;

    @FXML
    private TextField confirmPasswordField;

    @FXML
    private Label errorLabel;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/musemakers";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    @FXML
    private Button retourid, clientid;

    public void initialize() {
        retourid.setOnAction(event -> {
            try {
                // Charger l'interface AfficherClientNV.fxml
                Parent root = FXMLLoader.load(getClass().getResource("/AccueilArtiste.fxml"));
                Stage stage = (Stage) retourid.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }
    @FXML
    public void changePassword() {
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Vérifiez si les champs sont vides
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Tous les champs doivent être remplis");
            return;
        }

        // Récupérez l'utilisateur avec l'ancien mot de passe
        User user = getUserByPassword( oldPassword);
        if (user == null) {
            // L'utilisateur avec l'ancien mot de passe n'existe pas, affichez un message d'erreur
            errorLabel.setText("L'ancien mot de passe est incorrect");
            return;
        }

        // Vérifiez si le nouveau mot de passe et le mot de passe de confirmation correspondent
        if (!newPassword.equals(confirmPassword)) {
            errorLabel.setText("Le nouveau mot de passe et le mot de passe de confirmation ne correspondent pas");
            return;
        }

        // Mettez à jour le mot de passe
        updatePassword(oldPassword, newPassword);
        errorLabel.setText("Mot de passe mis à jour avec succès");
    }




    public void updatePassword(String oldPassword, String newPassword) {
        String req = "UPDATE user SET mdp = ? WHERE mdp = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = conn.prepareStatement(req)) {
            pst.setString(1, newPassword);
            pst.setString(2, oldPassword);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Mot de passe mis à jour avec succès pour l'utilisateur avec le mot de passe : " + oldPassword);
            } else {
                System.out.println("Aucun utilisateur trouvé avec le mot de passe : " + oldPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public User getUserByPassword(String password) {
        User user = null;
        String req = "SELECT * FROM user WHERE mdp = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = conn.prepareStatement(req)) {

            pst.setString(1, password);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                user = new User();
                // Remplissez l'objet user avec les données de la base de données
                // Par exemple : user.setEmail(rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }}
