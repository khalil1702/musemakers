package controllers;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import entities.Client;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.ServiceUser;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;



public class InscriptionClient {

    @FXML
    private TextField nomid;

    @FXML
    private TextField prenomid;

    @FXML
    private TextField mailid;

    @FXML
    private PasswordField passeid;
    @FXML
    private PasswordField confirmerid;

    @FXML
    private TextField telid;

    @FXML
    private DatePicker dateid;

    @FXML
    private Button validerid;
    @FXML
    private Button retourid;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/musemakers";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public InscriptionClient() {
    }

    @FXML
    public void initialize() {
        retourid.setOnAction(this::loadAccueil);

        if (validerid != null) {
            validerid.setOnAction(e -> handleSubmit());
        } else {
            System.out.println("validerid is null");
        }
    }



    private void handleSubmit() {
        String nom = nomid.getText();
        String prenom = prenomid.getText();
        String mail = mailid.getText();
        String passe = passeid.getText();
        String tel = telid.getText();
        LocalDate date = dateid.getValue();
        String confirmerPassword = confirmerid.getText();
        if (!passe.equals(confirmerPassword)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur d'inscription");
            alert.setHeaderText(null);
            alert.setContentText("Les mots de passe ne correspondent pas !");
            alert.showAndWait();
            return;
        }
        if (passeExists(passe)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur d'inscription");
            alert.setHeaderText(null);
            alert.setContentText("Le mot de passe existe déjà !");
            alert.showAndWait();
            return;
        }


        // Créez un nouvel utilisateur en fonction des informations du formulaire
        // Ici, je suppose que vous créez un Client, mais vous pouvez créer un Admin ou un Artiste si nécessaire
        Client newUser = new Client();
        newUser.setNom_user(nom);
        newUser.setPrenom_user(prenom);
        newUser.setEmail(mail);
        newUser.setMdp(passe);
        newUser.setNum_tel(Integer.parseInt(tel)); // Assurez-vous que le numéro de téléphone est un nombre entier
        newUser.setDate_de_naissance(java.sql.Date.valueOf(date));

        // Créez une instance de votre service
        ServiceUser serviceUser = new ServiceUser();

        // Ajoutez le nouvel utilisateur à l'aide de votre service
        serviceUser.ajouter(newUser);
        // Affichez une alerte d'inscription réussie
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Inscription réussie");
        alert.setHeaderText(null);
        alert.setContentText("Inscription réussie!");

        // Fermez l'alerte après un délai
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> alert.close());
        delay.play();

        alert.showAndWait();

        // Chargez l'interface LoginAdmin.fxml
        loadFXML("/LoginAdmin.fxml", new ActionEvent());


    }
    private boolean passeExists(String password) {
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

    private void loadAccueil(ActionEvent event) { // Ajouter cette méthode
        loadFXML("/Accueil.fxml", event);
    }
    private void loadFXML(String fxmlPath, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
  }
