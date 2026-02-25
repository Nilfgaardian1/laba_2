package com.example.laba2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.Random;

public class GameController {
    public Circle ball;
    public Pane gamePane;

    @FXML
    private void initialize() {
        // При изменении размера центрируем шарик
        gamePane.widthProperty().addListener((obs, oldVal, newVal) -> {
            ball.setCenterX(gamePane.getWidth() / 2);
        });

        gamePane.heightProperty().addListener((obs, oldVal, newVal) -> {
            ball.setCenterY(gamePane.getHeight() / 2);
        });

        // Начальная позиция
        javafx.application.Platform.runLater(() -> {
            ball.setCenterX(gamePane.getWidth() / 2);
            ball.setCenterY(gamePane.getHeight() / 2);
        });
    }


    public void handleMouseClick(MouseEvent event)
    {
        // Ппроверка попадания
        double dx = event.getX() - ball.getCenterX();
        double dy = event.getY() - ball.getCenterY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance <= ball.getRadius()) {
            // При попадании Перемещаем шарик
            moveBallToRandomPosition();
        }
    }

    private void moveBallToRandomPosition()
    {
        Random random = new Random();

        double newX = ball.getRadius() +
                random.nextDouble() * (gamePane.getWidth() - 2 * ball.getRadius());
        double newY = ball.getRadius() +
                random.nextDouble() * (gamePane.getHeight() - 2 * ball.getRadius());

        ball.setCenterX(newX);
        ball.setCenterY(newY);
    }


}