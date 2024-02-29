package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Accueil {

    @FXML
    private Button artisteid;

    @FXML
    private Button clientid;
    @FXML
    private Button retourid;

    @FXML
    public void initialize() {
        artisteid.setOnAction(this::loadInscriptionArtiste);
        clientid.setOnAction(this::loadInscriptionClient);
        retourid.setOnAction(this::loadLoginAdmin);
    }

    private void loadInscriptionArtiste(ActionEvent event) {
        loadFXML("/InscriptionArtiste.fxml", event);
    }

    private void loadInscriptionClient(ActionEvent event) {
        loadFXML("/InscriptionClient.fxml", event);
    }
    private void loadLoginAdmin(ActionEvent event) { // Ajouter cette méthode
        loadFXML("/LoginAdmin.fxml", event);
    }
    private void loadFXML(String fxmlPath, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
