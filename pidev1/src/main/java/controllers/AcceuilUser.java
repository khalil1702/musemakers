package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class AcceuilUser {
    @FXML
    private Button courid;


    public void initialize() {
        courid.setOnAction(event -> {
                    try {
                        // Mettre à jour le statut de tous les utilisateurs


                        // Charger l'interface loginAdmin.fxml
                        Parent root = FXMLLoader.load(getClass().getResource("/frontafficher.fxml"));
                        Stage stage = (Stage) courid.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );}
}


