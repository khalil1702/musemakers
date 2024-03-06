package edu.esprit.controllers;

import edu.esprit.entities.Exposition;
import edu.esprit.entities.Reservation;
import edu.esprit.entities.User;
import edu.esprit.pdf.PdfGenerator;
import edu.esprit.pdf.PdfGeneratorMail;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DemandeReservation {
    private List<Reservation> resrInitiales;
    @FXML
    private ComboBox<String> ComboBox;
    @FXML
    private TableColumn<Exposition, Timestamp> dateDebutColumn;

    @FXML
    private TableColumn<Exposition, Timestamp> dateFinColumn;

    @FXML
    private TableColumn<Reservation, Timestamp> dateReservationColumn;

    @FXML
    private TableColumn<User, String> emailColumn;


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

                        // Obtain the email address, start date, end date, and other details from the reservation object
                        String toEmail = reservation.getUserEmail();
                        String nomExpo = reservation.getExpositionNom();
                        String dateDebut = reservation.getExpositionDateD().toString();
                        String dateFin = reservation.getExpositionDateF().toString();
                        String timeD = reservation.getHeureDebutExpo().toString();
                        String timeF = reservation.getHeureFinExpo().toString();

                        // Ensure that you have the correct user object with non-null values
                        User user = reservation.getClient(); // Assuming there is a getUser method in the Reservation class
                        Exposition exposition = reservation.getExposition(); // Assuming there is a getExposition method in the Reservation class

                        if (user != null && exposition != null) {
                            byte[] pdfData = PdfGeneratorMail.generatePDFMail(exposition, user, reservation);

                            SendEmailExpo.send(toEmail, dateDebut, dateFin, nomExpo, timeD, timeF, pdfData);

                            // Show a success alert
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Email envoyé avec succès");
                            alert.setHeaderText(null);
                            alert.setContentText("L'e-mail a été envoyé avec succès à \"" + toEmail + "\"\n"
//                                    "Nom de l'exposition : " + nomExpo + "\n" +
//                                    "Date de début : " + dateDebut + "\n" +
//                                    "Date de fin : " + dateFin + "\n" +
//                                    "Heure de début : " + timeD + "\n" +
//                                    "Heure de fin : " + timeF
                                    );
                            alert.showAndWait();
                        } else {
                            // Handle the case where the user or exposition object is null
                            System.err.println("User or exposition object is null");
                        }
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
        ComboBox.getItems().add("");

        // Ajoutez les options de tri au ComboBox
        ComboBox.getItems().addAll("Date ascendante", "Date descendante");

        // Faites une copie de la liste initiale des œuvres
        resrInitiales = FXCollections.observableArrayList(reservationTableView.getItems());

        // Ajoutez un écouteur pour détecter quand l'utilisateur change l'option de tri
        ComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            trierReser(newValue);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/afficherExpo_un.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/ajouterExpo_1.fxml"));
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
    private void trierReser(String option) {
        List<Reservation> reser;

        if (option.equals("")) {
            // Si l'option est vide, réinitialisez la TableView à son état initial
            reser = resrInitiales;
        } else {
            // Sinon, faites une copie de la liste initiale et triez-la
            reser = resrInitiales.stream().collect(Collectors.toList());
            switch (option) {
                case "Date ascendante":
                    serviceReservation.triParDateReser(reser, true);
                    break;
                case "Date descendante":
                    serviceReservation.triParDateReser(reser, false);
                    break;
                default:break;

            }
        }

        reservationTableView.setItems(FXCollections.observableArrayList(reser));
    }





}


