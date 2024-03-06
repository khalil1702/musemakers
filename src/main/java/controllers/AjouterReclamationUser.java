package controllers;

import entities.Reclamation;
import entities.User;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import service.ReclamationService;
import service.ServiceUser;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class AjouterReclamationUser {
    private final ReclamationService rs= new ReclamationService( );
    private final ServiceUser su= new ServiceUser();

    @FXML
    private TextField CategorieRecTF;

    @FXML
    private TableColumn<?, ?> CvCat;

    @FXML
    private TableColumn<?, ?> CvDate;

    @FXML
    private TableColumn<?, ?> CvDescri;

    @FXML
    private TableColumn<?, ?> CvStatut;

    @FXML
    private TableView<Reclamation> TableViewRec;

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

    @FXML
    private Button modifier;

    @FXML
    private Button supprimer;
    @FXML
    private Button rec;

    List<Reclamation> RecList;

    @FXML
    private ListView<Reclamation> ListViewRec;

    @FXML
    private ComboBox<String> CatRecCB;

    public void initialize() throws IOException {
        ObservableList<String> options = FXCollections.observableArrayList(
                "Produits", "Service Client", "Problème Technique"
        );
        CatRecCB.setItems(options);
        ListViewRec.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // Vérifie si c'est un simple clic
                Reclamation selectedReclamation = ListViewRec.getSelectionModel().getSelectedItem();
                if (selectedReclamation != null) {
                    // Afficher les informations de la séance sélectionnée dans le formulaire
                    displayActiviteInfo(selectedReclamation);
                }
            }
        });

        ShowReclamation();

    }

    private void displayActiviteInfo(Reclamation r) {
        CatRecCB.setValue(r.getCategorieRec());
        descriRecTF.setText(r.getDescriRec());

    }

    @FXML
    void ajouter(ActionEvent event) throws IOException {
        Reclamation r=new Reclamation();
        User userAdd= su.getOneById(3);
        String descriRec = descriRecTF.getText();

        // Ajouter le contrôle de saisie ici
        if (descriRec.length() > 50) {
            new Alert(Alert.AlertType.WARNING, "Vous avez dépassé 50 caractères.", ButtonType.OK).showAndWait();
            return;
        } else if (descriRec.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "La description est vide.", ButtonType.OK).showAndWait();
            return;
        }

        r.setCategorieRec(CatRecCB.getValue());
        //r.setCategorieRec(CategorieRecTF.getText());
        r.setStatutRec("En cours");
        r.setDescriRec(descriRec);
        r.setUser(userAdd);
        r.setDateRec(new Date(System.currentTimeMillis()));

        try {
            rs.ajouter(r);
            showNotification();
            // Afficher un message de succès
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Message d'information");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Ajouté avec succès !");
            successAlert.showAndWait();
            ShowReclamation(); // Rafraîchir les données de la table
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void modifier(ActionEvent event) throws IOException, SQLException {
        // Obtenez la réclamation sélectionnée dans la table
        Reclamation r = ListViewRec.getSelectionModel().getSelectedItem();
        if (r != null) {
            String descriRec = descriRecTF.getText();

            // Ajouter le contrôle de saisie ici
            if (descriRec.length() > 50) {
                new Alert(Alert.AlertType.WARNING, "Vous avez dépassé 50 caractères.", ButtonType.OK).showAndWait();
                return;
            } else if (descriRec.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "La description est vide.", ButtonType.OK).showAndWait();
                return;
            }



            // Mettez à jour les champs de la réclamation
            r.setCategorieRec(CatRecCB.getValue());
            r.setDescriRec(descriRec);
            // Demander une confirmation à l'utilisateur (vous pouvez personnaliser cela selon vos besoins)
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de modification");
            alert.setHeaderText("Modifier la reclamation");
            alert.setContentText("Êtes-vous sûr de vouloir modifier la reclamation sélectionnée ?");

            Optional<ButtonType> result = alert.showAndWait();
            // Si l'utilisateur confirme la suppression, procéder
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Mettez à jour la réclamation dans la base de données
                rs.modifier(r);
                // Rafraîchir les données de la table
                ShowReclamation();
                ListViewRec.refresh(); // Ajoutez cette ligne
            }
        }
    }

    @FXML
    void supprimer(ActionEvent event) throws IOException, SQLException {
        // Obtenez l'objet Reclamation sélectionné
        Reclamation selectedReclamation = ListViewRec.getSelectionModel().getSelectedItem();

        // Vérifiez si une réclamation est sélectionnée
        if (selectedReclamation != null) {
            // Supprimez cet objet de votre base de données

            // Demander une confirmation à l'utilisateur (vous pouvez personnaliser cela selon vos besoins)
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("supprimer la reclamation");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer la réclamation sélectionnée ?");

            Optional<ButtonType> result = alert.showAndWait();
            // Si l'utilisateur confirme la suppression, procéder
            if (result.isPresent() && result.get() == ButtonType.OK) {
                rs.supprimer(selectedReclamation.getIdRec());
            }


            // Rafraîchir les données de la table
            ShowReclamation();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune réclamation sélectionnée");
            alert.setHeaderText("Aucune réclamation sélectionnée");
            alert.setContentText("Veuillez sélectionner une réclamation à supprimer.");
            alert.showAndWait();

        }
    }

    @FXML
    void rec(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/AfficherReclamations.fxml"));

        // Créer une nouvelle scène
        Scene scene = new Scene(root);

        // Configurer la nouvelle scène dans une nouvelle fenêtre
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Reclamations");

        // Afficher la nouvelle fenêtre
        stage.show();
    }

    public void ShowReclamation() throws IOException {
        try {
            RecList = rs.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        User userAdd = su.getOneById(3);
        List<Reclamation> filteredRecList = new ArrayList<>();

        for (Reclamation r : RecList) {
            if (r.getUser().equals(userAdd)) {
                filteredRecList.add(r);
            }
        }

        // Créer une FilteredList
        FilteredList<Reclamation> filteredData = new FilteredList<>(FXCollections.observableArrayList(filteredRecList), p -> true);

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

        // Définir la cellFactory personnalisée pour afficher les réclamations
        ListViewRec.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Reclamation item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("Catégorie : " + item.getCategorieRec() + "\nDescription : " + item.getDescriRec() + "\nDate : " + item.getDateRec()+ "\nStatut : " + item.getStatutRec());
                }
            }
        });
    }
    @FXML
    void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AccueilUser.fxml"));
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

    private void showNotification() {
        try {
            Image image = new Image("/img/tick.png");

            // Redimensionner l'image
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(70); // ajustez la largeur comme vous le souhaitez
            imageView.setFitHeight(70); // ajustez la hauteur comme vous le souhaitez

            Notifications notifications = Notifications.create();
            notifications.graphic(imageView); // Utilisez l'ImageView avec l'image redimensionnée
            notifications.text("Reclamation added successfully");
            notifications.title("Success Message");
            notifications.hideAfter(Duration.seconds(4));
            notifications.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void chat(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChatBot.fxml"));
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

    @FXML
    void joke(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/joke.fxml"));
        Parent jokeRoot = loader.load();

        // Créer une nouvelle scène avec l'interface joke
        Scene jokeScene = new Scene(jokeRoot);

        // Créer une nouvelle fenêtre pour l'interface joke
        Stage Stage = new Stage();
        Stage.setScene(jokeScene);
       Stage.setTitle("joke");

        // Afficher la nouvelle fenêtre
        Stage.show();
    }
}