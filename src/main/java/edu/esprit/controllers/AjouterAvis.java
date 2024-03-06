package edu.esprit.controllers;

import edu.esprit.entities.Avis;
import edu.esprit.entities.Oeuvre;
import edu.esprit.entities.User;
import edu.esprit.services.ServiceAvis;
import edu.esprit.services.ServiceUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.prefs.Preferences;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import org.controlsfx.control.Rating;
import org.controlsfx.control.ToggleSwitch;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


public class AjouterAvis  {

    @FXML
    private TextArea comment_id;

    @FXML
    private Label details_id;

    @FXML
    private Button button_envoyer;

    @FXML
    private ImageView image_id;
    @FXML
    private Button like_id;
    @FXML
    private Button deslike_id;

    @FXML
    private Rating note_id;

    @FXML
    private ToggleSwitch favorisToggleSwitch;

    @FXML
    private Button historique_id;

    @FXML
    private Button gallerie_id;

    @FXML
    private Text commentaireerreur;

    @FXML
    private Text dateerreur;

    @FXML
    private Text noteerreur;

    private Oeuvre oeuvre;

    @FXML
    private VBox vbox1;

    private Preferences preferences;
    private int currentOeuvreId;



    private boolean likeClique = false;
    private boolean dislikeClique = false;
    ServiceUser servicePersonne = new ServiceUser();
    ServiceAvis serviceAvis = new ServiceAvis();
    Avis a=new Avis();

    private int ClientId = servicePersonne.getOneById(4).getId_user();;


    @FXML
    public void initialize() {

    }


    public void setOeuvre(Oeuvre oeuvre) {
        vbox1.getChildren().clear();
        this.oeuvre = oeuvre;
        currentOeuvreId = oeuvre.getId();

        // Supposons que getId() retourne l'ID de l'œuvre

        // Set the image in the ImageView
        //details_id.setText("Exposition: " + oeuvre.getNom() );
        // Récupérez les avis de la base de données
        List<Avis> avisList = serviceAvis.getAvisByOeuvre(oeuvre);

        // Ajoutez les avis au VBox
        for (Avis avis : avisList) {
            Label userLabel = new Label("Utilisateur: " + avis.getClient().getNom_user() + " " + avis.getClient().getPrenom_user());
            Label commentLabel = new Label("Commentaire: " + avis.getCommentaire());
            Label noteLabel = new Label("Note: " );
            // Créez un objet Rating pour afficher la note
            Rating rating = new Rating();
            rating.setRating(avis.getNote()); // Définissez le nombre d'étoiles en fonction de la note de l'avis
            rating.setDisable(true); // Empêchez l'utilisateur de modifier la note
            rating.setStyle(" -fx-fill-color: #FFD700;"); // Couleur des étoiles : jaune
            rating.setOpacity(2.0);

            VBox avisBox = new VBox(userLabel, commentLabel, noteLabel,rating);
            avisBox.setStyle("-fx-background-color: white;"); // Définissez la couleur de fond de la VBox
            avisBox.setPadding(new Insets(10, 0, 10, 0));  // Ajoutez du padding autour de chaque avis
            vbox1.getChildren().add(avisBox);
        }
        preferences = Preferences.userNodeForPackage(getClass()).node("user_" + ClientId);

        likeClique = preferences.getBoolean("likeClique_" + currentOeuvreId , false);
        dislikeClique = preferences.getBoolean("dislikeClique_" + currentOeuvreId , false);
        if (currentOeuvreId != -1  ) {
            if (likeClique) {
                like_id.setStyle("-fx-background-color: green;");
            }

            if (dislikeClique) {
                deslike_id.setStyle("-fx-background-color: red;");
            }
        }
    }

