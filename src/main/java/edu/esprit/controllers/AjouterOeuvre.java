package edu.esprit.controllers;

import edu.esprit.entities.Oeuvre;
import edu.esprit.services.ServiceOeuvre;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.Parent;

import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.io.File;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import javafx.scene.paint.Color;
import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
public class AjouterOeuvre {

    private final ServiceOeuvre PS = new ServiceOeuvre();

    private File selectedFile;

    @FXML
    private TextField Nom_id;

    @FXML
    private ChoiceBox<String> categorie_id;

    @FXML
    private DatePicker date_id;

    @FXML
    private TextArea description_id;

    @FXML
    private TextField image_id;

    @FXML
    private TextField prix_id;

    @FXML
    private Button button_ajouter;

    @FXML
    private Button button_afficher;

    @FXML
    private Button browseButton;

    @FXML
    private Text erreurCategorieLabel;
    @FXML
    private Text erreurDateLabel;

    @FXML
    private Text erreurprix;

    @FXML
    private Text erreurNomLabel;
    @FXML
    private Text erreurDescriptionLabel;
    @FXML
    private Text erreurImageLabel;

    @FXML
    public void initialize() {
        // Ajoutez des éléments à la ChoiceBox dans la méthode initialize
        categorie_id.getItems().addAll("Peinture", "Sculpture", "scene");

    }

    @FXML
    public void add(ActionEvent event) {
        // Validation du champ nom
        String nom = Nom_id.getText();
        String erreurNom = nom.isEmpty() || !nom.matches("[a-zA-Z,\\- ]+") ? "Le nom doit contenir uniquement des lettres et ne peut pas être vide." : "";

        // Validation du champ catégorie
        String categorie = categorie_id.getValue();
        String erreurCategorie = (categorie == null || categorie.isEmpty()) ? "Veuillez sélectionner une catégorie." : "";

        // Validation du champ date
        LocalDate localDate = date_id.getValue();
        String erreurDate = (localDate == null) ? "Veuillez sélectionner une date." : "";

        // Validation du champ prix
        String prixText = prix_id.getText();
        String erreurPrix = "";
        if (prixText.isEmpty()) {
            erreurPrix = "Veuillez saisir un prix.";
        } else {
            try {
                float prix = Float.parseFloat(prixText);
            } catch (NumberFormatException e) {
                erreurPrix = "Veuillez saisir un prix valide.";
            }
        }

        // Validation du champ description
        String description = description_id.getText();
        String erreurDescription = (description.isEmpty() || description.length() > 30 || !description.matches("[a-zA-Z0-9,\\- ]+")) ? "Le description est vide, ou elle a  dépasse 30 caractères et doit contenir uniquement des lettres, des chiffres, des virgules et des tirets." : "";

        // Validation du champ image
        String image = image_id.getText();
        String erreurImage = image.isEmpty() ? "Veuillez fournir une URL d'image." : "";

        // Mise à jour des champs de texte d'erreur
        erreurNomLabel.setText(erreurNom);
        erreurNomLabel.setFill(Color.RED);

        erreurCategorieLabel.setText(erreurCategorie);
        erreurCategorieLabel.setFill(Color.RED);

        erreurDateLabel.setText(erreurDate);
        erreurDateLabel.setFill(Color.RED);

        erreurprix.setText(erreurPrix);
        erreurprix.setFill(Color.RED);

        erreurDescriptionLabel.setText(erreurDescription);
        erreurDescriptionLabel.setFill(Color.RED);

        erreurImageLabel.setText(erreurImage);
        erreurImageLabel.setFill(Color.RED);

        // Si aucune erreur n'est détectée, ajoutez l'oeuvre
        if (erreurNom.isEmpty() && erreurCategorie.isEmpty() && erreurDate.isEmpty() && erreurPrix.isEmpty() && erreurDescription.isEmpty() && erreurImage.isEmpty()) {
            PS.ajouter(new Oeuvre(nom, categorie, Float.parseFloat(prixText), Date.valueOf(localDate), description, image));
        }
    }

    @FXML
    void Afficher(ActionEvent event) throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/admin/AfficherOeuvre.fxml"));
        Parent root=loader.load();
//        Scene scene = new Scene(root);
//
//        // Create a new stage (window)
//        Stage stage = new Stage();
//        stage.setTitle("Exhibition List"); // Set a title for the new window
//        stage.setScene(scene);

        // Show the new stage
//        stage.show();
        Nom_id.getScene().setRoot(root);
    }
    @FXML
    void browseImage(ActionEvent event) {
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            // Enregistrez le chemin du fichier dans le champ de texte pathimageid
            image_id.setText(selectedFile.getPath());

            // Chargez l'image depuis le fichier sélectionné
            try {
                // Utilisez la classe Paths pour obtenir un chemin de fichier correct
                String imagePath = Paths.get(selectedFile.getPath()).toUri().toString();
                Image image = new Image(imagePath);

                // Affichez l'image dans l'ImageView si nécessaire
                // imageView.setImage(image);
            } catch (Exception e) {
                System.out.println("Error loading image: " + e.getMessage());

            }
        }
    }
}