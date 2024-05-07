import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class for calculating line compositions / polylines out of simple lines, with respect to specific constrains.
 * Lines and polylines are characterized through points. The main functionality of this class is a recursive algorithm
 * that grows these polylines while choosing points and lines from the input data under certain conditions.
 */
public class LineCrafter {

    /**
     * Converts the list of Integer x,y coordinate pairs to a corresponding list of points with these coordinates.
     * Each inner list from the 2-dimensional input ArrayList is transformed into a list of two points with type 5.
     * Type 5 indicates that no type has been assigned yet.
     * @param rawCoordinates 2-dimensional ArrayList where each inner list describes a line with the values x1, y1, x2, y2
     * @return 2-dimensional ArrayList where each inner list consists of two points
     *
     * modified: type 7 standard
     */
    public ArrayList<ArrayList<Point>> createStarterList(ArrayList<ArrayList<Integer>> rawCoordinates) {

        ArrayList<ArrayList<Point>> startList = new ArrayList<>();

        for (ArrayList<Integer> startLine : rawCoordinates
        ) {
            ArrayList<Point> pointLine = new ArrayList<>();
            Point p1 = new Point(startLine.get(0), startLine.get(1), 7);
            Point p2 = new Point(startLine.get(2), startLine.get(3), 7);

            pointLine.add(p1);
            pointLine.add(p2);

            startList.add(pointLine);
        }
        return startList;
    }

    /**
     * Creates a deep copy of a list of polylines.
     * @param originalList input list
     * @return copy of input list
     */
    public static ArrayList<ArrayList<Point>> deepCopy(ArrayList<ArrayList<Point>> originalList) {
        ArrayList<ArrayList<Point>> copiedList = new ArrayList<>();

        for (ArrayList<Point> innerList : originalList) {
            ArrayList<Point> copiedInnerList = new ArrayList<>();
            for (Point point : innerList) {
                Point copiedPoint = new Point(point.getX(), point.getY(), point.getType());
                copiedInnerList.add(copiedPoint);
            }
            copiedList.add(copiedInnerList);
        }

        return copiedList;
    }


    /**
     * Counts the frequency of each point in the given list and sets the type of the point based on its frequency.
     * If the point is compared with itself, the counter does not increase.
     * Type 1 = Point occurs exactly once (single point SP)
     * Type 3 = Point occurs exactly twice (connector point CN)
     *          later if a connector has been used once, it gets type 2
     * Type 4 = Point occurs three or more times (terminator point TER)
     * @param startList Input list consisting of lines composed of points
     * @return modified input list, where each point has been assigned the correct type.
     *
     * MOD terminator types. muss dann dynamisch vergößern wenn er mehr als 3 mal vorkommt.
     */
    public ArrayList<ArrayList<Point>> getAllPointTypes(ArrayList<ArrayList<Point>> startList) {

        for (ArrayList<Point> line : startList) {
            for (Point currentPoint : line) {
                int counter = 1; // each point must occur at least one time

                // compare point to all other points in other lines
                for (ArrayList<Point> otherLine : startList) {
                    for (Point otherPoint : otherLine) {

                        if (currentPoint.equals(otherPoint)) {
                            counter++;
                        }
                    }
                }
                int pointType;

                //If a point occurs 3 or more times, is has type 4
                if (counter >= 3) {
                    pointType = 6; //Terminator: 6 -> 5 -> 4 -> 0
                } else if (counter == 2) {
                    pointType = 3; //Connector
                } else {
                    pointType = counter; //SinglePoint
                }
                currentPoint.setType(pointType);
            }
        }
        return startList;
    }


    /**
     * Creates a new line list based on the input, but without lines containing terminators.
     * @param TypeAssignedList List of points with their assigned types
     * @return new cleaned list containing only lines with points of type 1 and 3
     */
    public ArrayList<ArrayList<Point>> removeTerminators(ArrayList<ArrayList<Point>> TypeAssignedList) {

        ArrayList<ArrayList<Point>> withoutTerminators = new ArrayList<>();

        for (ArrayList<Point> line : TypeAssignedList) {

            boolean toRemove = false;

            for (Point currentPoint : line) {
                if (currentPoint.getType() == 4) {
                    toRemove = true;
                    break;
                }
            }
            if (!toRemove) { //linie remains
                withoutTerminators.add(line);
            }
        }
        return withoutTerminators;
    }


