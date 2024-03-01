package controllers;

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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import service.ServiceCour;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class AjouterCourNV1 {



        @FXML
        private DatePicker datePicker;
        @FXML
        private DatePicker datefin;

        @FXML
        private TextField titreField;

        @FXML
        private TextField descriptionField;



        @FXML
        private Text title;

        private final ServiceCour serviceCour = new ServiceCour();
    @FXML
    void Ajouterr(ActionEvent event) {
        try {
            LocalDate dateDebut = datePicker.getValue();
            LocalDate dateFin = datefin.getValue();

            // Vérifier si les dates de début et de fin sont sélectionnées
            if (dateDebut == null || dateFin == null) {
                throw new IllegalArgumentException("Veuillez sélectionner une date de début et une date de fin.");
            }

            // Vérifier que la date de fin est postérieure à la date de début
            if (dateFin.isBefore(dateDebut)) {
                throw new IllegalArgumentException("La date de fin doit être postérieure à la date de début.");
            }

            // Vérifier que le titre ne contient que des caractères et des espaces
            if (!titreField.getText().matches("[a-zA-Z\\s]+")) {
                throw new IllegalArgumentException("Le titre ne doit contenir que des lettres et des espaces.");
            }

            // Vérifier que le champ description n'est pas vide
            if (descriptionField.getText().isEmpty()) {
                throw new IllegalArgumentException("La description ne doit pas être vide.");
            }

            // Si toutes les validations sont réussies, créer l'objet Cour et l'ajouter
            String titre = titreField.getText();
            String description = descriptionField.getText();
            Cour cour = new Cour(titre, description, dateDebut, dateFin, null);
            serviceCour.ajouter(cour);

            // Afficher une confirmation à l'utilisateur
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validation");
            alert.setContentText("Cour ajouté avec succès");
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




        public void afficheriid(ActionEvent actionEvent) throws IOException {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/AfficherCourNv.fxml")));
            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Configurer la nouvelle scène dans une nouvelle fenêtre
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Afficher les cours");

            // Afficher la nouvelle fenêtre
            stage.show();
        }
    public void ajoutid(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/AjouterAtelierNv.fxml")));
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





