package javafxapplication1.bots;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import javafxapplication1.data.GameState;
import javafxapplication1.data.players.BotPlayer;
import javafxapplication1.data.GomokuBoard;

public class RandomBot extends BotPlayer {

    public RandomBot(Boolean side, TurnListProvider tl) {
        super(side);
        ready = true;
        this.turnSeeker = tl;
    }
    /**
     * Игра бота
     * @return точка
     */
    @Override
    public Point getTurn() {
        ArrayList<Point> s = new ArrayList();
        this.turnSeeker.possibleTurns(GameState.singleton.getBoard(), lineSize, s);
        Random r = new Random();
        return s.get(r.nextInt(s.size()));
    }
            
}
