package javafxapplication1.data;

import java.awt.Point;

public enum Direction {
    N(0,-1),NE(1,-1),E(1,0),SE(1,1),S(0,1),SW(-1,1),W(-1,0),NW(-1,-1);
    private int dx,dy;
    Direction(int x, int y) {
        dx = x;
        dy = y;
    }
    
    public Point sidePoint(Point from) {
        Point s = new Point(from.x+dx,from.y+dy);

        return s;
    }
    
    public Direction opposite() {
        if (this==N) return S;
        if (this==S) return N;
        if (this==W) return E;
        if (this==E) return W;
        if (this==NE) return SW;
        if (this==SW) return NE;
        if (this==NW) return SE;
        if (this==SE) return NW;
        throw new IllegalStateException();
    }

    public int getOffsetX() {
        return dx;
    }

    public int getOffsetY() {
        return dy;
    }
    
    public static final Direction[] ALL_DIRECTIONS = {Direction.N,Direction.S,Direction.W,Direction.E,Direction.NE,Direction.SE,Direction.NW,Direction.SW};
    
}