    /**
     * Converts the 2D list of lines into a simple 1D list of points from these lines.
     * There are no duplicates in the output list. Multiple Occurrence of a point is indicated by its type.
     * This list now contains only SPs or CNs.
     * Type 3 = Connector that has not yet been used
     * (Type 2 = Connector that has been used once)
     * Type 1 = SinglePoint
     * @param startListWithoutTer List of all lines that do not contain terminators
     * @return simple list of all points from the input file that are either single points or connectors,
     * after the terminators have been removed. All SPs have type 1, all CNs have type 3 initially.
     */
    public ArrayList<Point> getAllPointsAndSetStartlistTypes(ArrayList<ArrayList<Point>> startListWithoutTer){
        ArrayList<Point> SPandCNandTER = new ArrayList<>();

        for (ArrayList<Point> line: startListWithoutTer
             ) {
            for (Point point: line
                 ) {
                int pointType = point.getType();
                if (pointType == 1){
                    SPandCNandTER.add(new Point(point.getX(), point.getY(), point.getType()));
                    point.setType(1);
                } else if (pointType == 3){
                    if (SPandCNandTER.contains(point)){ //equals method of point is called.
                        // if a point with the same x,y is already there, skip the step of adding the point
                        continue;
                    } else {
                        SPandCNandTER.add(new Point(point.getX(), point.getY(), 3));
                        point.setType(1);
                    }
                } else if (pointType == 6) {
                    if (SPandCNandTER.contains(point)){
                        continue;
                    } else {
                        SPandCNandTER.add(new Point(point.getX(),point.getY(), 6));
                        point.setType(1);
                    }
                }
            }
        }
        return SPandCNandTER;
    }


