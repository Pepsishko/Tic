package javafxapplication1.data;

import javafxapplication1.Utils;
import java.awt.Point;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafxapplication1.Utils.Run;
import javafxapplication1.data.GomokuBoard;
import javafxapplication1.data.players.LocalPlayer;
import javafxapplication1.data.players.Player;

//enum Direction {}

public class GameState {

    GameConfig cfg;    
    GomokuBoard board;
    FinishAction onFinish;
    
    int sleepDur = 150;
    
    private Player nowTurns;
    private boolean nowTurns1;
    private Boolean nowMark;
    private int turnNumber;
    
    public static GameState singleton;
    
    public Run repainter;
    /**
     * Конструктор - передача данных в класс
     * @param cfg - передача параметров
     * @param board - поле
     * @param onFinish - окончание игры
     * @see GomokuChain#GomokuChain()
     */
    public GameState(GameConfig cfg, GomokuBoard board, FinishAction onFinish) {
        this.cfg = cfg;
        this.board = board;
        this.onFinish = onFinish;
    }

    public boolean isNowTurns1() {
        return nowTurns1;
    }

    public int getTurnNumber() {
        return turnNumber;
    }
    
    public static interface FinishAction {
        public void action(Boolean winPlayer);
    }
    
    public Boolean update(int x, int y) {
        return null;
    }
    
    public Boolean getPointAt(int x, int y) {
        return board.get(new Point(x,y));
    }
    /**
     * Получение настроек
     * @return настройки
     */
    public GameConfig getConfig() {
        return cfg;
    }
    /**
     * Получение поля
     * @return поле
     */
    public GomokuBoard getBoard() {
        return board;
    }
    /**
     * Определение текущего игрока
     * @return текущий игрок
     */
    public Player getCurrentPlayer() {
        return this.nowTurns;
    }
    
    public Player getOtherPlayer() {
        return nowTurns1 ? cfg.p2 : cfg.p1;
    }
    
    public static void restoreLocalPlayer(Player p) {
        if (p instanceof LocalPlayer) {
            ((LocalPlayer)p).restoreTurn();
        }
    }
    
    boolean isWinner(Boolean test) {
        return Utils.existsChain(board, test, cfg.lineLength);
    }
    /**
     * Ход следующего игрока
     */
    void nextPlayer() {
        nowTurns1 = !nowTurns1;
        if (nowTurns1) {
            nowTurns = cfg.p1;
            nowMark = Boolean.TRUE;
        } else {
            nowTurns = cfg.p2;
            nowMark = Boolean.FALSE;
        }
        turnNumber++;
    }
    
    class Runner extends Thread {
        /**
         * Смена игроков во время хода игры
         */
        @Override
        public void run() {
            try {
                Boolean winner = null;
                do {
                    Thread.sleep(sleepDur);
                    if (nowTurns.turnReady()) {
                        Point turn = nowTurns.getTurn();

                        if (board.containsKey(turn)) {                            
                            throw new IllegalStateException("Player has sended impossible turn! Cell ["+turn.x+","+turn.y+"] is already busy.");
                        }
                        board.put(turn, nowTurns.getSide());
                        Player oth = getOtherPlayer();
                        boolean wins = isWinner(nowTurns.getSide());
                        if (wins) {
                            winner = nowTurns.getSide();
                            break;                           
                        }
                        if (repainter != null) repainter.run();
                        restoreLocalPlayer(nowTurns);
                        nextPlayer();                        
                    }             
                } while (true);
                if (repainter != null) repainter.run();
                System.out.println("Game finished.");

                onFinish.action(winner);
            } catch (InterruptedException ex) {
                System.err.println("Interruped main thread.");
            }
        }
        
    }
    /**
     * начало игры
     */
    public void startGame() throws InterruptedException {
        System.out.println("Starting game...");
        nowTurns = cfg.getFirst();
        nowTurns1 = cfg.firstMove1;        
        nowMark = new Boolean(cfg.firstMove1);
        Runner ru = new Runner();
        turnNumber = 1;
        ru.start();
        System.out.println("Game started");
    }
    
    public void dump() {
        System.out.println("nowTurns.side - "+nowTurns.getSide());
    }
    
}
