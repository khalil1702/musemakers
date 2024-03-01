package controllers;


import entities.Atelier;
import entities.Cour;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import service.ServiceAtelier;
import service.ServiceCour;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import java.sql.SQLException;
public class FrontAfficherAtelier {



        private final ServiceAtelier serviceAtelier = new ServiceAtelier();
        private Set<Atelier> listeAteliers;
        private final int ATELIERS_PER_PAGE = 6;
        @FXML
        private Pagination PaginiationAteliers;

        @FXML
        private Button afficheratelier;




        @FXML
        private VBox chosenCoursCard;

        @FXML
        private GridPane grid;
        @FXML
        private Button artistiqueid;


        @FXML
        private ScrollPane scroll;

        private List<Atelier> ateliersList = new ArrayList<>();

        @FXML
        public void initialize()
        {
            System.out.println("test");
            ateliersList.addAll(serviceAtelier.getAll());
            innitAteliersUI();
        }
        public void initialize(URL url, ResourceBundle resourceBundle) {
            // Obtention des données des cours
            System.out.println("test");
            ateliersList.addAll(serviceAtelier.getAll());
            innitAteliersUI();
            // Affichage des cours dans la grille
            //displayCoursInGrid();
        }

        @FXML
        void handle1(ActionEvent event) throws IOException {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/AfficherAtelierNv.fxml")));
            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Configurer la nouvelle scène dans une nouvelle fenêtre
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Afficher les ateliers");

            // Afficher la nouvelle fenêtre
            stage.show();
        }

