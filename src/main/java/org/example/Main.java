package org.example;

import entities.Commentaire;
import entities.Reclamation;
import entities.User;
import service.CommentaireService;
import service.ReclamationService;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        ReclamationService service = new ReclamationService();
/*
        // Créer un nouvel utilisateur et une nouvelle réclamation
        User user = new User();DD
        user.setId_user(2);
        // ... initialiser les autres attributs de l'utilisateur ...

        Reclamation r = new Reclamation(user, "description", new Date(), "gg", "statut");

       /* //Tester la méthode 'ajouter'
         service.ajouter(r);
        System.out.println("Reclamation ajoutée: " + r);

        // Tester la méthode 'modifier'
       // Reclamation r3 = new Reclamation(111,user, "description", new Date(), "categorie", "statut");
        r.setDescriRec("nouvelle desc");
        r.setCategorieRec("problemee");
        r.setStatutRec("CHKI");
       /* //service.modifier(r3);
        System.out.println("Reclamation modifiée: " + r3);*/

        // Tester la méthode 'getAll'
        List<Reclamation> all = service.getAll();
        System.out.println("Toutes les réclamations: " + all);
/*
        // Tester la méthode 'getOneById'
        Reclamation r2 = service.getOneById(r.getIdRec());
        System.out.println("Reclamation obtenue par ID: " + r2);

        // Tester la méthode 'supprimer'

        service.supprimer(112);
        System.out.println("Reclamation supprimée: " ); */

        //commentaire
        ReclamationService recService = new ReclamationService();
        CommentaireService comService = new CommentaireService();


        // Créer un nouvel utilisateur
        User user = new User(1, "Nom", "Prenom", "email@example.com", "motdepasse", 1234567890, new Date(), "cartepro", "role");

        // Créer une nouvelle réclamation avec cet utilisateur
        Reclamation rec = new Reclamation(user, "description", new Date(), "categorie", "statut");

        // Ajouter la réclamation à la base de données
        // recService.ajouter(rec);
        //System.out.println("Reclamation ajoutée: " + rec);

        // Récupérer la réclamation avec idRec
        Reclamation rec35 = recService.getOneById(35);
        if (rec35 != null) {
            // Créer un nouveau commentaire avec cette réclamation
            //Commentaire com = new Commentaire(rec35, new Date(), "contenu");

            // Tester la méthode 'ajouter'

            //  comService.ajouter(com);
            // System.out.println("Commentaire ajouté: " + com);

            // Tester la méthode 'modifier'
            Commentaire com22 = new Commentaire(2, rec35, new Date(), "contenu");


            com22.setContenuCom("khalil");
            comService.modifier(com22);
            System.out.println("Commentaire modifié: " + com22);
/*
                // Tester la méthode 'getAll'
                List<Commentaire> all = comService.getAll();
                System.out.println("Tous les commentaires: " + all);

                // Tester la méthode 'getOneById'
                Commentaire com2 = comService.getOneById(5);
                System.out.println("Commentaire obtenu par ID: " + com2);

                // Tester la méthode 'supprimer'
               comService.supprimer(26);
                System.out.println("Commentaire supprimé: " + com);
            } else {
                System.out.println("Aucune réclamation trouvée avec idRec = 35");
            } */

        }
    }}