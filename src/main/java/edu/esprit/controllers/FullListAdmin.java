package edu.esprit.controllers;

import edu.esprit.entities.Exposition;
import edu.esprit.entities.Reservation;
import edu.esprit.entities.User;
import edu.esprit.services.ServiceExposition;
import edu.esprit.services.ServicePersonne;
import edu.esprit.services.ServiceReservation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.Set;

public class FullListAdmin {
    @FXML
    private TableColumn<Exposition, Timestamp> dateDebutColumn;

    @FXML
    private TableColumn<Exposition, Timestamp> dateFinColumn;

    @FXML
    private TableColumn<Reservation, Timestamp> dateReservationColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<Reservation, Integer> etatrdv;

    @FXML
    private TableColumn<Exposition, String> nomExpositionColumn;

    @FXML
    private TableView<Reservation> reservationTableView;

    @FXML
    private TableColumn<Exposition, String> themeExpositionColumn;

    @FXML
    private TableColumn<Reservation, Integer> ticketsNumberColumn;

    private ServiceReservation serviceReservation = new ServiceReservation();


    private String getStatusText(Integer accessByAdmin) {
        if (accessByAdmin == null) {
            return null;
        }

        switch (accessByAdmin) {
            case 0:
                return "En Cours";
            case 1:
                return "Accepté";
            case 2:
                return "Refusé";
            case 3:
                return "Annulé";
            default:
                return "Unknown";
        }
    }
    @FXML
    private void initialize() throws SQLException {
        // Assuming you have a method to retrieve "En cours" reservations in your service class
        Set<Reservation> enCoursReservations = serviceReservation.getAll();

        // Populate the TableView with "En cours" reservations
        reservationTableView.getItems().addAll(enCoursReservations);

        // Set up the columns (you might need to adjust the properties based on your Reservation class)
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("userEmail"));
        nomExpositionColumn.setCellValueFactory(new PropertyValueFactory<>("expositionNom"));
        themeExpositionColumn.setCellValueFactory(new PropertyValueFactory<>("expositionTheme"));
        dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("ExpositionDateD"));
        dateFinColumn.setCellValueFactory(new PropertyValueFactory<>("ExpositionDateF"));
        ticketsNumberColumn.setCellValueFactory(new PropertyValueFactory<>("ticketsNumber"));
        dateReservationColumn.setCellValueFactory(new PropertyValueFactory<>("dateReser"));
       etatrdv.setCellValueFactory(new PropertyValueFactory<>("accessByAdmin"));

        etatrdv.setCellFactory(column -> new TableCell<Reservation, Integer>() {
            @Override
            protected void updateItem(Integer accessByAdmin, boolean empty) {
                super.updateItem(accessByAdmin, empty);
                if (empty || accessByAdmin == null) {
                    setText("");
                    setStyle(""); // Clear any previous styles
                } else {
                    String statusText = getStatusText(accessByAdmin);
                    setText(statusText != null ? statusText : "Unknown");

                    // Apply styles based on the status
                    if (accessByAdmin == 1) {
                        // Accepté (Green)
                        setStyle("-fx-background-color: #B1DE77;");
                    } else if (accessByAdmin == 2) {
                        // Refusé (Red)
                        setStyle("-fx-background-color: #F4A48F;");
                    } else if (accessByAdmin == 3) {
                        // Annulé (Orange)
                        setStyle("-fx-background-color: #F5DEA2;");
                    } else {
                        setStyle(""); // Clear any previous styles
                    }
                }
            }
        });

    }

    // Method to show a confirmation dialog


    // Method to refresh the table
    private void refreshTable() throws SQLException {
        reservationTableView.getItems().clear();
        Set<Reservation> getallReservations = serviceReservation.getAll();
        reservationTableView.getItems().addAll(getallReservations);
    }
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
    private void displayStatistics(ActionEvent event) {
        ServiceReservation serviceReservation = new ServiceReservation(/* pass your database connection */);
        serviceReservation.afficherStatistiques();
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

