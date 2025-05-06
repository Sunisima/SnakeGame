package org.example.snakegame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

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

    private Timeline timeline;

    @Override
    public void start(Stage stage) {
        gamePane.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);

        // Score text
        scoreText.setFill(Color.WHITE);
        scoreText.setLayoutX(10);
        scoreText.setLayoutY(20);
        gamePane.getChildren().add(scoreText);

        // Length text
        lengthText.setFill(Color.WHITE);
        lengthText.setLayoutX(10);
        lengthText.setLayoutY(40);
        gamePane.getChildren().add(lengthText);

        spawnFood();

        Scene scene = new Scene(gamePane);
        scene.setFill(Color.BLACK);
        setupControls(scene);

        stage.setTitle("Snake Game");
        stage.setScene(scene);
        stage.show();

        startGameLoop();
    }

    private void setupControls(Scene scene) {
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.UP) snake.setDirection(Direction.UP);
            else if (code == KeyCode.DOWN) snake.setDirection(Direction.DOWN);
            else if (code == KeyCode.LEFT) snake.setDirection(Direction.LEFT);
            else if (code == KeyCode.RIGHT) snake.setDirection(Direction.RIGHT);
        });
    }

    private void startGameLoop() {
        timeline = new Timeline(new KeyFrame(Duration.millis(200), e -> {
            snake.move();
            checkFoodCollision();
            render();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void render() {
        // Remove old segments
        gamePane.getChildren().removeAll(snakeNodes);
        snakeNodes.clear();

        // New segments
        for (Segment s : snake.getSegments()) {
            Circle c = new Circle(TILE_SIZE / 2.0, Color.PINK);
            c.setLayoutX(s.getX() * TILE_SIZE + TILE_SIZE / 2.0);
            c.setLayoutY(s.getY() * TILE_SIZE + TILE_SIZE / 2.0);
            snakeNodes.add(c);
        }

        gamePane.getChildren().addAll(snakeNodes);

        // Update score and length
        scoreText.setText("Score: " + score.getScore());
        lengthText.setText("Length: " + snake.getLength());
    }

    private void spawnFood() {
        food = new Food(gamePane);
    }

    private void checkFoodCollision() {
        Segment head = snake.getSegments().get(0);
        double headX = head.getX() * TILE_SIZE;
        double headY = head.getY() * TILE_SIZE;

        double foodX = food.getAppleImage().getLayoutX();
        double foodY = food.getAppleImage().getLayoutY();

        double distance = Math.hypot(headX - foodX, headY - foodY);
        if (distance < TILE_SIZE) {
            snake.grow();
            score.updateScore(1);
            gamePane.getChildren().remove(food.getAppleImage());
            spawnFood();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
