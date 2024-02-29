package controllers;
import entities.Cour;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import service.IService;
import service.ServiceCour;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Set;
public class AfficherCourFrontNV {







        @FXML
        private TextField txtTitre;

        @FXML
        private TextField txtDescription;

        @FXML
        private DatePicker datePickerDebut;

        @FXML
        private DatePicker datePickerFin;

        @FXML
        private Button btnSave;





        @FXML
        private TableView<Cour> tableCour;

        @FXML
        private TableColumn<Cour, String> colTitre;

        @FXML
        private TableColumn<Cour, String> colDescription;

        @FXML
        private TableColumn<Cour, LocalDate> colDateDebut;

        @FXML
        private TableColumn<Cour, LocalDate> colDateFin;

        @FXML
        private TableColumn<Cour, Void> colDelete;
    @FXML
    private ListView<?> listviewcour ;
        @FXML
        private TableColumn<Cour, Void> colEdit;
    private Cour cour;

    private final IService<Cour> serviceCour = new ServiceCour();}
/*
        @FXML
        void initialize() throws SQLException {
            Set<Cour> cours = serviceCour.getAll();
            ObservableList<Cour> courList = FXCollections.observableArrayList(cours);

            colTitre.setCellValueFactory(new PropertyValueFactory<>("titre_cours"));
            colDescription.setCellValueFactory(new PropertyValueFactory<>("description_cours"));
            colDateDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut_cours"));
            colDateFin.setCellValueFactory(new PropertyValueFactory<>("dateFin_cours"));
            tableCour.setOnMouseClicked(event -> {
                if (!tableCour.getSelectionModel().isEmpty()) {
                    Cour courSelectionne = tableCour.getSelectionModel().getSelectedItem();
                    // Remplir les champs de texte avec les informations du cours sélectionné
                    txtTitre.setText(courSelectionne.getTitre_cours());
                    txtDescription.setText(courSelectionne.getDescription_cours());
                    datePickerDebut.setValue(courSelectionne.getDateDebut_cours());
                    datePickerFin.setValue(courSelectionne.getDateFin_cours());
                    cour = courSelectionne;
                }

            });
            ShowCours();
        }
   /*

}

}/*


*/
