package edu.esprit.controllers;

import edu.esprit.entities.Oeuvre;
import edu.esprit.entities.Avis;
import edu.esprit.services.ServiceAvis;
import edu.esprit.services.ServiceOeuvre;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.geometry.Insets;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class AfficherOeuvre {

    @FXML
    private TableView<Oeuvre> TableView;

    @FXML
    private TableColumn<Oeuvre, String> categorie_id;

    @FXML
    private TableColumn<Oeuvre, Date> datecreation_id;

    @FXML
    private TableColumn<Oeuvre,String> description_id;

    @FXML
    private ImageView image_id;



    @FXML
    private TableColumn<Oeuvre, String> nom_id;

    @FXML
    private TableColumn<Oeuvre, Float> prix_id;

    @FXML
    private Button button_avis;

    @FXML
    private TextField categorieSearchID;

    @FXML
    private TextField nameSearchID;

    @FXML
    private Button button_stat1;
    @FXML
    private Button button_stat;

    @FXML
    private ComboBox<String> comboBox;

    private Oeuvre selectedOeuvre;

    @FXML
    private Button nouvelajout_id;
    @FXML
    private final ServiceOeuvre PS=new ServiceOeuvre();

    @FXML
    private final ServiceAvis serviceAvis=new ServiceAvis();
    private File selectedFile;

    private List<Oeuvre> oeuvresInitiales;


    @FXML
    void initialize()  {

        TableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                handleTableViewDoubleClick();
            }
        });

        List<Oeuvre> Ps= new ArrayList<>(PS.getAll());
        ObservableList<Oeuvre> observableList = FXCollections.observableList(Ps);
        TableView.setItems(observableList);

        nom_id.setCellValueFactory(new PropertyValueFactory<>("nom"));
        categorie_id.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        datecreation_id.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
        prix_id.setCellValueFactory(new PropertyValueFactory<>("prix"));

        button_avis.setOnAction(event -> {
            Oeuvre selectedOeuvre = TableView.getSelectionModel().getSelectedItem();
            if (selectedOeuvre != null) {
                showAvisDialog(selectedOeuvre);
            }
        });

        // Créez un Tooltip pour afficher l'image de l'oeuvre
        Tooltip tooltip = new Tooltip();

        ImageView imageView = new ImageView();
        imageView.setFitHeight(100); // Ajustez la taille de l'image comme vous le souhaitez
        imageView.setFitWidth(100);


        tooltip.setGraphic(imageView);

        // Configurez la factory de ligne pour afficher l'image de l'oeuvre dans l'Tooltip
        TableView.setRowFactory(tv -> {
            TableRow<Oeuvre> row = new TableRow<>();

            row.setOnMouseEntered(event -> {
                if (!row.isEmpty()) {
                    Oeuvre oeuvre = row.getItem();
                    if (oeuvre != null) {
                        String imageURL = oeuvre.getImage();
                        if (imageURL != null && !imageURL.isEmpty()) {
                            Image image = new Image(new File(imageURL).toURI().toString());
                            imageView.setImage(image);
                            image_id.setImage(image);
                        }
                    }
                }
            });

            row.setOnMouseExited(event -> {
                // Masquez le Tooltip lorsque la souris quitte la ligne
                imageView.setImage(null);
                image_id.setImage(null);
            });

            return row;
        });

        categorieSearchID.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearch();
        });

        nameSearchID.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearch();
        });

        // Ajoutez une option vide au début du ComboBox
        comboBox.getItems().add("");

        // Ajoutez les options de tri au ComboBox
        comboBox.getItems().addAll("Nom ascendant", "Nom descendant", "Date de création ascendante", "Date de création descendante", "Prix ascendant", "Prix descendant");

        // Faites une copie de la liste initiale des œuvres
        oeuvresInitiales = FXCollections.observableArrayList(TableView.getItems());

        // Ajoutez un écouteur pour détecter quand l'utilisateur change l'option de tri
        comboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            trierOeuvres(newValue);
        });

        button_stat.setOnAction(e -> afficherStatistiquesOeuvre());
        button_stat1.setOnAction(e1 -> afficherStatistiquesParAvis());
    }
    @FXML
    private void delete(ActionEvent event) throws SQLException {
        Oeuvre selectedRec = (Oeuvre) TableView.getSelectionModel().getSelectedItem();
        if (selectedRec != null) {
            // Delete from database
            PS.supprimer(selectedRec.getId());

            // Refresh table view
            initialize();
        }
    }

    private void openEditDialog(Oeuvre oeuvre) {

        System.out.println("Row double-clicked");
        Dialog<Oeuvre> dialog = new Dialog<>();
        dialog.setTitle("Modifier votre Oeuvre");
        dialog.getDialogPane().setMinWidth(800);
        dialog.getDialogPane().setMinHeight(300);

        // Set the button types.
        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // champ categorie
        ChoiceBox<String> categorieField = new ChoiceBox<>();
        categorieField.getItems().addAll("Peinture", "Sculpture", "Scene");
        // Sélectionner la valeur actuelle
        categorieField.setValue(oeuvre.getCategorie());

        //champ date
        DatePicker dateCreationField = new DatePicker();
        dateCreationField.setValue(oeuvre.getDateCreation().toLocalDate());


        // Create the fields and populate with existing data
        TextField nomField = new TextField(oeuvre.getNom());
        //TextField categorieField = new TextField(oeuvre.getCategorie());
        TextField prixField = new TextField(String.valueOf(oeuvre.getPrix()));
        // TextField dateCreationField = new TextField(oeuvre.getDateCreation().toString());
        TextField descriptionField = new TextField(oeuvre.getDescription());
        TextField imageField = new TextField(oeuvre.getImage());
        Button browseButton = new Button("Parcourir");

        // Create error labels
        Label nomErrorLabel = new Label();
        nomErrorLabel.setTextFill(Color.RED);
        Label categorieErrorLabel = new Label();
        categorieErrorLabel.setTextFill(Color.RED);
        Label dateErrorLabel = new Label();
        dateErrorLabel.setTextFill(Color.RED);
        Label prixErrorLabel = new Label();
        prixErrorLabel.setTextFill(Color.RED);
        Label descriptionErrorLabel = new Label();
        descriptionErrorLabel.setTextFill(Color.RED);
        Label imageErrorLabel = new Label();
        imageErrorLabel.setTextFill(Color.RED);

        // Add listeners to fields
        nomField.textProperty().addListener((observable, oldValue, newValue) -> {
            String erreurNom = newValue.isEmpty() || !newValue.matches("[a-zA-Z,\\- ]+") ? "Le nom doit contenir uniquement des lettres et ne peut pas être vide." : "";
            nomErrorLabel.setText(erreurNom);
        });

        categorieField.valueProperty().addListener((observable, oldValue, newValue) -> {
            String erreurCategorie = (newValue == null || newValue.isEmpty()) ? "Veuillez sélectionner une catégorie." : "";
            categorieErrorLabel.setText(erreurCategorie);
        });

        dateCreationField.valueProperty().addListener((observable, oldValue, newValue) -> {
            String erreurDate = (newValue == null) ? "Veuillez sélectionner une date." : "";
            dateErrorLabel.setText(erreurDate);
        });

        prixField.textProperty().addListener((observable, oldValue, newValue) -> {
            String erreurPrix = "";
            if (newValue.isEmpty()) {
                erreurPrix = "Veuillez saisir un prix.";
            } else {
                try {
                    float prix = Float.parseFloat(newValue);
                } catch (NumberFormatException e) {
                    erreurPrix = "Veuillez saisir un prix valide.";
                }
            }
            prixErrorLabel.setText(erreurPrix);
        });

        descriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            String erreurDescription = (newValue.isEmpty() || newValue.length() > 30 || !newValue.matches("[a-zA-Z0-9, ]+")) ? "La description ne peut pas être vide de longeur < 30 et doit contenir uniquement des lettres, des chiffres, des virgules." : "";
            descriptionErrorLabel.setText(erreurDescription);
        });

        imageField.textProperty().addListener((observable, oldValue, newValue) -> {
            String erreurImage = newValue.isEmpty() ? "Veuillez fournir une URL d'image." : "";
            imageErrorLabel.setText(erreurImage);
        });



        // Layout for dialog
        GridPane grid = new GridPane();
        grid.setVgap(30);
        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(nomErrorLabel, 1, 1);

        grid.add(new Label("Catégorie:"), 0, 2);
        grid.add(categorieField, 1, 2);
        grid.add(categorieErrorLabel, 1, 3);

        grid.add(new Label("Prix:"), 0, 4);
        grid.add(prixField, 1, 4);
        grid.add(prixErrorLabel, 1, 5);

        grid.add(new Label("Date de Création:"), 0, 6);
        grid.add(dateCreationField, 1, 6);
        grid.add(dateErrorLabel, 1, 7);

        grid.add(new Label("Description:"), 0, 8);
        grid.add(descriptionField, 1, 8);
        grid.add(descriptionErrorLabel, 1, 9);

        grid.add(new Label("Image:"), 0, 10);
        grid.add(imageField, 1, 10);
        grid.add(browseButton, 2, 10);
        grid.add(imageErrorLabel, 1, 11);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to an Oeuvre when the save button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String nom = nomField.getText();
                String categorie = categorieField.getValue();
                LocalDate localDate = dateCreationField.getValue();
                Date date = Date.valueOf(localDate); // Conversion LocalDate en Date
                String description = descriptionField.getText();
                String image = imageField.getText();

                if (nomErrorLabel.getText().isEmpty() && categorieErrorLabel.getText().isEmpty() && dateErrorLabel.getText().isEmpty() && prixErrorLabel.getText().isEmpty() && descriptionErrorLabel.getText().isEmpty() && imageErrorLabel.getText().isEmpty()) {
                    oeuvre.setNom(nom);
                    oeuvre.setCategorie(categorie);
                    oeuvre.setPrix(Float.parseFloat(prixField.getText()));
                    oeuvre.setDateCreation(date);
                    oeuvre.setDescription(description);
                    oeuvre.setImage(image);

                    // Update the database
                    PS.modifier(oeuvre);

                    // Refresh table view
                    TableView.getItems().clear();
                    TableView.getItems().addAll(PS.getAll());
                }
                return oeuvre;
            }
            return null;
        });
        browseButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir une image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            File selectedFile = fileChooser.showOpenDialog(dialog.getDialogPane().getScene().getWindow());
            if (selectedFile != null) {
                imageField.setText(selectedFile.getAbsolutePath());
            }
        });

        // Show dialog and get result
        Optional<Oeuvre> result = dialog.showAndWait();

        result.ifPresent(updatedOeuvre -> {
            // Update the database
            PS.modifier(updatedOeuvre);

            // Refresh table view
            TableView.getItems().clear();
            TableView.getItems().addAll(PS.getAll());
        });
    }



    private void handleTableViewDoubleClick() {
        Oeuvre selectedRec = TableView.getSelectionModel().getSelectedItem();
        if (selectedRec != null) {
            openEditDialog(selectedRec);
        }
    }


    @FXML
    void AfficherAjoutOeuvre(ActionEvent event) throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/admin/AjouterOeuvre.fxml"));
        Parent root=loader.load();
