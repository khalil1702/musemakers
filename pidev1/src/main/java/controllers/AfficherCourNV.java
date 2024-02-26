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
public class AfficherCourNV {




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
    private TableColumn<Cour, Void> colEdit;

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
    private Button btnAdd;

    @FXML
    private Label lblTitreError, lblDescriptionError, lblDateDebutError, lblDateFinError;

    private Cour cour;

    private final IService<Cour> serviceCour = new ServiceCour();

    @FXML
    void initialize() throws SQLException  {
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

        // Ajout de la colonne de suppression
        colDelete.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    deleteButton.setOnAction(event -> {
                        Cour cour = getTableView().getItems().get(getIndex());
                        // Supprimer l'élément
                        serviceCour.supprimer(cour.getId_cours());
                        tableCour.getItems().remove(cour);
                    });

                    setGraphic(deleteButton);
                }
            }
        });

        tableCour.setItems(courList);
    }

    @FXML
    void saveChanges() {
        // Mettre à jour l'objet cour avec les nouvelles valeurs
        cour.setTitre_cours(txtTitre.getText());
        cour.setDescription_cours(txtDescription.getText());
        cour.setDateDebut_cours(datePickerDebut.getValue());
        cour.setDateFin_cours(datePickerFin.getValue());

        // Mettre à jour l'objet cour dans la base de données
        serviceCour.modifier(cour);

        // Rafraîchir le tableau
        tableCour.refresh();
    }}