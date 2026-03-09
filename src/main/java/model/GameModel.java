package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Random;

public class GameModel
{
    private IntegerProperty score = new SimpleIntegerProperty(0);

    private boolean isRunning = false;
    private double ballX, ballY;
    private double ballRadius;
    private double panelWidth, panelHeight;

    private Random random = new Random();

    public GameModel(double ballRadius)
    {
        this.ballRadius = ballRadius;
    }


    public int getScore()
    {
        return score.get();
    }

    public IntegerProperty scoreProperty()
    {
        return score;
    }

    public void increaseScore() // увелечения очков
    {
        score.set(score.getValue()+1);
    }

    public boolean isRunning() {  // Для boolean принято называть is...
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public double getBallX()
    {
        return ballX;
    }

    public double getBallY()
    {
        return ballY;
    }

    public void SetBallPosition(double x,double y) //установление позиции мячу в начале новой игры
    {
          this.ballX=x;
          this.ballY=y;
    }

    public void setPaneSize(double w, double h) // размер панели
    {
        panelWidth =w;
        panelHeight =h;
    }

    public void moveBallRandomly() // перемещение шарика по полю случайным образом
    {
        ballX = ballRadius + random.nextDouble() * (panelWidth - 2 * ballRadius);
        ballY = ballRadius + random.nextDouble() * (panelHeight - 2 * ballRadius);
    }

    public void centerBall() // центрирование положения
    {
        ballY = panelHeight / 2;
        ballX = panelWidth / 2;
    }

    public boolean checkHit(double clickX ,double clickY) // проверяем попал ли пользователь
    {
        double dx = clickX - ballX;// Проверка попадания
        double dy = clickY - ballY;
        return Math.sqrt(dx * dx + dy * dy) <=ballRadius;
    }



}
