package entities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class JokeApi {
    public static String getJoke() throws IOException {
        String apiUrl = "https://v2.jokeapi.dev/joke/Any";

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return parseJoke(response.toString());
        } finally {
            connection.disconnect();
        }
    }

    public static String parseJoke(String jsonResponse) {
        System.out.println("Response from API: " + jsonResponse);

        try {
            // Essayons de créer un objet JSON
            JSONObject jsonObject = new JSONObject(new String(jsonResponse.getBytes("ISO-8859-1"), "UTF-8"));

            // Si la création réussit, c'est une réponse JSON
            if (jsonObject.has("joke")) {
                return jsonObject.getString("joke");
            } else if (jsonObject.has("setup") && jsonObject.has("delivery")) {
                // Si la blague est de type "twopart"
                return jsonObject.getString("setup") + " " + jsonObject.getString("delivery");
            } else {
                // Si la structure JSON est inattendue
                return "Structure JSON inattendue.";
            }
        } catch (JSONException e) {
            // Si la création de l'objet JSON échoue, c'est une chaîne de caractères directe
            return jsonResponse;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "Erreur lors de l'analyse de la réponse JSON.";
        }
    }
}