    @FXML
    private void likeAction(ActionEvent event) {
        preferences.putBoolean("likeClique_" + currentOeuvreId , !likeClique);
        preferences.putBoolean("dislikeClique_" + currentOeuvreId, false);

        if (!likeClique) {
            likeClique = true;
            dislikeClique = false;
            // Autres actions à effectuer lors du clic sur le bouton Like
            like_id.setStyle("-fx-background-color: green;"); // Exemple de changement de couleur
            deslike_id.setStyle("-fx-background-color: transparent;"); // Réinitialiser le style du bouton Dislike
        } else {
            likeClique = false;
            // Réinitialiser l'état du bouton Like
            like_id.setStyle("-fx-background-color: transparent;");
        }

    }

    // Méthode pour gérer l'action du bouton Dislike
    @FXML
    private void dislikeAction(ActionEvent event) {
        preferences.putBoolean("dislikeClique_" + currentOeuvreId , !dislikeClique);
        preferences.putBoolean("likeClique_" + currentOeuvreId , false);
        if (!dislikeClique) {
            dislikeClique = true;
            likeClique = false;
            // Autres actions à effectuer lors du clic sur le bouton Dislike
            deslike_id.setStyle("-fx-background-color: red;"); // Exemple de changement de couleur
            like_id.setStyle("-fx-background-color: transparent;"); // Réinitialiser le style du bouton Like
        } else {
            dislikeClique = false;
            // Réinitialiser l'état du bouton Dislike
            deslike_id.setStyle("-fx-background-color: transparent;");
        }
    }

    public void setImage(Image image) {
        image_id.setImage(image);
    }



    @FXML
    private void submitAvis(ActionEvent event) {
        try {
            // Get the note entered by the user
            // Integer note = note_id.getValue();
            int note = (int) note_id.getRating();

            // Get the comment entered by the user
            String commentaire = comment_id.getText();
            String  erreurCommentaire = (commentaire.isEmpty() || commentaire.length() > 30 || !commentaire.matches("[a-zA-Z0-9,\\- ]+")) ? "Le commentaire est vide, ou il a  dépasse 30 caractères et doit contenir uniquement des lettres, des chiffres, des virgules et des tirets." : "";


            // Get the user with ID 4  (You can modify this part based on your requirements)
            User client = servicePersonne.getOneById(4);

            commentaireerreur.setText(erreurCommentaire);
            commentaireerreur.setFill(Color.RED);

            if (erreurCommentaire.isEmpty()) {
                int likes = 0;
                int dislikes = 0;

                if (like_id.getStyle().contains("-fx-background-color: green;")) {
                    likes=1;
                } else if  (deslike_id.getStyle().contains("-fx-background-color: red;")) {
                    dislikes=1;
                }
                boolean isFavoris = favorisToggleSwitch.isSelected();
                // Si l'utilisateur clique sur Like, likes sera 1 et dislikes sera 0
                // Si l'utilisateur clique sur Dislike, likes sera 0 et dislikes sera 1

                // Create an avis
                Avis avis = new Avis(commentaire, note, oeuvre, client, likes, dislikes, isFavoris);

                // Add the avis to the database
                serviceAvis.ajouter(avis);


                comment_id.clear();

                //note_id.setValue(null);

                // Show a confirmation message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Avis soumis");
                alert.setHeaderText(null);
                alert.setContentText("Votre avis a été enregistré. Merci pour votre feedback!");
                alert.showAndWait();
                setOeuvre(oeuvre);
                // Close the dialog

            }
        } catch (NumberFormatException e) {
            // Handle the case where the user enters a non-numeric value for note
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez entrer un avis valide.");
            alert.showAndWait();
        }
    }

    @FXML
    void Afficherhistoriqueavis(ActionEvent event) throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/client/HistoriqueAvis.fxml"));
        Parent root=loader.load();

        comment_id.getScene().setRoot(root);
    }
    @FXML
    void Affichergallerie(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/client/AfficherOeuvreClient.fxml"));
        Parent root=loader.load();

        comment_id.getScene().setRoot(root);

    }
}