        private void displayCoursInGrid() {
            int column = 0;
            int row = 1;

            for (Atelier ateliers : ateliersList) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/itemAtelier.fxml"));
                    AnchorPane anchorPane = fxmlLoader.load();
                    Object itemCour = fxmlLoader.getController();
                    itemCour.equals(ateliers); // Envoyer les données du cours au contrôleur
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
        public void innitAteliersUI()
        {
            listeAteliers = serviceAtelier.getAll();
            int pageSizeUsers;
            if(!listeAteliers.isEmpty()) {
                pageSizeUsers = listeAteliers.size() / ATELIERS_PER_PAGE;
                if (listeAteliers.size() % ATELIERS_PER_PAGE != 0)
                {
                    pageSizeUsers++;
                }
                PaginiationAteliers.setPageCount(pageSizeUsers);
                PaginiationAteliers.setPageFactory(this::createUsersPage);
                PaginiationAteliers.setCurrentPageIndex(0);
            }
            else
            {
                pageSizeUsers = 1;
                PaginiationAteliers.setPageCount(pageSizeUsers);
                PaginiationAteliers.setCurrentPageIndex(0);
            }
        }
        private AnchorPane createUsersPage(int pageIndex) {
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.setPrefWidth(600); // Decreased width
            anchorPane.setPrefHeight(500); // Increased height

            int startIndex = pageIndex * ATELIERS_PER_PAGE;
            int endIndex = Math.min(startIndex + ATELIERS_PER_PAGE, listeAteliers.size());

            int xOffset = 10; // Margin added on the left
            int yOffset = 20; // Decreased yOffset
            int xIncrement = 180; // Decreased xIncrement
            int yIncrement = 210; // Increased yIncrement
            int index = 0;
            for (Atelier item : listeAteliers) {

                if (index >= startIndex && index < endIndex) {
                    AnchorPane productPane = new AnchorPane();
                    productPane.setLayoutX(xOffset);
                    productPane.setLayoutY(yOffset);
                    productPane.setPrefSize(160, 180); // Increased height and slightly decreased width
                    productPane.setStyle(
                            "-fx-background-color: white; /* Change background color to white */\n" +
                                    "-fx-background-radius: 20px;\n" +
                                    "-fx-border-color: #ccc;\n" +
                                    "-fx-border-width: 0px;\n" +
                                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);\n" +
                                    "color: white; /* Change text color to red */\n"); // Smaller drop shadow

                    Label questionLabel = new Label(item.getLien().toUpperCase());
                    questionLabel.setLayoutX(8); // Smaller layout X
                    questionLabel.setLayoutY(20); // Smaller layout Y
                    questionLabel.setMaxWidth(150); // Set maximum width
                    questionLabel.setMinWidth(productPane.getPrefWidth() - 30); // Set minimum width based on pane width
                    questionLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 10px;");
                    questionLabel.setWrapText(true);
                    productPane.getChildren().add(questionLabel);

                    Circle profilePic = new Circle(40, 40, 21); // Adjust size and position as needed
                    profilePic.setLayoutX(productPane.getPrefWidth() - 70); // Move the circle to the right
                    profilePic.setLayoutY(-10); // Adjust vertical position as needed
                    profilePic.setFill(new ImagePattern(new Image("img/toile.png"))); // Load user's profile picture
                    profilePic.setStyle("-fx-background-color: white; \n" +
                            "    -fx-background-radius: 50%; \n" +
                            "    -fx-border-color: #d3d3d3;\n" +
                            "    -fx-border-width: 2px; \n" +
                            "    -fx-effect: dropshadow(three-pass-box, rgba(80, 80, 80, 0.6), 15, 0, 0, 0);");
                    productPane.getChildren().add(profilePic);

                    Label lien = new Label();
                    TextFlow textFlowx = new TextFlow();
                    Text boldTextx = new Text("lien: ");
                    boldTextx.setStyle("-fx-font-weight: bold;");
                    Text regularTextx = new Text(item.getLien());
                    textFlowx.getChildren().addAll(boldTextx, regularTextx);

                    lien.setGraphic(textFlowx);
                    lien.setLayoutX(8); // Smaller layout X
                    lien.setLayoutY(60); // Smaller layout Y
                    lien.setMaxWidth(150); // Set maximum width
                    lien.setMinWidth(productPane.getPrefWidth() - 40); // Set minimum width based on pane width
                    lien.setStyle("-fx-text-fill: #333333; -fx-font-size: 10px;");
                    lien.setWrapText(true);
                    lien.setPrefWidth(200);
                    productPane.getChildren().add(lien);




                    Label datedebut = new Label();
                    TextFlow textFlowxxx = new TextFlow();
                    Text boldTextxxx = new Text("datedebut: ");
                    boldTextxxx.setStyle("-fx-font-weight: bold;");
                    Text regularTextxxx = new Text(item.getDateDebut_atelier().toString());
                    textFlowxxx.getChildren().addAll(boldTextxxx, regularTextxxx);

                    datedebut.setGraphic(textFlowxxx);
                    datedebut.setLayoutX(8); // Smaller layout X
                    datedebut.setLayoutY(100); // Smaller layout Y
                    datedebut.setMaxWidth(150); // Set maximum width
                    datedebut.setMinWidth(productPane.getPrefWidth() - 40); // Set minimum width based on pane width
                    datedebut.setStyle("-fx-text-fill: #333333; -fx-font-size: 10px;");
                    datedebut.setWrapText(true);
                    datedebut.setPrefWidth(200);
                    productPane.getChildren().add(datedebut);

                    Label datefin = new Label();
                    TextFlow textFlowxxxx = new TextFlow();
                    Text boldTextxxxx = new Text("datefin: ");
                    boldTextxxxx.setStyle("-fx-font-weight: bold;");
                    Text regularTextxxxx = new Text(item.getDateFin_atelier().toString());
                    textFlowxxxx.getChildren().addAll(boldTextxxxx, regularTextxxxx);

                    datefin.setGraphic(textFlowxxxx);
                    datefin.setLayoutX(8); // Smaller layout X
                    datefin.setLayoutY(120); // Smaller layout Y
                    datefin.setMaxWidth(150); // Set maximum width
                    datefin.setMinWidth(productPane.getPrefWidth() - 40); // Set minimum width based on pane width
                    datefin.setStyle("-fx-text-fill: #333333; -fx-font-size: 10px;");
                    datefin.setWrapText(true);
                    datefin.setPrefWidth(200);
                    productPane.getChildren().add(datefin);

                    anchorPane.getChildren().add(productPane);


                    xOffset += xIncrement + 10; // Margin added on the right
                    if ((index + 1) % 3 == 0) {
                        xOffset = 10; // Reset xOffset for the next row
                        yOffset += yIncrement;
                    }
                }
                index++;
            }

            return anchorPane;

        }

    }




