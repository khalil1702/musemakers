package edu.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class AccueilUser {

    @FXML
    private Button button_afficher;

    @FXML
    void AfficherInterfaceOeuvre(ActionEvent event) throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/client/AfficherOeuvreClient.fxml"));
        Parent root=loader.load();

        button_afficher.getScene().setRoot(root);
    }

}
