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
import javafx.stage.Stage;
import service.CommentaireService;
import service.ReclamationService;
import service.ServiceUser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AjouterComUser {
    private final CommentaireService cs = new CommentaireService();
    private final ReclamationService rs = new ReclamationService();
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

    private static final List<String> BAD_WORDS = Arrays.asList("Sick", "Bad", "Dump","khalil");
    private static final Map<String, String> EMOJI_MAP = new HashMap<>();

    static {
        EMOJI_MAP.put(":)", "😊");
        EMOJI_MAP.put(":(", "😢");
        EMOJI_MAP.put(":D", "😃");
        EMOJI_MAP.put(":-)", "😊");
        EMOJI_MAP.put(":-(", "😢");
        EMOJI_MAP.put(":p", "😛");
        EMOJI_MAP.put(";)", "😉");
        EMOJI_MAP.put("<3", "❤️");
        EMOJI_MAP.put(":/", "☹");
        EMOJI_MAP.put("-_-", "😑");
    }

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
                e.printStackTrace();
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
            new Alert(Alert.AlertType.WARNING, "Vous avez dépassé 50 caractères.", ButtonType.OK).showAndWait();
            return;
        } else if (contenuCom.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Le contenu du commentaire est vide.", ButtonType.OK).showAndWait();
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

        // Censor bad words before displaying them
        CommentaireList.forEach(commentaire -> {
            String censoredContenuCom = censorBadWords(commentaire.getContenuCom());
            commentaire.setContenuCom(censoredContenuCom);
            // Convert symbols to emojis in the censored message
            String emojiMessage = convertSymbolsToEmojis(censoredContenuCom);
            commentaire.setContenuCom(emojiMessage);
        });

        ListViewCommentaire.setItems(FXCollections.observableArrayList(CommentaireList));

    }

    private String censorBadWords(String text) {
        for (String badWord : BAD_WORDS) {
            // Replace bad words with **
            text = text.replaceAll("(?i)" + badWord, "*****");
        }
        return text;
    }

    private String convertSymbolsToEmojis(String text) {
        for (Map.Entry<String, String> entry : EMOJI_MAP.entrySet()) {
            // Escape special characters in symbols and replace symbols with emojis
            text = text.replaceAll(Pattern.quote(entry.getKey()), entry.getValue());
        }
        return text;
    }

    // Méthode pour rechercher les commentaires en fonction du contenu
    private void searchCommentaire(String searchText) throws IOException {
        List<Commentaire> searchResult = CommentaireList.stream()
                .filter(commentaire ->
                        commentaire.getContenuCom().toLowerCase().contains(searchText.toLowerCase()) ||
                                commentaire.getReclamation().getUser().getNom_user().toLowerCase().contains(searchText.toLowerCase()) ||
                                commentaire.getReclamation().getStatutRec().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());

        // Mettre à jour la ListView avec les résultats de la recherche
        ListViewCommentaire.setItems(FXCollections.observableArrayList(searchResult));
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherReclamations.fxml"));
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
