package edu.esprit.controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import edu.esprit.entities.Exposition;
import edu.esprit.entities.Reservation;
import edu.esprit.entities.User;
import edu.esprit.pdf.PdfGenerator;
import edu.esprit.services.ServiceReservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class HistoriqueReservation {
    @FXML
    private TableColumn<Exposition, Timestamp> dateDebutColumn;

    @FXML
    private TableColumn<Exposition, Timestamp> dateFinColumn;

    @FXML
    private TableColumn<Reservation, Integer> etatrdv;


    @FXML
    private TableColumn<Exposition, Time> timeDebutColumn;
    @FXML
    private TableColumn<Exposition, Time> timeFinColumn;

    @FXML
    private TableColumn<Exposition, String> nomExpositionColumn;

    @FXML
    private TableView<Reservation> reservationTableView;



//    @FXML
//    private TableColumn<Exposition, String> themeExpositionColumn;

    @FXML
    private TableColumn<Reservation, Integer> ticketsNumberColumn;

    @FXML
    private TableColumn<Reservation, Void> action;  // Change TableColumn type to include Void

    private ServiceReservation serviceReservation = new ServiceReservation();
    private ObservableList<Reservation> userReservations = FXCollections.observableArrayList();
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

    //3amla haja bsh netfakrha ; 0en cours, 1:accepté,2 :refusé par admin, 3 anuuler
    private void handleEditButtonAction(Reservation reservation) {
        if (reservation.getAccessByAdmin() == 0) {
            // Display a popup for editing the number of tickets
            TextInputDialog dialog = new TextInputDialog(String.valueOf(reservation.getTicketsNumber()));
            dialog.setTitle("Edit Number of Tickets");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter the new number of tickets:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newTicketsNumber -> {
                try {
                    int newTickets = Integer.parseInt(newTicketsNumber);
                    // Call the method to modify the number of tickets
                    serviceReservation.modifierNombreTickets(reservation.getClient().getId_user(), reservation.getIdReservation(), newTickets);
                    // Update the modified reservation in the TableView
                    reservation.setTicketsNumber(newTickets);
                    reservationTableView.refresh();
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input for the number of tickets.");
                }
            });
        }
    }
    @FXML
    private void initialize() {
        // Assuming you have a method to retrieve reservations for a specific user
        userReservations.addAll(serviceReservation.getReservationsByUser(LoginAdmin.getLoggedInUser().getId_user()));

        // Populate the TableView with user reservations
        reservationTableView.setItems(userReservations);

        // Set up the columns (you might need to adjust the properties based on your Reservation class)
        dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("expositionDateD"));
        dateFinColumn.setCellValueFactory(new PropertyValueFactory<>("expositionDateF"));

        // Set up the columns (vous devrez peut-être ajuster les propriétés en fonction de votre classe Reservation)
        timeDebutColumn.setCellValueFactory(new PropertyValueFactory<>("heureDebutExpo"));
        timeFinColumn.setCellValueFactory(new PropertyValueFactory<>("heureFinExpo"));

        timeDebutColumn.setCellFactory(column -> new TableCell<Exposition, Time>() {
            @Override
            protected void updateItem(Time time, boolean empty) {
                super.updateItem(time, empty);
                if (empty || time == null) {
                    setText("");
                } else {
                    setText(formatHourMinute(time));
                }
            }
        });

        timeFinColumn.setCellFactory(column -> new TableCell<Exposition, Time>() {
            @Override
            protected void updateItem(Time time, boolean empty) {
                super.updateItem(time, empty);
                if (empty || time == null) {
                    setText("");
                } else {
                    setText(formatHourMinute(time));
                }
            }
        });


        ticketsNumberColumn.setCellValueFactory(new PropertyValueFactory<>("ticketsNumber"));
        nomExpositionColumn.setCellValueFactory(new PropertyValueFactory<>("expositionNom"));
