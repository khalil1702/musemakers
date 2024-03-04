package edu.esprit.controllers;

import edu.esprit.entities.Avis;
import edu.esprit.entities.Oeuvre;
import edu.esprit.services.ServiceAvis;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class AfficherOeuvreFavoris {

    @FXML
    private Button Historique_id;

    @FXML
    private Button button_afficher;

    @FXML
    private VBox exhibitionVBox;
    ServiceAvis serviceAvis = new ServiceAvis();

    @FXML
    public void initialize() {
        displayFavoriteOeuvres();
    }

    // Méthode pour afficher les œuvres favorites de l'utilisateur
    private void displayFavoriteOeuvres() {
        ServiceAvis serviceAvis = new ServiceAvis();
        int userId = getUserId();

        // Récupérer les œuvres favorites de l'utilisateur
        Set<Oeuvre> favoritoeuvre = serviceAvis.getReviewedOeuvreByUser(userId);

        for (Oeuvre o : favoritoeuvre) {
            try {
                // Créer un HBox pour chaque œuvre
                HBox oeuvreBox = new HBox(10);

                // Créer un ImageView pour l'image de l'œuvre
                ImageView imageView = new ImageView();
                Image image = new Image(new File(o.getImage()).toURI().toString());
                imageView.setImage(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(200);

                // Créer un Label pour le nom de l'œuvre
                Label nameLabel = new Label("Nom: " + o.getNom());

                // Créer un Label pour la description de l'œuvre
                Label descriptionLabel = new Label("Description: " + o.getDescription());

                // Créez un bouton pour retirer l'œuvre des favoris
                Button removeButton = new Button("Retirer des favoris");
                removeButton.setStyle("-fx-background-color: red");
                removeButton.setOnAction(event -> {
                    // Récupérer l'identifiant de l'œuvre associée à cette action
                    int idOeuvre = o.getId();


                    // Mettre à jour l'avis associé à cette œuvre pour marquer l'œuvre comme non favorite
                    serviceAvis.modifierAvis(idOeuvre,userId);

                    exhibitionVBox.getChildren().clear();
                    // Actualiser l'affichage des œuvres favorites
                    displayFavoriteOeuvres();
                });

                // Ajouter les éléments à la HBox
                oeuvreBox.getChildren().addAll(imageView, nameLabel, descriptionLabel, removeButton);

                // Ajouter la HBox à la VBox
                exhibitionVBox.getChildren().add(oeuvreBox);
            } catch (Exception e) {
                System.out.println("Error loading image: " + e.getMessage());
                // Gérer l'exception
            }
        }
    }

    // Méthode pour récupérer l'ID de l'utilisateur (à implémenter)
    private int getUserId() {
        // Vous devez implémenter la logique pour récupérer l'ID de l'utilisateur
        return 4;
    }

    @FXML
    void Afficheroeuvre(ActionEvent event) throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/client/AfficherOeuvreClient.fxml"));
        Parent root=loader.load();

        exhibitionVBox.getScene().setRoot(root);
    }

}