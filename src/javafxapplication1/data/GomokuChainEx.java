package javafxapplication1.data;

import java.awt.Point;
import java.util.*;
/**
 * Класс для добавления в цепь новых элементов
 */
public class GomokuChainEx {

    public Point start;
    public int length;
    public Direction dir;
    /**
     * Конструктор - передача данных в класс
     * @param start - начальная точка
     * @param length - длина цепи
     * @param dir - направление
     * @see GomokuChain#GomokuChain()
     */
    public GomokuChainEx(Point start, int length, Direction dir) {
        this.start = start;
        this.length = length;
        this.dir = dir;
    }

    public GomokuChainEx(GomokuChain src) {
        
    }
    /**
     * Функция получения следующей точки
     * @return возвращает следующую точку в цепи
     */
    public Point nextPoint() {
        return dir.sidePoint(getPoint(length-1));
    }
    /**
     * Функция получения предыдущей точки
     * @return возвращает предыдущую точку
     */
    public Point prevPoint() {
        return dir.opposite().sidePoint(start);
    }
    
    public Point getPoint(int index) {
        if (index>=length) throw new IllegalArgumentException("Out of bounds.");
        if (index == 0) return start;
        Point n = new Point(start.x + dir.getOffsetX()*index, start.y + dir.getOffsetY()*index);
        return n;
    }
        
    public static final int FREE_BEFORE = 1, 
                            FREE_AFTER = 2,
                            FREE = 3,
                            LOCKED = 0;
    /**
     * Определеление свободных клеток позади и впереди цепи
     * @param board - поле
     * @return количество свободных ячеек для продолжения цепи
     */
    public int getStatus(GomokuBoard board) {
        Point last = getPoint(length-1);
        Point before = dir.opposite().sidePoint(start), 
              after = dir.sidePoint(last);
        int b=0, a=0;
        if (board.inBounds(before)) {
            if (board.get(before) == null) b = FREE_BEFORE;
        }
        if (board.inBounds(after)) {
            if (board.get(after) == null) a = FREE_AFTER;
        }
        return (a + b);
    }
    
    public static final int SAFE = 1, 
                            DANGER = 2,
                            MORTALE = 3;
    /**
     * Определеление опастности
     * @param board - поле
     * @param winSize - количество элементов в цепи
     * @return степень опастности
     */
    public int getDanger(GomokuBoard board, int winSize) {
        if (length <= winSize-3) return SAFE;
        int st = getStatus(board);
        if (st == LOCKED) return SAFE;
        if (length == winSize-2) {
            if (st==FREE) return DANGER;
            return SAFE;
        }
        if (length == winSize-1) {
            if (st==FREE) return MORTALE;
            return DANGER;
        }
        return (length == winSize) ? MORTALE : SAFE;
    }
           
    
    public static final Direction[] STARTS = {Direction.E,Direction.SE,Direction.S,Direction.SW};
    /**
     * Задание направления новой цепи
     * @param p - входящая точка
     * @param b - поле
     * @return следующая точка в цепи
     */
    public static Set<Direction> canStartChain(Point p, GomokuBoard b) {
        Boolean v = b.get(p);
        if (v==null) return null;
        Set<Direction> poss = new HashSet();
        for (Direction d : STARTS) {
            Direction o = d.opposite();
            Point before = o.sidePoint(p);
            Boolean v2 = b.get(before);
            if (v.equals(v2)) continue;
            Point next = d.sidePoint(p);
            if (v.equals(b.get(next))) poss.add(d);            
        }
        if (poss.isEmpty()) return null;
        return poss;
    }
    /**
     * Построение уже существующей цепи
     * @param p - входящая точка
     * @param b - поле
     * @return обновленная цепь
     */
    public static Set<GomokuChainEx> buildChains(Point p, GomokuBoard b){
        Boolean v = b.get(p);
        if (v==null) return null;
        Set<Direction> dirs = canStartChain(p,b);
        if (dirs == null) return null;
        Set<GomokuChainEx> ret = new HashSet();
        for (Direction d : dirs) {
            Point n = p;
            int len = 1;
            while (true) {
                n = d.sidePoint(n);
                if (!b.inBounds(n)) break;
                if (!v.equals(b.get(n))) break;
                len++;
            }
            ret.add(new GomokuChainEx(p, len, d));
        }
        return ret;
    }
    /**
     * Количество всех цепей
     * @param side - сторона
     * @param addTo - коллекция цепей
     * @param bo - поле
     * @return количество цепей
     */
    public static int collectAllChains(Boolean side, GomokuBoard bo, Collection<GomokuChainEx> addTo) {        
        Set<GomokuChainEx> ret = new HashSet();
        Set<Point> points = new HashSet();
        bo.enumPoints(side, points);
        if (points.isEmpty()) return 0;
        for (Point p : points) {
            Set<GomokuChainEx> ch = buildChains(p,bo);
            if (ch!=null) ret.addAll(ch);
        }
        if (ret.isEmpty()) return 0;
        addTo.addAll(ret);
        return ret.size();
    }
    
}
