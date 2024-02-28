package edu.esprit.controllers;

import edu.esprit.entities.Exposition;
import edu.esprit.entities.Reservation;
import edu.esprit.services.ServiceExposition;
import edu.esprit.services.ServicePersonne;
import edu.esprit.services.ServiceReservation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Set;

public class AfficherExpositionClient {
    @FXML
    private TextField nameSearchID;

    @FXML
    private ComboBox<String> themeSearchID;


    private final Reservation reser = new Reservation();
    private final ServicePersonne servicePersonne = new ServicePersonne();
    private final ServiceExposition exp = new ServiceExposition();

    private Set<Exposition> listexpo = exp.getAll();

    @FXML
    private VBox exhibitionVBox;
    public AfficherExpositionClient() throws SQLException {
    }

    // Méthode appelée lors de l'initialisation de la vue
    @FXML
    public void initialize() {

        // Add a ChangeListener to the nameSearchID TextField
        nameSearchID.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());

        // Add a ChangeListener to the themeSearchID TextField
        themeSearchID.valueProperty().addListener((observable, oldValue, newValue) -> handleSearch());

        // Initial display of all exhibitions
        displayExhibitions();
    }


    // Méthode pour afficher toutes les expositions
    private void displayExhibitions() {
        for (Exposition expo : listexpo) {
            HBox exhibitionBox = new HBox(10);
            exhibitionBox.setAlignment(javafx.geometry.Pos.CENTER);

            ImageView imageView = new ImageView();
            try {
                Image image = new Image(new File(expo.getImage()).toURI().toString());
                imageView.setImage(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(200);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            VBox detailsVBox = new VBox(5);
            detailsVBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

            Label nameLabel = new Label("Nom: " + expo.getNom());
            nameLabel.getStyleClass().addAll("bold-label");

            Label dateTimeLabel = new Label("Date : " + formatDateTime(expo.getDateDebut()) + " - " + formatDateTime(expo.getDateFin()));
            dateTimeLabel.getStyleClass().addAll("bold-label");

            Label timeLabel = new Label("Heure: " +
                    formatHourMinute(expo.getHeure_debut()) +
                    " - " +
                    formatHourMinute(expo.getHeure_fin()));
            timeLabel.getStyleClass().addAll("bold-label");

            Label themeLabel = new Label("Thème: " + expo.getTheme());
            themeLabel.getStyleClass().addAll("bold-label");

            Label descriptionLabel = new Label("Description: " + expo.getDescription());
            descriptionLabel.getStyleClass().addAll("bold-label");


            Button reserveButton = new Button("Réserver");
            reserveButton.setId("btnreserverexposition");
            reserveButton.setOnAction(event -> showReservationDialog(expo));

            detailsVBox.getChildren().addAll(nameLabel, dateTimeLabel, themeLabel, descriptionLabel, timeLabel, reserveButton);

            exhibitionBox.getChildren().addAll(imageView, detailsVBox);
            exhibitionBox.getStyleClass().add("exhibition-box");

            exhibitionVBox.getChildren().add(exhibitionBox);
        }
        if (listexpo.isEmpty()) {
            Label noResultLabel = new Label("Aucune exposition trouvée.");
            exhibitionVBox.getChildren().add(noResultLabel);
        }
    }

    private String formatDateTime(java.util.Date date) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateTimeFormat.format(date);
    }

    private void showReservationDialog(Exposition expo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/Reservation.fxml"));
            Parent root = loader.load();

            ReservationController controller = loader.getController();
            controller.setExposition(expo);

            Stage stage = new Stage();
            stage.setTitle("Formulaire de Réservation");

            Label titleLabel = new Label("Réserver des Billets");
            titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Réserver l'exposition");
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void histoClientNav(javafx.event.ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/histoReservationClient.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the stage from the event source (button) and set the new scene
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }
    @FXML
    void reserverNav(javafx.event.ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/afficherExpoClient.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the stage from the event source (button) and set the new scene
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }
    @FXML
    private void handleSearch() {
        String theme = themeSearchID.getValue();
        String name = nameSearchID.getText();

        try {
            if ((theme == null || theme.isEmpty()) && (name == null || name.isEmpty())) {
                // If both theme and name are empty, fetch all exhibitions
                listexpo = exp.getAll();
            } else {
                // Otherwise, execute the search
                Set<Exposition> searchResult = exp.chercherParThemeOuNom(theme, name);
                listexpo.clear(); // Clear the existing list
                listexpo.addAll(searchResult); // Update the listexpo
            }

            // Clear the previous search results from exhibitionVBox
            exhibitionVBox.getChildren().clear();

            // Display the updated listexpo in exhibitionVBox
            displayExhibitions();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    ////////
    private String formatHourMinute(Time time) {
        // Extract hours and minutes from the Time object
        int hours = time.toLocalTime().getHour();
        int minutes = time.toLocalTime().getMinute();

        // Format as "HH:mm"
        return String.format("%02d:%02d", hours, minutes);
    }







}

