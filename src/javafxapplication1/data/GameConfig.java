package javafxapplication1.data;

import java.awt.Point;
import javafx.scene.image.Image;
import javafxapplication1.data.players.LocalPlayer;
import javafxapplication1.data.players.Player;

public class GameConfig {
    
    boolean firstMove1;
    public LocalPlayer p1;
    public Player p2;
    int lineLength, boardLength = -1, minXY = Integer.MIN_VALUE, maxXY = Integer.MAX_VALUE;
    public Image imTrue = new Image(getClass().getResource("X.png").toString()), 
            imFalse = new Image(getClass().getResource("O.png").toString());
    
    public static final Boolean[] PLAYER_MARKS = {null, Boolean.TRUE, Boolean.FALSE}; //1st - true, 2nd - false
    /**
     * Метод для подтверждения нахождения точки в ячейке
     * @param x - координаты по х
     * @param y -координаты по у
     * @return правда
     */
    public boolean inBounds(int x, int y) {
        return true;
    }

    public boolean isFirstPlayerFirstTurn() {
        return firstMove1;
    }
    /**
     * Изменение параметров класса {@link GameConfig#firstMove1}
     * @param firstMove1 - ходит первый игрок
     */
    public void setFirstPlayerFirstTurn(boolean firstMove1) {
        this.firstMove1 = firstMove1;
    }
    
    public boolean getBoardLimited() {
        return boardLength > 0;
    }
    /**
     * Задание количества линий
     * @param v - количество линий
     */
    public void setBoardLength(int v) {
        boardLength = v;
        evalBounds();
    }

    public int getBoardLength() {
        return boardLength;
    }
    /**
     * Метод для задания первого хада
     * @return кто ходит первым
     */
    protected Player getFirst() {
        return firstMove1 ? p1 : p2;
    }
    /**
     * Минимальная величина поля
     * @return минимальное число
     */
    public int getMin() {
        return minXY;
    }
    /**
     * Максимальная величина поля
     * @return максимальное число
     */
    public int getMax() {
        return maxXY;
    }

    public int getLineLength() {
        return lineLength;
    }

    public void setLineLength(int lineLength) {
        this.lineLength = lineLength;
    }
    /**
     * Проверка на бесконечное поле, если нет то поле ограничивается
     */
    public void evalBounds() {
        if (boardLength <= 0) {
            minXY = Integer.MIN_VALUE;
            maxXY = Integer.MAX_VALUE;
            return;
        }
        int halfLen = boardLength / 2;
        minXY = -halfLen;
        maxXY = halfLen;
        int rem = boardLength % 2;
        if (rem == 1) return;
        minXY += 1;
    }
        
}
