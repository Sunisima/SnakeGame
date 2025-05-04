package org.example.snakegame;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private Pane gamePane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Food food = new Food(gamePane);
    }
}
