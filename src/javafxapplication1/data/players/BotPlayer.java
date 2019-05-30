package javafxapplication1.data.players;

import javafxapplication1.bots.TurnListProvider;
import javafxapplication1.data.GomokuBoard;

public abstract class BotPlayer extends Player {

    public BotPlayer(Boolean side) {
        super(side);
    }
    
    public TurnListProvider turnSeeker;
    public GomokuBoard board;
    public int lineSize;
    
}
