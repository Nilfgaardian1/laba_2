package model;

import javafx.beans.property.*;

public class GameModel
{
    private DoubleProperty ballX = new SimpleDoubleProperty();
    private DoubleProperty ballY = new SimpleDoubleProperty();
    private IntegerProperty score = new SimpleIntegerProperty(0);
    private BooleanProperty gameActive = new SimpleBooleanProperty(false);

    private double ballRadius;
    private double panelWidth;
    private double panelHeight;
    private double mobX;
    private double mobY;
    private double mobWidth = 48;
    private double mobHeight = 47;
    private boolean isPaused = false;

    public GameModel(double ballRadius)
    {
        this.ballRadius = ballRadius;
    }

    public DoubleProperty ballXProperty()
    {
        return ballX;
    }

    public DoubleProperty ballYProperty()
    {
        return ballY;
    }

    public IntegerProperty scoreProperty()
    {
        return score;
    }
    public void increaseScore()
    {
        score.set(score.get() + 1);
    }

    public boolean isGameActive()
    {
        return gameActive.get();
    }

    public void setGameActive(boolean active)
    {
        gameActive.set(active);
    }

    public double getMobX()
    {
        return mobX;
    }

    public double getMobY()
    {
        return mobY;
    }

    public boolean isPaused()
    {
        return isPaused;
    }
    public void setPaused(boolean paused)
    {
        isPaused = paused;
    }

    public void setPaneSize(double w, double h)
    {
        panelWidth = w;
        panelHeight = h;
    }

    public void centerBall() {
        if (panelWidth > 0 && panelHeight > 0) {
            ballX.set(panelWidth / 2);
            ballY.set(panelHeight / 2);
        }
    }

    // Метод для перемещения шарика от мыши
    public void moveBallAwayFromMouse(double mouseX, double mouseY) {
        if (!gameActive.get() || isPaused) return;

        // Вектор от мыши к шарику
        double dx = ballX.get() - mouseX;
        double dy = ballY.get() - mouseY;

        // Нормализуем вектор и двигаем шарик в противоположную сторону
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance > 0.1) {
            double moveSpeed = 5; // Скорость движения шарика
            double moveX = (dx / distance) * moveSpeed;
            double moveY = (dy / distance) * moveSpeed;

            double newX = ballX.get() + moveX;
            double newY = ballY.get() + moveY;

            // Проверка границ
            newX = Math.min(Math.max(newX, ballRadius), panelWidth - ballRadius);
            newY = Math.min(Math.max(newY, ballRadius), panelHeight - ballRadius);

            ballX.set(newX);
            ballY.set(newY);
        }
    }

    // Проверка столкновения с мобом
    public boolean checkCollisionMob() {
        double ballCenterX = ballX.get();
        double ballCenterY = ballY.get();

        // Находим ближайшую точку прямоугольника к центру круга
        double closestX = Math.max(mobX, Math.min(ballCenterX, mobX + mobWidth));
        double closestY = Math.max(mobY, Math.min(ballCenterY, mobY + mobHeight));

        double dx = ballCenterX - closestX;
        double dy = ballCenterY - closestY;

        return Math.sqrt(dx * dx + dy * dy) <= ballRadius;
    }

    // Спавн моба в случайном месте
    public void respawnMob() {
        double maxX = panelWidth - mobWidth;
        double maxY = panelHeight - mobHeight;

        if (maxX > 0 && maxY > 0) {
            mobX = Math.random() * maxX;
            mobY = Math.random() * maxY;
        } else {
            mobX = 0;
            mobY = 0;
        }
    }

    public void resetScore()
    {
        score.set(0);
    }
}
