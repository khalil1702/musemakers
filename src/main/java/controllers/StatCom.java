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
import service.CommentaireService; // Importez votre service CommentaireService

import java.io.IOException;
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
        populatePieChart(StatCte, " Commentaires sur les Categories des reclamations", categoryCounts);
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
