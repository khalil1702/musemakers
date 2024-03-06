package edu.esprit.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Chat {
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private WebView webView;

    @FXML
    private Button return_id;


    public Chat() {
    }

    @FXML
    void initialize() {
        WebEngine webEngine = this.webView.getEngine();

        String htmlContent = "<html><head><script>\n" +
                "window.embeddedChatbotConfig = {\n" +
                "chatbotId: \"fPg281-z-2IqXFtLP9-Xj\",\n" +
                "domain: \"www.chatbase.co\"\n" +
                "}\n" +
                "</script>\n" +
                "<script\n" +
                "src=\"https://www.chatbase.co/embed.min.js\"\n" +
                "chatbotId=\"fPg281-z-2IqXFtLP9-Xj\"\n" +
                "domain=\"www.chatbase.co\"\n" +
                "defer>\n" +
                "</script></head><body></body></html>";
        webEngine.loadContent(htmlContent);
    }

    public void AfficherOeuvre(ActionEvent event) throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/admin/AfficherOeuvre.fxml"));
        Parent root=loader.load();
//
        return_id.getScene().setRoot(root);
    }


}
