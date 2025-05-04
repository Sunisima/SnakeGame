package org.example.snakegame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.Random;

public class Food {
    private final ImageView imageView;
    private final double imageWidth = 30;
    private final double imageHeight = 30;
    private final Random random = new Random();

    public Food(Pane gamePane) {
        Image appleImage = new Image(getClass().getResource("/Pictures/pixel_apple.png").toExternalForm());
        imageView = new ImageView(appleImage);

        // Indstil størrelse på billedet
        imageView.setFitWidth(imageWidth);
        imageView.setFitHeight(imageHeight);
        imageView.setPreserveRatio(true);

        // Tilføj billedet til pane
        gamePane.getChildren().add(imageView);

        // Placér æblet tilfældigt
        relocate(gamePane);
    }

    public void relocate(Pane gamePane) {
        double maxX = gamePane.getPrefWidth() - imageWidth;
        double maxY = gamePane.getPrefHeight() - imageHeight;

        double x = random.nextDouble() * maxX;
        double y = random.nextDouble() * maxY;

        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
    }


    public ImageView getNode() {
        return imageView;
    }
}
