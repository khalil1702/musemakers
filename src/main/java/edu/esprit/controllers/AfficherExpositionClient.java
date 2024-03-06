package edu.esprit.controllers;

import edu.esprit.entities.Exposition;
import edu.esprit.entities.Reservation;
import edu.esprit.entities.User;
import edu.esprit.services.ServiceExposition;
import edu.esprit.services.ServicePersonne;
import edu.esprit.services.ServiceReservation;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;

import java.sql.Date;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

public class AfficherExpositionClient {
    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private TextField nameSearchID;

    @FXML
    private ComboBox<String> themeSearchID;

    private final Date currentDate = new Date(System.currentTimeMillis());
    private final Reservation reser = new Reservation();
    private final ServicePersonne servicePersonne = new ServicePersonne();
    private final ServiceExposition exp = new ServiceExposition();
    private final ServiceReservation serviceReservation = new ServiceReservation();

    private Set<Exposition> listexpo = exp.getAll();

    @FXML
    private VBox exhibitionVBox;
    public AfficherExpositionClient() throws SQLException {
    }

    // Méthode appelée lors de l'initialisation de la vue
    @FXML
    public void initialize() throws SQLException {
        displayExhibitions();

        // Add a ChangeListener to the nameSearchID TextField
        nameSearchID.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());

        // Add a ChangeListener to the themeSearchID TextField
        themeSearchID.valueProperty().addListener((observable, oldValue, newValue) -> handleSearch());

        comboBox.setItems(FXCollections.observableArrayList(
                "date Ascendante", "date Descendante"
        ));

        comboBox.setOnAction(event -> handleSortAction());

    }



    // Méthode pour afficher toutes les expositions
    private void displayExhibitions() {
        exhibitionVBox.getChildren().clear();
        for (Exposition expo : listexpo) {
            if (expo.getDateDebut().toLocalDate().isBefore(currentDate.toLocalDate())) {
                continue;  // Skip this exhibition
            }

            HBox exhibitionBox = new HBox(10);
            exhibitionBox.setAlignment(javafx.geometry.Pos.CENTER);
            ImageView imageView = new ImageView();
            try {
                String imagePath = "file:C:\\xampp\\htdocs\\user_images\\" + expo.getImage();
                Image image = new Image(imagePath);
                imageView.setImage(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(200);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

//            ImageView imageView = new ImageView();
//            try {
//                Image image = new Image(new File(expo.getImage()).toURI().toString());
//                imageView.setImage(image);
//                imageView.setFitWidth(200);
//                imageView.setFitHeight(200);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }

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

            // Check if the exhibition is already reserved by the current user
            User currentUser = getCurrentUser();
            boolean isAlreadyReserved = currentUser != null && userHasReservationForExhibition(currentUser, expo);

            if (isAlreadyReserved) {
                reserveButton.setDisable(true); // Disable the button if already reserved

                // Get the Reservation object for the current user and exhibition
                Reservation existingReservation = serviceReservation.getReservationByUserAndExposition(currentUser, expo);

                // Show "In Progress" or "Accepté" based on accessByAdmin
                double progressValue = (existingReservation.getAccessByAdmin() == 0) ? 0.5 : 1.0;
                ProgressBar progressBar = new ProgressBar(progressValue);

                Label statusLabel = new Label((existingReservation.getAccessByAdmin() == 0) ? "En Cours" : "Accepté");
                statusLabel.getStyleClass().add("status-label");

// Apply different styles based on the status
                if (existingReservation.getAccessByAdmin() == 0) {
                    statusLabel.setStyle("-fx-text-fill: blue;");
                } else {
                    statusLabel.setStyle("-fx-text-fill: green;");
                }
                detailsVBox.getChildren().addAll(nameLabel, dateTimeLabel, themeLabel, descriptionLabel, timeLabel, reserveButton, progressBar, statusLabel);
            } else {
                ProgressBar progressBar = new ProgressBar(0.0);

                reserveButton.setOnAction(event -> {
                    // Perform reservation logic here
                    // For demonstration purposes, I'll set the progress to 0.5 (50%)
                    progressBar.setProgress(0.5);
                    showReservationDialog(expo);

                    Platform.runLater(() -> reserveButton.setDisable(true));
                });

                detailsVBox.getChildren().addAll(nameLabel, dateTimeLabel, themeLabel, descriptionLabel, timeLabel, reserveButton, progressBar);
            }

            exhibitionBox.getChildren().addAll(imageView, detailsVBox);
            exhibitionBox.getStyleClass().add("exhibition-box");

            exhibitionVBox.getChildren().add(exhibitionBox);
        }
        if (listexpo.isEmpty()) {
            Label noResultLabel = new Label("Aucune exposition trouvée.");
            exhibitionVBox.getChildren().add(noResultLabel);
        }
    }

    //
    private boolean userHasReservationForExhibition(User user, Exposition exposition) {
        // Use your actual logic to check if the user has a reservation for the given exhibition
        // For example, you can call a service method to check if a reservation exists
        // Return true if the user has a reservation, false otherwise
        Reservation existingReservation = serviceReservation.getReservationByUserAndExposition(user, exposition);
        return existingReservation != null;
    }

    // Replace this with your actual logic to get the current user
    private User getCurrentUser() {
        // Simulated logic to get the current user (replace with your actual logic)
        return servicePersonne.getOneById(LoginAdmin.getLoggedInUser().getId_user());
    }
    //

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
    private String formatDateTime(LocalDate date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(dateTimeFormatter);
    }

    //    @FXML
//    private void sortByDateAscending() {
//        try {
//            listexpo.clear();
//            listexpo.addAll(exp.trierParDateDebutAsc());
//
//            // Force a refresh of the UI
//            Platform.runLater(() -> {
//                // Clear the previous exhibitions from exhibitionVBox
//                exhibitionVBox.getChildren().clear();
//
//                // Display the updated listexpo in exhibitionVBox
//                displayExhibitions();
//            });
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @FXML
//    private void sortByDateDescending() {
//        try {
//            listexpo.clear();
//            listexpo.addAll(exp.trierParDateDebutDesc());
//
//            // Force a refresh of the UI
//            Platform.runLater(() -> {
//                // Clear the previous exhibitions from exhibitionVBox
//                exhibitionVBox.getChildren().clear();
//
//                // Display the updated listexpo in exhibitionVBox
//                displayExhibitions();
//            });
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    private void handleSortAction() {
        String selectedOption = comboBox.getValue();

        switch (selectedOption) {
            case "date Ascendante":
                System.out.println("Sorting by date Ascendante");
                // Trier les expositions par date de début ascendante
                listexpo = listexpo.stream().sorted(Comparator.comparing(Exposition::getDateDebut)).collect(Collectors.toCollection(LinkedHashSet::new));
                break;
            case "date Descendante":
                System.out.println("Sorting by date Descendante");
                // Trier les expositions par date de début descendante
                listexpo = listexpo.stream().sorted(Comparator.comparing(Exposition::getDateDebut).reversed()).collect(Collectors.toCollection(LinkedHashSet::new));
                break;
            default:
                break;
        }

        // Print the sorted list for debugging
        System.out.println("Sorted Exhibitions: " + listexpo);

        // Réafficher les expositions après le tri
        displayExhibitions();
    }









}

