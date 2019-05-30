package javafxapplication1;

import java.awt.Point;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafxapplication1.data.GameConfig;
import javafxapplication1.data.GameState;
import javafxapplication1.data.GomokuBoard;
import javafxapplication1.data.players.LocalPlayer;
import javafxapplication1.data.players.Player;
/**
 * Класс контроллер.
 * @author Pepsishko
 */
public class GameBoardCtrl implements Initializable {

    @FXML Canvas canvas;
    @FXML Slider observe;
    @FXML Label labPos;
    
    private int boardSize, cellWidth, cellHeight, offX=-7, offY=-7;


    private Repainter2 rep2 = new Repainter2();
    /**
     * Метод для перерисовки поля со сдвигом вправо
     * @param actionEvent - событие нажатия на кнопку
     */
    public void vpravo(ActionEvent actionEvent) {
        offX+=1;
        refreshInts();
        repaint();
    }
    /**
     * Метод для перерисовки поля со сдвигом влево
     * @param actionEvent - событие нажатия на кнопку
     */
    public void left(ActionEvent actionEvent) {
        offX-=1;
        refreshInts();
        repaint();
    }
    /**
     * Метод для перерисовки поля со сдвигом вниз
     * @param actionEvent - событие нажатия на кнопку
     */
    public void down(ActionEvent actionEvent) {
        offY+=1;
        refreshInts();
        repaint();
    }
    /**
     * Метод для перерисовки поля со сдвигом вверх
     * @param actionEvent - событие нажатия на кнопку
     */
    public void up(ActionEvent actionEvent) {
        offY-=1;
        refreshInts();
        repaint();
    }
    /**
     * Класс наследующий интерфейс
     */
    class Repainter2 implements javafxapplication1.Utils.Run {
        /**
         * Метод запускающий перерисовку поля в новом потоке
         */
        @Override
        public void run() {
            Platform.runLater(new Runnable() {
                @Override public void run() {repaint();}
            });                        
        }
    
    }
    /**
     * Метод перерисовки поля
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (GameState.singleton != null) {
            GameState.singleton.repainter = rep2;
        } else {
            System.err.println("Cannot setup GameState link.");
        }
        refreshInts();
        repaint();
    }
    /**
     * Обработчик события кнопки для изменения количества отображаемых линий на поле
     * @param event - само событие
     */
    @FXML private void vpApply(ActionEvent event) {
        refreshInts();
        repaint();
    }
    /**
     * Метод изменяющий параметры класса которые влияют на отображение игрового поля
     */
    private void refreshInts() {
        boardSize = (int)observe.getValue();
        cellWidth = (int) canvas.getWidth() / boardSize;
        cellHeight = (int) canvas.getHeight() / boardSize;
    }
    
    boolean TEST = false;
    /**
     * Обработчик события клика по полю
     * @param event - само событие
     */
    @FXML private void canvasClick(MouseEvent event) {
        Point coord = evalClickPnt((int)event.getX(),(int)event.getY());
        //synchronized(GameState.singleton) {
            GameConfig cfg = GameState.singleton.getConfig();
            GomokuBoard b = GameState.singleton.getBoard();
            Boolean valAt = b.get(coord);
            //Test.showPoint(coord, "Coord");        
            if (!b.inBounds(coord)) {
                System.out.println("Out of bounds.");
                return;
            }
            if (valAt != null) {
                System.out.println("Busy cell "+coord.x+","+coord.y);
                return;
            }
            Player now = GameState.singleton.getCurrentPlayer();
            if (now instanceof LocalPlayer) {
                LocalPlayer now2 = (LocalPlayer)now;
                now2.setReady(coord);
            }
    }
    /**
     * Определение координат нажатой ячейки
     * @param cx - координаты по х
     * @param cy - координаты по у
     */
    private Point evalClickPnt(int cx, int cy) {
        if (cellWidth==0 || cellHeight==0) this.refreshInts();
        return new Point(cx/cellWidth + offX, cy/cellHeight + offY);        
    }
    /**
     * Метод для создания точек для отрисовки линий поля для игры
     * @param cx - координаты по x
     * @param cy - координаты по у
     */
    private Point getCellLeftTop(int cx, int cy) {
        Point p = new Point(cx*cellWidth, cy*cellHeight);
        return p;
    }
    /**
     * Метод перерисовки поля
     */
    void repaint() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        drawCells(g);
    }
    /**
     * Параметры перерисовки поля
     * @param g - игровое поле
     */
    void drawCells(GraphicsContext g) { //отрисовка поля
        GameConfig conf = GameState.singleton.getConfig();
        GomokuBoard bo = GameState.singleton.getBoard();
        g.setStroke(Color.BLACK);
        g.setLineWidth(2.0);
        for (int x=0; x<boardSize; x++) for (int y=0; y<boardSize; y++) {
            Point k = new Point(x+offX, y+offY);
            Boolean v = bo.get(k);
            Point lt = getCellLeftTop(x, y);
            if (v == null) {                
                if (bo.inBounds(k.x, k.y)) {
                    g.setFill(Color.LIGHTGREY);
                } else {
                    g.setFill(Color.DARKGREY);
                }                
                g.fillRect(lt.x, lt.y, cellWidth, cellHeight);
                g.strokeRect(lt.x, lt.y, cellWidth, cellHeight);
            } else {
                Image im = v.booleanValue() ? conf.imTrue : conf.imFalse;
                
                g.drawImage(im, lt.x, lt.y, cellWidth, cellHeight);
            }
        }
    }
    
    private void dump() {
        System.out.println("Canvas size: "+canvas.getWidth()+","+canvas.getHeight());
        GomokuBoard b =  GameState.singleton.getBoard();
        for (Point k : b.keySet()) {
            Boolean v = b.get(k);
            if (v==null) {
                System.out.println("null at "+k.x+","+k.y);
                continue;
            }
            System.out.println(k.x+","+k.y+" -> "+v.toString());
        }
    }
    
}