    /**
     * Checks for a list of points whether all have type 0 and therefore have been used.
     * @param singlePointsAndConnectors List of points
     * @return true if all points have type 0 and have been used, false otherwise.
     */
    private boolean allPointsAreUsed(ArrayList<Point> singlePointsAndConnectors) {
        for (Point point : singlePointsAndConnectors) {
            if (point.getType() != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the remaining list contains a circular structure and has only connectors
     * @param singlePointsAndConnectors contains the available points and their types
     * @return true if there are only connectors left to use, false otherwise
     */
    private boolean isCricle(ArrayList<Point> singlePointsAndConnectors) {
        for (Point point : singlePointsAndConnectors) {
            if (point.getType() != 2 && point.getType() != 3 && point.getType() != 0) {
                return false;
            }
        }
        return true;
    }


    /**
     * If a current polyline is passed, work continues on it, and the current point is the last
     * point in this polyline, which is then returned. If no polyline is in the process of being built,
     * the next SP from singlePointsAndConnectors is returned as the current point, or the next CN if there is no SP.
     * @param singlePointsAndConnectors List of all points
     * @param linienZug polyline being currently built. If the list is empty, no polyline is being built.
     * @return Point with which to continue working.
     *
     * MOD return proirity TER > SP > CN
     */
    public Point getCurrentPoint(ArrayList<Point> singlePointsAndConnectors,
                                 ArrayList<Point> linienZug){
        if ( linienZug.isEmpty()){
            // if no line is being build, start with TER, then SP then CN
            for (Point point: singlePointsAndConnectors
            ) {
                if (point.getType() >= 4){
                    return point;
                }
            }
            for (Point point: singlePointsAndConnectors
                 ) {
                if (point.getType() == 1){
                    return  point;
                }
            }
            for (Point point : singlePointsAndConnectors){
                if (point.getType() == 2 || point.getType() == 3){
                    return point;
                }
            }
            // there is a polyline currently being bild, and the current point is the last element of it
        } else {
            return linienZug.get(linienZug.size()-1);
        }
        return null;
    }


    /**
     * Searches for a match for a point.
     * For a given current point, returns the next matching point so that these two points
     * form a line in the initial list of lines. Finding a CN is prioritized over finding an SP.
     * Once a line of two points is found, the types of the matching line in starterList are set.
     * Nodes in the starterList always have type 1 and are marked as deleted with type 0 after being used once.
     * @param singlePointsAndConnectors List of available points. Includes deleted points that are not considered (type 0).
     * @param currentPoint current point for which a match is to be found
     * @param starterList available lines
     * @return Point that forms a line with current point, which exists in starterList.
     *
     * MOD check for CN then for SP then for TER
     */
    public Point searchForCNorSP(ArrayList<Point> singlePointsAndConnectors,
                                 Point currentPoint,
                                 ArrayList<ArrayList<Point>> starterList
    ) {
        for (ArrayList<Point> line : starterList) {
            for (Point otherPoint : singlePointsAndConnectors
            ) {
                if (otherPoint.getType() == 2 || otherPoint.getType() == 3) {
                    //check if current point and other point build a line in starter list, for both directions (p1,p2 or p2,p1)
                    //also check if the line in starter list is available
                    if ((line.get(0).equals(currentPoint) && line.get(1).equals(otherPoint) && line.get(0).getType() != 0 && line.get(1).getType() != 0)
                            || (line.get(1).equals(currentPoint) && line.get(0).equals(otherPoint)&& line.get(0).getType() != 0 && line.get(1).getType() != 0)) {
                        //actualize types in starterList to 0 which marks the line as deleted
                        line.get(0).setType(0);
                        line.get(1).setType(0);
                        return otherPoint;
                    }
                }
            }
        }
        for (ArrayList<Point> line : starterList
        ) {
            for (Point otherPoint : singlePointsAndConnectors
            ) {
                if (otherPoint.getType() == 1) {
                    //check if current point and other point build a line in starter list, for both directions (p1,p2 or p2,p1)
                    //also check if the line in starter list is available
                    if ((line.get(0).equals(currentPoint) && line.get(1).equals(otherPoint) && line.get(0).getType() != 0 && line.get(1).getType() != 0)
                            || (line.get(1).equals(currentPoint) && line.get(0).equals(otherPoint)&& line.get(0).getType() != 0 && line.get(1).getType() != 0)) {
                        line.get(0).setType(0);
                        line.get(1).setType(0);
                        return otherPoint;
                    }
                }
            }
        }
        for (ArrayList<Point> line : starterList
        ) {
            for (Point otherPoint : singlePointsAndConnectors
            ) {
                if (otherPoint.getType() >=4 ) {
                    //check if current point and other point build a line in starter list, for both directions (p1,p2 or p2,p1)
                    //also check if the line in starter list is available
                    if ((line.get(0).equals(currentPoint) && line.get(1).equals(otherPoint) && line.get(0).getType() != 0 && line.get(1).getType() != 0)
                            || (line.get(1).equals(currentPoint) && line.get(0).equals(otherPoint)&& line.get(0).getType() != 0 && line.get(1).getType() != 0)) {
                        line.get(0).setType(0);
                        line.get(1).setType(0);
                        return otherPoint;
                    }
                }
            }
        }
        return null;
    }


    /**
     * Updates the type of the point in the singlePointsAndConnectors list.
     * If the point is an SP (type 1) and has been used, it now receives (type 0).
     * If the point is a CN and has never been used (type 3), it now receives type 2 after being used once.
     * If the connector point has already been used once (type 2), it is marked as no longer available (type 0).
     * The update of TER goes as following: 6 -> 5 -> 4 -> 0
     * @param singlePointsAndConnectors contains available points with types
     * @param currentPoint current point whose type is being updated.
     */
    public void updateType (ArrayList<Point> singlePointsAndConnectors,
                            Point currentPoint){

        // if the reference of currentpoint and point in SinglePointsAndConnectors is the same,
        // equals method will return false for a comparison. Therefore a simple workaround with a copy
        // to compare the points, and if same, update the type
        Point pointCopy = new Point(currentPoint.getX(),currentPoint.getY(),currentPoint.getType());

        int currentPointType = pointCopy.getType();
        for (Point point: singlePointsAndConnectors
        ) {
            if (point.equals(pointCopy)){
                if (currentPointType == 1){
                    point.setType(0);
                } else if (currentPointType == 3) {
                    point.setType(2);
                } else if (currentPointType == 2) {
                    point.setType(0);
                } else if (currentPointType == 6) {
                    point.setType(5);
                } else if (currentPointType == 5) {
                    point.setType(4);
                } else if (currentPointType == 4){
                    point.setType(0);
                }
            }
        }
    }

    /**
     * Recursive algorithm for forming polylines.
     * It starts with a valid point from SinglePointsAndConnectors for which another point is found so that
     * these two points form a line that exists in starterList. The line is marked as deleted in starterList
     * by setting their points to type 0.
     * The found line is either of type SP-SP, then the line ends, and a recursive call to the function is made,
     * where the list of polylines(linienzuege) has increased by one entry.
     * If the found line is of type SP-CN, points are further searched for, that extend the line until an SP is found
     * that completes the polyline (linienzug). This is then added to polylines.
     * (For the case that there are no SPs, the line starts with a CN. If it starts with a CN and there are only CNs,
     * this means it must be a type of circular structure, which will also be found then.)
     * All points are updated in type after their use, with connectors being able to be used twice.
     * Thus, with each recursive call, the algorithm approaches the base case.
     * The base case is reached when all points have been used sufficiently often and have reached type 0.
     * Then the list of polylines is returned.
     * @param starterList holds all initial standard lines from the input file, but without the lines that contained terminators.
     *                   point types here are all 1, and will be set to 0 after the line has been used.
     * @param SPandCNandTER contains all SPs an CN from starterList with their types (0,1,2,3).
     * @param linienZug temporal container for the poly line that is being build
     * @param linienzuege list of polylines crafted so far
     * @return complete list of all polylines that have been found (linienzuege),
     * under the considerations of the given constraints
     */
    public ArrayList<ArrayList<Point>> CraftConnectedLines(
            ArrayList<ArrayList<Point>> starterList,
            ArrayList<Point> SPandCNandTER,
            ArrayList<Point> linienZug,
            ArrayList<ArrayList<Point>> linienzuege) {


        // Base case: When no points are left to form a polyline,
        // terminate the recursion and return the updated list of polylines.
        if (allPointsAreUsed(SPandCNandTER)) {
            return linienzuege;
        }

        // Take a point from SPandCNandTER. If a polyline is in the process of being built (in linienZug), elongate it.
        // That means take the last element of it as current point. Else take another point from SPandCNandTER,
        // that is available (based on the point type).
        Point currentPoint = getCurrentPoint(SPandCNandTER,linienZug);

        if (currentPoint.getType() == 1) {
            Point nextPoint = searchForCNorSP(SPandCNandTER,currentPoint,starterList);
            //For a found match (current point, next point) in starterList, the equivalent line in starterList
            // gets marked as deleted (type= 0)
            if (nextPoint.getType() == 1){
                //No connector found, therefore there is no polyline and the simple line (SP,SP = linienzug) is added
                // to the list of polylines as a new entry (linienzuege)
                ArrayList<Point> linienzug = new ArrayList<>();
                linienzug.add(currentPoint);
                linienzug.add(nextPoint);
                linienzuege.add(new ArrayList<>(linienzug));
                linienzug.clear();

                //both SP points get type update from 1 to 0 (update in staterList already happened above)
                updateType(SPandCNandTER,currentPoint);
                updateType(SPandCNandTER,nextPoint);

                // recursive call with actualized list of polylines and empty linienzug
                return CraftConnectedLines(starterList,SPandCNandTER,linienzug,linienzuege);
            }
            if (nextPoint.getType() == 2 || nextPoint.getType()== 3){
                // Connector has been found which triggers the elongation of the polyline
                ArrayList<Point> linienzug = new ArrayList<>();
                linienzug.add(currentPoint);
                linienzug.add(nextPoint);

                // In this case the polyline linienzug is not added to the list of polylines (because it is not complete),
                // but is passed to the recursive function call, to be further elongated.

                updateType(SPandCNandTER,currentPoint);
                updateType(SPandCNandTER, nextPoint);

                return CraftConnectedLines(starterList,SPandCNandTER,linienzug,linienzuege);
            } else if (nextPoint.getType() >= 4) {
                // next point is a terminator, meaning the line ends here
                // therefore similar as for case nextpoint.type = 1: linienzuege gets a new entry

                ArrayList<Point> linienzug = new ArrayList<>();
                linienzug.add(currentPoint);
                linienzug.add(nextPoint);
                linienzuege.add(new ArrayList<>(linienzug)); //TODO muss das wirklich 2 mal ?
                linienzug.clear();

                //both points get type update
                updateType(SPandCNandTER,currentPoint);
                updateType(SPandCNandTER,nextPoint);

                // recursive call with actualized list of polylines and empty linienzug
                return CraftConnectedLines(starterList,SPandCNandTER,linienzug,linienzuege);
            }
        }
        if (currentPoint.getType() == 2 || currentPoint.getType() == 3){
            Point nextPoint = searchForCNorSP(SPandCNandTER,currentPoint,starterList);
            if (nextPoint.getType() == 1){
                // Because the current point was a connector, the polyline is in the process of being built. Therefore
                // linienZug has to be extended with next point. Because next point is a SP, it ends the polyline, and
                // the polyline gets added to the list of polylines.
                linienZug.add(nextPoint);
                linienzuege.add(new ArrayList<>(linienZug));
                linienZug.clear();

                updateType(SPandCNandTER,currentPoint);
                updateType(SPandCNandTER,nextPoint);

                // recursive call with empty linienZug means: begin with a new point to build a new poly line
                return CraftConnectedLines(starterList,SPandCNandTER,linienZug,linienzuege);
            }
            if (nextPoint.getType() >= 4){
                //similar case as for type = 1; the polyline that is currently being extended ends here
                linienZug.add(nextPoint);
                linienzuege.add(new ArrayList<>(linienZug));
                linienZug.clear();

                updateType(SPandCNandTER,currentPoint);
                updateType(SPandCNandTER,nextPoint);

                // recursive call with empty linienZug means: begin with a new point to build a new poly line
                // linienzuege got an extra entry
                return CraftConnectedLines(starterList,SPandCNandTER,linienZug,linienzuege);
            }
            if (nextPoint.getType() == 2 || nextPoint.getType()== 3){
                // If current point is a CN and next point is also an CN, the current polyline (linienZug) must be further extended
                ArrayList<Point> linienzug = new ArrayList<>();
                // If there is a circle:
                // The case when current and next point are CNs, also can happen for a circular polyline at the start.
                // Therefore next to extending the polyline, is must also be taken care for initializing a new one.
                if (isCricle(SPandCNandTER)){
                    if (linienZug.isEmpty()){
                        linienzug.add(currentPoint);
                        linienzug.add(nextPoint);
                    } else {
                        linienZug.add(nextPoint);
                    }
                }else {
                    linienZug.add(nextPoint);
                }

                //update types of the used points accordingly
                updateType(SPandCNandTER,currentPoint);
                updateType(SPandCNandTER, nextPoint);

                // A circular structure will end in this case (when current and next point are CNs).
                // but after all types are 0, this circle has to be appended to the final list of polylines
                if (allPointsAreUsed(SPandCNandTER)){
                    linienzuege.add(linienZug);
                }

                if (linienZug.isEmpty()){
                    //recursive function call with the circular polyline that just started being build
                    return CraftConnectedLines(starterList,SPandCNandTER,linienzug,linienzuege);
                } else {
                    //recursive function call with the polyline that is currently in build process
                    return CraftConnectedLines(starterList,SPandCNandTER,linienZug,linienzuege);
                }
            }
        } if (currentPoint.getType() >= 4){ //current point is a TER
            Point nextPoint = searchForCNorSP(SPandCNandTER,currentPoint,starterList);
            //case 1: next point is SP: end line and add entry to polylines
            if (nextPoint.getType() == 1){
                //No CN or TER found, therefore there is no polyline and the simple line (TER,SP = linienzug) is added
                // to the list of polylines as a new entry (linienzuege)
                ArrayList<Point> linienzug = new ArrayList<>();
                linienzug.add(currentPoint);
                linienzug.add(nextPoint);
                linienzuege.add(new ArrayList<>(linienzug)); //TODO muss das zweimal ?
                linienzug.clear();

                //both points get type update
                updateType(SPandCNandTER,currentPoint);
                updateType(SPandCNandTER,nextPoint);

                // recursive call with actualized list of polylines and empty linienzug
                return CraftConnectedLines(starterList,SPandCNandTER,linienzug,linienzuege);
            }
            //case 2: next point is TER: end line (TER, TER) and add entry to polylines
            else if (nextPoint.getType() >= 4) {

                ArrayList<Point> linienzug = new ArrayList<>();
                linienzug.add(currentPoint);
                linienzug.add(nextPoint);
                linienzuege.add(new ArrayList<>(linienzug)); //TODO muss das wirklich 2 mal ?
                linienzug.clear();

                //both points get type update
                updateType(SPandCNandTER,currentPoint);
                updateType(SPandCNandTER,nextPoint);

                // recursive call with actualized list of polylines and empty linienzug
                return CraftConnectedLines(starterList,SPandCNandTER,linienzug,linienzuege);
            }
            //case 3: next point is CN: elongate polyline (TER,CN) through passing it into the temporary array
            if (nextPoint.getType() == 2 || nextPoint.getType()== 3){
                // Connector has been found which triggers the elongation of the polyline
                ArrayList<Point> linienzug = new ArrayList<>();
                linienzug.add(currentPoint);
                linienzug.add(nextPoint);

                // In this case the polyline linienzug is not added to the list of polylines (because it is not complete),
                // but is passed to the recursive function call, to be further elongated.

                updateType(SPandCNandTER,currentPoint);
                updateType(SPandCNandTER, nextPoint);

                return CraftConnectedLines(starterList,SPandCNandTER,linienzug,linienzuege);
            }
        }
        // if an error in the algorithmic logic happened
        return null;
    }

    /**
     * Calculates the distance between two points in a Cartesian coordinate system,
     * using the Pythagoras theorem.
     * @param p1 Point 1 with its coordinates
     * @param p2 Point 2 with its coordinates
     * @return Distance between the two points
     */
    public double calculateDistance(Point p1, Point p2){
        //Pythagoras theorem: a^2 + b^2 = c^2, also ist c = Sqrt( a^2 + b^2 )
        int deltaX = p2.getX() - p1.getX();
        int deltaY = p2.getY() - p1.getY();

        //Squaring always results in a positive number
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }


    /**
     * Sorts a list of polylines based on their length in descending order.
     * The length of a polyline is the sum of the lengths of the lines that it is build of.
     * @param linienzuege List of polylines, where each polyline consists of multiple points.
     * @return Map of polylines sorted in descending order based on their length.
     */
    public Map<Double, ArrayList<Point>> sortLinienzuege (ArrayList<ArrayList<Point>> linienzuege){

        // TreeMap to keep fixed order receives reverse comparator as input.
        Map<Double, ArrayList<Point>> distances = new TreeMap<>(Collections.reverseOrder());

        for (ArrayList<Point> linienzug: linienzuege
             ) {
            double sum = 0;
            for (int i = 0; i < linienzug.size()-1; i++) {
                sum += calculateDistance(linienzug.get(i), linienzug.get(i+1));
            }
            distances.put(sum,linienzug);
        }
        return distances;
    }
}
