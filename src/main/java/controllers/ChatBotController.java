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
import java.net.URL;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.util.ResourceBundle;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set button action
        btnSend.setOnAction(event -> {
            String input = txtInput.getText();
            System.out.println("input"+input);
            String response = getResponse(input);
            System.out.println(response);
            lblOutput.setText(response);
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
                return "EnergyBox Bot:This content may violate our content policy. If you believe this to be in error, \n "
                        + "please submit your feedback \n your input will aid our research in this area.";
            }
        }

        // Return a specific response for certain keywords or phrases
        if (input.toLowerCase().contains("salut")) {
            return "EnergyBox Bot:Salut comment je peux vous aider ?";
        }
        if (input.toLowerCase().contains("reclamation") || input.toLowerCase().contains("commentaire") || input.toLowerCase().contains("Reclamation") ) {
            return "EnergyBox Bot:Vous trouvez tous les informations qui concerne la reclamation\n"
                    + " dans la section reclamation \n"
                    + "ou bien la partie du commentaire ";
        }
        if (input.toLowerCase().contains("I have some questions")) {
            return "EnergyBox Bot:I will do my best to help you";
        }

        // Return default response if no specific response is matched
        return "EnergyBox Bot:Je suis désolé, je ne comprends pas. \n"
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