//
        TableView.getScene().setRoot(root);
    }

    public void showAvisDialog(Oeuvre oeuvre) {

            // Créez un ScrollPane
            ScrollPane scrollPane = new ScrollPane();

            // Créez un VBox pour contenir les avis
            VBox vbox = new VBox();
             vbox.setSpacing(10); // Ajoutez de l'espace entre les avis

            // Récupérez les avis de la base de données (vous avez déjà cette partie)
            List<Avis> avisList = serviceAvis.getAvisByOeuvre(oeuvre);

            // Ajoutez les avis au VBox
            for (Avis avis : avisList) {
                /*Label userLabel = new Label("Utilisateur: " + avis.getClient().getNom_user() + " " + avis.getClient().getPrenom_user());
                userLabel.setStyle("-fx-font-weight: bold;");

                Label commentLabel = new Label("Commentaire: " + avis.getCommentaire());
                commentLabel.setStyle("-fx-font-weight: bold;");

                Label noteLabel = new Label("Note: " + avis.getNote());
                noteLabel.setStyle("-fx-font-weight: bold;");

                avisbox_id.getChildren().addAll(userLabel, commentLabel, noteLabel);*/
                // Créez un Label personnalisé pour chaque avis
                Label avisLabel = new Label();
                avisLabel.setStyle("-fx-font-weight: bold; -fx-background-color: #d0d7ac; -fx-padding: 10px; -fx-border-radius: 20px;");

                // Remplissez le contenu du Label avec les détails de l'avis
                avisLabel.setText("Utilisateur: " + avis.getClient().getNom_user() + " " + avis.getClient().getPrenom_user()
                        + "\nCommentaire: " + avis.getCommentaire()
                        + "\nNote: " + avis.getNote());

                vbox.getChildren().add(avisLabel);
            }

            // Liez le VBox au ScrollPane
            scrollPane.setContent(vbox);

            // Créez la scène
            Scene scene = new Scene(scrollPane, 800, 600);
        Stage stage = new Stage();
        stage.setTitle("Avis sur " + oeuvre.getNom());

        stage.setMinWidth(800);
        stage.setScene(scene);

        // Affichez la Stage
        stage.show();

        }


    @FXML
    private void handleSearch() {
        String categorie = categorieSearchID.getText();
        String name = nameSearchID.getText();

        try {
            Set<Oeuvre> searchResult;
            if (categorie.isEmpty() && name.isEmpty()) {
                searchResult = PS.getAll(); // Utilisez cette méthode pour obtenir toutes les œuvres
            } else {
                searchResult = PS.chercherParCategorieOuNom(categorie, name);
            }

            // Convertir le Set en ObservableList pour la TableView
            ObservableList<Oeuvre> observableList = FXCollections.observableArrayList();
            observableList.addAll(searchResult);

            // Mettre à jour la TableView
            TableView.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void trierOeuvres(String option) {
        List<Oeuvre> oeuvres;

        if (option.equals("")) {
            // Si l'option est vide, réinitialisez la TableView à son état initial
            oeuvres = oeuvresInitiales;
        } else {
            // Sinon, faites une copie de la liste initiale et triez-la
            oeuvres = oeuvresInitiales.stream().collect(Collectors.toList());
            switch (option) {
                case "Nom ascendant":
                    PS.triParNom(oeuvres, true);
                    break;
                case "Nom descendant":
                    PS.triParNom(oeuvres, false);
                    break;
                case "Date de création ascendante":
                    PS.triParDateCreation(oeuvres, true);
                    break;
                case "Date de création descendante":
                    PS.triParDateCreation(oeuvres, false);
                    break;
                case "Prix ascendant":
                    PS.triParPrix(oeuvres, true);
                    break;
                case "Prix descendant":
                    PS.triParPrix(oeuvres, false);
                    break;
                default:
                    // Option non reconnue, vous pouvez gérer cette situation comme vous le souhaitez
                    break;
            }
        }

        TableView.setItems(FXCollections.observableArrayList(oeuvres));
    }
    public void afficherStatistiquesOeuvre() {

        PS.afficherStatistiques();
    }
    public void afficherStatistiquesParAvis()
    {

        serviceAvis.afficherStatistiquesAvis();
    }
}

