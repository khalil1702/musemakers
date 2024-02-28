package edu.esprit.controllers;

import edu.esprit.entities.Exposition;
import edu.esprit.entities.Reservation;
import edu.esprit.entities.User;
import edu.esprit.services.ServiceExposition;
import edu.esprit.services.ServicePersonne;
import edu.esprit.services.ServiceReservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import mailing.SendEmailExpo;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.Set;

public class DemandeReservation {
    @FXML
    private TableColumn<Exposition, Timestamp> dateDebutColumn;

    @FXML
    private TableColumn<Exposition, Timestamp> dateFinColumn;

    @FXML
    private TableColumn<Reservation, Timestamp> dateReservationColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private Button tripardate;
    @FXML
    private TableColumn<Exposition, String> nomExpositionColumn;

    @FXML
    private TableView<Reservation> reservationTableView;

    @FXML
    private TableColumn<Exposition, String> themeExpositionColumn;

    @FXML
    private TableColumn<Reservation, Integer> ticketsNumberColumn;

    @FXML
    private TableColumn action;


    private ServiceReservation serviceReservation = new ServiceReservation();
    private ServiceExposition serviceExposition = new ServiceExposition();
    private ServicePersonne servicePersonne = new ServicePersonne();

    Exposition exposition = new Exposition();
    User user = new User();
    @FXML
    private void initialize() {
        // Assuming you have a method to retrieve "En cours" reservations in your service class
        Set<Reservation> enCoursReservations = serviceReservation.getEnCoursReservations();

        // Populate the TableView with "En cours" reservations
        reservationTableView.getItems().addAll(enCoursReservations);

        // Set up the columns (you might need to adjust the properties based on your Reservation class)
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("userEmail"));
        nomExpositionColumn.setCellValueFactory(new PropertyValueFactory<>("expositionNom"));
        themeExpositionColumn.setCellValueFactory(new PropertyValueFactory<>("expositionTheme"));
        dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("ExpositionDateD"));
        dateFinColumn.setCellValueFactory(new PropertyValueFactory<>("ExpositionDateF"));
        ticketsNumberColumn.setCellValueFactory(new PropertyValueFactory<>("ticketsNumber"));
        tripardate.setOnAction(event -> triparDateAncienne());

        dateReservationColumn.setCellValueFactory(new PropertyValueFactory<>("dateReser"));
//        etatrdv.setCellValueFactory(new PropertyValueFactory<>("accessByAdmin"));
        action.setCellFactory(column -> new TableCell<Reservation, Void>() {
            private final Button checkButton = new Button("✔");
            private final Button xButton = new Button("X");

            {

                checkButton.setOnAction(event -> {
                    checkButton.getStyleClass().add("edit-button");
                    Reservation reservation = getTableRow().getItem();
                    if (reservation != null && showConfirmationDialog("accepter cette reservation")) {
                        serviceReservation.acceptReservation(reservation.getIdReservation());
                        refreshTable();

                        // Obtenez l'adresse e-mail, la date de début et la date de fin de l'objet reservation
                        String toEmail = reservation.getUserEmail();
                        String nomExpo=reservation.getExpositionNom();
                        String dateDebut = reservation.getExpositionDateD().toString();
                        String dateFin = reservation.getExpositionDateF().toString();
                        String timeD = reservation.getHeureDebutExpo().toString();
                        String timeF = reservation.getHeureFinExpo().toString();


                        // Envoyez l'e-mail
                        SendEmailExpo.send(toEmail, dateDebut, dateFin,nomExpo,timeD,timeF);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("email envoyé avec succes");
                        alert.setHeaderText(null);
                        alert.setContentText("L'e-mail a été envoyé avec succès à \" "+ toEmail);
                        alert.showAndWait();
                    }
                });
                xButton.setOnAction(event -> {
                    xButton.getStyleClass().add("x-button");
                    Reservation reservation = getTableRow().getItem();
                    if (reservation != null && showConfirmationDialog("X")) {
                        serviceReservation.refuserReservation(reservation.getIdReservation());
                        refreshTable();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, checkButton, xButton));
                }
            }
        });
    }

    // Method to show a confirmation dialog
    private boolean showConfirmationDialog(String action) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmer l'action" );
        alert.setContentText("vous etes sure?" );

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    // Method to refresh the table
    private void refreshTable() {
        reservationTableView.getItems().clear();
        Set<Reservation> enCoursReservations = serviceReservation.getEnCoursReservations();
        reservationTableView.getItems().addAll(enCoursReservations);
    }
    @FXML
    private void triparDateAncienne() {
        // Implement your sorting logic here
        Set<Reservation> sortedReservations = serviceReservation.triparDateAncienne();

        // Clear and repopulate the TableView with the sorted data
        reservationTableView.getItems().clear();
        reservationTableView.getItems().addAll(sortedReservations);
    }

    //////////
    @FXML
    void Afficher(ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/afficherExpo.fxml"));
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
    void ajouterNav(ActionEvent event) throws IOException {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/ajouterexpo.fxml"));
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
    void demandeNav(ActionEvent event) throws IOException {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/demandeReser.fxml"));
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
    void histoAdminNav(ActionEvent event) throws IOException {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/listeReser.fxml"));
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





    }


