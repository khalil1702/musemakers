package edu.esprit.controllers;

import edu.esprit.entities.Avis;
import edu.esprit.entities.Oeuvre;
import edu.esprit.entities.User;
import edu.esprit.services.ServiceAvis;
import edu.esprit.services.ServicePersonne;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.util.prefs.Preferences;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import org.controlsfx.control.Rating;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class AjouterAvis {

    @FXML
    private TextArea comment_id;

    @FXML
    private DatePicker dateex_id;

    //@FXML
    //private ChoiceBox<Integer> note_id;

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
    ServicePersonne servicePersonne = new ServicePersonne();
    ServiceAvis serviceAvis = new ServiceAvis();
    Avis a=new Avis();

    private int ClientId = servicePersonne.getOneById(4).getId_user();;


    @FXML
    public void initialize() {
        System.out.println("cc");
        // Ajoutez des éléments à la ChoiceBox dans la méthode initialize
       // note_id.getItems().addAll(1,2,3,4,5);


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

            VBox avisBox = new VBox(userLabel, commentLabel, noteLabel,rating);
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
            //String erreurnote = (note == null) ? "Veuillez sélectionner une note." : "";

            // Get the comment entered by the user
            String commentaire = comment_id.getText();
            String  erreurCommentaire = (commentaire.isEmpty() || commentaire.length() > 30 || !commentaire.matches("[a-zA-Z0-9,\\- ]+")) ? "Le commentaire est vide, ou il a  dépasse 30 caractères et doit contenir uniquement des lettres, des chiffres, des virgules et des tirets." : "";


            // Get the user with ID 4  (You can modify this part based on your requirements)
            User client = servicePersonne.getOneById(4);

            // Récupérer la date sélectionnée dans le DatePicker
            LocalDate localdate = dateex_id.getValue();
            String erreurDate = (localdate == null) ? "Veuillez sélectionner une date." : "";
            java.sql.Date date = null;
            if (localdate != null) {
              date = java.sql.Date.valueOf(localdate); // Conversion de LocalDate en java.sql.Date
            }

            commentaireerreur.setText(erreurCommentaire);
            commentaireerreur.setFill(Color.RED);

            dateerreur.setText(erreurDate);
            dateerreur.setFill(Color.RED);

           // noteerreur.setText(erreurnote);
            noteerreur.setFill(Color.RED);

            if (erreurCommentaire.isEmpty() && erreurDate.isEmpty()  /*erreurnote.isEmpty()*/) {
                int likes = 0;
                int dislikes = 0;

                // Si l'utilisateur clique sur Like, likes sera 1 et dislikes sera 0
                // Si l'utilisateur clique sur Dislike, likes sera 0 et dislikes sera 1

                // Vous n'avez pas besoin de likeClique et dislikeClique ici car vous gérez cela directement dans les méthodes associées aux boutons.

                if (like_id.getStyle().contains("-fx-background-color: green;")) {
                    likes = 1;
                } else if (deslike_id.getStyle().contains("-fx-background-color: red;")) {
                    dislikes = 1;
                }

                // Create an avis
                Avis avis = new Avis(commentaire, date, note, oeuvre, client, likes, dislikes);

                // Add the avis to the database
                serviceAvis.ajouter(avis);


                comment_id.clear();
                dateex_id.setValue(null);
                //note_id.setValue(null);

                // Show a confirmation message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Avis soumis");
                alert.setHeaderText(null);
                alert.setContentText("Votre avis a été enregistré. Merci pour votre feedback!");
                alert.showAndWait();
                setOeuvre(oeuvre);
            }
            // Close the dialog

            //Stage stage = (Stage) comment_id.getScene().getWindow();
          //  stage.close();
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
//        Scene scene = new Scene(root);
//
//        // Create a new stage (window)
//        Stage stage = new Stage();
//        stage.setTitle("Exhibition List"); // Set a title for the new window
//        stage.setScene(scene);

        // Show the new stage
//        stage.show();
        comment_id.getScene().setRoot(root);
    }
    @FXML
    void Affichergallerie(javafx.event.ActionEvent event) throws IOException {
       FXMLLoader loader= new FXMLLoader(getClass().getResource("/client/AfficherOeuvreClient.fxml"));
       Parent root=loader.load();

        comment_id.getScene().setRoot(root);
        /*try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherOeuvreClient.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the stage from the event source (button) and set the new scene
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);

            // Show the stage
            stage.show();
        //} catch (IOException e) {
            //e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }*/
    }
}
