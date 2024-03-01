package controllers;

import entities.Cour;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import service.ServiceCour;


import entities.Cour;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import service.ServiceCour;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import java.sql.SQLException;
import java.util.Set;

public class FrontFront {

    private final ServiceCour serviceCour = new ServiceCour();
    private Set<Cour> listeCours;

    @FXML
    private VBox exhibitionVBox;


   

        @FXML
        private VBox chosenCoursCard;

        @FXML
        private GridPane grid;

        @FXML
        private ScrollPane scroll;

        private List<Cour> coursList = new ArrayList<>();


       
        public void initialize(URL url, ResourceBundle resourceBundle) {
            // Obtention des données des cours
            coursList.addAll(serviceCour.getAll());

            // Affichage des cours dans la grille
            displayCoursInGrid();
        }

        private void displayCoursInGrid() {
            int column = 0;
            int row = 1;

            for (Cour cours : coursList) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/itemCour.fxml"));
                    AnchorPane anchorPane = fxmlLoader.load();
                    Object itemCour = fxmlLoader.getController();
                    itemCour.equals(cours); // Envoyer les données du cours au contrôleur
                    grid.add(anchorPane, column, row);
                    grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                    grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    grid.setMaxWidth(Region.USE_PREF_SIZE);

                    // set grid height
                    grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                    grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    grid.setMaxHeight(Region.USE_PREF_SIZE);

                    GridPane.setMargin(anchorPane, new Insets(10));

                    // Mise à jour de la position dans la grille
                    column++;
                    if (column == 3) {
                        column = 0;
                        row++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
