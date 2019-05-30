package javafxapplication1.bots;

import java.awt.Point;
import javafxapplication1.data.players.BotPlayer;
import java.util.*;
import javafxapplication1.data.GameState;
import javafxapplication1.data.GomokuChainEx;

public class DefaultBot extends RandomBot {

    public DefaultBot(Boolean side, TurnListProvider tl) {
        super(side, tl);
    }
    /**
     * Нахождение опасных цепей
     * @return  количество
     */
    private int findDangerousChains(Boolean sideFor, Set<GomokuChainEx> app) {
        Set<GomokuChainEx> ch = new HashSet();
        int ec = GomokuChainEx.collectAllChains(sideFor, board, ch);
        if (ec==0) return 0;
        ec = 0;
        for (GomokuChainEx c : ch) {
            int d = c.getDanger(board, lineSize);
            if (d == GomokuChainEx.DANGER) {
                app.add(c);
                ec++;
            }
        }
        return ec;
    }
    /**
     * Нахождение завершающей точки для комбинации
     * @return если true то ставится prevpoint если false то nextpoint
     */
    private Point findLockPoint(GomokuChainEx ch) {
        int st = ch.getStatus(board);
        if (st == GomokuChainEx.FREE_BEFORE) return ch.prevPoint();
        if (st == GomokuChainEx.FREE_AFTER) return ch.nextPoint();
        Random r = new Random();
        return r.nextBoolean() ? ch.prevPoint() : ch.nextPoint();
    }
    /**
     * Игра бота
     * @return точка
     */
    @Override
    public Point getTurn() {
        if (board==null) board = GameState.singleton.getBoard();
        if (lineSize==0) lineSize = GameState.singleton.getConfig().getLineLength();
        Set<GomokuChainEx> enemy = new HashSet(),
                            ours = new HashSet();
        int en = findDangerousChains(!side, enemy);
        System.out.println("found danger: "+en);
        if (en > 0) {
            Object ch = enemy.toArray()[0];
            return findLockPoint((GomokuChainEx)ch);
        }
        int ou = findDangerousChains(side, ours);
        System.out.println("found perspective: "+ou);
        if (ou == 0) return super.getTurn();
        Object ch1 = ours.toArray()[0];
        return findLockPoint((GomokuChainEx)ch1);
    }
    
}
