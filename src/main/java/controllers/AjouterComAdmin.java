package controllers;

import entities.Commentaire;
import entities.Reclamation;
import entities.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.CommentaireService;
import service.ReclamationService;
import service.ServiceUser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class AjouterComAdmin {
    private final CommentaireService cs = new CommentaireService();
    private final ServiceUser su = new ServiceUser();
    private final ReclamationService rs= new ReclamationService( );

    @FXML
    private TextField comTF;

    @FXML
    private TableColumn<?, ?> CvCom;

    @FXML
    private TableColumn<?, ?> CvNomA;

    @FXML
    private TableView<Commentaire> TableViewComA;
    private List<Commentaire> CommentaireList;

    @FXML
    private TextField contenuCommentaireTF;

    @FXML
    private TextField searchTF;

    @FXML
    private ListView<Commentaire> ListViewCommentaire;

    @FXML
    private Button ajout;

    @FXML
    private Button modifier;

    @FXML
    private Button supprimer;


    private Reclamation reclamation;

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
        // Faites ce que vous devez faire avec la réclamation ici
    }

    public void initialize() throws IOException {
        ListViewCommentaire.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // Vérifie si c'est un simple clic
                Commentaire selectedCommentaire = ListViewCommentaire.getSelectionModel().getSelectedItem();
                if (selectedCommentaire != null) {
                    // Afficher les informations de la séance sélectionnée dans le formulaire
                    displayCommentaireInfo(selectedCommentaire);
                }
            }
        });
        ShowCommentaire();

        // Ajoutez un écouteur sur le TextField de recherche pour gérer la recherche dynamique
        searchTF.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                searchCommentaire(newValue); // Appel de la méthode de recherche avec le nouveau texte
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    void ajouter(ActionEvent event) throws IOException {
        Commentaire c = new Commentaire();
        Reclamation r = null ; // Remplacez 1 par l'ID de la réclamation appropriée
        try {
            r = rs.getOneById(reclamation.getIdRec());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String contenuCom = contenuCommentaireTF.getText();

        // Ajouter le contrôle de saisie ici
        if (contenuCom.length() > 50) {
            System.out.println("Vous avez dépassé 50 caractères.");
            return;
        } else if (contenuCom.isEmpty()) {
            System.out.println("Le contenu du commentaire est vide.");
            return;
        }

        c.setReclamation(r);
        c.setContenuCom(contenuCom);
        c.setDateCom(new Date(System.currentTimeMillis()));

        try {
            cs.ajouter(c);
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Message d'information");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Ajouté avec succès !");
            successAlert.showAndWait();
            ShowCommentaire(); // Rafraîchir les données de la table
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    ServiceUser us = new ServiceUser();
  /*  @FXML
    void modifier(ActionEvent event) throws IOException {
        // Obtenez le commentaire sélectionné dans la table
        Commentaire c = ListViewCommentaire.getSelectionModel().getSelectedItem();
        User userAdd= us.getOneById(2);
        if (c != null && reclamation.getUser().equals(userAdd)) {
            // Obtenez une instance de la réclamation que vous souhaitez associer
            Reclamation r = null;
            try {
                r = rs.getOneById(reclamation.getIdRec()); // Remplacez 35 par l'ID de la réclamation appropriée
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            String contenuCom = contenuCommentaireTF.getText();

            // Ajouter le contrôle de saisie ici
            if (contenuCom.length() > 50) {
                System.out.println("Vous avez dépassé 50 caractères.");
                return;
            } else if (contenuCom.isEmpty()) {
                System.out.println("Le contenu du commentaire est vide.");
                return;
            }

            // Associez la réclamation au commentaire
            c.setReclamation(r);
            // Mettez à jour le contenu du commentaire avec le texte du TextField
            c.setContenuCom(contenuCom);
            c.setDateCom(new Date(System.currentTimeMillis()));


            try {
                // Appelez la méthode modifier pour mettre à jour le commentaire dans la base de données
                cs.modifier(c);
                // Rafraîchir les données de la table
                ShowCommentaire();
            } catch (SQLException e) {
                throw new RuntimeException("Erreur lors de la modification du commentaire", e);
            }
        }
    }
*/
    @FXML
    void supprimer(ActionEvent event) throws IOException {
        // Obtenez le commentaire sélectionné dans la ListView
        Commentaire c = ListViewCommentaire.getSelectionModel().getSelectedItem();

        if (c != null) {
            try {
                // Demander une confirmation à l'utilisateur (vous pouvez personnaliser cela selon vos besoins)
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation de suppression");
                alert.setHeaderText("supprimer la reclamation");
                alert.setContentText("Êtes-vous sûr de vouloir supprimer le commentaire sélectionnée ?");

                Optional<ButtonType> result = alert.showAndWait();
                // Si l'utilisateur confirme la suppression, procéder
                if (result.isPresent() && result.get() == ButtonType.OK) {
                cs.supprimer(c.getIdCom()); }

                ShowCommentaire(); // Rafraîchir les données de la table
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void displayCommentaireInfo(Commentaire c) {
        contenuCommentaireTF.setText(c.getContenuCom());
    }

    public void ShowCommentaire() throws IOException {
        try {
            CommentaireList = cs.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            Reclamation reclamationAdd = rs.getOneById(178);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<Reclamation> filteredCommentaireList = new ArrayList<>();



        ListViewCommentaire.setItems(FXCollections.observableArrayList(CommentaireList));

    }


    @FXML
    void rec(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherRecBack.fxml"));
        Parent root = loader.load();

        // Créer une nouvelle scène
        Scene scene = new Scene(root);

        // Configurer la nouvelle scène dans une nouvelle fenêtre
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle(" Gérer les Reclamations ");

        // Afficher la nouvelle fenêtre
        stage.show();
    }
    private void searchCommentaire(String searchText) throws IOException {
        List<Commentaire> searchResult = CommentaireList.stream()
                .filter(commentaire ->
                        commentaire.getContenuCom().toLowerCase().contains(searchText.toLowerCase()) ||
                                commentaire.getReclamation().getUser().getNom_user().toLowerCase().contains(searchText.toLowerCase()) ||
                                commentaire.getReclamation().getDescriRec().toLowerCase().contains(searchText.toLowerCase()) ||
                                commentaire.getReclamation().getStatutRec().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());

        // Mettre à jour la ListView avec les résultats de la recherche
        ListViewCommentaire.setItems(FXCollections.observableArrayList(searchResult));
    }

}