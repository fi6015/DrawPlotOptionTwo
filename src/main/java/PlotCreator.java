import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Class for visualizing the calculated assembled lines
 */
public class PlotCreator {

    /**
     * Displays the calculated line compositions (polylines) each in a different colour in a 2d coordinate System in a window.
     * @param linienzuege 2d Array of line compositions (returned by LineCrafter Class)
     *                   An inner list consists of consecutive points and characterizes one polyline.
     */
    public static void createChart(ArrayList<ArrayList<Point>> linienzuege){

        // Create an initial line composition / polyline to initialize the chart.
        // Subsequent polylines will be iteratively added.

        // Arrays with a size equal to the number of points in the polyline
        double[] xData = new double[linienzuege.get(0).size()];
        double[] yData = new double[linienzuege.get(0).size()];


        // Fill the arrays with the corresponding data from the polyline
        for (int i = 0; i < linienzuege.get(0).size(); i++) {
            xData[i] = linienzuege.get(0).get(i).getX();
            yData[i] = linienzuege.get(0).get(i).getY();
        }

        // Initialize the chart with the first polyline
        XYChart chart = QuickChart.getChart("Linienzuege", "X", "Y", "Linienzug 1", xData, yData);

        //Add all remaining polylines to the chart. As the first is already included, proceed with the second polyline
        //Which is the second inner list from linienzuege.
        for (int i = 1; i < linienzuege.size(); i++) {
            double[] x = new double[linienzuege.get(i).size()];
            double[] y = new double[linienzuege.get(i).size()];

            //Fill x and y with the coordinates of the polyline
            for (int j = 0; j < linienzuege.get(i).size(); j++) {
                x[j] = linienzuege.get(i).get(j).getX();
                y[j] = linienzuege.get(i).get(j).getY();
            }
            //add the polyline to the chart
            chart.addSeries("Linienzug " + (i+1), x, y);
        }

        JFrame frame = new JFrame("Linienzuege");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(new XChartPanel<>(chart), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

    }
}

