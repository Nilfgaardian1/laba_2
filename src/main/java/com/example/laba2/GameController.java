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

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameController {
    public Circle ball;
    public Pane gamePane;
    public Label scoreLabel;
    public Button BtStartGame;

    private boolean isStarted = false;

    Timer timer;
    Random random = new Random();
    private IntegerProperty score = new SimpleIntegerProperty();

    @FXML
    private void initialize() {

        scoreLabel.textProperty().bind(
                javafx.beans.binding.Bindings.concat("Счет: ", score)
        );

        // При изменении размера центрируем шарик
        gamePane.widthProperty().addListener((obs, oldVal, newVal) -> {
            // Центрируем ТОЛЬКО если игра не запущена
            if (!isStarted) {
                ball.setCenterX(gamePane.getWidth() / 2);
            }
        });

        gamePane.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (!isStarted) {
                ball.setCenterY(gamePane.getHeight() / 2);
            }
        });

        // Начальная позиция
        Platform.runLater(() -> {
            if (gamePane.getWidth() > 0 && gamePane.getHeight() > 0) {
                ball.setCenterX(gamePane.getWidth() / 2);
                ball.setCenterY(gamePane.getHeight() / 2);
            }
        });
    }

    @FXML
    //обработка кликов мыши
    public void handleMouseClick(MouseEvent event)
    {
        if(!isStarted){return;} // если игры нет, то выходим из метода

        // Проверка попадания
        double dx = event.getX() - ball.getCenterX();
        double dy = event.getY() - ball.getCenterY();
        double distance = Math.sqrt(dx * dx + dy * dy);

//        if (distance <= ball.getRadius()) {
//            // При попадании Перемещаем шарик
//            moveBallToRandomPosition();
//        }

        if(distance <= ball.getRadius()){
            score.set(score.get() + 1);
        }

    }

    //перемещение шарика в случайную позицию
    private void moveBallToRandomPosition()
    {
        //if(gamePane.getWidth() <= 0 || gamePane.getHeight()<=0) return; //если размеры окна равны нулю или меньше возвращается функция

        double newX = ball.getRadius() +
                random.nextDouble() * (gamePane.getWidth() - 2 * ball.getRadius());
        double newY = ball.getRadius() +
                random.nextDouble() * (gamePane.getHeight() - 2 * ball.getRadius());

        ball.setCenterX(newX);
        ball.setCenterY(newY);
    }

    @FXML
    public void StartGame(ActionEvent actionEvent)
    {
        if(isStarted) // проверяем запущена ли игра и
            // если она уже была начата тогда мы останавливаем ее
        {
            timer.cancel();
            timer = null;
            isStarted = false;
            BtStartGame.setText("Старт");

            ball.setCenterX(gamePane.getWidth() / 2);
            ball.setCenterY(gamePane.getHeight() / 2);
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
                        moveBallToRandomPosition();
                    });
                }
            }, 0 ,500);

            isStarted = true; //флаг, что игра работает
            BtStartGame.setText(("Стоп"));
        }



    }
}