package controllers;

import entities.Reclamation;
import entities.User;
import entities.sendSMS;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.ReclamationService;
import service.ServiceUser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AfficherRecBack {
    private final ReclamationService rs = new ReclamationService();
    private final service.ServiceUser su = new ServiceUser();

    @FXML
    private TableColumn<?, ?> CvCat;

    @FXML
    private TableColumn<?, ?> CvDate;

    @FXML
    private TableColumn<?, ?> CvDescri;

    @FXML
    private TableColumn<?, ?> CvNom;

    @FXML
    private TableColumn<?, ?> CvStatut;
    @FXML
    private TableView<Reclamation> TableViewRecB;
    @FXML
    private Button modifier;

    @FXML
    private Label satut;
    @FXML
    private TextField searchTF;
    @FXML
    private TextField stat;
    @FXML
    private Button supprimer;
    @FXML
    private ComboBox<String> StatutCB;
    @FXML
    private ComboBox<String> trie;

    public void initialize() throws IOException {
        ObservableList<String> options = FXCollections.observableArrayList(
                "En cours", "Resolue", "Fermée"
        );
        StatutCB.setItems(options);
        TableViewRecB.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // Vérifie si c'est un simple clic
                Reclamation selectedReclamation = TableViewRecB.getSelectionModel().getSelectedItem();
                if (selectedReclamation != null) {
                    // Afficher les informations de la séance sélectionnée dans le formulaire
                    displayActiviteInfo(selectedReclamation);
                }
            }
        });
        ShowReclamation();
