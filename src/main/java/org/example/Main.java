package org.example;

import entities.Admin;
import entities.Artiste;
import entities.Client;
import entities.User;
import service.ServiceUser;
import utils.DataSource;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

// Press Shift twice to open the Search Everywhere dialog and type show whitespaces,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws ParseException {

        DataSource ds = DataSource.getInstance();
        System.out.println(ds);
        /*
        User client = new Client("dido", "prenom_client", "email_client@", "mdp_client", 1234567890, new Date());
        User admin = new Admin("nom_admin", "prenom_admin", "email_admin@", "mdp_admin");
        User artiste = new Artiste("nom_artiste", "prenom_artiste", "email_artiste@", "mdp_artiste", 1234567890, new Date(), "cartepro");

        ServiceUser serviceUser = new ServiceUser();

        // Définissez la date de naissance
        String dateInString = "31/12/1980";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = formatter.parse(dateInString);

        ((Client) client).setDate_de_naissance(date);
        serviceUser.ajouter(client); // Ajoutez le client à la base de données

        serviceUser.ajouter(admin); // Ajoutez l'admin à la base de données

        ((Artiste) artiste).setDate_de_naissance(date);
        serviceUser.ajouter(artiste);
*/
        //ajouter version finale
        // Appelez la fonction pour ajouter l'utilisateur à la base de données
      /*
        ServiceUser ServiceUser = new ServiceUser();

        // Créer des instances de User
        Admin admin = new Admin("nom_admin", "prenom_admin", "email_admin@", "mdp_admin");
        Client client = new Client("nom_client", "prenom_client", "email_client@", "mdp_client", 1234567890, new Date());
        Artiste artiste = new Artiste("nom_artiste", "prenom_artiste", "email_artiste@", "mdp_artiste", 1234567890, new Date(), "cartepro");
        ServiceUser.ajouter(admin);
        ServiceUser.ajouter(client);
        ServiceUser.ajouter(artiste);
*/

        // Créez une instance de User (ou Client, Artiste, Admin)
        User user = new User(1, "nom", "prenom", "email@example.com", "mdp");
        ServiceUser ServiceUser = new ServiceUser();
        // Modifiez certains attributs de l'utilisateur
        user.setNom_user("nouveau_nom");
        user.setPrenom_user("nouveau_prenom");

        // Appelez la méthode 'modifier'
        ServiceUser.modifier(user);

        /*int id = 7;

        // Appelez la méthode 'supprimer'
         ServiceUser.supprimer(id);*/
/*
        User user = ServiceUser.getOneById(3);
        System.out.println(user);
*/
      /*  Set<User> users = ServiceUser.getAll();

        // Afficher les utilisateurs
        for (User user : users) {
            System.out.println(user);
        }    }*/}}


