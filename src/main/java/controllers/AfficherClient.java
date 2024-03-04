package controllers;

import entities.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import service.ServiceUser;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

public class AfficherClient {

    @FXML
    private TableView<Client> tableid;

    @FXML
    private TableColumn<Client, String> nomid;

    @FXML
    private TableColumn<Client, String> prenomid;

    @FXML
    private TableColumn<Client, String> mailid;

    @FXML
    private TableColumn<Client, String> passeid;

    @FXML
    private TableColumn<Client, Integer> telid;

    @FXML
    private TableColumn<Client, Date> dateid;
    @FXML
    private Button supprimerid;
    @FXML
    private Button modifierid;

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField mailField;

    @FXML
    private TextField passeField;

    @FXML
    private TextField telField;

    @FXML
    private DatePicker dateField;

    @FXML
    private ListView<Client> listViewid;

    public void initialize() {
        // Obtenez tous les clients

        Set<Client> clients = getAll();

        // Ajoutez les clients à la ListView
        listViewid.getItems().addAll(clients);

        // Ajoutez un ChangeListener à la sélection du ListView
        listViewid.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Client selectedClient = listViewid.getSelectionModel().getSelectedItem();
                nomField.setText(selectedClient.getNom_user());
                prenomField.setText(selectedClient.getPrenom_user());
                mailField.setText(selectedClient.getEmail());
                passeField.setText(selectedClient.getMdp());
                telField.setText(String.valueOf(selectedClient.getNum_tel()));
                java.sql.Date sqlDate = (Date) selectedClient.getDate_de_naissance();
                java.time.LocalDate localDate = sqlDate.toLocalDate();
                dateField.setValue(localDate);
            }
        });

        //delete
        supprimerid.setOnAction(e -> handleDelete());
        modifierid.setOnAction(e -> handleUpdate());
    }

    public Set<Client> getAll() {
        Set<Client> clients = new HashSet<>();
        String sql = "SELECT * FROM user WHERE role = 'Client'";

        try (Connection cnx = DriverManager.getConnection("jdbc:mysql://localhost:3306/musemakers", "root", "");
             PreparedStatement pstmt = cnx.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Client user = new Client();
                user.setId_user(rs.getInt("id_user")); // Ajoutez cette ligne

                user.setNom_user(rs.getString("nom_user"));
                user.setPrenom_user(rs.getString("prenom_user"));
                user.setEmail(rs.getString("email"));
                user.setMdp(rs.getString("mdp"));
                user.setNum_tel(rs.getInt("num_tel"));
                user.setDate_de_naissance(rs.getDate("date_de_naissance"));

                clients.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return clients;
    }
    private void handleDelete() {
        // Obtenez le client sélectionné dans la ListView
        Client selectedClient = listViewid.getSelectionModel().getSelectedItem();

        if (selectedClient != null) {
            // Supprimez le client sélectionné à l'aide de votre service
            ServiceUser serviceUser = new ServiceUser();
            serviceUser.supprimer(selectedClient.getId_user());

            // Supprimez le client de la ListView
            listViewid.getItems().remove(selectedClient);
        } else {
            System.out.println("Aucun client n'est sélectionné.");
        }
    }
    private void handleUpdate() {
        // Obtenez le client sélectionné dans la ListView
        Client selectedClient = listViewid.getSelectionModel().getSelectedItem();

        if (selectedClient != null) {
            // Mettez à jour le client avec les informations des champs de texte
            selectedClient.setNom_user(nomField.getText());
            selectedClient.setPrenom_user(prenomField.getText());
            selectedClient.setEmail(mailField.getText());
            selectedClient.setMdp(passeField.getText());
            selectedClient.setNum_tel(Integer.parseInt(telField.getText()));
            selectedClient.setDate_de_naissance(java.sql.Date.valueOf(dateField.getValue()));
            System.out.println("ID du client sélectionné : " + selectedClient.getId_user());

            // Mettez à jour le client dans la base de données à l'aide de votre service
            ServiceUser serviceUser = new ServiceUser();
            if (selectedClient.getId_user() != -1) {
                serviceUser.modifier(selectedClient);
            } else {
                System.out.println("L'utilisateur sélectionné n'a pas d'ID.");
            }

            // Mettez à jour la ListView
            listViewid.refresh();
        } else {
            System.out.println("Aucun client n'est sélectionné.");
        }
    }

}
