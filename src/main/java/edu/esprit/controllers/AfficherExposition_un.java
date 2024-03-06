package edu.esprit.controllers;

import edu.esprit.entities.Exposition;
import edu.esprit.services.ServiceExposition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class AfficherExposition_un {

    @FXML
    private TableView<Exposition> TableView;

    @FXML
    private TableColumn<Exposition, Date> date_debut;

    @FXML
    private TableColumn<Exposition, Date> date_fin;

    @FXML
    private ImageView imageView;

    @FXML
    private TableColumn<Exposition, String> nom_expo;

    @FXML
    private TableColumn<Exposition, String> theme;

    @FXML
    private TableColumn<Exposition, Time> heure_debut;

    @FXML
    private TableColumn<Exposition, Time> heure_fin;

    @FXML
    private ServiceExposition expo = new ServiceExposition();

    @FXML
    private Exposition e = new Exposition();

    @FXML
    void initialize() {
        TableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                handleTableViewClick();
            } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
               handleTableViewDoubleClick();
            }
        });

        try {
            List<Exposition> ex = new ArrayList<>(expo.getAll());
            ObservableList<Exposition> observableList = FXCollections.observableList(ex);
            TableView.setItems(observableList);
            TableView.getSelectionModel().selectFirst();
            if (!ex.isEmpty()) {
                displayImage(ex.get(0).getImage(),imageView); // Pass the imageView here
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        nom_expo.setCellValueFactory(new PropertyValueFactory<>("nom"));
        date_debut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        date_fin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        theme.setCellValueFactory(new PropertyValueFactory<>("theme"));
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
            displayImage(selectedRec.getImage(),imageView);
        }
    }


    private void handleTableViewDoubleClick() {
        Exposition selectedRec = TableView.getSelectionModel().getSelectedItem();
        if (selectedRec != null) {
            openEditDialog(selectedRec);
        }
    }

    private void displayImage(String imageName, ImageView imageView) {
        if (imageName != null && !imageName.isEmpty()) {
            Image image = new Image("file:C:\\xampp\\htdocs\\user_images\\" + imageName);
            imageView.setImage(image);
        } else {
            // Set a default image or clear the image view
            imageView.setImage(null);
        }
    }




    private String saveImageLocally(File file) {
        try {
            Path destination = Paths.get("C:\\xampp\\htdocs\\user_images\\" + file.getName());
            Files.copy(file.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
            return destination.toString();
        } catch (IOException e) {
            System.out.println("Erreur lors de la copie du fichier : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
        VBox dragAndDropContainer = new VBox();
        dragAndDropContainer.setAlignment(Pos.CENTER);
        dragAndDropContainer.setSpacing(10);

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
        Label imageLabel = new Label("Image:");
        ImageView imageView = new ImageView();
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        displayImage(exposition.getImage(), imageView);

        ImageView gifImageView = new ImageView(new Image("file:C:\\Users\\MSI\\Desktop\\drag-drop.gif"));
        gifImageView.setFitWidth(200); // Définissez la largeur maximale
        gifImageView.setFitHeight(150);
        dragAndDropContainer.getChildren().addAll(imageView, gifImageView);

        dragAndDropContainer.setOnDragOver(event -> {
            if (event.getGestureSource() != dragAndDropContainer && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        dragAndDropContainer.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasFiles()) {
                success = true;
                for (File file : db.getFiles()) {
                    // Obtenez uniquement le nom du fichier (pas le chemin complet)
                    String fileName = file.getName();

                    // Stockez uniquement le nom du fichier
                    exposition.setImage(fileName);

                    File destinationFile = new File("C:\\xampp\\htdocs\\user_images\\" + fileName);
                    try {
                        // Copiez le fichier sélectionné vers le fichier de destination
                        Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        // Affichez l'image mise à jour
                        displayImage(fileName, imageView);
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });





        GridPane grid = new GridPane();
        grid.setVgap(10);

        grid.add(new Label("Nom de l'exposition:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Date Début:"), 0, 1);
        grid.add(dateDebutField, 1, 1);
        grid.add(new Label("heure Début:"), 0, 2);
        grid.add(heureDField, 1, 2);
        grid.add(new Label("Date Fin:"), 0, 3);
        grid.add(dateFinField, 1, 3);
        grid.add(new Label("heure fin:"), 0, 4);
        grid.add(heureFField, 1, 4);
        grid.add(new Label("Description:"), 0, 5);
        grid.add(descriptionField, 1, 5);
        grid.add(themeLabel, 0, 6);
        grid.add(themeComboBox, 1, 6);
        grid.add(new Label("Image:"), 0, 7);
        grid.add(dragAndDropContainer, 1, 7);
        dialog.getDialogPane().setContent(grid);
      //  dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == buttonTypeOk) {
                String newNom = nomField.getText();
                String newDescription = descriptionField.getText();
                String newTheme = themeComboBox.getValue();
                String newHeureD = heureDField.getText();
                String newHeureF = heureFField.getText();


                try {
                    LocalTime.parse(newHeureD);
                    LocalTime.parse(newHeureF);
                } catch (DateTimeParseException e) {
                    showAlert("Erreur", "Format de l'heure invalide, veuillez réessayer avec cette format: (hh:mm)", Alert.AlertType.ERROR);
                    return null;
                }

                if (newHeureD.isEmpty() || newHeureF.isEmpty() || newNom.isEmpty() || dateDebutField.getValue() == null ||
                        dateFinField.getValue() == null || newDescription.isEmpty() ||
                        newTheme == null) {
                    showAlert("Erreur", "Veuillez remplir tous les champs", Alert.AlertType.ERROR);
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
                    exposition.setHeure_fin(heureFin);
                    exposition.setDescription(newDescription);
                    exposition.setTheme(newTheme);
                    expo.modifier(exposition);

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


    @FXML
    private void setupDragAndDrop() {
        // Set a default image
        String defaultImagePath = "file:C:\\Users\\MSI\\Desktop\\drag-drop.gif";
        imageView.setImage(new Image(defaultImagePath));

        // Handle drag and drop for the image
        imageView.setOnDragOver(event -> {
            if (event.getGestureSource() != imageView && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        imageView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasFiles()) {
                List<File> files = db.getFiles();
                if (!files.isEmpty()) {
                    File file = files.get(0); // Get the first file
                    String imagePath = "file:C:\\xampp\\htdocs\\user_images\\" + file.getName();
                    displayImage(imagePath,imageView); // Update the ImageView
                    success = true;
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    private boolean isImageFile(Path path) {
        try {
            String contentType = Files.probeContentType(path);
            return contentType != null && contentType.startsWith("image/");
        } catch (Exception e) {
            return false;
        }
    }


    ////////////********** navigation

    @FXML
    void Afficher(ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/afficherExpo_un.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/ajouterExpo_1.fxml"));
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




