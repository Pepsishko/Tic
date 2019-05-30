/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1.bots;

import java.awt.Point;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javafxapplication1.data.GomokuBoard;


public class AnyCellProvider implements TurnListProvider {
    int distanceLimit = 2;

    public AnyCellProvider() {
    }

    public AnyCellProvider(int dl) {
        distanceLimit = dl;
    }
    /**
     * Определение возможных ходов
     * @param b - поле
     * @param appTo - коллекция точек
     */
    @Override
    public void possibleTurns(GomokuBoard b, int line, Collection<Point> appTo) {
        Set<Point> free = new HashSet();
        Set<Point> busy = new HashSet();
        b.enumPoints(Boolean.TRUE, busy);
        b.enumPoints(Boolean.FALSE, busy);
        if (busy.isEmpty()) {
            appTo.add(new Point(0, 0));
            return;
        }
        for (Point bp : busy) {
            for (int x = -distanceLimit; x < distanceLimit + 1; x++) {
                for (int y = -distanceLimit; y < distanceLimit + 1; y++) {
                    Point pt = new Point(bp.x + x, bp.y + y);
                    if (b.inBounds(pt)) {
                        if (!b.containsKey(pt)) {
                            free.add(pt);
                        }
                    }
                }
            }
        }
        appTo.addAll(free);

    }
    
}
