import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class InputHandler {

    public ArrayList<ArrayList<Integer>> readCoordinates(String filename){

        ArrayList<ArrayList<Integer>> coordsOfInitalLines = new ArrayList<>();

        try {

            File file = new File(filename);

            Scanner scanner = new Scanner(file);

            while (scanner.hasNext()){

                ArrayList<Integer> lineCoords = new ArrayList<>();

                String fileLine = scanner.nextLine();
                String[] numbers = fileLine.split(" ");

                for (String numberString: numbers
                     ) {
                    lineCoords.add(Integer.parseInt(numberString));
                }

                coordsOfInitalLines.add(lineCoords);

            }
            System.out.println("Extraction of coordinates from file has been successfull.");

        } catch (FileNotFoundException FnFexception){
            System.err.println("No file found ! " + filename);
            FnFexception.printStackTrace();
        }

        return coordsOfInitalLines;

    }

}
