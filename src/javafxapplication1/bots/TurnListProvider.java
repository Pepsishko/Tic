package javafxapplication1.bots;

import java.awt.Point;
import java.util.Collection;
import javafxapplication1.data.GomokuBoard;

public interface TurnListProvider {
    
    void possibleTurns(GomokuBoard b, int line, Collection<Point> appTo);
    
}
