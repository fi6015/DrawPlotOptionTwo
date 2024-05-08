
/**
 * Class for representing a point as a simple data structure that has one x and one y coordinate.
 * The Point also has a type that specifies its frequency of occurrence
 * in the initial input data list. Later the type is modified and used
 * to determine the points availability for building lines.
 *
 * Explanation of point types: (see also getAllPointTypes() in LineCrafter class)
 * Type 1 = Point occurs exactly once (single point SP) (after usage it gets type 0)
 * Type 3 = Point occurs exactly twice (connector point CN)
 *          later if a connector has been used once, it gets type 2 (after second usage it gets type 0)
 * Types for Terminators: initially 6 or upward, depending on the connectivity of the TER.
 * Connectivity of a point means its number of occurrence in the input.txt data. The type for a terminator is then set
 * to such a number, that the terminator can then be used as many times as it occurs, and then gets set to type 0.
 * Assuming that every time the TER point has been used, its type is reduced by one.
 * (But in the last step it gets from 4 to zero.)
 * For the given input.txt data example TERs just have the initial type of 6.
 * The evolution is 6 -> 5 -> 4 -> 0.
 * This type solution concept is for data that has terminators from which more than three lines originate.
 */
public class Point {

    // x coordinate
    private final int x;
    // y coordinate
    private final int y;
    // for type explanation see documentation
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
