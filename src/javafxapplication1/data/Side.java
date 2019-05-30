package javafxapplication1.data;

public enum Side {
    HORZ(Direction.W,Direction.E), 
    VERT(Direction.N,Direction.S), 
    FWD(Direction.NW,Direction.SE), 
    BKD(Direction.NE,Direction.SW);
    public Direction from, to;

    private Side(Direction from, Direction to) {
        this.from = from;
        this.to = to;
    }
    public static final Side[] ALL_SIDES = {HORZ,VERT,FWD,BKD};
}
