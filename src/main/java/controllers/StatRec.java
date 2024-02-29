package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import service.ReclamationService;

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

        populatePieChart(StatCat, "Categories", categoryCounts);
        populatePieChart(StatS, "Statut", statusCounts);
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
}
