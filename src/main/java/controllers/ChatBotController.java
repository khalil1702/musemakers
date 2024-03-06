package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.stage.Stage;


public class ChatBotController implements Initializable {

    @FXML
    private Label lblTitle;
    @FXML
    private TextField txtInput;
    @FXML
    private Button btnSend;
    @FXML
    private Label lblOutput;
    private StringBuilder chatHistory = new StringBuilder();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set button action
        btnSend.setOnAction(event -> {
            String input = txtInput.getText();
            chatHistory.append("Client: ").append(input).append("\n"); // Add client message to chat history
            String response = getResponse(input);
            chatHistory.append("ArtWaves Bot: ").append(response).append("\n"); // Add bot response to chat history
            lblOutput.setText(chatHistory.toString()); // Display chat history
            txtInput.clear();
        });
    }

    private static final String[] INAPPROPRIATE_WORDS = {
            "badword1",
            "badword2",
            "badword3"
    };

    private static final String DEFAULT_RESPONSE = "Je suis désolé, je ne comprends pas !. Pouvez-vous reformuler votre question s'il vous plaît ?";

    private String getResponse(String input) {
        // Check for inappropriate words
        for (String word : INAPPROPRIATE_WORDS) {
            if (input.toLowerCase().contains(word)) {
                return "ArtWaves Bot:This content may violate our content policy. If you believe this to be in error, \n "
                        + "please submit your feedback \n your input will aid our research in this area.";
            }
        }

        // Return a specific response for certain keywords or phrases
        if (input.toLowerCase().contains("salut")) {
            return "ArtWaves Bot:Salut comment je peux vous aider ?";
        }
        if (input.toLowerCase().contains("reclamation") || input.toLowerCase().contains("réclamation") || input.toLowerCase().contains("Reclamation") ) {
            return "ArtWaves Bot:Vous trouvez tous les informations qui concerne la reclamation\n"
                    + " dans la section reclamation \n";

        }
        if (input.toLowerCase().contains("information commentaire") || input.toLowerCase().contains("information comment") || input.toLowerCase().contains(" informations Commentaire")|| input.toLowerCase().contains(" informations Commentaires")|| input.toLowerCase().contains(" informations commentaire") ) {
            return "ArtWaves Bot:Vous trouvez tous les informations qui concerne le commentaire\n"
                    + " dans la section commentaire \n"; }
        if (input.toLowerCase().contains("reservation") || input.toLowerCase().contains("réservation") || input.toLowerCase().contains("Reservation") ) {
            return "ArtWaves Bot:Vous trouvez tous les informations qui concerne la reservation\n"
                    + " dans la section reservation \n"; }
        if (input.toLowerCase().contains("cours artistiques") || input.toLowerCase().contains("cours artistique") || input.toLowerCase().contains("Cours Artistiques")|| input.toLowerCase().contains("Cours artistique")|| input.toLowerCase().contains("Cours artistiques") ) {
            return "ArtWaves Bot:Vous trouvez tous les informations qui concerne le commentaire\n"
                    + " dans la section commentaire \n"; }
        if (input.toLowerCase().contains("exposition") || input.toLowerCase().contains("expositions") || input.toLowerCase().contains("Expostions") ) {
            return "ArtWaves Bot:Vous trouvez tous les informations qui concerne les expositions \n"
                    + " dans la section expositions \n"; }
        if (input.toLowerCase().contains("les chefs d'oeuvres") || input.toLowerCase().contains("Les chefs d'oeuvres") || input.toLowerCase().contains("LES CHEFS D'OEUVRES") ) {
            return "ArtWaves Bot:Vous trouvez tous les informations qui concerne Les chefs d'oeuvres \n"
                    + " dans la section des chefs d'oeuvres \n"; }
        if (input.toLowerCase().contains("statut") || input.toLowerCase().contains("Statut") || input.toLowerCase().contains("STATUT") ) {
            return "ArtWaves Bot:Vous trouvez les statuts de vos réclamations   \n"
                    +"dans la section Vos Réclamations \n"
                    + " Si le statut est en cours, attendez le sms auprès de l'admin \n"; }
        if (input.toLowerCase().contains("supprimer") || input.toLowerCase().contains("effacer") || input.toLowerCase().contains("delete")|| input.toLowerCase().contains("Supprimer")|| input.toLowerCase().contains("SUPPRIMER")  ) {
            return "ArtWaves Bot:Vous pouvez contactez l'admin  \n"
                    +"pour la suppression de vos commentaires \n"
                    + " par ce mail : khalil.chekili@esprit.tn \n"+
                    " ou par ce numero : 27163524 \n"; }
        if (input.toLowerCase().contains("modifier") || input.toLowerCase().contains("Modifier") || input.toLowerCase().contains("update")|| input.toLowerCase().contains("MODIFIER")|| input.toLowerCase().contains("UPDATE") || input.toLowerCase().contains("Update") ) {
            return "ArtWaves Bot:Vous pouvez contactez l'admin  \n"
                    +"pour la modification de vos commentaires \n"
                    + " par ce mail : khalil.chekili@esprit.tn \n"+
                    " ou par ce numero : 27163524 \n"; }
        if (input.toLowerCase().contains("I have some questions")) {
            return "ArtWaves Bot:I will do my best to help you";
        }

        // Return default response if no specific response is matched
        return "ArtWaves Bot:Je suis désolé, je ne comprends pas. \n"
                + "Pouvez-vous reformuler votre question s'il vous plaît ?";
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReclamationUser.fxml"));
        Parent root = loader.load();

        // Créer une nouvelle scène
        Scene scene = new Scene(root);

        // Configurer la nouvelle scène dans une nouvelle fenêtre
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Reclamations");

        // Afficher la nouvelle fenêtre
        stage.show();
    }
   /* @FXML
    void com(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterComUser.fxml"));
        Parent root = loader.load();

        // Créer une nouvelle scène
        Scene scene = new Scene(root);

        // Configurer la nouvelle scène dans une nouvelle fenêtre
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Commentaires");

        // Afficher la nouvelle fenêtre
        stage.show();
    }
*/
}























