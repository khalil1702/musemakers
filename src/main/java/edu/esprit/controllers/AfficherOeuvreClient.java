package edu.esprit.controllers;


import edu.esprit.entities.Oeuvre;
import edu.esprit.entities.Avis;
import edu.esprit.services.ServiceOeuvre;
import edu.esprit.services.ServicePersonne;
import edu.esprit.services.ServiceAvis;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class AfficherOeuvreClient {
    private final ServiceAvis serviceAvis = new ServiceAvis();
    private final ServicePersonne servicePersonne = new ServicePersonne();
    private final ServiceOeuvre oe = new ServiceOeuvre();
    private Set<Oeuvre> listeo = oe.getAll();


    @FXML
    private TextField categorieSearchID;

    @FXML
    private TextField nameSearchID;

    @FXML
    private Button Historique_id;



    @FXML
    private ComboBox<String> comboBox;


    @FXML
    private VBox exhibitionVBox;

    public AfficherOeuvreClient() throws SQLException {
    }

    // Méthode appelée lors de l'initialisation de la vue
    @FXML
    public void initialize() {


        displayExhibitions();
        categorieSearchID.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearch();
        });

        nameSearchID.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearch();
        });



    }

    // Méthode pour afficher toutes les expositions
    private void displayExhibitions() {
        System.out.println("Affichage des œuvres :"); // Ajoutez cette ligne

        exhibitionVBox.getChildren().clear();

        for (Oeuvre o: listeo) {
            // Créer un HBox pour chaque exposition (to arrange components horizontally)
            HBox exhibitionBox = new HBox(10);
            exhibitionBox.setAlignment(Pos.CENTER);

            // ImageView pour l'image de l'exposition
            ImageView imageView = new ImageView();
            try {
                Image image = new Image(new File(o.getImage()).toURI().toString());
                imageView.setImage(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(200);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                // Gérer l'exception, par exemple, définir une image par défaut
            }

            // VBox pour les détails de l'oeuvre
            VBox detailsVBox = new VBox(5);
            detailsVBox.setAlignment(Pos.CENTER_LEFT);


            // Labels pour les détails de l'oeuvre
            Label nomLabel = new Label("Nom: " + o.getNom());
            Label categorieLabel = new Label("Catégorie: " + o.getCategorie());
            Label prixLabel = new Label("Prix: " + o.getPrix());
            Label dateCreationLabel = new Label("Date de création: " + o.getDateCreation());
            Label descriptionLabel = new Label("Description: " + o.getDescription());

            // Bouton pour donner un avis a propos l oeuvre
            Button avisButton = new Button("Details");
            avisButton.setId("buttonavis");
            avisButton.setOnAction(event ->showAvisDialog(o));
            avisButton.getStyleClass().add("submit_button");


            // Ajouter les composants au VBox des détails
            detailsVBox.getChildren().addAll(nomLabel, categorieLabel, prixLabel,dateCreationLabel, descriptionLabel, avisButton);

            // Ajouter les composants à l'HBox principale
            exhibitionBox.getChildren().addAll(imageView, detailsVBox);
            exhibitionBox.getStyleClass().add("exhibition-box");


            // Ajouter HBox à la VBox principale
            exhibitionVBox.getChildren().add(exhibitionBox);
        }
    }

    private void showAvisDialog(Oeuvre o) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/AjouterAvis.fxml"));

            // Load the FXML after setting the controller
            Parent root = loader.load();

            // Set the artwork to the controller
            AjouterAvis controller = loader.getController();
            controller.setOeuvre(o);


            // Load the image and set it to the controller
            Image image = new Image(new File(o.getImage()).toURI().toString());
            controller.setImage(image);

            // Create a new stage (window) ;
           // Stage stage = new Stage();
           //stage.initModality(Modality.APPLICATION_MODAL);
            //stage.setTitle("donner votre avis");
            //stage.setScene(new Scene(root));
            // Close the current stage (AfficherClientOeuvre)
            //currentStage.close();


            exhibitionVBox.getScene().setRoot(root);



           // displayExhibitions();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch()  {
        String categorie = categorieSearchID.getText();
        String name = nameSearchID.getText();

        try {
            Set<Oeuvre> searchResult;
            if (categorie.isEmpty() && name.isEmpty()) {
                searchResult = oe.getAll(); // Utilisez cette méthode pour obtenir toutes les œuvres
            } else {
                searchResult = oe.chercherParCategorieOuNom(categorie, name);
            }
            listeo = searchResult; // Update the listexpo

            // Clear the previous search results from exhibitionVBox
            exhibitionVBox.getChildren().clear();

            // Display the search results in exhibitionVBox
            displayExhibitions();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void Afficherhistoriqueavis(ActionEvent event) throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/client/HistoriqueAvis.fxml"));
        Parent root=loader.load();

        nameSearchID.getScene().setRoot(root);
    }

}


