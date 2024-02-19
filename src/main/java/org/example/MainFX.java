package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {  public static void main(String[] args) {
    launch(args);
}
    @Override
    public void start(Stage primaryStage) throws IOException {
       //FXMLLoader loader= new FXMLLoader(getClass().getResource("/AjouterReclamation.fxml"));
     //FXMLLoader loader= new FXMLLoader(getClass().getResource("/AfficherReclamationBack.fxml"));
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/AjouterCommentaireUser.fxml"));
        Parent root=loader.load();
        Scene scene=new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ajout");
        primaryStage.show();
    }


}