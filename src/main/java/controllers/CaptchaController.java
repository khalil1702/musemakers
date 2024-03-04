 package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class CaptchaController implements Initializable {
    public Label captchaLabel;
    public TextField captchaCode;
    public Label msg;
    public Button refreshCaptcha;
    public Button submit;
    public String Captcha;
    public String GenerateCaptcha() {

        char[] data = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
                'm', 'n', 'o', 'p', 'q', 'r', 's','t', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B',
                'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
                'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

        int max = 36;
        String captcha="";
        int min = 0;
        for(int i =0;i<6;i++) {
            int rand = (int)Math.floor(Math.random()*(max-min+1)+min);
            captcha+=data[rand];
        }
        System.out.println(captcha);
        return captcha;

    }

    @FXML
    void onRefreshCaptcha(ActionEvent e) {
        Captcha = GenerateCaptcha();
        captchaLabel.setText(Captcha);
        System.out.println(Captcha);
        captchaCode.setText("");
    }

    @FXML
    void onSubmit(ActionEvent event) {/*
        if(captchaCode.getText().equals(String.valueOf(Captcha))){
            msg.setText("You have entered correct code!");
            msg.setTextFill(Color.GREEN);
        }else{
            msg.setText("You have entered wrong");
            msg.setTextFill(Color.RED);
            Captcha = GenerateCaptcha();
            captchaLabel.setText(Captcha);
            captchaCode.setText("");
        }*/
        if (captchaCode.getText().equals(String.valueOf(Captcha))) {
            msg.setText("You have entered correct code!");
            msg.setTextFill(Color.GREEN);

            // Fermer la fenêtre modale du captcha
            Stage stage = (Stage) captchaCode.getScene().getWindow(); // Récupérer la fenêtre modale actuelle
            stage.close();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/loginAdmin.fxml"));
                Parent root = loader.load();

                Scene scene = new Scene(root);
                Stage stage1 = new Stage();
                stage1.setScene(scene);
                stage1.show();
            } catch (IOException e) {
                e.printStackTrace();
            }// Fermer la fenêtre modale
        } else {
            msg.setText("You have entered wrong");
            msg.setTextFill(Color.RED);
            Captcha = GenerateCaptcha();
            captchaLabel.setText(Captcha);
            captchaCode.setText("");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ImageView imageView = new ImageView(Objects.requireNonNull(getClass().getResource("/refresh.png")).toExternalForm());
        refreshCaptcha.setGraphic(imageView);
        String Captcha = GenerateCaptcha();
        captchaLabel.setText(Captcha);
    }

}