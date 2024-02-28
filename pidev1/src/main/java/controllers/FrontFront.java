package controllers;

import entities.Cour;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import service.ServiceCour;

import java.sql.SQLException;
import java.util.Set;

public class FrontFront {

    private final ServiceCour serviceCour = new ServiceCour();
    private Set<Cour> listeCours;

    @FXML
    private VBox exhibitionVBox;

    public void AfficherFrontFro() throws SQLException {
    }

    @FXML
    public void initialize() {
        listeCours = serviceCour.getAll();
        displayCour();
    }

    private void displayCour() {
        System.out.println("Affichage des cours :");

        exhibitionVBox.getChildren().clear();

        for (Cour cour : listeCours) {
            HBox exhibitionBox = new HBox(10);
            exhibitionBox.setAlignment(javafx.geometry.Pos.CENTER);

            VBox detailsVBox = new VBox(5);
            detailsVBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

            Label nomLabel = new Label("Nom: " + cour.getTitre_cours());
            Label descriptionLabel = new Label("Description: " + cour.getDescription_cours());
            Label dateDebutLabel = new Label("Date de début: " + cour.getDateDebut_cours());
            Label dateFinLabel = new Label("Date de fin: " + cour.getDateFin_cours());

            detailsVBox.getChildren().addAll(nomLabel, descriptionLabel, dateDebutLabel, dateFinLabel);

            exhibitionBox.getChildren().addAll(detailsVBox);
            exhibitionBox.getStyleClass().add("exhibition-box");

            exhibitionVBox.getChildren().add(exhibitionBox);
        }
    }
}
