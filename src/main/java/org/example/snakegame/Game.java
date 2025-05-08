package org.example.snakegame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Main class for the Snake game.
 * Handles game loop, rendering, controls, food and score display
 */
public class Game extends Application {
    private static final int TILE_SIZE = 20;
    private static final int WIDTH = 30;
    private static final int HEIGHT = 20;

    private final Pane gamePane = new Pane();
    private final Snake snake = new Snake();
    private final List<Circle> snakeNodes = new ArrayList<>();
    private Food food;
    private final Score score = new Score();
    private final Text scoreText = new Text();
    private final Text lengthText = new Text();
    private Timeline resetSpeedTimer;
    private Timeline resetHeadTimer;
    private final Text gameOverText = new Text();

    private Timeline timeline;

    //Trigger insane mode when score reaches this value
    private int nextInsaneTrigger = 10;

    @Override
    public void start(Stage stage) {
        gamePane.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);

        // Score text setup
        scoreText.setFill(Color.WHITE);
        scoreText.setLayoutX(10);
        scoreText.setLayoutY(20);
        gamePane.getChildren().add(scoreText);

        // Length text setup
        lengthText.setFill(Color.WHITE);
        lengthText.setLayoutX(10);
        lengthText.setLayoutY(40);
        gamePane.getChildren().add(lengthText);

        // Game over text setup
        gameOverText.setFill(Color.WHITE);
        gameOverText.setFont(new Font(24));
        gameOverText.setLayoutX(50);
        gameOverText.setLayoutY(HEIGHT * TILE_SIZE / 2);
        gameOverText.setVisible(false); // Initially hidden
        gamePane.getChildren().add(gameOverText);

        spawnFood();

        Scene scene = new Scene(gamePane);
        scene.setFill(Color.BLACK);
        setupControls(scene);

        stage.setTitle("Snake Game");
        stage.setScene(scene);
        stage.show();

