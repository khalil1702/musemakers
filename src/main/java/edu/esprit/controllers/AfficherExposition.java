package edu.esprit.controllers;

import edu.esprit.entities.Exposition;
import edu.esprit.services.ServiceExposition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class AfficherExposition {
    @FXML
    private TableView<Exposition>  TableView;

    @FXML
    private TableColumn<Exposition, Date> date_debut;

    @FXML
    private TableColumn<Exposition, Date> date_fin;
    @FXML
    private TableColumn<Exposition, String> imgDisplay;

    @FXML
    private ImageView imageView;

//    @FXML
//    private TableColumn<Exposition, String> img;

    @FXML
    private TableColumn<Exposition, String> nom_expo;

    @FXML
    private TableColumn<Exposition, String> theme;
    @FXML
    private TableColumn<Exposition, Time> heure_debut;
    @FXML
    private TableColumn<Exposition, Time> heure_fin;
    @FXML
    private ServiceExposition expo=new ServiceExposition();
    @FXML
    private Exposition e=new Exposition();


    @FXML
    void initialize() {
        TableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (event.getClickCount() == 1) {
                    handleTableViewClick();
                } else if (event.getClickCount() == 2) {
                    handleTableViewDoubleClick();
                }
            }
        });

        try {
            List<Exposition> ex = new ArrayList<>(expo.getAll());
            ObservableList<Exposition> observableList = FXCollections.observableList(ex);
            TableView.setItems(observableList);
            TableView.getSelectionModel().selectFirst();
            if (!ex.isEmpty()) {
                displayImage(ex.get(0).getImage());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        nom_expo.setCellValueFactory(new PropertyValueFactory<>("nom"));
        date_debut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        date_fin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        theme.setCellValueFactory(new PropertyValueFactory<>("theme"));
       // img.setCellValueFactory(new PropertyValueFactory<>("image"));
        heure_debut.setCellValueFactory(new PropertyValueFactory<>("heure_debut"));
        heure_fin.setCellValueFactory(new PropertyValueFactory<>("heure_fin"));

        heure_debut.setCellFactory(column -> new TableCell<Exposition, Time>() {
            @Override
            protected void updateItem(Time time, boolean empty) {
                super.updateItem(time, empty);
                if (empty || time == null) {
                    setText("");
                } else {
                    setText(formatHourMinute(time));
                }
            }
        });

        heure_fin.setCellFactory(column -> new TableCell<Exposition, Time>() {
            @Override
            protected void updateItem(Time time, boolean empty) {
                super.updateItem(time, empty);
                if (empty || time == null) {
                    setText("");
                } else {
                    setText(formatHourMinute(time));
                }
            }
        });

    }

    private void handleTableViewClick() {
        Exposition selectedRec = TableView.getSelectionModel().getSelectedItem();
        if (selectedRec != null) {
            displayImage(selectedRec.getImage());
        }}


    private void displayImage(String imagePath) {
        try {
            String imageURL = Paths.get(imagePath).toUri().toString();
            Image image = new Image(imageURL);
            imageView.setImage(image);
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
            // Handle the exception, for example, display a placeholder image
            imageView.setImage(null);
        }
    }
    @FXML
    private void supprimer(ActionEvent event) throws SQLException {
        Exposition selectedRec = TableView.getSelectionModel().getSelectedItem();
        if (selectedRec != null) {
            // Display a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Supprimer l'exposition");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cette exposition?");

            // Capture the user's choice
            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

            // If the user clicks OK, proceed with deletion
            if (result == ButtonType.OK) {
                // Delete from database
                expo.supprimer(selectedRec.getId());

                // Refresh table view
                initialize();
            }
        }
    }


    private void openEditDialog(Exposition exposition) {
        Dialog<Exposition> dialog = new Dialog<>();
        dialog.setTitle("Modifier Exposition");
        dialog.setHeaderText(null);

        // Customize the controls in the dialog for editing
        TextField nomField = new TextField(exposition.getNom());
        TextField heureDField = new TextField(exposition.getHeure_debut().toString());
        TextField heureFField = new TextField(exposition.getHeure_fin().toString());
        DatePicker dateDebutField = new DatePicker(exposition.getDateDebut().toLocalDate());
        DatePicker dateFinField = new DatePicker(exposition.getDateFin().toLocalDate());

        TextArea descriptionField = new TextArea(exposition.getDescription());

        // Ajout du ComboBox pour le thème
        Label themeLabel = new Label("Thème:");
        ComboBox<String> themeComboBox = new ComboBox<>();
        themeComboBox.getItems().addAll(
                "Peinture à l'huile",
                "Photographie contemporaine",
                "Sculptures abstraites",
                "Art numérique",
                "Art moderne",
                "Street Art",
                "Portraits contemporains",
                "Art fantastique"
        );
        themeComboBox.setValue(exposition.getTheme());

        TextField imageField = new TextField(exposition.getImage());

        GridPane grid = new GridPane();
        grid.setVgap(10); // Set the vertical gap between rows

        grid.add(new Label("Nom de l'exposition:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Date Début:"), 0, 1);
        grid.add(dateDebutField, 1, 1);
        grid.add(new Label("heure Début:"), 0, 2); // Increase row index to add space
        grid.add(heureDField, 1, 2); // Increase row index to add space
        grid.add(new Label("Date Fin:"), 0, 3); // Increase row index to add space
        grid.add(dateFinField, 1, 3); // Increase row index to add space
        grid.add(new Label("heure fin:"), 0, 4); // Increase row index to add space
        grid.add(heureFField, 1, 4); // Increase row index to add space
        grid.add(new Label("Description:"), 0, 5); // Increase row index to add space
        grid.add(descriptionField, 1, 5); // Increase row index to add space
        grid.add(themeLabel, 0, 6); // Increase row index to add space
        grid.add(themeComboBox, 1, 6); // Increase row index to add space
        //grid.add(new Label("Image:"), 0, 7); // Increase row index to add space

        HBox imageBox = new HBox();
        imageBox.getChildren().addAll(imageField, createBrowseButton(imageField));
        grid.add(imageBox, 1, 7);

        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == buttonTypeOk) {
                String newNom = nomField.getText();

                String newDescription = descriptionField.getText();
                String newTheme = themeComboBox.getValue(); // Utilisez le ComboBox pour obtenir la valeur du thème
                String newImage = imageField.getText();
                String newHeureD=heureDField.getText();
                String newHeureF=heureFField.getText();

                try {
                    LocalTime.parse(newHeureD);
                    LocalTime.parse(newHeureF);
                } catch (DateTimeParseException e) {
                    showAlert("Erreur", "Format de l'heure invalide, veuillez réessayer avec cette format: (hh:mm)", Alert.AlertType.ERROR);
                    return null;
                }

                if (newHeureD.isEmpty() || newHeureF.isEmpty()||newNom.isEmpty() || dateDebutField.getValue() == null ||
                        dateFinField.getValue() == null || newDescription.isEmpty() ||
                        newTheme == null || newImage.isEmpty()) {
                    showAlert("Erreur", "Veuillez remplir tous les champs", Alert.AlertType.ERROR);
                    return null;
                }
                if (!isImageFile(Paths.get(newImage))) {
                    showAlert("Erreur", "Veuillez sélectionner un fichier image valide (png, jpg, jpeg)", Alert.AlertType.ERROR);
                    return null;
                }
                // Convert String to Time
                Time heureDebut = Time.valueOf(LocalTime.parse(newHeureD));
                Time heureFin = Time.valueOf(LocalTime.parse(newHeureF));



                LocalDate newLocalDateDebut = dateDebutField.getValue();
                LocalDate newLocalDateFin = dateFinField.getValue();

                if (newLocalDateDebut.isBefore(LocalDate.now())) {
                    showAlert("Erreur", "La date de début ne peut pas être antérieure à la date actuelle", Alert.AlertType.ERROR);
                    return null;
                }

                if (newLocalDateFin.isBefore(newLocalDateDebut)) {
                    showAlert("Erreur", "La date de fin doit être après la date de début", Alert.AlertType.ERROR);
                    return null;
                }

                try {
                    exposition.setNom(newNom);
                    exposition.setDateDebut(Date.valueOf(newLocalDateDebut));
                    exposition.setDateFin(Date.valueOf(newLocalDateFin));
                    exposition.setHeure_debut(heureDebut);
                    exposition.setHeure_debut(heureFin);
                    exposition.setDescription(newDescription);
                    exposition.setTheme(newTheme);
                    exposition.setImage(newImage);

                    expo.modifier(exposition);
                    displayImage(newImage);

                    int index = TableView.getItems().indexOf(exposition);
                    TableView.getItems().set(index, exposition);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private boolean isImageFile(Path path) {
        try {
            String contentType = Files.probeContentType(path);
            return contentType != null && contentType.startsWith("image/");
        } catch (Exception e) {
            return false;
        }
    }


    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private Button createBrowseButton(TextField imageField) {
        Button browseButton = new Button("Browse");
        browseButton.setOnAction(event -> browseImage(imageField));
        return browseButton;
    }

    private void browseImage(TextField imageField) {
        Stage primaryStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            // Enregistrez le chemin du fichier dans le champ de texte pathimageid
            imageField.setText(selectedFile.getPath());

            // Chargez l'image depuis le fichier sélectionné
            try {
                // Utilisez la classe Paths pour obtenir un chemin de fichier correct
                String imagePath = Paths.get(selectedFile.getPath()).toUri().toString();
                Image image = new Image(imagePath);

                // Affichez l'image dans l'ImageView si nécessaire
                // imageView.setImage(image);
            } catch (Exception e) {
                System.out.println("Error loading image: " + e.getMessage());
                // Gérer l'exception, par exemple, afficher une image par défaut
            }
        }
    }


    private void handleTableViewDoubleClick() {
        Exposition selectedRec = TableView.getSelectionModel().getSelectedItem();
        if (selectedRec != null) {
            openEditDialog(selectedRec);
        }
    }



    ////////////********** navigation

    @FXML
    void Afficher(ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/afficherExpo.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the stage from the event source (button) and set the new scene
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }
    @FXML
    void ajouterNav(ActionEvent event) throws IOException {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/ajouterexpo.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the stage from the event source (button) and set the new scene
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }
    @FXML
    void demandeNav(ActionEvent event) throws IOException {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/demandeReser.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the stage from the event source (button) and set the new scene
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }

    }
    @FXML
    void histoAdminNav(ActionEvent event) throws IOException {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/listeReser.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the stage from the event source (button) and set the new scene
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }
    private String formatHourMinute(Time time) {
        // Extract hours and minutes from the Time object
        int hours = time.toLocalTime().getHour();
        int minutes = time.toLocalTime().getMinute();

        // Format as "HH:mm"
        return String.format("%02d:%02d", hours, minutes);
    }

}




