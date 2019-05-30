package javafxapplication1;

import javafxapplication1.data.GomokuChain;
import java.awt.Point;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafxapplication1.data.Direction;
import javafxapplication1.data.Direction;
import javafxapplication1.data.GameState;
import javafxapplication1.data.GameState;
import javafxapplication1.data.GomokuBoard;
import javafxapplication1.data.Side;
import javafxapplication1.data.Side;

public class Utils {

    // ---------------- Types ------------
    
    // nf
    public static interface CellVisitor {
        public void visit(int x, int y, Boolean state);
        public Point nextCell(int oldX, int oldY);
    }
    
    public static interface Run {
        public void run();
    }
    
    // ---------------- Visitors -------------------
    
   // public static class DirectMarktypeVisitor implements CellVisitor {
        
   // }
    
    // walk through filled cells
    // nf
    public static abstract class DirectFilledVisitor implements CellVisitor {

        private GomokuBoard gb;
        private Direction dd;
        private Boolean cellType;
        
        public DirectFilledVisitor(GomokuBoard gb, Direction d, Boolean path) {
            this.gb = gb;
            dd=d;
            cellType=path;
        }
        
        @Override
        public Point nextCell(int oldX, int oldY) {
            Point n = dd.sidePoint(new Point(oldX,oldY));
            Boolean v = gb.get(n);
            if (v==null) return null;
            if (cellType==null) return n;
            if (cellType.booleanValue() == v.booleanValue()) return n;
            return null;
        }

    } 
    
    // ---------------- Board ------------
    
