package controllers;
import entities.Reclamation;
import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import service.ReclamationService;
import service.ServiceUser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AfficherReclamations {
    private final ReclamationService rs = new ReclamationService();
    private final ServiceUser su = new ServiceUser();

    @FXML
    private TextField CategorieRecTF;

    @FXML
    private ListView<Reclamation> ListViewRec;

    @FXML
    private Button ajouter;

    @FXML
    private Label cate;

    @FXML
    private Label desc;

    @FXML
    private TextField descriRecTF;

    @FXML
    private TextField searchTF;

    List<Reclamation> RecList;

    private Reclamation selectedReservation;

    public void initialize() throws IOException {
        ShowReclamation();
    }

    public void ShowReclamation() throws IOException {
        try {
            RecList = rs.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Créer une FilteredList
        FilteredList<Reclamation> filteredData = new FilteredList<>(FXCollections.observableArrayList(RecList), p -> true);

        // Ajouter un listener à searchTF pour qu'il réagisse aux changements de texte
        searchTF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(reclamation -> {
                // Si le texte de recherche est vide, afficher toutes les réclamations
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Comparer le texte de recherche avec la catégorie et la description de chaque réclamation
                String lowerCaseFilter = newValue.toLowerCase();
                if (reclamation.getCategorieRec().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Le filtre correspond à la catégorie
                } else if (reclamation.getDescriRec().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Le filtre correspond à la description
                }
                return false; // Aucune correspondance
            });
        });

        // Envelopper la FilteredList dans une SortedList
        SortedList<Reclamation> sortedData = new SortedList<>(filteredData);

        // Ajouter les données triées (et filtrées) à la ListView
        ListViewRec.setItems(sortedData);


        // Ajoutez ceci à votre méthode initialize() après l'initialisation de la cellFactory
        ListViewRec.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Reclamation item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null); // Assurez-vous de vider également le graphique si l'élément est vide
                } else {
                    // Créer un bouton pour chaque ligne
                    Button button = new Button("Commenter"); // Vous pouvez changer le texte selon vos besoins
                    button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: black; -fx-underline: true;");
                    button.setOnAction(event -> {
                        // Récupérer la réclamation associée à cette ligne
                        Reclamation reclamation = getItem();
                        // Logique pour ouvrir une autre interface
                        try {

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterComUser.fxml"));
                            Parent root = loader.load();
                            // Obtenir le contrôleur de la nouvelle interface
                            AjouterComUser ajouterComUser = loader.getController();
                            System.out.println("Passage de reclamation au contrôleur AjouterComUserController : " + reclamation.getDescriRec());
                            ajouterComUser.setReclamation(reclamation); // Passer la réclamation au contrôleur de la nouvelle interface

                            // Créer une nouvelle scène
                            Scene scene = new Scene(root);

                            // Configurer la nouvelle scène dans une nouvelle fenêtre
                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            stage.setScene(scene);
                            stage.setTitle("Commenter pour : " + reclamation.getDescriRec());


                            // Afficher la nouvelle fenêtre
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            // Gérer l'exception
                        }
                    });

                    // Ajouter le texte à gauche et le bouton à droite dans un conteneur HBox
                    // Créer un conteneur HBox pour disposer le bouton à droite et le texte à gauche
                    HBox hbox = new HBox();
                    hbox.getChildren().addAll(new Label("Nom du client : " + item.getUserNom1() + "\nCatégorie : " + item.getCategorieRec() + "\nDescription : " + item.getDescriRec() + "\nDate : " + item.getDateRec()), button);
                    hbox.setAlignment(Pos.CENTER_LEFT); // Aligner le texte à gauche
                    hbox.setSpacing(170); // Espace entre le texte et le bouton

                    setGraphic(hbox); // Ajouter le conteneur HBox à la cellule
                }
            }
        });

    }

    @FXML
    void exit(ActionEvent event) throws IOException {
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


}