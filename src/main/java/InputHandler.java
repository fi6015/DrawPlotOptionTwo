import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class for handling the input data
 */
public class InputHandler {

    /**
     * Converts the coordinates of the input file into an according
     * 2-dimensional ArrayList with equivalent rows and columns
     * @param filename filename of the input data
     * @return 2d ArrayList, with every inner lists representing
     * one line in the input data file with the according coordinates.
     */
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
            System.out.println("Extraction of coordinates from file has been successful.");

        } catch (FileNotFoundException FnFexception){
            System.err.println("No file found ! " + filename);
            FnFexception.printStackTrace();
        }

        return coordsOfInitalLines;

    }

}
