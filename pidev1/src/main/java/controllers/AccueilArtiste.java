package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AccueilArtiste {
    @FXML
    private Button courid ;


    public void initialize() {
        courid.setOnAction(event -> {
                    try {
                        // Mettre à jour le statut de tous les utilisateurs


                        // Charger l'interface loginAdmin.fxml
                        Parent root = FXMLLoader.load(getClass().getResource("/AjouterCourNV.fxml"));
                        Stage stage = (Stage) courid.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );}
}
