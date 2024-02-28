package edu.esprit.controllers;

import edu.esprit.entities.Exposition;
import edu.esprit.services.ServiceExposition;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.control.Alert;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class AjouterExposition {
    private final ServiceExposition exp = new ServiceExposition();

    private File selectedFile;
    private String imageUrl;

    @FXML
    public Button buttonaddExpoID;
    @FXML
    private DatePicker startDateTimeTextField;
    @FXML
    private DatePicker endDateTimeTextField;
    @FXML
    public TextField pathimageid;
    @FXML
    private TextField startTime;
    @FXML
    private TextField endTime;

    @FXML
    public ComboBox<String> themeID;
    @FXML
    public TextArea descriptionId;
    @FXML
    public TextField nomExpoId;

    private final String FORMAT_ATTENDU = "yyyy-MM-dd";

    @FXML
    public Button browseButton;

    public void addExpo(ActionEvent event) throws IOException, SQLException {
        // Vérifiez si tous les champs sont remplis
        if (nomExpoId.getText().isEmpty() || startDateTimeTextField.getValue() == null ||
                endDateTimeTextField.getValue() == null || descriptionId.getText().isEmpty() ||
                themeID.getValue() == null || pathimageid.getText().isEmpty()||startTime.getText().isEmpty()||endTime.getText().isEmpty()) {
            // Affichez un message d'erreur et quittez la méthode
            showAlert("Erreur", "Veuillez remplir tous les champs", Alert.AlertType.ERROR);
            return;
        }
        LocalDate startDate = startDateTimeTextField.getValue();
        LocalDate endDay = endDateTimeTextField.getValue();

        LocalTime startTimeValue = null;
        LocalTime endTimeValue = null;
        try {
            startTimeValue = LocalTime.parse(startTime.getText());
            endTimeValue = LocalTime.parse(endTime.getText());
        } catch (DateTimeParseException e) {
            showAlert("Erreur", "Format de l'heure invalide, veuillez réssayer avec cette format: (hh:mm)", Alert.AlertType.ERROR);
            return;
        }

        // Vérifiez si la date de début est antérieure à la date actuelle
        if (startDate.isBefore(LocalDate.now())) {
            showAlert("Erreur", "La date de début ne peut pas être antérieure à la date actuelle", Alert.AlertType.ERROR);
            return;
        }
        if (!isImageFile(Paths.get(pathimageid.getText()))) {
            showAlert("Erreur", "Veuillez sélectionner un fichier image valide (png, jpg, jpeg)", Alert.AlertType.ERROR);
            return;
        }

        // Vérifiez si la date de fin est postérieure à la date de début
        if (endDay.isBefore(startDate)) {
            showAlert("Erreur", "La date de fin doit être après la date de début", Alert.AlertType.ERROR);
            return;
        }

        // Ajoutez l'exposition uniquement si toutes les validations ont réussi
        exp.ajouter(new Exposition(
                nomExpoId.getText(),
                Date.valueOf(startDate),
                Date.valueOf(endDay),
                Time.valueOf(startTimeValue),
                Time.valueOf(endTimeValue),
                descriptionId.getText(),
                themeID.getValue(),
                pathimageid.getText()));

        // Affichez un message de succès
        showAlert("Succès", "Exposition ajoutée avec succès!", Alert.AlertType.INFORMATION);
    }

    // Méthode pour afficher une boîte de dialogue d'alerte
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void Afficher(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/afficherExpo.fxml"));
        Parent root = loader.load();
        nomExpoId.getScene().setRoot(root);
    }

    @FXML
    void ajouterNav(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/ajouterexpo.fxml"));
        Parent root = loader.load();
        nomExpoId.getScene().setRoot(root);
    }

    @FXML
    void demandeNav(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/demandeReser.fxml"));
        Parent root = loader.load();
        nomExpoId.getScene().setRoot(root);
    }

    @FXML
    void histoAdminNav(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/listeReser.fxml"));
        Parent root = loader.load();
        nomExpoId.getScene().setRoot(root);
    }

    public void initialize() {
        // Ajouter un écouteur de changement de texte
        endDateTimeTextField.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Si le champ est vidé, rétablir le texte d'exemple
            if (newValue == null) {
                endDateTimeTextField.setPromptText(FORMAT_ATTENDU);
            }
        });
    }

    @FXML
    void browseImage(ActionEvent event) {
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            // Check if the selected file is an image
            if (!isImageFile(selectedFile.toPath())) {
                showAlert("Erreur", "Veuillez sélectionner un fichier image valide (png, jpg, jpeg)", Alert.AlertType.ERROR);
                return;
            }

            // Load the image from the selected file
            try {
                // Utilisez la classe Paths pour obtenir un chemin de fichier correct
                String imagePath = Paths.get(selectedFile.getPath()).toUri().toString();
                Image image = new Image(imagePath);

                // Affichez l'image dans l'ImageView si nécessaire
                // imageView.setImage(image);

                // Enregistrez le chemin du fichier dans le champ de texte pathimageid
                pathimageid.setText(selectedFile.getPath());
            } catch (Exception e) {
                showAlert("Erreur", "Erreur lors du chargement de l'image : " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    // Helper method to check if a file is an image
    private boolean isImageFile(Path path) {
        try {
            String contentType = Files.probeContentType(path);
            return contentType != null && contentType.startsWith("image/");
        } catch (Exception e) {
            return false;
        }
    }


}
