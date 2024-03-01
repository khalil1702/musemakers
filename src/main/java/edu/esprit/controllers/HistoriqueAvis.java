package edu.esprit.controllers;
import edu.esprit.entities.Avis;
import edu.esprit.services.ServiceAvis;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;


import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;


public class HistoriqueAvis {

    @FXML
    private TableColumn<Avis, Integer> Note_id;

    @FXML
    private TableView<Avis> TableView;

    @FXML
    private TableColumn<Avis, String> commentaire_id;

    @FXML
    private TableColumn<Avis, Date> dateexp_id;

    @FXML
    private TableColumn<Avis, String> nomoeuvre_id;

    @FXML
    private Button buttonsupprimer_id;


    @FXML
    private ImageView image_id;




    ServiceAvis serviceAvis = new ServiceAvis();
    @FXML
    public void initialize() {

        TableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
               handleTableViewDoubleClick();
            }
        });
        // Get the user's reviews

        ObservableList<Avis> avisList = FXCollections.observableArrayList(serviceAvis.getAvisByUserId(4)); // Replace 4 with the actual user ID

        // Set up the columns in the table
        //Note_id.setCellValueFactory(new PropertyValueFactory<Avis, Integer>("note"));
        // Définissez la cellule de la colonne Note pour afficher le Rating
        Note_id.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNote()).asObject());
        Note_id.setCellFactory(column -> {
            return new TableCell<Avis, Integer>() {
                private Rating rating = new Rating();

                @Override
                protected void updateItem(Integer note, boolean empty) {
                    super.updateItem(note, empty);
                    if (empty || note == null) {
                        setGraphic(null);
                    } else {
                        rating.setRating(note);
                        rating.setDisable(true); // Empêchez l'utilisateur de modifier la note
                        setGraphic(rating);
                    }
                }
            };
        });
        commentaire_id.setCellValueFactory(new PropertyValueFactory<Avis, String>("commentaire"));
        dateexp_id.setCellValueFactory(new PropertyValueFactory<Avis, Date>("dateExperience"));
        //nomoeuvre_id.setCellValueFactory(new PropertyValueFactory<Avis, String>("oeuvre"));
        // Créez un Tooltip pour afficher l'image de l'oeuvre
        Tooltip tooltip = new Tooltip();
        ImageView imageView = new ImageView();
        imageView.setFitHeight(100); // Ajustez la taille de l'image comme vous le souhaitez
        imageView.setFitWidth(100);
        tooltip.setGraphic(imageView);

        // Définissez la factory de ligne pour afficher l'image de l'oeuvre dans l'ImageView image_id
        TableView.setRowFactory(tv -> {
            TableRow<Avis> row = new TableRow<>();

            row.setOnMouseEntered(event -> {
                if (!row.isEmpty()) {
                    Avis avis = row.getItem();
                    if (avis != null && avis.getOeuvre() != null) {
                        String imageURL = avis.getOeuvre().getImage();
                        if (imageURL != null && !imageURL.isEmpty()) {
                            Image image = new Image(new File(imageURL).toURI().toString());
                            imageView.setImage(image);
                            image_id.setImage(image);
                        }
                    }
                }
            });

            row.setOnMouseExited(event -> {
                // Effacez l'image lorsque la souris quitte la ligne
                imageView.setImage(null);
                image_id.setImage(null);
            });

            return row;
        });

        // Load the data into the table
        TableView.setItems(avisList);



    }
    @FXML
    private void delete(ActionEvent event) throws SQLException {
        Avis selectedRec = (Avis) TableView.getSelectionModel().getSelectedItem();
        if (selectedRec != null) {
            // Delete from database
            serviceAvis.supprimer(selectedRec.getIdAvis());

            // Refresh table view
            initialize();
        }
    }

    private void openEditDialog(Avis avis) {

        System.out.println("Row double-clicked");
        Dialog<Avis> dialog = new Dialog<>();
        dialog.setTitle("Modifier votre avis");
        dialog.getDialogPane().setMinWidth(800);
        dialog.getDialogPane().setMinHeight(300);

        // Set the button types.
        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        //champ date
        DatePicker dateexperienceField = new DatePicker();
        dateexperienceField.setValue(avis.getDateExperience().toLocalDate());
        // champ note
       // ChoiceBox<Integer> noteField = new ChoiceBox<>();
        //noteField.getItems().addAll(1,2,3,4,5);
        // Sélectionner la valeur actuelle
        //noteField.setValue(avis.getNote());
// Créez un objet Rating pour afficher la note
        Rating rating = new Rating();
        rating.setRating(avis.getNote()); // Définissez le nombre d'étoiles en fonction de la note de l'avis
        rating.setDisable(false); // Permettez à l'utilisateur de modifier la note


        // Create the fields and populate with existing data
        TextField commentaireField = new TextField(avis.getCommentaire());
        //TextField noteField = new TextField(String.valueOf(avis.getNote()));
        //TextField dateexperienceField = new TextField(avis.getDateExperience().toString());

        // Create error labels
        Label commentaireErrorLabel = new Label();
        commentaireErrorLabel.setTextFill(Color.RED);
        Label noteErrorLabel = new Label();
        noteErrorLabel.setTextFill(Color.RED);
        Label dateErrorLabel = new Label();
        dateErrorLabel.setTextFill(Color.RED);

        // Add listeners to fields
        commentaireField.textProperty().addListener((observable, oldValue, newValue) -> {
            String erreurCommentaire = (newValue.isEmpty() || newValue.length() > 30 || !newValue.matches("[a-zA-Z0-9,\\-]+")) ? "Le commentaire ne peut pas être vide, ne doit pas dépasser 30 caractères et doit contenir uniquement des lettres,des chiffres, des virgules et des tirets." : "";
            commentaireErrorLabel.setText(erreurCommentaire);
        });

        /*noteField.valueProperty().addListener((observable, oldValue, newValue) -> {
            String erreurnote = (newValue == null) ? "Veuillez sélectionner une note." : "";
            noteErrorLabel.setText(erreurnote);
        });*/

        dateexperienceField.valueProperty().addListener((observable, oldValue, newValue) -> {
            String erreurDate = (newValue == null) ? "Veuillez sélectionner une date." : "";
            dateErrorLabel.setText(erreurDate);
        });

        // Layout for dialog
        GridPane grid = new GridPane();
        grid.setVgap(30);
        grid.add(new Label("Commentaire:"), 0, 0);
        grid.add(commentaireField, 1, 0);
        grid.add(commentaireErrorLabel, 1, 1);

        //grid.add(new Label("Note:"), 0, 2);
        //grid.add(noteField, 1, 2);
        //grid.add(noteErrorLabel, 1, 3);
        // Ajoutez le Rating au grid
        grid.add(new Label("Note:"), 0, 2);
        grid.add(rating, 1, 2);
        grid.add(noteErrorLabel, 1, 3);


        grid.add(new Label("Date de votre experience:"), 0, 4);
        grid.add(dateexperienceField, 1, 4);
        grid.add(dateErrorLabel, 1, 5);



        dialog.getDialogPane().setContent(grid);

        // Convert the result to an Avis when the save button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String commentaire = commentaireField.getText();
                Integer note = (int) rating.getRating(); // Obtenez la valeur de la note
                LocalDate localDate = dateexperienceField.getValue();
                Date date = Date.valueOf(localDate); // Conversion LocalDate en Date

                if (commentaireErrorLabel.getText().isEmpty() && noteErrorLabel.getText().isEmpty() && dateErrorLabel.getText().isEmpty()) {
                    avis.setCommentaire(commentaire);
                    avis.setNote(note);
                    avis.setDateExperience(date);

                    // Update the database
                    serviceAvis.modifier(avis);

                    // Refresh table view
                    TableView.getItems().clear();
                    TableView.getItems().addAll(serviceAvis.getAvisByUserId(4));
                }

                return avis;
            }
            return null;
        });


        // Show dialog and get result
        Optional<Avis> result = dialog.showAndWait();

        result.ifPresent(updatedAvis -> {
            // Update the database
            serviceAvis.modifier(updatedAvis);

            // Refresh table view
            TableView.getItems().clear();
            TableView.getItems().addAll(serviceAvis.getAvisByUserId(4));
        });
    }

    @FXML
    void Affichergallerie(ActionEvent event) throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/client/AfficherOeuvreClient.fxml"));
        Parent root=loader.load();

        // Obtenez la scène actuelle à partir de n'importe quel nœud inclus dans cette scène
        Node sourceNode = (Node) event.getSource();
        Stage currentStage = (Stage) sourceNode.getScene().getWindow();

        // Mettre en place la nouvelle scène
        Scene scene = new Scene(root);
        currentStage.setScene(scene);
        currentStage.show();
    }
    private void handleTableViewDoubleClick() {
        Avis selectedRec = TableView.getSelectionModel().getSelectedItem();
       if (selectedRec != null) {
            openEditDialog(selectedRec);
       }
    }



}

