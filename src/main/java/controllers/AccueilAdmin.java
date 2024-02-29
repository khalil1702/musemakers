package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AccueilAdmin {
    @FXML
    private Button clientid, artisteid;

    public void initialize() {
        clientid.setOnAction(event -> {
            try {
                // Charger l'interface AfficherClientNV.fxml
                Parent root = FXMLLoader.load(getClass().getResource("/AfficherClientNV.fxml"));
                Stage stage = (Stage) clientid.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        artisteid.setOnAction(event -> {
            try {
                // Charger l'interface AfficherArtisteNV.fxml
                Parent root = FXMLLoader.load(getClass().getResource("/AfficherArtisteNV.fxml"));
                Stage stage = (Stage) artisteid.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