//        themeExpositionColumn.setCellValueFactory(new PropertyValueFactory<>("expositionTheme"));
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


        ticketsNumberColumn.setCellFactory(column -> new TableCell<Reservation, Integer>() {
            private final Button editButton = new Button("✎");


            {
                editButton.getStyleClass().add("edit-button");
                editButton.setOnAction(event -> {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    handleEditButtonAction(reservation);
                });
            }


            @Override
            protected void updateItem(Integer ticketsNumber, boolean empty) {
                super.updateItem(ticketsNumber, empty);

                if (empty || ticketsNumber == null) {
                    setGraphic(null);
                    setText(null); // Clear text if the cell is empty
                } else {
                    Reservation reservation = getTableView().getItems().get(getIndex());

                    // Check if accessByAdmin is 2 or 3, hide the button
                    if (reservation.getAccessByAdmin() == 1 ||reservation.getAccessByAdmin() == 2 || reservation.getAccessByAdmin() == 3) {
                        setGraphic(null);
                        setText(String.valueOf(ticketsNumber)); // Display the ticketsNumber
                    } else {
                        setGraphic(editButton);
                        setText(String.valueOf(ticketsNumber)); // Display the ticketsNumber
                    }
                }
            }
        });


        // Add the button column
        action.setCellFactory(column -> new TableCell<Reservation, Void>() {
            private final Button xButton = new Button("X");

            {
                xButton.getStyleClass().add("x-button");
                xButton.setOnAction(event -> {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    handleXButtonAction(reservation);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Reservation reservation = getTableView().getItems().get(getIndex());

                    // Check if accessByAdmin is 2 or 3
                    if (reservation.getAccessByAdmin() == 2 || reservation.getAccessByAdmin() == 3) {
                        xButton.setVisible(false); // Hide the button
                    } else {
                        xButton.setVisible(true); // Show the button
                    }
                    setGraphic(xButton);
                }
            }
        });
        TableColumn<Reservation, Void> pdfDownloadColumn = new TableColumn<>("PDF réservation");
        pdfDownloadColumn.setCellFactory(column -> new TableCell<Reservation, Void>() {
            private final Button pdfDownloadButton = new Button("⇩");

            {
                pdfDownloadButton.setOnAction(event -> {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    handlePDFDownloadButtonAction(reservation);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Reservation reservation = getTableView().getItems().get(getIndex());

                    // Check if accessByAdmin is 1 (accepted)
                    if (reservation.getAccessByAdmin() == 1) {
                        setGraphic(pdfDownloadButton);
                    } else {
                        setGraphic(null); // Hide the button if not accepted
                    }
                }
            }
        });

        // Add the PDF download column to the TableView
        reservationTableView.getColumns().add(pdfDownloadColumn);
    }
    private void handlePDFDownloadButtonAction(Reservation reservation) {
        if (reservation.getAccessByAdmin() == 1) { // Check if the reservation is accepted
            PdfGenerator.generatePDF(reservation.getExposition(), reservation.getClient(), reservation);
        } else {
            // Display an alert that PDF download is only available for accepted reservations
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("PDF Download Error");
            alert.setContentText("PDF download is only available for accepted reservations.");
            alert.showAndWait();
        }
    }
//    private void pdf(Exposition exposition, User user, Reservation reservation) {
//        try {
//            Document document = new Document();
//            PdfWriter.getInstance(document, new FileOutputStream("output.pdf"));
//            document.open();
//
//            Rectangle rect = new Rectangle(577, 825, 18, 15);
//            rect.enableBorderSide(1);
//            rect.enableBorderSide(2);
//            rect.enableBorderSide(4);
//            rect.enableBorderSide(8);
//            rect.setBorderColor(BaseColor.BLUE);
//            rect.setBorderWidth(5);
//            document.add(rect);
//
//            LineSeparator ls = new LineSeparator();
//            ls.setLineColor(new BaseColor(135, 206, 235));
//            document.add(new Chunk(ls));
//
//            Chunk chunk = new Chunk("Confirmation de la Reservation");
//            Paragraph p = new Paragraph(chunk);
//            p.setAlignment(Element.ALIGN_CENTER);
//            document.add(p);
//
//            String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
//            Chunk dateChunk = new Chunk(date);
//            Paragraph dateParagraph = new Paragraph(dateChunk);
//            dateParagraph.setAlignment(Element.ALIGN_CENTER);
//            document.add(new Chunk(ls));
//            document.add(dateParagraph);
//
//            document.add(new Chunk(ls));
//
//            Paragraph clientParagraph = new Paragraph();
//            clientParagraph.add(new Phrase("Cher(e) " + user.getNom_user() + ",\n\n"));
//            clientParagraph.add(new Phrase("Merci d'avoir réservé une exposition dans notre salle de gallerie. Nous sommes impatients de vous accueillir etc.\n\n"));
//            clientParagraph.add(new Phrase("Ci-dessous, vous trouverez les détails de votre réservation :\n\n"));
//            document.add(clientParagraph);
//
//            document.add(new Paragraph("\n\n\n\n\n"));
//
//            PdfPTable table = new PdfPTable(1);
//
//            PdfPCell cell;
//            p = new Paragraph("Nom de la séance : " + exposition.getNom());
//            cell = new PdfPCell(p);
//            cell.setBorder(Rectangle.BOX);
//            cell.setPadding(10);
//            cell.setBackgroundColor(BaseColor.WHITE);
//            table.addCell(cell);
//
//            p = new Paragraph("Nom et prénom du client : " + user.getPrenom_user());
//            cell = new PdfPCell(p);
//            cell.setBorder(Rectangle.BOX);
//            cell.setPadding(10);
//            cell.setBackgroundColor(new BaseColor(224, 224, 224));
//            table.addCell(cell);
//
//            p = new Paragraph("nombre de ticket " + reservation.getTicketsNumber());
//            cell = new PdfPCell(p);
//            cell.setBorder(Rectangle.BOX);
//            cell.setPadding(10);
//            cell.setBackgroundColor(BaseColor.WHITE);
//            table.addCell(cell);
//
//            document.add(table);
//
//            document.add(new Paragraph("\n\n\n\n\n\n\n\n"));
//
//            Paragraph footer = new Paragraph("MuseMakers Gallery, 1, 2 rue André Ampère - 2083 - Pôle Technologique - El Ghazala.,nTel : 71500500,Email : musemakers@gmail.com");
//            footer.setAlignment(Element.ALIGN_CENTER);
//            document.add(footer);
//
//            document.close();
//            File pdfFile = new File("output.pdf");
//            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
//                Desktop.getDesktop().open(pdfFile);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }




    private void handleXButtonAction(Reservation reservation) {
        // Implement the logic to handle the X button action
        // For example, update the reservation status in the database
        serviceReservation.annulerReservation(reservation.getIdReservation());

        // Update the data in the ObservableList directly
        reservation.setAccessByAdmin(3); // Assuming 3 is the status for cancellation
        reservationTableView.refresh();
    }
    //
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
    private String formatHourMinute(Time time) {
        // Extract hours and minutes from the Time object
        int hours = time.toLocalTime().getHour();
        int minutes = time.toLocalTime().getMinute();

        // Format as "HH:mm"
        return String.format("%02d:%02d", hours, minutes);
    }



}
