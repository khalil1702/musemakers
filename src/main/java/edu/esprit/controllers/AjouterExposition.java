package edu.esprit.controllers;

import edu.esprit.entities.Exposition;
import edu.esprit.services.ServiceExposition;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;

public class AjouterExposition {
    private final ServiceExposition exp = new ServiceExposition();

    @FXML
    public Button buttonaddExpoID;
    @FXML
    public DatePicker datefinId;
    @FXML
    public DatePicker dateDebutId;
    @FXML
    public TextField ImageId;
    @FXML
    public TextField themeID;
    @FXML
    public TextArea descriptionId;
    @FXML
    public TextField nomExpoId;
    public void addExpo(ActionEvent event)throws IOException {
//
        LocalDate startDate = dateDebutId.getValue();
        LocalDate endDate = datefinId.getValue();

        Timestamp sqlStartDate = Timestamp.valueOf(startDate.atStartOfDay());
        Timestamp sqlEndDate = Timestamp.valueOf(endDate.atStartOfDay());

        exp.ajouter(new Exposition(
                nomExpoId.getText(),
                sqlStartDate,
                sqlEndDate,
                descriptionId.getText(),
                themeID.getText(),
                ImageId.getText()));
//
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Validation");
//            alert.setContentText("Expo added successfully");
//            alert.showAndWait();
//
//
    }
    @FXML
    void Afficher(ActionEvent event) throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/AfficherExposition.fxml"));
        Parent root=loader.load();
//        Scene scene = new Scene(root);
//
//        // Create a new stage (window)
//        Stage stage = new Stage();
//        stage.setTitle("Exhibition List"); // Set a title for the new window
//        stage.setScene(scene);

        // Show the new stage
//        stage.show();
        nomExpoId.getScene().setRoot(root);
     }


}

