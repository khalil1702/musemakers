package org.example;

import controllers.InscriptionClient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DataSource;

import java.io.IOException;

public class MainFx extends Application {

    public static void main(String[] args) {
        DataSource ds = DataSource.getInstance();
        System.out.println(ds);

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
          // FXMLLoader loader = new FXMLLoader(getClass().getResource("/InscriptionClient.fxml"));
     //     FXMLLoader loader = new FXMLLoader(getClass().getResource("/InscriptionArtiste.fxml"));
  //   FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherClientNV.fxml"));
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginAdmin.fxml"));
 // FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModificationCompteArtiste.fxml"));
          //  FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierPassword.fxml"));

            //   FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherArtisteNV.fxml"));
                // FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherArtiste.fxml"));

            //     FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginClient.fxml"));

            Parent root = loader.load(); // Chargez le fichier FXML et le contrôleur

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}