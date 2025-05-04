package org.example.snakegame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.util.Random;

/**
 * The food for the snake to eat.
 */
public class Food {
    private final ImageView imageView;
    private final double imageWidth = 30;
    private final double imageHeight = 35;
    private final Random random = new Random();

    /**
     * Creates a Food object (an apple) and adds it to the gamePane.
     * The image of the apple will be placed randomly within the pane.
     * @param gamePane The Pane where the apple appears
     */
    public Food(Pane gamePane) {
        //Loads and creates the image of an apple to be shown in the gamePane
        Image appleImage = new Image(getClass().getResource("/Pictures/pixel_apple.png").toExternalForm());
        imageView = new ImageView(appleImage);

        // Sets the size of the Food (apple)
        imageView.setFitWidth(imageWidth);
        imageView.setFitHeight(imageHeight);
        imageView.setPreserveRatio(true);

        // Adds the image of the apple to Pane
        gamePane.getChildren().add(imageView);
        // Places the apples randomly each time the game starts anew.
        relocate(gamePane);
    }

    /**
     * Gets the size of the Pane and get a random location to put the image of the apple.
     * @param gamePane The Pane where the apple appears
     */
    public void relocate(Pane gamePane) {
        double maxX = gamePane.getPrefWidth() - imageWidth;
        double maxY = gamePane.getPrefHeight() - imageHeight;

        double x = random.nextDouble() * maxX;
        double y = random.nextDouble() * maxY;

        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
    }

    /**
     * Returns the imageView representing the apple in the scene.
     * Can be used for other methods to access the apples position or check for collision.
     * @return The ImageView displaying the appleImage
     */
    public ImageView getAppleImage() {
        return imageView;
    }
}
