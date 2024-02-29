package controllers;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import service.CommentaireService; // Importez votre service CommentaireService

import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;

public class StatCom implements Initializable {

    @FXML
    private PieChart StatCte;

    private final CommentaireService commentaireService = new CommentaireService(); // Initialisez votre service CommentaireService

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadStatistics();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérez l'exception selon vos besoins
        }
    }

    private void loadStatistics() throws SQLException {
        Map<String, Integer> categoryCounts = commentaireService.countCommentsByCategory(); // Utilisez la méthode de votre service pour obtenir les statistiques par catégorie
        populatePieChart(StatCte, " Commentaires sur les Categories", categoryCounts);
    }

    private void populatePieChart(PieChart pieChart, String dataName, Map<String, Integer> data) {
        if (pieChart != null) {
            pieChart.setTitle("Statistiques pour les " + dataName);
            pieChart.getData().clear(); // Effacez les données existantes

            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                PieChart.Data pieData = new PieChart.Data(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue());
                pieChart.getData().add(pieData);
            }
        }
    }
}
