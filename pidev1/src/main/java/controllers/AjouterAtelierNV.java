package controllers;

import entities.Atelier;
import entities.Cour;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.ServiceAtelier;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class AjouterAtelierNV {


    @FXML
    private DatePicker datePickerDebut;

    @FXML
    private DatePicker datePickerFin;

    @FXML
    private TextField lienField;

    private final ServiceAtelier serviceAtelier = new ServiceAtelier();
    @FXML
    void Ajouter(ActionEvent event) {
        try {
            LocalDate dateDebut = datePickerDebut.getValue();
            LocalDate dateFin = datePickerFin.getValue();

            // Vérifier si les dates de début et de fin sont sélectionnées
            if (dateDebut == null || dateFin == null) {
                throw new IllegalArgumentException("Veuillez sélectionner une date de début et une date de fin.");
            }

            // Vérifier que la date de fin est postérieure à la date de début
            if (dateFin.isBefore(dateDebut)) {
                throw new IllegalArgumentException("La date de fin doit être postérieure à la date de début.");
            }

            // Vérifier que le lien respecte un format spécifique (par exemple, un lien HTTP ou HTTPS)
            String lien = lienField.getText();
            if (!lien.matches("^https?://.*$")) {
                throw new IllegalArgumentException("Le lien doit commencer par 'http://' ou 'https://'.");
            }

            // Si toutes les validations sont réussies, créer l'objet Atelier et l'ajouter
            // Vous devez obtenir le cour sélectionné depuis votre interface utilisateur
            Cour cour = null; // Remplacez null par le cour sélectionné
            Atelier atelier = new Atelier(cour, dateDebut, dateFin, lien);
            serviceAtelier.ajouter(atelier);

            // Afficher une confirmation à l'utilisateur
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validation");
            alert.setContentText("Atelier ajouté avec succès");
            alert.showAndWait();
        } catch (Exception e) {
            // Gérer les exceptions et afficher une alerte d'erreur
            afficherAlerteErreur("Erreur", e.getMessage());
        }
    }


    private void afficherAlerteErreur(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }




    public void handle(ActionEvent actionEvent) throws IOException {

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/AfficherAtelierNv.fxml")));
            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Configurer la nouvelle scène dans une nouvelle fenêtre
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Afficher les cours");

            // Afficher la nouvelle fenêtre
            stage.show();
    }


}



