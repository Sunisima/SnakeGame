package org.example.snakegame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Random;

/**
 * Represents a food item that randomly appears in the game.
 * Each food has a type (red, blue, green) and triggers different effects when eaten.
 * The food relocates itself after a random time between 4–8 seconds.
 */

public class Food {
    private final ImageView imageView;
    private final double imageWidth = 30;
    private final double imageHeight = 35;
    private final Random random = new Random();
    private final Pane gamePane;
    private Timeline lifetimeTimer;
    private FoodType currentType;

    /**
     * Creates a Food object and spawns it in the given pane.
     * @param gamePane The pane where the food should appear.
     */
    public Food(Pane gamePane) {
        this.gamePane = gamePane;
        imageView = new ImageView();
        imageView.setFitWidth(imageWidth);
        imageView.setFitHeight(imageHeight);
        imageView.setPreserveRatio(false);

        gamePane.getChildren().add(imageView);
        relocate();
    }

    /**
     * Places the food randomly in the pane with a random food type and image.
     * Also sets a timer to auto-relocate after 4–8 seconds.
     */
    public void relocate() {
        // Choose random type
        FoodType[] types = FoodType.values();
        currentType = types[random.nextInt(types.length)];

        // Choose image based on type
        String imagePath = switch (currentType) {
            case redApple -> "/Pictures/red_apple.png";
            case blueApple -> "/Pictures/blue_apple.png";
            case greenApple -> "/Pictures/green_apple.png";
        };

        Image image = new Image(getClass().getResource(imagePath).toExternalForm());
        imageView.setImage(image);

        // Set new random position
        double maxX = gamePane.getPrefWidth() - imageWidth;
        double maxY = gamePane.getPrefHeight() - imageHeight;
        imageView.setLayoutX(random.nextDouble() * maxX);
        imageView.setLayoutY(random.nextDouble() * maxY);

        // Set a timer to relocate the food after 4–8 seconds
        if (lifetimeTimer != null) lifetimeTimer.stop();
        lifetimeTimer = new Timeline(new KeyFrame(Duration.seconds(7), e -> relocate()));
        lifetimeTimer.play();
    }

    /**
     * Returns the ImageView node used to render the food in the scene.
     * This can be used for positioning or collision detection.
     * @return The ImageView for the food.
     */
    public ImageView getAppleImage() {
        return imageView;
    }

    /**
     * Returns the current food type (redApple, blueApple, greenApple).
     * Can be used to determine which effect to apply when eaten.
     * @return The type of this food.
     */
    public FoodType getCurrentType() {
        return currentType;
    }
}
