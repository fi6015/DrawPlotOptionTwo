import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static List<List<Integer>> getLineCoordinates(ArrayList<ArrayList<Point>> linienzüge) {
        List<List<Integer>> linienzügeCoordinates = new ArrayList<>();

        for (ArrayList<Point> linie : linienzüge) {
            List<Integer> coordinates = new ArrayList<>();
            for (Point point : linie) {
                coordinates.add(point.getX());
                coordinates.add(point.getY());
            }
            linienzügeCoordinates.add(coordinates);
        }

        return linienzügeCoordinates;
    }





    public static void main(String[] args) {


        InputHandler inputHandler = new InputHandler();
        ArrayList<ArrayList<Integer>> rawCoordinates = inputHandler.readCoordinates("src/main/java/input.txt");

        // Print the result for verification
        for (ArrayList<Integer> coordinates : rawCoordinates) {
            System.out.println(coordinates);
        }

        LineCrafter lineCrafter = new LineCrafter();

        ArrayList<ArrayList<Point>> starterListNotypes = lineCrafter.createStarterList(rawCoordinates);

        ArrayList<ArrayList<Point>> starterListAllTypes = lineCrafter.getAllPointTypes(LineCrafter.deepCopy(starterListNotypes));

        ArrayList<ArrayList<Point>> starterListInitState = lineCrafter.removeTerminators(LineCrafter.deepCopy(starterListAllTypes));

        for (ArrayList<Point> line: starterListInitState
        ) {
            System.out.println("Die Linie besteht aus ");
            for (Point p: line
            ) {
                System.out.println(p.toString());
            }
        }
        System.out.println("Ende ");

        ArrayList<ArrayList<Point>> starterListFinal = lineCrafter.getAllPointTypes(LineCrafter.deepCopy(starterListInitState));

        for (ArrayList<Point> line: starterListFinal
        ) {
            System.out.println("Die Linie besteht aus ");
            for (Point p: line
            ) {
                System.out.println(p.toString());
            }
        }
        System.out.println("Ende ");

        ArrayList<Point> singlePointsAndConnectors = lineCrafter.getSPsAndCNsAndSetStartlistTypes(starterListFinal);

        ArrayList<Point> linienzugInitial = new ArrayList<>();

        ArrayList<ArrayList<Point>> linienzuegeInitial = new ArrayList<>();

        ArrayList<ArrayList<Point>> linienzuege = lineCrafter.CraftConnectedLines(starterListFinal, singlePointsAndConnectors,linienzugInitial,linienzuegeInitial);

        Map<Double, ArrayList<Point>> distances = lineCrafter.sortLinienzuege(linienzuege);

        for (Map.Entry<Double,ArrayList<Point>> entry: distances.entrySet()
             ) {
            System.out.println("Distanz des Linienzuges: " + entry.getKey() + "; x,y Koordinatenpaare der Punkte im Linienzug " + entry.getValue());
        }


       for (ArrayList<Point> line: linienzuege
                    ) {
                   System.out.println("Der Linienzug besteht aus ");
                   for (Point p: line
                        ) {
                       System.out.println(p.toString());
                   }
               }

        List<List<Integer>> linienzuegeCoordinates = getLineCoordinates(linienzuege);
        // Ausgabe zur Überprüfung
        for (List<Integer> coordinates : linienzuegeCoordinates) {
            System.out.println(coordinates);
        }

        SwingUtilities.invokeLater(() -> PlotCreator.drawChart(linienzuegeCoordinates));



    } //Main Method


} //Class
