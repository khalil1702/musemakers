package edu.esprit.tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFx extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/admin/ajouterexpo.fxml"));
//       FXMLLoader loader= new FXMLLoader(getClass().getResource("/admin/afficherExpo.fxml"));
//       FXMLLoader loader= new FXMLLoader(getClass().getResource("/client/histoReservationClient.fxml"));
        Parent root=loader.load();
        Scene scene=new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Art Waves");
        stage.show();


    }
    public static void main(String[] args) {
        launch(args);
    }
}
