package javafxapplication1.data;

import javafxapplication1.Utils;
import java.awt.Point;
import java.util.Collection;
import java.util.*;

public class GomokuBoard {
    int min;
    int max;

    class Entry {
        Point p;
        Boolean b;

        public Entry(Point p, Boolean b) {
            this.p = p;
            this.b = b;
        }
        
    }
    
    private List<Entry> items = new ArrayList();
    
    public GomokuBoard(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public GomokuBoard() {
        this(Integer.MIN_VALUE,Integer.MAX_VALUE);
    }

    public void visitCells(Point start, Utils.CellVisitor vis) {
        if (start == null) {
            throw new IllegalStateException("Null start Point.");
        }
        Point p = start;
        while (true) {
            Boolean v = this.get(p);
            vis.visit(p.x, p.y, v);
            p = vis.nextCell(p.x, p.y);
            if (p == null) {
                break;
            }
        }
    }

    public void enumPoints(Boolean kind, Collection<Point> out) {        
        for (Entry e : items) {
            if (e.b.booleanValue() == kind.booleanValue()) out.add(e.p);
        }        
    }

    public boolean inBounds(int x, int y) {        
        if (x < min || x > max || y < min || y > max) {
            return false;
        }
        return true;
    }
    
    public boolean inBounds(Point i) {
        if (i == null) {
            return false;
        }        
        return inBounds(i.x, i.y);
    }
    
    public boolean freeCell(int x, int y) {        
        for (Entry e : items) {
            if (e.p.x == x && e.p.y == y) return false;
        }
        return true;
    }
    
    public void put(Point key, Boolean value) {
        if (!inBounds(key)) {
            return;
        }
        if (value==null) throw new IllegalArgumentException("Tried to put null");        
        items.add(new Entry(key, value));
    }
    
    public Boolean get(Point n) {
        for (int i=0; i<items.size(); i++) {
            Entry e = items.get(i);
            if (e.p.x == n.x && e.p.y == n.y) return e.b;
        }
        return null;
    }
    
    public Set<Point> keySet() {
        Set s = new HashSet();
        for (Entry e : items) s.add(e.p);        
        return s;
    }
    
    public int size() {
        return items.size();
    }
    
    public boolean containsKey(Point k) {
        for (Entry e : items) if (e.p.x == k.x && e.p.y == k.y) return true;
        return false;
    }
    
}
    