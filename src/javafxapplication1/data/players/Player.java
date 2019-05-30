package javafxapplication1.data.players;

import java.awt.Point;


public abstract class Player {

    public abstract Point getTurn();

    public boolean turnReady() {
        return ready;
    }
    protected Boolean side;
    protected boolean ready = false;

    public Player(Boolean side) {
        this.side = side;
    }

    public Boolean getSide() {
        return side;
    }
    
}
