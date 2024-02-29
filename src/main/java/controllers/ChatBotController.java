package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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



}























/*
  private String getResponse(String input) {
    if (input.contains("hello") || input.contains("bonjour")) {
        return "Bonjour! Comment puis-je vous aider aujourd'hui?";
    } else if (input.contains("Je veux savoir l'heure quand mon plat sera pret")) {
        return "EnergyBox Bot :Vous trouvez tous les informations qui concerne la réservation dans le QrcCode \n qui apparait lors de réserver ou bien le SMS qui sera envoyé à vous";
    } else if (input.contains("Merci")) {
        return "Je suis désolé pour cela. Je vais demander au coach de vous contacter directement.";
    } else if (input.contains("je veux changer la date du cours avec ce coach")) {
        return "D'accord, nous allons vous contacter pour fixer une nouvelle date.";
    } else if (input.contains("merci")) {
        return "Merci pour votre fidélité avec Sportifly !";
    } else {
        return "Je suis désolé, je ne comprends pas. Pouvez-vous reformuler votre question s'il vous plaît ?";
    }
}
}

*/