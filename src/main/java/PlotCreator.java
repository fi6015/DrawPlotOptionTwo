import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PlotCreator {

    public static void drawChart(List<List<Integer>> lines) {
        double[] xData = new double[lines.get(0).size() / 2];
        double[] yData = new double[lines.get(0).size() / 2];

        for (int i = 0; i < lines.get(0).size(); i += 2) {
            xData[i / 2] = lines.get(0).get(i);
            yData[i / 2] = lines.get(0).get(i + 1);
        }

        XYChart chart = QuickChart.getChart("Linienzuege", "X", "Y", "Linie", xData, yData);

        for (int i = 1; i < lines.size(); i++) {
            double[] x = new double[lines.get(i).size() / 2];
            double[] y = new double[lines.get(i).size() / 2];
            for (int j = 0; j < lines.get(i).size(); j += 2) {
                x[j / 2] = lines.get(i).get(j);
                y[j / 2] = lines.get(i).get(j + 1);
            }
            chart.addSeries("Linie" + i, x, y);
        }

        JFrame frame = new JFrame("Linienzuege");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(new XChartPanel<>(chart), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}