        startGameLoop();
    }

    /**
     * Sets up keyboard controls to move the snake.
     * Arrow keys are used. 90° turning enforced by Snake class.
     */
    private void setupControls(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (!gameOverText.isVisible()) {
                KeyCode code = event.getCode();
                if (code == KeyCode.UP) snake.setDirection(Direction.UP);
                else if (code == KeyCode.DOWN) snake.setDirection(Direction.DOWN);
                else if (code == KeyCode.LEFT) snake.setDirection(Direction.LEFT);
                else if (code == KeyCode.RIGHT) snake.setDirection(Direction.RIGHT);
            }
        });
    }

    /**
     * Starts the main game loop using a Timeline.
     */
    private void startGameLoop() {
        timeline = new Timeline(new KeyFrame(Duration.millis(snake.getSpeed()), e -> {
            snake.move();
            if (snake.checkEdgeCollision(WIDTH, HEIGHT) || snake.checkCollision()) {
                gameOver();
                return;
            }
            checkFoodCollision();
            render();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Renders the snake, score and length on screen.
     */

    private void render() {
        // Remove old segments
        gamePane.getChildren().removeAll(snakeNodes);
        snakeNodes.clear();

        // Draw each segment of the snake
        for (int i = 0; i < snake.getSegments().size(); i++) {
            Segment s = snake.getSegments().get(i);

            // If heas is enlarged, increase size
            double radius = (i == 0 && snake.isHeadEnlarged()) ? TILE_SIZE / 1.2 : TILE_SIZE / 2.0;

            Circle c = new Circle(radius, Color.PINK);
            c.setLayoutX(s.getX() * TILE_SIZE + TILE_SIZE / 2.0);
            c.setLayoutY(s.getY() * TILE_SIZE + TILE_SIZE / 2.0);

            snakeNodes.add(c);
        }

        gamePane.getChildren().addAll(snakeNodes);

        // Update display score and length
        scoreText.setText("Score: " + score.getScore());
        lengthText.setText("Length: " + snake.getLength());
    }

    /**
     * Spawns a new food item at a random location.
     */
    private void spawnFood() {
        food = new Food(gamePane);
    }


    /**
     * Checks if the snake's head has collided with the food.
     * If a collision is detected:
     * - redApple: no effect.
     * - blueApple: increases snake speed for 3 seconds, then resets.
     * - greenApple: enlarges snake head for 4 seconds, then resets.
     * After any effect, the snake grows, score increases,
     * and a new food item is spawned. If the score crosses a multiple of 10,
     * Insane Mode is triggered.
     */
    private void checkFoodCollision() {
        Segment head = snake.getSegments().get(0);
        double headX = head.getX() * TILE_SIZE;
        double headY = head.getY() * TILE_SIZE;

        double foodX = food.getAppleImage().getLayoutX();
        double foodY = food.getAppleImage().getLayoutY();

        double distance = Math.hypot(headX - foodX, headY - foodY);
        if (distance < TILE_SIZE) {
            // Apply effect based on apple type
            switch (food.getCurrentType()) {
                case redApple -> { //No effect
                }
                case blueApple -> {
                    snake.setSpeed(100);
                    restartGameLoop();

                    if (resetSpeedTimer != null) resetSpeedTimer.stop();
                    resetSpeedTimer = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
                        snake.setSpeed(250);
                        restartGameLoop();
                    }));
                    resetSpeedTimer.play();
                }
                case greenApple -> {
                    snake.enlargeHead();

                    if (resetHeadTimer != null) resetHeadTimer.stop();
                    resetHeadTimer = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
                        snake.resetHeadSize();
                    }));
                    resetHeadTimer.play();
                }
            }

            snake.grow();
            score.updateScore(1);
            gamePane.getChildren().remove(food.getAppleImage());
            spawnFood();

            if (score.getScore() >= nextInsaneTrigger) {
                triggerInsaneMode();
                // Next trigger at +10 score
                nextInsaneTrigger += 10;
            }
        }
    }

    /**
     * Triggers Insane Mode:
     * - Rotates the gamePane randomly (90°, 180°, 270°)
     * - Blinks background in random colors for 2 seconds
     */
    private void triggerInsaneMode() {
        Random random = new Random();
        int rotation = (random.nextInt(3) + 1) * 90;
        gamePane.setRotate((gamePane.getRotate() + rotation) % 360);

        Timeline blinkTimeline = new Timeline();
        for (int i = 0; i < 10; i++) {
            blinkTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(i * 200), e -> {
                Color color = Color.color(Math.random(), Math.random(), Math.random());
                gamePane.setStyle("-fx-background-color: rgb(" + (int)(color.getRed()*255) + "," + (int)(color.getGreen()*255) + "," + (int)(color.getBlue()*255) + ");");
            }));
        }
        // Reset background color after blinking
        blinkTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000), e -> {
            gamePane.setStyle("");
        }));
        blinkTimeline.play();
    }

    /**
     * Restarts the game loop with the current snake speed.
     * Necessary when the snake speed changes temporarily (e.g., after eating blue food).
     */
    private void restartGameLoop() {
        // Stop the current game loop before restarting
        if (timeline != null) {
            timeline.stop();
        }

        // Create a new game loop with the updated speed
        timeline = new Timeline(new KeyFrame(Duration.millis(snake.getSpeed()), e -> {
            snake.move();
            checkFoodCollision();
            if (snake.checkEdgeCollision(WIDTH, HEIGHT) || snake.checkCollision()) {
                gameOver();
                return;
            }
            render();
        }));

        // Restart the game loop
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Stops the game and displays a text
     */
    private void gameOver() {
        timeline.stop();
        gameOverText.setText("Game Over! Press any key to restart.");
        gameOverText.setVisible(true);

        // Puts the focus on the game pane
        gamePane.requestFocus();
        gamePane.setOnKeyPressed(null);

        // Set up an event handler to restart the game pane
        gamePane.setOnKeyPressed(event -> {
            restartGame();
            gamePane.setOnKeyPressed(null);
        });
    }

    /**
     * Method to reset the game pane back to the original state
     */
    private void restartGame() {
        // Reset game state
        snake.getSegments().clear();
        snake.getSegments().add(new Segment(5, 5));
        snake.setDirection(Direction.RIGHT);
        score.resetScore();
        scoreText.setText("Score: 0");
        lengthText.setText("Length: 1");

        // Hide game over text
        gameOverText.setVisible(false);

        // Remove existing food
        gamePane.getChildren().remove(food.getAppleImage());

        // Spawn new food
        spawnFood();

        // Resets the rotation of the game board
        gamePane.setRotate(0);

        // Restart game loop
        startGameLoop();

        // Re-enable game controls
        setupControls(gamePane.getScene());
    }


    public static void main(String[] args) {
        launch();
    }
}
