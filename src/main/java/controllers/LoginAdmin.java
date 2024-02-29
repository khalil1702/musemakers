package controllers;

import entities.Admin;
import entities.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import mailling.SendEmail;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.Optional;
import java.util.Random;

public class LoginAdmin {

    @FXML
    private TextField mailid;

    @FXML
    private PasswordField passeid;
    @FXML
    private CheckBox showid;

    @FXML
    private Button inscrireid;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField newPasswordField;
    @FXML
    private TextField  confirmPasswordField;
    @FXML
    private Label passwordChangeStatusLabel;
    @FXML
    private VBox changePasswordVBox;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/musemakers";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // Mettez votre mot de passe de base de données ici




    private Connection conn;

    public void initialize() {

        showid.setOnAction(e -> handleShowPassword());
        inscrireid.setOnAction(this::handleInscription);
    }
    @FXML
    private void handleShowPassword() {
        if (showid.isSelected()) {
            passeid.setPromptText(passeid.getText());
            passeid.setText(null);
            passeid.setStyle("-fx-prompt-text-fill: black;"); // Change la couleur du texte d'invite en noir
        } else {
            passeid.setText(passeid.getPromptText());
            passeid.setPromptText(null);
            passeid.setStyle(""); // Réinitialise le style
        }
    }
    public void seConnecter() {

            String email = mailid.getText();
            String motDePasse = passeid.getText();

            Platform.runLater(() -> {
                errorLabel.setText("");
            });

            if (email.isEmpty() || motDePasse.isEmpty()) {
                Platform.runLater(() -> {
                    errorLabel.setText("L'email et le mot de passe ne peuvent pas être vides");
                });
                return;
            }

            String role = findUserByEmailAndPassword(email, motDePasse);
            if (role != null) {
                // Affichez le rôle de l'utilisateur après la connexion
                System.out.println("Login effectué en tant que " + role);
                updateUserStatus(email, "connected");

                try {
                    // Définir le chemin du fichier FXML en fonction du rôle
                    String fxmlFile = "";
                    switch (role) {
                        case "Admin":
                            fxmlFile = "/AccueilAdmin.fxml";
                            break;
                        case "Artiste":
                            fxmlFile = "/AccueilArtiste.fxml";
                            break;
                        case "Client":
                            fxmlFile = "/AccueilUser.fxml";
                            break;
                    }

                    // Charger le fichier FXML
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(getClass().getResource(fxmlFile));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    // Créer une nouvelle scène
                    Scene scene = new Scene(root);

                    // Obtenir le stage actuel
                    Stage currentStage = (Stage) mailid.getScene().getWindow();

                    // Afficher la nouvelle scène
                    currentStage.setScene(scene);
                    currentStage.show();
                } catch (RuntimeException e) {
                    e.printStackTrace();


                }
            } else {
                // Les informations d'identification ne sont pas valides, affichez un message d'erreur
                Platform.runLater(() -> {
                    errorLabel.setText("Email ou mot de passe invalide");
                });
            }

   }

    private String findUserByEmailAndPassword(String email, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM user WHERE email = ? AND mdp = ?")) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void handleInscription(ActionEvent e) {
        // Votre code d'inscription ici...

        // Charger l'interface d'accueil
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Accueil.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void updateUserStatus(String email, String status) {
        String req = "UPDATE user SET status = ? WHERE email = ?";
        Connection conn = null;
        try {
            // Chargez le pilote JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Créez la connexion
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            PreparedStatement pst = conn.prepareStatement(req);
            pst.setString(1, status);
            pst.setString(2, email);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Status updated successfully for user with email: " + email);
            } else {
                System.out.println("No user found with the email: " + email);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error updating status for user with email: " + email);
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


    public  String generateCode() {
        // Define characters to be used in the code
        String characters = "0123456789";

        // Initialize random object
        Random random = new Random();

        // Initialize StringBuilder to store the generated code
        StringBuilder code = new StringBuilder();

        // Generate code of length 4
        for (int i = 0; i < 4; i++) {
            // Generate random index within the characters string length
            int randomIndex = random.nextInt(characters.length());

            // Append character at the random index to the code
            code.append(characters.charAt(randomIndex));
        }

        // Convert StringBuilder to String and return
        return code.toString();
    }


        public void modifierPassword(String email, String newPassword) {
            String req = "UPDATE user SET mdp = ? WHERE email = ?";
            Connection conn = null;
            try {
                // Chargez le pilote JDBC
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Créez la connexion
                conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

                PreparedStatement pst = conn.prepareStatement(req);
                pst.setString(1, newPassword);
                pst.setString(2, email);
                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Password updated successfully for user with email: " + email);
                } else {
                    System.out.println("No user found with the email: " + email);
                }
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Error updating password for user with email: " + email);
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



    @FXML
    void forgotPasswordButtonAction(ActionEvent event) {
        // Generate a random code
        if (mailid.getText().isEmpty()) {
            // Show a warning message
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please enter your email address.");
            alert.showAndWait();
            return; // Exit the method
        }

        String generatedCode = generateCode();

        // Send the code via email
        SendEmail.send(mailid.getText(), Integer.parseInt(generatedCode));

        // Prompt the user to enter the code
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Verification");
        dialog.setHeaderText("Enter the verification code sent to your email:");
        dialog.setContentText("Code:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String enteredCode = result.get();
            if (enteredCode.equals(generatedCode)) {
                // Code is correct, show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Verification successful. You can proceed to reset your password.");
                alert.showAndWait();
                changePasswordVBox.setVisible(true);

                // Proceed to reset password page
                // Add your code to navigate to the reset password page here
            } else {
                // Code is incorrect, show alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Invalid verification code. Please try again.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    void changePasswordAction(ActionEvent event) {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Check if new password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            passwordChangeStatusLabel.setText("Passwords do not match.");
            return;
        }



        // Update the password in the database

        String email = mailid.getText(); // Assuming you have an emailField for the user's email
        modifierPassword(email, newPassword);

        // Show success message
        passwordChangeStatusLabel.setText("Password changed successfully.");

        changePasswordVBox.setVisible(false);
    }

    /*@FXML
  private void handleButtonAction(ActionEvent event) throws IOException {
      // Charger le deuxième fichier FXML
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginClient.fxml"));

      // Créer une nouvelle scène
      Parent root = loader.load();
      Scene scene = new Scene(root);

      // Obtenir le contrôleur pour le deuxième fichier FXML
      LoginClient secondController = loader.getController();

      // Fermer la fenêtre actuelle
      Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      currentStage.close();

      // Afficher la nouvelle scène
      Stage stage = new Stage();
      stage.setScene(scene);
      stage.show();
  }*/
}
