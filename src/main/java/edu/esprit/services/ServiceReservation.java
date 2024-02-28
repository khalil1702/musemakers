package edu.esprit.services;

import edu.esprit.entities.Exposition;
import edu.esprit.entities.Reservation;
import edu.esprit.entities.User;
import edu.esprit.utils.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;
import java.sql.*;
import java.util.*;

public class ServiceReservation implements IService<Reservation> {
    Connection cnx= DataSource.getInstance().getCnx();

    private boolean isValidReservation(Reservation r) {
        // Check your constraints here
        if (r.getDateReser() == null || r.getTicketsNumber() <= 0 || r.getExposition() == null || r.getClient() == null) {
            return false;
        }

        return true;
    } //
    @Override
    public void ajouter(Reservation r) {
        if (isValidReservation(r)) {

            String req="INSERT INTO `reservation`(`date_reser`, `tickets_number`,`accessByAdmin`,`id_exposition`,`id_user`) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement ps=cnx.prepareStatement(req);
            ps.setTimestamp(1,r.getDateReser());
            ps.setInt(2,r.getTicketsNumber());
            ps.setInt(3,r.getAccessByAdmin());
            ps.setInt(4, r.getExposition().getId());
            ps.setInt(5,r.getClient().getId_user());

            ps.executeUpdate();
            System.out.println("Reservation added!");



        }catch(SQLException e){
            System.out.println(e.getMessage());

        }}
    }



    @Override
    public void modifier(Reservation r) {
        if (isValidReservation(r)) {

            String req = "UPDATE reservation SET date_reser=?, tickets_number=?, accessByAdmin=?, id_exposition=?, id_user=? WHERE id_reservation=?";
        try{
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setTimestamp(1, r.getDateReser());
            ps.setInt(2,r.getTicketsNumber());
            ps.setInt(3,r.getAccessByAdmin());
            ps.setInt(4, r.getExposition().getId());
            ps.setInt(5,r.getClient().getId_user());
            ps.setInt(6, r.getIdReservation());
            int line_tomodify = ps.executeUpdate();
            if(line_tomodify>0){
                System.out.println("reservation modifié  !");}
            else {
                System.out.println("reservation with ID " +  r.getIdReservation() + " does not exist!");
            }

        }catch (SQLException e) {
            System.out.println(e.getMessage());

        }}


    }

    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM reservation WHERE id_reservation=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            int line_todelete = ps.executeUpdate();

            if (line_todelete > 0) {
                System.out.println("Reservation deleted successfully!");
            } else {
                System.out.println("Reservation with ID " + id + " does not exist!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public Reservation getOneById(int id) {
        String req = "SELECT * FROM reservation WHERE id_reservation=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id); //  the value for the placeholder
            ResultSet res = ps.executeQuery(); // Execution ta3 prepared statement

            if (res.next()) {
                Timestamp datereser = res.getTimestamp("Date_reser");
                int ticketsNumber = res.getInt("tickets_number");
                int idExposition = res.getInt("id_exposition");
                int id_user = res.getInt("id_user");

                ServiceExposition serviceExposition = new ServiceExposition();
                Exposition exposition = serviceExposition.getOneById(idExposition);

                ServicePersonne servicePersonne = new ServicePersonne();
                User user = servicePersonne.getOneById(id_user);

                int accessByAdmin = res.getInt("accessByAdmin");


                System.out.println("reservation mijouda  !");
                return new Reservation(id, datereser, ticketsNumber, accessByAdmin, exposition, user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    public Set<Reservation> getAll()throws SQLException {
        Set<Reservation> reservations=new HashSet<>();
        String req="SELECT * FROM reservation";
        try{
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while(res.next()){
                int id = res.getInt(1);
                Timestamp datereser = res.getTimestamp("Date_reser");
                int ticketsNumber = res.getInt("tickets_number");
                int idExposition = res.getInt("id_exposition");
                int id_user = res.getInt("id_user");

                ServiceExposition serviceExposition = new ServiceExposition();
                Exposition exposition = serviceExposition.getOneById(idExposition);

                ServicePersonne servicePersonne = new ServicePersonne();
                User user = servicePersonne.getOneById(id_user);

                int accessByAdmin = res.getInt("accessByAdmin");

                Reservation reser=new Reservation(id, datereser, ticketsNumber, accessByAdmin, exposition, user);
                reservations.add(reser);
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reservations;
    }


    public Set<Reservation> triparDateAncienne() {

        Set<Reservation> reservations = new TreeSet<>((r1, r2) -> r2.getDateReser().compareTo(r1.getDateReser()));
        String req = "SELECT * FROM reservation WHERE accessByAdmin = 0 ORDER BY Date_reser ASC";  // Order by Date_reser in ascending order
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()) {
                int id = res.getInt(1);
                Timestamp datereser = res.getTimestamp("Date_reser");
                int ticketsNumber = res.getInt("tickets_number");
                int idExposition = res.getInt("id_exposition");
                int id_user = res.getInt("id_user");

                int accessByAdmin = res.getInt("accessByAdmin");

                ServiceExposition serviceExposition = new ServiceExposition();
                Exposition exposition = serviceExposition.getOneById(idExposition);

                ServicePersonne servicePersonne = new ServicePersonne();
                User user = servicePersonne.getOneById(id_user);

                Reservation reser = new Reservation(id, datereser, ticketsNumber, accessByAdmin, exposition, user);
                reservations.add(reser);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reservations;
    }

    // Add this method in your ServiceReservation class
    public Set<Reservation> getEnCoursReservations() {
        Set<Reservation> enCoursReservations = new HashSet<>();
        String req = "SELECT * FROM reservation WHERE accessByAdmin = 0";
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()) {
                int id = res.getInt(1);
                Timestamp datereser = res.getTimestamp("Date_reser");
                int ticketsNumber = res.getInt("tickets_number");
                int accessByAdmin = res.getInt("accessByAdmin");
                int idExposition = res.getInt("id_exposition");
                int id_user = res.getInt("id_user");

                ServiceExposition serviceExposition = new ServiceExposition();
                Exposition exposition = serviceExposition.getOneById(idExposition);

                ServicePersonne servicePersonne = new ServicePersonne();
                User user = servicePersonne.getOneById(id_user);

                Reservation reser = new Reservation(id, datereser, ticketsNumber, accessByAdmin, exposition, user);
                enCoursReservations.add(reser);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return enCoursReservations;
    }
    public void acceptReservation(int reservationId) {
        String updateQuery = "UPDATE reservation SET accessByAdmin = 1 WHERE id_reservation = ?";

        try (PreparedStatement statement = cnx.prepareStatement(updateQuery)) {
            statement.setInt(1, reservationId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Reservation accepted successfully.");
            } else {
                System.out.println("Failed to accept the reservation. Make sure the reservation ID is valid.");
            }
        } catch (SQLException e) {
            System.out.println("Error accepting reservation: " + e.getMessage());
        }
    }
    public void refuserReservation(int reservationId) {
        String updateQuery = "UPDATE reservation SET accessByAdmin = 2 WHERE id_reservation = ?";

        try (PreparedStatement statement = cnx.prepareStatement(updateQuery)) {
            statement.setInt(1, reservationId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Reservation cancelled successfully.");
            } else {
                System.out.println("Failed to cancel the reservation. Make sure the reservation ID is valid.");
            }
        } catch (SQLException e) {
            System.out.println("Error canceling reservation: " + e.getMessage());
        }
    }
    public void annulerReservation(int reservationId) {
        String updateQuery = "UPDATE reservation SET accessByAdmin = 3 WHERE id_reservation = ?";

        try (PreparedStatement statement = cnx.prepareStatement(updateQuery)) {
            statement.setInt(1, reservationId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Reservation cancelled successfully.");
            } else {
                System.out.println("Failed to cancel the reservation. Make sure the reservation ID is valid.");
            }
        } catch (SQLException e) {
            System.out.println("Error canceling reservation: " + e.getMessage());
        }
    }
    public Set<Reservation> getReservationsByUser(int userId) {
        // Utilisez une requête SQL pour récupérer les réservations liées à l'utilisateur avec l'ID userId
        Set<Reservation> userReservations = new HashSet<>();
        String req = "SELECT * FROM reservation WHERE id_user = ?";

        try (PreparedStatement statement = cnx.prepareStatement(req)) {
            statement.setInt(1, userId);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                int id = res.getInt(1);
                Timestamp datereser = res.getTimestamp("Date_reser");
                int ticketsNumber = res.getInt("tickets_number");
                int accessByAdmin = res.getInt("accessByAdmin");
                int idExposition = res.getInt("id_exposition");
                int id_user = res.getInt("id_user");

                ServiceExposition serviceExposition = new ServiceExposition();
                Exposition exposition = serviceExposition.getOneById(idExposition);

                ServicePersonne servicePersonne = new ServicePersonne();
                User user = servicePersonne.getOneById(id_user);

                Reservation reser = new Reservation(id, datereser, ticketsNumber, accessByAdmin, exposition, user);
                userReservations.add(reser);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return userReservations;
    }
    public void modifierNombreTickets(int userId, int reservationId, int newTicketsNumber) {
        // Check if the reservation exists, belongs to the user, and has accessByAdmin equal to 0
        String selectQuery = "SELECT * FROM reservation WHERE id_reservation = ? AND id_user = ? AND accessByAdmin = 0";

        try (PreparedStatement selectStatement = cnx.prepareStatement(selectQuery)) {
            selectStatement.setInt(1, reservationId);
            selectStatement.setInt(2, userId);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                // Reservation exists, belongs to the user, and has accessByAdmin equal to 0, proceed with the update
                String updateQuery = "UPDATE reservation SET tickets_number = ? WHERE id_reservation = ?";

                try (PreparedStatement updateStatement = cnx.prepareStatement(updateQuery)) {
                    updateStatement.setInt(1, newTicketsNumber);
                    updateStatement.setInt(2, reservationId);

                    int rowsAffected = updateStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Number of tickets modified successfully.");
                    } else {
                        System.out.println("Failed to modify the number of tickets. Make sure the reservation ID is valid.");
                    }
                }
            } else {
                System.out.println("You can only modify the number of tickets for your reservations with accessByAdmin = 0.");
            }
        } catch (SQLException e) {
            System.out.println("Error modifying the number of tickets: " + e.getMessage());
        }
    }
    public Set<Reservation> getReservationsByExposition(Exposition exposition) {
        Set<Reservation> expositionReservations = new HashSet<>();
        String req = "SELECT * FROM reservation WHERE id_exposition = ?";

        try (PreparedStatement statement = cnx.prepareStatement(req)) {
            statement.setInt(1, exposition.getId());
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                int id = res.getInt(1);
                Timestamp dateReser = res.getTimestamp("date_reser");
                int ticketsNumber = res.getInt("tickets_number");
                int accessByAdmin = res.getInt("accessByAdmin");
                int id_user = res.getInt("id_user");

                ServicePersonne servicePersonne = new ServicePersonne();
                User user = servicePersonne.getOneById(id_user);

                Reservation reservation = new Reservation(id, dateReser, ticketsNumber, accessByAdmin, exposition, user);
                expositionReservations.add(reservation);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return expositionReservations;
    }
    public Reservation getReservationByUserAndExposition(User user, Exposition exposition) {
        String req = "SELECT * FROM reservation WHERE id_user = ? AND id_exposition = ? AND accessByAdmin IN (0, 1)";

        try (PreparedStatement statement = cnx.prepareStatement(req)) {
            statement.setInt(1, user.getId_user());
            statement.setInt(2, exposition.getId());
            ResultSet res = statement.executeQuery();

            if (res.next()) {
                int id = res.getInt(1);
                Timestamp dateReser = res.getTimestamp("date_reser");
                int ticketsNumber = res.getInt("tickets_number");
                int accessByAdmin = res.getInt("accessByAdmin");

                return new Reservation(id, dateReser, ticketsNumber, accessByAdmin, exposition, user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public void afficherStatistiques() {
        ObservableList<PieChart.Data> accessData = FXCollections.observableArrayList();

        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery("SELECT accessByAdmin, COUNT(*) as count FROM reservation GROUP BY accessByAdmin");

            while (res.next()) {
                int accessByAdmin = res.getInt("accessByAdmin");
                int count = res.getInt("count");

                String accessCategory;
                switch (accessByAdmin) {
                    case 0:
                        accessCategory = "En cours";
                        break;
                        case 1:
                        accessCategory = "Acceptés";
                        break;
                    case 2:
                        accessCategory = "Refusés";
                        break;
                    case 3:
                        accessCategory = "Annulés";
                        break;
                    default:
                        continue;
                }

                accessData.add(new PieChart.Data(accessCategory + " (" + count + ")", count));
            }

            final PieChart accessChart = new PieChart(accessData);
            accessChart.setTitle("Statistique de reservation");

            // Create the dialog for overall statistics
            Dialog<Void> dialog = new Dialog<>();
            dialog.initStyle(StageStyle.UTILITY);
            dialog.getDialogPane().setContent(accessChart);

            // Add a close button for overall statistics
            ButtonType closeButton = new ButtonType("Close");
            dialog.getDialogPane().getButtonTypes().add(closeButton);
            dialog.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }











}











