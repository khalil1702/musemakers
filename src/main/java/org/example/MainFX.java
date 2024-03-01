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

    //FXMLLoader loader= new FXMLLoader(getClass().getResource("/AccueilUser.fxml"));
        //FXMLLoader loader= new FXMLLoader(getClass().getResource("/AccueilAdmin.fxml"));
      //  FXMLLoader loader= new FXMLLoader(getClass().getResource("/ChatBot.fxml"));
          //FXMLLoader loader= new FXMLLoader(getClass().getResource("/StatRec.fxml"));
         FXMLLoader loader= new FXMLLoader(getClass().getResource("/StatCom.fxml"));
        Parent root=loader.load();
        Scene scene=new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ajout");
        primaryStage.show();
    }


}