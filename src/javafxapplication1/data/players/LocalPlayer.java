package javafxapplication1.data.players;

import java.awt.Point;

public class LocalPlayer extends Player {

    public LocalPlayer(Boolean side) {
        super(side);
    }

    private Point myNextTurn;
    
    public void setReady(Point p) {
        if (p==null) throw new IllegalArgumentException("Trying apply null turn.");
        myNextTurn = p;
        ready = true;
    }

    public void restoreTurn() {
        ready = false;
    }
    
    @Override
    public Point getTurn() {
        return myNextTurn;
    }
    
}
