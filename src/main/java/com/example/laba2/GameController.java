package com.example.laba2;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import model.GameModel;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameController {
    public Circle ball;
    public Pane gamePane;
    public Label scoreLabel;
    public Button BtStartGame;


    Timer timer;
    private GameModel model;
    private boolean isStarted = false;

    @FXML
    private void initialize() {

        model = new GameModel(ball.getRadius());

        scoreLabel.textProperty().bind( // привязка счета к текстовому полю
                javafx.beans.binding.Bindings.concat("Счет: ", model.scoreProperty())
        );

        // установление размера поля для модели (для проверки столкновения)

        gamePane.widthProperty().addListener((obs, oldVal, newVal) -> {


            model.setPaneSize(gamePane.getWidth(), gamePane.getHeight());
            if (!isStarted)
            {
                model.centerBall();
                ball.setCenterX(model.getBallX());
                ball.setCenterY(model.getBallY());
            }
        });

        gamePane.heightProperty().addListener((obs, oldVal, newVal) -> {

            model.setPaneSize(gamePane.getWidth(), gamePane.getHeight());
            if (!isStarted)
            {
                model.centerBall();
                ball.setCenterX(model.getBallX());
                ball.setCenterY(model.getBallY());
            }
        });

        // Начальная позиция
        Platform.runLater(() ->
        {
            model.setPaneSize(gamePane.getWidth(), gamePane.getHeight());
            model.centerBall();
            ball.setCenterX(model.getBallX());
            ball.setCenterY(model.getBallY());

        });
    }

    @FXML
    //обработка кликов мыши
    public void handleMouseClick(MouseEvent event)
    {
        if(!isStarted){return;} // если игры нет, то выходим из метода

        if(model.checkHit(event.getX(),event.getY()))
        {
            model.increaseScore();
        }

        // то что мы сократили написав класс
//        double dx = event.getX() - ball.getCenterX();// Проверка попадания
//        double dy = event.getY() - ball.getCenterY();
//        double distance = Math.sqrt(dx * dx + dy * dy);
//
//        if(distance <= ball.getRadius()) //увеличиваем счетчик при нажатии на мяч
//        {
//            score.set(score.get() + 1);
//        }

    }

    @FXML
    public void StartGame(ActionEvent actionEvent)
    {
        if(isStarted)//стоп игры
        {
            timer.cancel();
            timer = null;
            isStarted = false;
            BtStartGame.setText("Старт");

            model.setRunning(false);

            model.centerBall();
            ball.setCenterX(model.getBallX());
            ball.setCenterY(model.getBallY());
        }
        else //Запуск игры
        {
            timer = new Timer();
            timer.schedule(new TimerTask() //запускает таймер
            {
                @Override
                public void run() // метод запускается в зависимости от времени
                {
                    Platform.runLater(()->{
                        model.moveBallRandomly();
                        ball.setCenterX(model.getBallX());
                        ball.setCenterY(model.getBallY());
                    });
                }
            }, 0 ,500);

            isStarted = true; //флаг, что игра работает
            model.setRunning(true);
            BtStartGame.setText(("Стоп"));
        }

    }
}