    public static boolean isStartOfChain(Point from, int len, GameState gameState, GomokuBoard board) {
        for (Direction dir : Direction.ALL_DIRECTIONS) {
            boolean st = Utils.isStartOfChainInDirection(from, len, dir, board);
            if (st) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isStartOfChainInDirection(Point from, int len, Direction dir, GomokuBoard board) {
        Boolean b = board.get(from);
        Boolean initialB = b;
        Point pt = from;
        int chLen = 1;
        while (b != null) {
            pt = dir.sidePoint(pt);
            b = board.get(pt);
            if (initialB.equals(b)) chLen++; else break;
        }
        return (chLen >= len);
    }
        
    public static boolean hasNextSamePoint(Point from, Direction di, GomokuBoard board) {
        Point sp = di.sidePoint(from);
        Boolean fb = board.get(from);
        Boolean sb = board.get(sp);
        if (sb==null) return false;
        return fb.equals(sb);
    }
    
    public static void collectSameMarksInDirection(Point p, GomokuBoard b, Direction d, 
            Set<Point> append, boolean appendFirst) {
        Boolean val = b.get(p);
        if (val==null) return;
        Point pp = p;
        if (appendFirst) append.add(p);
        while (true) {
            Point neigh = d.sidePoint(pp);
            Boolean nextVal = b.get(neigh);
            if (nextVal == null) break;
            if (nextVal.booleanValue() != val.booleanValue()) break;
            pp = neigh;
            append.add(pp);            
        }        
    }
    
    // how many cells, "p" cell is NOT counted
    public static int exactMarksInDirectionCount(Point p, GomokuBoard b, Direction d) {
        Boolean m = b.get(p);
        if (m==null) return -1;
        Point pp = p;
        int found = 0;
        while (true) {
            Point neigh = d.sidePoint(pp);
            Boolean nextVal = b.get(neigh);
            if (nextVal == null) break;
            if (nextVal.booleanValue() != m.booleanValue()) break;
            pp = neigh;
            found++;
        }
        return found;
    }
    
    public static int getChainLength(Point x, GomokuBoard bo, Side si) {
        Boolean val = bo.get(x);
        if (val==null) return -1;
        int back = exactMarksInDirectionCount(x,bo,si.from);
        int fw = exactMarksInDirectionCount(x,bo,si.to);
        return fw + back + 1;
    }
        
    public static GomokuChain getChain(Point x, GomokuBoard bo, Side si) {
        Boolean val = bo.get(x);
        if (val==null) return null;        
        GomokuChain ret = new GomokuChain();
        collectSameMarksInDirection(x, bo, si.from, ret.points, false);
        collectSameMarksInDirection(x, bo, si.to, ret.points, true);        
        return ret;
    }
        
    public static void enumerateChainsFor(Point memberPt, int minSize, GomokuBoard board, Collection<GomokuChain> out) {
        Boolean pl = board.get(memberPt);
        if (pl==null) throw new IllegalStateException("Tried to find null chain (enumerateChainsFor).");
        for (Side si : Side.ALL_SIDES) {
            GomokuChain ch = getChain(memberPt, board, si);            
            if (ch!=null) {
                if (ch.size() >= minSize) out.add(ch);
            }            
        }
    }

    public static boolean existsChain(GomokuBoard board, Boolean mark, int minSize) {
        if (mark==null) throw new IllegalStateException("Tried to find null chain (existsChain).");
        List<Point> k = new LinkedList();
        List<GomokuChain> li = new LinkedList();
        board.enumPoints(mark, k);
        if (k.size()>0) for (Point pi : k) {            
            enumerateChainsFor(pi, minSize, board, li);
            if (li.size() > 0) return true;
        }        
        return false;
    }
    
    public static void enumerateAllChains(Boolean sid, int minSize, GomokuBoard board, Collection<GomokuChain> out) {
        Set<Point> curr = new HashSet();
        board.enumPoints(sid, curr);
        if (curr.isEmpty()) return;
        Set<GomokuChain> chains = new HashSet();
        for (Point p : curr) {            
            enumerateChainsFor(p, minSize, board, chains);            
        }
        out.addAll(chains);
    }
    
    // ---------------- Sets ------------
        
    
    // ---------------- Misc ------------
    
    public static boolean emptyStr(String s) {
        if (s == null) return true;
        if (s.length() == 0) return true;
        if (s.trim().length() == 0) return true;
        return false;
    }
    
    public static boolean inBitSet(int item, int set) {
        return ((set & item) == item);
    }
    
    public static boolean comparePoint(Point p, int xx, int yy) {
        if (p==null) return false;
        return ((p.x==xx) && (p.y==yy));
    }
    
    public static final int AT_NULL = 1,
                     AT_NOT_NULL = 4+8,
                     AT_TRUE = 4,
                     AT_FALSE = 8;                     
    
    public static boolean checkBool(Boolean bp, int typeSet) {
        if (bp == null) {
            return inBitSet(AT_NULL, typeSet);
        } else if (bp.booleanValue()) {
            return inBitSet(AT_TRUE, typeSet);
        } else {
            return inBitSet(AT_FALSE, typeSet);
        }
    }
    
    public static boolean checkBoolFromBoard(Point p, GomokuBoard b, int typeSet) {
        Boolean bp = b.get(p);
        return checkBool(bp,typeSet);
    }
    
    // -------------- Err asserts ------------------
    
    public static void assertPointCommon(Point p, GomokuBoard b, int typeSet, String ms) {
        boolean st = checkBoolFromBoard(p,b,typeSet);
        if (st==false) throw new IllegalStateException(ms);
    }
    
    public static void assertPointCommon(Point p, GomokuBoard b, int typeSet) {        
        assertPointCommon(p,b,typeSet,"Game point "+p.toString()+" assert error.");        
    }
    
    public static Boolean getAndAssert(Point p, GomokuBoard b, int typeSet) {
        Boolean vv = b.get(p);
        boolean chr = checkBool(vv,typeSet);
        if (!chr) throw new IllegalStateException("getAndAssert");
        return vv;
    }
    
    public static void assertNullPoint(Point p, GomokuBoard b, String ms) {
        assertPointCommon(p,b,AT_NULL,ms);
    }
    
    public static void assertFilledPoint(Point p, GomokuBoard b) {
        assertPointCommon(p,b,AT_NOT_NULL);
    }
    
    public static void assertFilledPoint(Point p, GomokuBoard b, String ms) {
        assertPointCommon(p,b,AT_NOT_NULL,ms);
    }
    
    public static void assertTypedPoint(Point p, GomokuBoard b, Boolean v, String ms) {
        int t = v.booleanValue() ? AT_TRUE : AT_FALSE;
        assertPointCommon(p,b,t,ms);
    }
}
