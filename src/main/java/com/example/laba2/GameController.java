package com.example.laba2;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import model.GameModel;

// Убираем импорт java.awt.event.ActionEvent
// Импортируем правильный ActionEvent из javafx
import javafx.event.ActionEvent;

public class GameController {
    @FXML public Circle ball;
    @FXML public Pane gamePane;
    @FXML public Label scoreLabel;
    @FXML public Button BtStartGame;
    @FXML public Rectangle mob;

    private GameModel model;
    private AnimationTimer gameStatus;
    private double lastMouseX = 0;
    private double lastMouseY = 0;

    @FXML
    private void initialize() {
        model = new GameModel(ball.getRadius());

        // Привязка свойств модели к UI
        scoreLabel.textProperty().bind(
                javafx.beans.binding.Bindings.concat("Счёт: ", model.scoreProperty())
        );

        // Привязка позиции шарика к модели
        ball.centerXProperty().bind(model.ballXProperty());
        ball.centerYProperty().bind(model.ballYProperty());

        // Установка размера поля для модели
        gamePane.widthProperty().addListener((obs, oldVal, newVal) -> {
            model.setPaneSize(gamePane.getWidth(), gamePane.getHeight());
            if (!model.isGameActive()) {
                model.centerBall();
                model.respawnMob();
                updateMobPosition();
            }
        });

        gamePane.heightProperty().addListener((obs, oldVal, newVal) -> {
            model.setPaneSize(gamePane.getWidth(), gamePane.getHeight());
            if (!model.isGameActive()) {
                model.centerBall();
                model.respawnMob();
                updateMobPosition();
            }
        });

        // Обработка движения мыши
        gamePane.setOnMouseMoved(this::handleMouseMove);

        // Обработка двойного клика для паузы
        gamePane.setOnMouseClicked(this::handleDoubleClick);

        // Инициализация позиций
        Platform.runLater(() -> {
            model.setPaneSize(gamePane.getWidth(), gamePane.getHeight());
            model.centerBall();
            model.respawnMob();
            updateMobPosition();
        });
    }

    private void handleMouseMove(MouseEvent event) {
        if (model.isGameActive() && !model.isPaused()) {
            lastMouseX = event.getX();
            lastMouseY = event.getY();
        }
    }

    private void handleDoubleClick(MouseEvent event) {
        // Проверяем, что клик был по пустой области (не по шарику)
        double dx = event.getX() - ball.getCenterX();
        double dy = event.getY() - ball.getCenterY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > ball.getRadius() && event.getClickCount() == 2 && model.isGameActive()) {
            // Двойной клик по пустой области
            model.setPaused(!model.isPaused());
            if (model.isPaused()) {
                showPauseLabel();
            } else {
                hidePauseLabel();
            }
        }
    }

    //    метод для паузы
    private void showPauseLabel() {
        Label pauseLabel = new Label("ПАУЗА");
        pauseLabel.setId("pauseLabel");
        pauseLabel.setStyle("-fx-font-size: 48px; -fx-text-fill: red; -fx-font-weight: bold;");
        pauseLabel.setLayoutX(gamePane.getWidth() / 2 - 100);
        pauseLabel.setLayoutY(gamePane.getHeight() / 2 - 30);
        gamePane.getChildren().add(pauseLabel);
    }

    //метод скрытия лейбл
    private void hidePauseLabel() {
        gamePane.getChildren().removeIf(node -> node.getId() != null && node.getId().equals("pauseLabel"));
    }

    private void updateMobPosition() {
        mob.setX(model.getMobX());
        mob.setY(model.getMobY());
    }

    @FXML
    public void StartGame(ActionEvent actionEvent) {
        if (model.isGameActive()) {
            // Остановка игры
            if (gameStatus != null) {
                gameStatus.stop();
                gameStatus = null;
            }
            model.setGameActive(false);
            model.setPaused(false);
            hidePauseLabel();
            BtStartGame.setText("Старт");
            model.resetScore();

            // Возвращаем шарик в центр
            model.centerBall();
            model.respawnMob();
            updateMobPosition();
        } else {
            // Запуск игры
            model.setGameActive(true);
            model.setPaused(false);
            BtStartGame.setText("Стоп");

            // Запуск игрового цикла
            gameStatus = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (model.isGameActive() && !model.isPaused()) {
                        // Двигаем шарик от мыши
                        model.moveBallAwayFromMouse(lastMouseX, lastMouseY);

                        // Проверяем столкновение с мобом
                        if (model.checkCollisionMob()) {
                            model.increaseScore();
                            model.respawnMob();
                            updateMobPosition();
                        }
                    }
                }
            };
            gameStatus.start();
        }
    }
}