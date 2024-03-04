package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import entities.Artiste;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.ServiceUser;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class InscriptionArtiste {

    @FXML
    private TextField nomid;
    @FXML
    private TextField prenomid;
    @FXML
    private TextField mailid;
    @FXML
    private PasswordField passeid;
    @FXML
    private TextField telid;
    @FXML
    private TextField carteproid;
    @FXML
    private DatePicker dateid;
    @FXML
    private Button browseid;
    @FXML
    private Button inscrireid;
    @FXML
    private CheckBox showPassword;
    @FXML
    private PasswordField confirmerPasseid;
    @FXML
    private Button retourid;
    private FileChooser fileChooser;
    private File file;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/musemakers";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public void initialize() {
        retourid.setOnAction(this::loadAccueil);

        fileChooser = new FileChooser();
        browseid.setOnAction(e -> handleBrowse());
        inscrireid.setOnAction(e -> handleInscription());
        showPassword.setOnAction(e -> handleShowPassword());
    }
    private void loadAccueil(ActionEvent event) { // Ajouter cette méthode
        loadFXML("/Accueil.fxml", event);
    }
    private void loadFXML(String fxmlPath, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleBrowse() {
        file = fileChooser.showOpenDialog(null);
        if (file != null) {
            carteproid.setText(file.toURI().toString());
        }
    }

    @FXML
    private void handleInscription() {
        String nom = nomid.getText();
        String prenom = prenomid.getText();
        String email = mailid.getText();
        String password = passeid.getText();
        String confirmerPassword = confirmerPasseid.getText();
        int tel = Integer.parseInt(telid.getText());
        LocalDate date = dateid.getValue();
        String cartepro = carteproid.getText();

        if (!password.equals(confirmerPassword)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur d'inscription");
            alert.setHeaderText(null);
            alert.setContentText("Les mots de passe ne correspondent pas !");
            alert.showAndWait();
            return;
        }
        if (passwordExists(password)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur d'inscription");
            alert.setHeaderText(null);
            alert.setContentText("Le mot de passe existe déjà !");
            alert.showAndWait();
            return;
        }
        try {
            // Charger la vue du captcha
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Captcha.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec la vue du captcha
            Scene scene = new Scene(root);

            // Créer une nouvelle fenêtre (stage) pour le captcha
            Stage captchaStage = new Stage();
            captchaStage.setScene(scene);

            // Afficher la fenêtre du captcha
            captchaStage.show();

            // ... (autres opérations d'inscription ici) ...
        } catch (IOException e) {
            e.printStackTrace();
        }



        Artiste artiste = new Artiste(nom, prenom, email, password, tel, Date.valueOf(date), cartepro);
        ServiceUser serviceUser = new ServiceUser();
        serviceUser.ajouter(artiste);


    }
    private boolean passwordExists(String password) {
        // Remplacez cette requête SQL par la requête appropriée pour votre base de données
        String sql = "SELECT COUNT(*) FROM user WHERE mdp = ?";

        try (Connection cnx = DriverManager.getConnection("jdbc:mysql://localhost:3306/musemakers", "root", "");
             PreparedStatement pstmt = cnx.prepareStatement(sql)) {

            pstmt.setString(1, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    @FXML
    private void handleShowPassword() {
        if (showPassword.isSelected()) {
            passeid.setPromptText(passeid.getText());
            passeid.setText(null);
            passeid.setDisable(true);
        } else {
            passeid.setText(passeid.getPromptText());
            passeid.setPromptText(null);
            passeid.setDisable(false);
        }
    }


}


