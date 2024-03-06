package controllers;



import entities.JokeApi;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;

import java.io.IOException;

public class JokeController {
    public Label jokeLabel;

    public void getNewJoke(ActionEvent event) {
        try {
            String jsonResponse = JokeApi.getJoke();
            String joke = JokeApi.parseJoke(jsonResponse);
            jokeLabel.setText(joke);
        } catch (IOException e) {
            e.printStackTrace();
            jokeLabel.setText("Erreur lors de la récupération de la blague.");
        } catch (Exception e) {
            e.printStackTrace();
            jokeLabel.setText("Erreur : " + e.getMessage());
        }
    }
}