package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;
import service.ReclamationService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;

public class StatRec implements Initializable {

    @FXML
    private PieChart StatCat;

    @FXML
    private PieChart StatS;

    private final ReclamationService rs= new ReclamationService( );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadStatistics();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }

    private void loadStatistics() throws SQLException {
        Map<String, Integer> categoryCounts = rs.countReclamationsByCategory();
        Map<String, Integer> statusCounts = rs.countReclamationsByStatus();

        populatePieChart(StatCat, "Categories des reclamations", categoryCounts);
        populatePieChart(StatS, "Statut des reclamations", statusCounts);
    }

    private void populatePieChart(PieChart pieChart, String dataName, Map<String, Integer> data) {
        if (pieChart != null) {
            pieChart.setTitle("Statistiques pour les " + dataName);
            pieChart.getData().clear(); // Clear existing data

            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                PieChart.Data pieData = new PieChart.Data(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue());
                pieChart.getData().add(pieData);
            }
        }
    }
    @FXML
    void exit(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherRecBack.fxml"));
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