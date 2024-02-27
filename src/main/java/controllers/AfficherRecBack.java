package controllers;

import entities.Reclamation;
import entities.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import service.ReclamationService;
import service.ServiceUser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AfficherRecBack {
    private final ReclamationService rs= new ReclamationService( );
    private final service.ServiceUser su= new ServiceUser();

    @FXML
    private TableColumn<?, ?> CvCat;

    @FXML
    private TableColumn<?, ?> CvDate;

    @FXML
    private TableColumn<?, ?> CvDescri;

    @FXML
    private TableColumn<?, ?> CvNom;

    @FXML
    private TableColumn<?, ?> CvStatut;
    @FXML
    private TableView<Reclamation> TableViewRecB;
    @FXML
    private Button modifier;

    @FXML
    private Label satut;

    @FXML
    private TextField stat;
    @FXML
    private Button supprimer;
    public void initialize() throws IOException {
        ShowReclamation();

    }


    List<Reclamation> RecList;

    public void ShowReclamation() throws IOException {
        try {
            RecList = rs.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        User userAdd = su.getOneById(2); // Assurez-vous que cette méthode retourne l'utilisateur correct

        List<Reclamation> filteredRecList = new ArrayList<>();

        for (Reclamation r : RecList) {
            if (r.getUser().equals(userAdd)) {
                r.setUserNom(userAdd.getNom_user()); // Assurez-vous que getNom() retourne le nom de l'utilisateur
                filteredRecList.add(r);
            }
        }

        CvNom.setCellValueFactory(new PropertyValueFactory<>("userNom"));
        CvDescri.setCellValueFactory(new PropertyValueFactory<>("descriRec"));
        CvDate.setCellValueFactory(new PropertyValueFactory<>("DateRec"));
        CvCat.setCellValueFactory(new PropertyValueFactory<>("CategorieRec"));
        CvStatut.setCellValueFactory(new PropertyValueFactory<>("StatutRec"));

        if (TableViewRecB != null && TableViewRecB instanceof TableView) {
            ((TableView<Reclamation>) TableViewRecB).setItems(FXCollections.observableArrayList(filteredRecList));
        }
    }




    @FXML
    private void modifier(ActionEvent event) throws SQLException {
        Reclamation selectedRec = (Reclamation) TableViewRecB.getSelectionModel().getSelectedItem();
        if (selectedRec != null) {
            // Change status here, for example:
            selectedRec.setStatutRec(stat.getText()); // get new status from TextField

            // Update in database
            rs.modifier(selectedRec);

            // Refresh table view
            try {
                ShowReclamation();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @FXML
    private void supprimer(ActionEvent event) throws SQLException {
        Reclamation selectedRec = (Reclamation) TableViewRecB.getSelectionModel().getSelectedItem();
        if (selectedRec != null) {
            // Delete from database
            rs.supprimer(selectedRec.getIdRec());

            // Refresh table view
            try {
                ShowReclamation();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}