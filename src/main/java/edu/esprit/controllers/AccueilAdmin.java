package edu.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AccueilAdmin {
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

    } @FXML
    void gotocalendrier(ActionEvent event) throws IOException {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/calendrier.fxml"));
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
