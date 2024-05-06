
/**
 * Class for representing a point as a simple data structure that has one x and one y coordinate.
 * The Point also has a type that specifies its frequency of occurrence
 * in the initial input data list. Later the type is modified and used
 * to determine the points availability for building lines.
 */
public class Point {

    // x coordinate
    private final int x;
    // y coordinate
    private final int y;
    // type of the point. default is 5, if the type has not yet been determined.
    // type 1 = singePoint (SP) (once occurrence), type 2 = Connector (CN) (two occurrences) (already used once),
    // type 3 = Connector (CN) (two occurrences) (never used) , type 4 = Terminator (TER) (three or more occurrences)
    private int type;


    //Constructor
    public Point(int x, int y, int type){
        this.x = x;
        this.y = y;
        this.type = type;
    }

    /**
     * Getter for x coordinate
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for y coordinate
     * @return y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * getter for type
     * @return type
     */
    public int getType() {
        return type;
    }

    /**
     * Setter for type
     * @param type new value for point type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Method for comparing two points
     * @param obj other point with which the current point is compared
     * @return true if the two compared points have the same x,y coordinates. False otherwise
     * If the point is compared with itself, meaning it has the same reference in memory,
     * false will be returned.
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return false; // same object in memory
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // object is null or not a point
        }
        Point otherPoint = (Point) obj;
        return this.x == otherPoint.x && this.y == otherPoint.y; // comparison of coordinates
    }


    /**
     * Characterizes the point
     * @return representation of the point as a String
     */
    @Override
    public String toString() {return "( x,y: " + x + ", " + y + ")"; }


}