// Ajoutez un écouteur sur le TextField de recherche pour gérer la recherche dynamique
        searchTF.textProperty().addListener((observable, oldValue, newValue) -> {
            SearchRec(newValue); // Appel de la méthode de recherche avec le nouveau texte
        });
        trie.getItems().addAll("Tri par nom utilisateur (ascendant)", "Tri par nom utilisateur (descendant)",
                "Tri par description (ascendant)", "Tri par description (descendant)",
                "Tri par statut (ascendant)", "Tri par statut (descendant)",
                "Tri par date de réclamation (ascendant)", "Tri par date de réclamation (descendant)");

        // Appel de la méthode trierOeuvres avec l'option sélectionnée lorsque l'utilisateur change la valeur de la ComboBox
        trie.setOnAction(event -> {
            String selectedOption = (String) trie.getValue();
            trierReclamation(selectedOption);
        });

    }


    List<Reclamation> RecList;

    public void ShowReclamation() throws IOException {
        try {
            RecList = rs.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        User userAdd = su.getOneById(2); // Assurez-vous que cette méthode retourne l'utilisateur correct
        List<Reclamation> filteredRecList = new ArrayList<>();

        for (Reclamation r : RecList) {
            User user = su.getOneById(r.getUser().getId_user()); // Récupérer l'utilisateur associé à la réclamation
            if (user != null) {
                r.setUserNom(user.getNom_user()); // Assurez-vous que getNom_user() retourne le nom de l'utilisateur
                filteredRecList.add(r);
            }
        }
        TableColumn<Reclamation, Void> commentColumn = new TableColumn<>("Commenter");

        // Ajoutez le reste de votre logique de création de colonnes ici...



        if (TableViewRecB != null && TableViewRecB instanceof TableView) {
            ((TableView<Reclamation>) TableViewRecB).setItems(FXCollections.observableArrayList(RecList));
        }
        commentColumn.setCellFactory(param -> new TableCell<Reclamation, Void>() {
            private final Button commentButton = new Button("Commenter");

            {
                commentButton.setOnAction(event -> {
                    Reclamation reclamation = getTableView().getItems().get(getIndex());
                    //Reclamation reclamation = getItem();
                    // Logique pour ouvrir une autre interface
                    Reclamation selectedReclamation = TableViewRecB.getSelectionModel().getSelectedItem();
                    if (selectedReclamation != null) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterComAdmin.fxml"));
                        Parent root = null;
                        try {
                            root = loader.load();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        AjouterComAdmin ajouterComAdmin = loader.getController();
                        ajouterComAdmin.setReclamation(selectedReclamation);
                        Scene scene = new Scene(root);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.setTitle("Ajouter un commentaire");
                        stage.show();
                    } else {
                        // Afficher un message d'erreur si aucune réclamation n'est sélectionnée
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur");
                        alert.setHeaderText(null);
                        alert.setContentText("Veuillez sélectionner une réclamation pour ajouter un commentaire.");
                        alert.showAndWait();
                    }
                    // Code pour afficher l'interface AjouterComAdmin avec cette réclamation
                    // Assurez-vous d'implémenter cette logique
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(commentButton);
                }
            }
        });

        TableViewRecB.getColumns().add(commentColumn);
        CvNom.setCellValueFactory(new PropertyValueFactory<>("userNom"));

        CvDescri.setCellValueFactory(new PropertyValueFactory<>("descriRec"));
        CvDate.setCellValueFactory(new PropertyValueFactory<>("DateRec"));
        CvCat.setCellValueFactory(new PropertyValueFactory<>("CategorieRec"));
        CvStatut.setCellValueFactory(new PropertyValueFactory<>("StatutRec"));

        if (TableViewRecB != null && TableViewRecB instanceof TableView) {
            ((TableView<Reclamation>) TableViewRecB).setItems(FXCollections.observableArrayList(filteredRecList));
        }
    }


    @FXML
    private void modifier(ActionEvent event) throws SQLException {
        Reclamation selectedRec = (Reclamation) TableViewRecB.getSelectionModel().getSelectedItem();
        if (selectedRec != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de modification");
            alert.setHeaderText("Modifier la reclamation");
            alert.setContentText("Êtes-vous sûr de vouloir modifier la reclamation sélectionnée ?");

            Optional<ButtonType> result = alert.showAndWait();

            // Si l'utilisateur confirme la suppression, procéder
            if (result.isPresent() && result.get() == ButtonType.OK) {
                selectedRec.setStatutRec(StatutCB.getValue());

                // Update in database
                rs.modifier(selectedRec);
                sendSMS.sendSMS(selectedRec);
            }

            // Refresh table view
            try {
                ShowReclamation();
                TableViewRecB.refresh();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @FXML
    private void supprimer(ActionEvent event) throws SQLException {
        Reclamation selectedRec = (Reclamation) TableViewRecB.getSelectionModel().getSelectedItem();

        if (selectedRec != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Supprimer la reclamation");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer la reclamation sélectionnée ?");

            Optional<ButtonType> result = alert.showAndWait();

            // Si l'utilisateur confirme la suppression, procéder
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Delete from database

                rs.supprimer(selectedRec.getIdRec());
            }


            // Refresh table view
            try {
                ShowReclamation();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void displayActiviteInfo(Reclamation r) {
        StatutCB.setValue(r.getStatutRec());


    }


    @FXML
    void retour(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AccueilAdmin.fxml"));
        Parent root = loader.load();

        // Créer une nouvelle scène
        Scene scene = new Scene(root);

        // Configurer la nouvelle scène dans une nouvelle fenêtre
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Acceuil");

        // Afficher la nouvelle fenêtre
        stage.show();
    }

    @FXML
    void SearchRec(String searchText) {
        List<Reclamation> searchResult = RecList.stream()
                .filter(reclamation ->
                        reclamation.getDescriRec().toLowerCase().contains(searchText.toLowerCase()) ||
                                (reclamation != null &&
                                        reclamation.getUser().getNom_user() != null &&
                                        reclamation.getUser().getNom_user().toLowerCase().contains(searchText.toLowerCase())) ||
                                (reclamation != null &&
                                        reclamation.getCategorieRec() != null &&
                                        reclamation.getCategorieRec().toLowerCase().contains(searchText.toLowerCase())) ||
                                (reclamation != null &&
                                        reclamation.getDescriRec() != null &&
                                        reclamation.getDescriRec().toLowerCase().contains(searchText.toLowerCase())) ||

                                (reclamation != null &&
                                        reclamation.getStatutRec() != null &&
                                        reclamation.getStatutRec().toLowerCase().contains(searchText.toLowerCase()))
                )
                .collect(Collectors.toList());

        // Mettre à jour la TableView avec les résultats de la recherche
        TableViewRecB.setItems(FXCollections.observableArrayList(searchResult));
    }

    private void trierReclamation(String option) {
        try {
            switch (option) {
                case "Tri par nom utilisateur (ascendant)":
                    TableViewRecB.setItems(FXCollections.observableArrayList(rs.trierParNomUserAscendant()));
                    break;
                case "Tri par nom utilisateur (descendant)":
                    TableViewRecB.setItems(FXCollections.observableArrayList(rs.trierParNomUserDescendant()));
                    break;
                case "Tri par description (ascendant)":
                    TableViewRecB.setItems(FXCollections.observableArrayList(rs.trierParDescriptionAscendant()));
                    break;
                case "Tri par description (descendant)":
                    TableViewRecB.setItems(FXCollections.observableArrayList(rs.trierParDescriptionDescendant()));
                    break;
                case "Tri par statut (ascendant)":
                    TableViewRecB.setItems(FXCollections.observableArrayList(rs.trierParStatutAscendant()));
                    break;
                case "Tri par statut (descendant)":
                    TableViewRecB.setItems(FXCollections.observableArrayList(rs.trierParStatutDescendant()));
                    break;
                case "Tri par date de réclamation (ascendant)":
                    TableViewRecB.setItems(FXCollections.observableArrayList(rs.trierParDateReclamationAscendant()));
                    break;
                case "Tri par date de réclamation (descendant)":
                    TableViewRecB.setItems(FXCollections.observableArrayList(rs.trierParDateReclamationDescendant()));
                    break;
                default:
                    // Faire quelque chose si aucune option ne correspond
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'exception
        }

    }
    @FXML
    void StatCom(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/StatRec.fxml"));
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
    void StatRec(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/StatCom.fxml"));
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