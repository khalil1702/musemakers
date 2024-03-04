package controllers;

import entities.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import service.ServiceUser;
import utils.DataSource;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class AfficherClientNV {

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
    @FXML
    private Button ajouterid;
    @FXML
    private Button logoutid;
    @FXML
    private Button compteid;
    @FXML
    private Button retourid ;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/musemakers";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // Mettez votre mot de passe de base de données ici
    Connection cnx = DataSource.getInstance().getCnx();

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
        logoutid.setOnAction(event -> {
            try {
                // Mettre à jour le statut de tous les utilisateurs
                updateAllUserStatusToNull();

                // Charger l'interface loginAdmin.fxml
                Parent root = FXMLLoader.load(getClass().getResource("/LoginAdmin.fxml"));
                Stage stage = (Stage) logoutid.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        retourid.setOnAction(event -> {
            try {
                // Charger l'interface AfficherClientNV.fxml
                Parent root = FXMLLoader.load(getClass().getResource("/AccueilAdmin.fxml"));
                Stage stage = (Stage) retourid.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        compteid.setOnAction(event -> {
            try {
                // Charger l'interface modifierpassword.fxml
                Parent root = FXMLLoader.load(getClass().getResource("/ModifierPassword.fxml"));
                Stage stage = (Stage) compteid.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //delete
        supprimerid.setOnAction(e -> handleDelete());
        modifierid.setOnAction(e -> handleUpdate());
        ajouterid.setOnAction(e -> handleAdd());


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
    private void handleAdd() {
        // Obtenez les informations du formulaire
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String mail = mailField.getText();
        String passe = passeField.getText();
        String tel = telField.getText();
        LocalDate date = dateField.getValue();

        // Vérifiez le mot de passe


        // Vérifiez si le mot de passe existe déjà
        ServiceUser serviceUser = new ServiceUser();
        if (passwordExists(passe)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur d'inscription");
            alert.setHeaderText(null);
            alert.setContentText("Le mot de passe existe déjà.");
            alert.showAndWait();
            return;
        }

        // Créez un nouvel utilisateur en fonction des informations du formulaire
        Client newUser = new Client();
        newUser.setNom_user(nom);
        newUser.setPrenom_user(prenom);
        newUser.setEmail(mail);
        newUser.setMdp(passe);
        newUser.setNum_tel(Integer.parseInt(tel)); // Assurez-vous que le numéro de téléphone est un nombre entier
        newUser.setDate_de_naissance(java.sql.Date.valueOf(date));

        // Ajoutez le nouvel utilisateur à l'aide de votre service
        serviceUser.ajouter(newUser);

        // Ajoutez le nouvel utilisateur à la ListView
        listViewid.getItems().add(newUser);
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
    public boolean passwordExists(String passe) {
        String sql = "SELECT * FROM user WHERE mdp = ?";

        try (Connection cnx = DriverManager.getConnection("jdbc:mysql://localhost:3306/musemakers", "root", "");
             PreparedStatement pstmt = cnx.prepareStatement(sql)) {

            pstmt.setString(1, passe);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }
    public void updateAllUserStatusToNull() {
        String req = "UPDATE user SET status = NULL";
        Connection conn = null;
        try {
            // Chargez le pilote JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Créez la connexion
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            PreparedStatement pst = conn.prepareStatement(req);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Status updated successfully for all users");
            } else {
                System.out.println("No users found in the database");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error updating status for all users");
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
