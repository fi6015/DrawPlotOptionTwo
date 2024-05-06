import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        // Reading the input.txt file and storing the data
        InputHandler inputHandler = new InputHandler();
        ArrayList<ArrayList<Integer>> rawCoordinates = inputHandler.readCoordinates("src/main/java/input.txt");

        // Print the result for verification
        System.out.println("\nDie initialen Koordinatendaten lauten: ");
        for (ArrayList<Integer> coordinates : rawCoordinates) {
            System.out.println(coordinates);
        }
        System.out.println("\n");

        LineCrafter lineCrafter = new LineCrafter();

        // Convert the list of integers to a list of lines described by points.
        // The types of the points will be assigned later.
        ArrayList<ArrayList<Point>> starterListNotypes = lineCrafter.createStarterList(rawCoordinates);

        // Count the frequency of each point and assign it the corresponding type.
        // Work on a deep copy to leave the previous lists unchanged (used for checking intermediate results).
        ArrayList<ArrayList<Point>> starterListAllTypes = lineCrafter.getAllPointTypes(LineCrafter.deepCopy(starterListNotypes));

        // Remove all lines that have points occurring three or more times, as no connection is made at these points.
        ArrayList<ArrayList<Point>> starterListInitState = lineCrafter.removeTerminators(LineCrafter.deepCopy(starterListAllTypes));

        // After terminators (type 4 points) and their lines are removed, the types of points that were
        // connected to the terminators may change.
        // Therefore, the types of points are reassigned correctly.
        ArrayList<ArrayList<Point>> starterListFinal = lineCrafter.getAllPointTypes(LineCrafter.deepCopy(starterListInitState));

        // Create a list of all points present in the cleaned starter list.
        ArrayList<Point> singlePointsAndConnectors = lineCrafter.getSPsAndCNsAndSetStartlistTypes(starterListFinal);

        // List for temporary storage of a polyline that is currently being constructed and should be further expanded.
        ArrayList<Point> linienzugInitial = new ArrayList<>();

        // List for storing multiple completed polylines.
        ArrayList<ArrayList<Point>> linienzuegeInitial = new ArrayList<>();


        //********** Main Function **********
        // Implements a recursive algorithm that solves the problem of forming polylines.
        // The result is a list of polylines.
        ArrayList<ArrayList<Point>> linienzuege = lineCrafter.CraftConnectedLines(starterListFinal, singlePointsAndConnectors,linienzugInitial,linienzuegeInitial);

        // For each polyline, its length is then calculated and stored together with the polyline in a map.
        // This map is then sorted in descending order, so that the longest polyline becomes the first entry.

        Map<Double, ArrayList<Point>> distances = lineCrafter.sortLinienzuege(linienzuege);

        // The results are then printed to the console.
        int counter  = 1;
        for (Map.Entry<Double,ArrayList<Point>> entry: distances.entrySet()
             ) {

            System.out.println("Distanz des Linienzuges " + counter + " betraegt: " + String.format("%.3f", entry.getKey()) +" Einheiten");
            System.out.println("x,y Koordinaten der Punkte im Linienzug " + entry.getValue()+ "\n" );
            counter++;
        }


        // Finally, a visualization is created.
        SwingUtilities.invokeLater(() -> PlotCreator.createChart(linienzuege));

    } //Main Method


} //Class
