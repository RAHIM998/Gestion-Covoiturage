package org.example.gestioncovoiturage.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AccueilContoller implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    @FXML
    private StackPane stackPane;

    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }
}
