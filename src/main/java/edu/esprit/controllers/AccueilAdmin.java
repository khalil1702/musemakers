package edu.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class AccueilAdmin {

    @FXML
    private Button Oeuvre_Admin;

    @FXML
    void AfficherInterfaceOeuvreA(ActionEvent event) throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/admin/AfficherOeuvre.fxml"));
        Parent root=loader.load();

        Oeuvre_Admin.getScene().setRoot(root);
    }

}
