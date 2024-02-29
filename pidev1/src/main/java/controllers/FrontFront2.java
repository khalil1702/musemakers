package controllers;

import entities.Atelier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.IService;
import service.ServiceAtelier;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;


import entities.Atelier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.IService;
import service.ServiceAtelier;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
public class FrontFront2 {




        @FXML
        private TableView<Atelier> tableAtelier;


        @FXML
        private TableColumn<Atelier, String> colDescription;

        @FXML
        private TableColumn<Atelier, LocalDate> colDateDebut;

        @FXML
        private TableColumn<Atelier, LocalDate> colDateFin;

        @FXML
        private TableColumn<Atelier, Void> colDelete;

        @FXML
        private TextField tflien;

        @FXML
        private DatePicker datePickerDebut;

        @FXML
        private DatePicker datePickerFin;

        @FXML
        private Button btnSave;



        @FXML
        private Label lblTitreError, lblDescriptionError, lblDateDebutError, lblDateFinError;

        @FXML
        private Button ajoutid;

        private Atelier atelier;

        private final IService<Atelier> serviceAtelier = new ServiceAtelier();

        @FXML
        void initialize() throws SQLException {
            // Récupérer la liste des ateliers depuis le service
            Set<Atelier> ateliers = serviceAtelier.getAll();
            ObservableList<Atelier> atelierList = FXCollections.observableArrayList(ateliers);

            // Initialiser les colonnes de la table avec les valeurs appropriées

            colDescription.setCellValueFactory(new PropertyValueFactory<>("lien"));
            colDateDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut_atelier"));
            colDateFin.setCellValueFactory(new PropertyValueFactory<>("dateFin_atelier"));

            // Ajouter les ateliers à la table
            tableAtelier.setItems(atelierList);

            // Gérer le clic sur une ligne de la table
            tableAtelier.setOnMouseClicked(event -> {
                if (!tableAtelier.getSelectionModel().isEmpty()) {
                    Atelier atelierSelectionne = tableAtelier.getSelectionModel().getSelectedItem();
                    // Remplir les champs avec les informations de l'atelier sélectionné
                    tflien.setText(atelierSelectionne.getLien());
                    System.out.println(atelierSelectionne.getLien());
                    datePickerDebut.setValue(atelierSelectionne.getDateDebut_atelier());
                    datePickerFin.setValue(atelierSelectionne.getDateFin_atelier());
                    atelier = atelierSelectionne;
                }
            });

            // Ajouter la fonctionnalité de suppression à la colonne Supprimer

        }

        @FXML
        void saveChanges() {
            try {
                // Vérifier si un atelier est sélectionné
                if (atelier != null) {
                    // Vérifier que le lien respecte un format spécifique (par exemple, un lien HTTP ou HTTPS)
                    String lien = tflien.getText();
                    if (!lien.matches("^https?://.*$")) {
                        throw new IllegalArgumentException("Le lien doit commencer par 'http://' ou 'https://'.");
                    }

                    // Vérifier que la date de fin est postérieure à la date de début
                    LocalDate dateDebut = datePickerDebut.getValue();
                    LocalDate dateFin = datePickerFin.getValue();
                    if (dateFin.isBefore(dateDebut)) {
                        throw new IllegalArgumentException("La date de fin doit être postérieure à la date de début.");
                    }

                    // Mettre à jour l'objet atelier avec les nouvelles valeurs
                    atelier.setLien(lien);
                    atelier.setDateDebut_atelier(dateDebut);
                    atelier.setDateFin_atelier(dateFin);

                    // Mettre à jour l'objet atelier dans la base de données
                    serviceAtelier.modifier(atelier);

                    // Rafraîchir le tableau
                    tableAtelier.refresh();
                } else {
                    // Afficher un message d'erreur si aucun atelier n'est sélectionné
                    afficherAlerteErreur("Erreur", "Aucun atelier sélectionné.");
                }
            } catch (Exception e) {
                // Gérer les exceptions et afficher une alerte d'erreur
                afficherAlerteErreur("Erreur", e.getMessage());
            }
        }


        // Méthode pour afficher une alerte d'erreur
        private void afficherAlerteErreur(String titre, String message) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(titre);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }




    }


