import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class LineCrafter {


    //nimmt rohe liste aus integer Koordinaten und macht daraus liste aus punkten, die alle type 4 haben

    /**
     * Konvertiert die Liste aus Integer x,y Koordinatenparen zu einer entsprechenden Liste aus Punkten mit selbigen Koordinaten
     * Jeder innere Liste aus der 2-dimensionalen ArrayList wird wird zu einer Liste aus zwei Punkten mit type 4.
     * Type 4 bedeutet es wurde noch kein Type zu zuwiesen.
     * @param rawCoordinates  2-dimensionalen ArrayList, in der jede innere Liste eine Linie beschreibt mit den Werten x1,x2,y1,y2
     * @return 2-dimensionale Arraylist, bei der jede innere Liste aus zwei Punkten besteht
     */
    public ArrayList<ArrayList<Point>> createStarterList(ArrayList<ArrayList<Integer>> rawCoordinates) {

        //hier einbauen dass die 2darraylist gecheckt wird, ob jeder eintrag 4 koodrinaten hat, sonst error in input !

        ArrayList<ArrayList<Point>> startList = new ArrayList<ArrayList<Point>>();

        for (ArrayList<Integer> startLine : rawCoordinates
        ) {
            ArrayList<Point> pointLine = new ArrayList<Point>();
            Point p1 = new Point(startLine.get(0), startLine.get(1), 4);
            Point p2 = new Point(startLine.get(2), startLine.get(3), 4);

            pointLine.add(p1);
            pointLine.add(p2);

            startList.add(pointLine);
        }
        return startList;
    }

    /**
     * Erstellt eine tiefe Kopie der Eingabeliste.
     * @param originalList Eingabeliste
     * @return Aushabeliste
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

    //zählt die häufigkeit jedes Punktes und setzt dadurch den attribut wert type im Punkt

    /**
     * Zählt für jeden Punkt in der Übergebenen Liste die Häufigkeit mit der dieser Punkt in der Liste vorkommt und setzt basierend auf
     * der Häufigkeit den type des Punktes. Wenn der Punkt mit sich selbst verglichen wird, erhöht sich der counter nicht.
     * type 1 = Punkt kommt genau einmal vor ( single point SP )
     * type 2 = Punkt kommt genau zwei mal vor ( connector point CN )
     * type 3 = Punkt kommt 3 oder mehrmals vor ( terminator point TER )
     * @param startList Eingabeliste aus Linien bestehend aus Punkten
     * @return modifizierte Eingabeliste, bei der jeder Punkt den korrekten type zugwiesen bekommen hat.
     */
    public ArrayList<ArrayList<Point>> getAllPointTypes(ArrayList<ArrayList<Point>> startList) {

        for (ArrayList<Point> line : startList) {
            // Durchlaufen jeder Punkt in der aktuellen Linie
            for (Point currentPoint : line) {
                int counter = 1;

                // Durchlaufen jeder andere Linie
                for (ArrayList<Point> otherLine : startList) {
                    // Durchlaufen jeder Punkt in der anderen Linie
                    for (Point otherPoint : otherLine) {
                        // Vergleichen des aktuellen Punktes mit jedem anderen Punkt
                        if (currentPoint.equals(otherPoint)) {
                            counter++;
                        }
                    }
                }

                //falls ein Punkt mehr als 3 mal vorkommt, zählt dieser auch als typ 3
                if (counter > 3) {
                    counter = 3;
                }

                currentPoint.setType(counter);
                //Liste aller Punkte enthält die korrekten Types. in startList haben alle Type1, sodass sie zwingen nur 1 mal verwendet werden
                // Ausgabe der Anzahl der aktuellen Punkte
                //System.out.println("Punkt " + currentPoint + " kommt " + counter + " mal vor.");
            }
        }
        return startList;
    }

    /**
     * Erstellt eine neue Linienliste auf Basis der Eingabe, aber ohne Linine die Terminatoren enthalten.
     * @param TypeAssignedList Liste aus Punkten mir ihren zugewiesenen types
     * @return neue bereinigte Liste in der die Linien nurnoch Punkte mit type 1 und 2 enthalten
     */
    public ArrayList<ArrayList<Point>> removeTerminators(ArrayList<ArrayList<Point>> TypeAssignedList) {

        ArrayList<ArrayList<Point>> withoutTerminators = new ArrayList<>();

        for (ArrayList<Point> line : TypeAssignedList) {

            boolean toRemove = false;

            for (Point currentPoint : line) {
                if (currentPoint.getType() == 3) {
                    toRemove = true;
                }
            }
            if (toRemove == false) { //linie wird beibehalten
                withoutTerminators.add(line);
            }
        }
        return withoutTerminators;
    }

    //erstellt die Liste singlePointsAndConnectors aus der StartListe,
    //setzt dabei alle Types in StartListe auf 1.
    //der PointType von Connectors muss bei 3 anfangen. das ist hier in der Funktion ein workaround.
    //das muss später bei coutoccurences schon richtig gemacht werden.

    /**
     * Erstellt aus der 2d Liste der Linien eine einfach 1d Liste aus Punkten dieser Linien.
     * Diese enthält nur noch SPs oder CNs. Nach dem Aufruf dieser Funktion gilt die neue
     * Codierung für die Connector types.
     * type 3 = Connector der noch nich verwendet wurde
     * type 2 = Connector der einmal verwendet wurde
     * type 1 = single Point
     * @param startListWithoutTer Liste aller Linien die keine Terminatoren enthalten
     * @return einfache Liste aller Punkte aus der Eingabedatei die entweder single Points oder connectoren sind,
     * nachdem die Terminatoren entfernt wurden. Alle SPs haben type 1, alle CNs type 3.
     */
    public ArrayList<Point> getSPsAndCNsAndSetStartlistTypes(ArrayList<ArrayList<Point>> startListWithoutTer){
        ArrayList<Point> singlePointsAndConnectors = new ArrayList<>();

        for (ArrayList<Point> line: startListWithoutTer
             ) {
            for (Point point: line
                 ) {
                int pointType = point.getType();
                if (pointType == 1){
                    singlePointsAndConnectors.add(new Point(point.getX(), point.getY(), point.getType()));
                    point.setType(1);
                } else if (pointType ==2){
                    if (singlePointsAndConnectors.contains(point)){
                        continue;
                    } else {
                        singlePointsAndConnectors.add(new Point(point.getX(), point.getY(), 3));
                    }
                }
            }
        }
        return singlePointsAndConnectors;
    }


    /**
     * Prüft für eine Liste von Punkten, ob alle type 0 haben und verwendet wurden.
     * @param singlePointsAndConnectors Liste von Punkten
     * @return true wenn alle Punkte type 0 haben und benutzt wurden, false wenn nicht.
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
     * Wenn ein aktueller Linienzug übergeben wird, wird auf diesem weitergearbeitet und current Point ist der letzte
     * Punkt in diesem Linienzug, welcher dann zurückgegeben wird. Wenn sich kein Linienzug im Erstellungsprozess befindet,
     * wird der nächste SP aus singlePointsAndConnectors  als current point zurückgegeben
     * @param singlePointsAndConnectors Liste aller Punkte
     * @param linienZug der gerade gebaut wird. Wenn die List leer ist, wird kein Linienzug gebaut.
     * @return Punkt mit welchem weitergearbeitet werden soll.
     */
    public Point getCurrentPoint(ArrayList<Point> singlePointsAndConnectors,
                                 ArrayList<Point> linienZug){
        if ( linienZug.isEmpty()){
            for (Point point: singlePointsAndConnectors
            ) {
                if (point.getType() == 1){
                    return  point;
                } else {
                    continue;
                }
            }
        } else if (!linienZug.isEmpty()){
            return linienZug.get(linienZug.size()-1);
        }
        return null;
    }

    /**
     * Such für den Punkt nach einem match.
     * Gibt für einen übergebenen Punkt current point den nächsten passenden Punkt zurück, sodass diese beiden Punkte
     * eine Linie bilden in der initialen Liste der Linien. Das finden eines CNs wird priorisiert über das finden eines SPs.
     * Wenn eine Linie aus zwei Punkten gefunden wurde, werden die types der matchenden Linie in starterList auf gesetzt.
     * Knoten von linienin der starterlist haben immer type 1 und sind nach einmaliger benutzung mit type 0 als gelöscht markiert.
     * @param singlePointsAndConnectors Liste der noch verfügbaren Punkte. Enthält auch gelöschte Punkte die aber nicht berücksichtigt werden.
     * @param currentPoint aktueller Punkt, fuer den ein match gefunden werden soll
     * @param starterList noch verfügbare Linien
     * @return Punkt der mir current point eine Linie bildet, die in starterList vorkommt.
     */
    public Point searchForCNorSP(ArrayList<Point> singlePointsAndConnectors,
                                 Point currentPoint,
                                 ArrayList<ArrayList<Point>> starterList
    ) {
        for (ArrayList<Point> line : starterList) {
            for (Point otherPoint : singlePointsAndConnectors
            ) {
                if (otherPoint.getType() == 2 || otherPoint.getType() == 3) {
                    if (line.get(0).equals(currentPoint) && line.get(1).equals(otherPoint)) {
                        //aktualisiere types in starterList
                        //makiere die benutze Linie als gelöscht.
                        line.get(0).setType(0);
                        line.get(1).setType(0);
                        return otherPoint;
                    } else if (line.get(1).equals(currentPoint) && line.get(0).equals(otherPoint)) { //vereinfach in eine IF Bed.
                        line.get(0).setType(0);
                        line.get(1).setType(0);
                        return otherPoint;
                    }
                } else {
                    continue;
                }
            }
        }
        for (ArrayList<Point> line : starterList
        ) {
            for (Point otherPoint : singlePointsAndConnectors
            ) {
                if (otherPoint.getType() == 1) {
                    if (line.get(0).equals(currentPoint) && line.get(1).equals(otherPoint)) {
                        //Muss ich hier noch prüfen ob neben p1,p2, auch p2,p1 Gilt ?
                        //makiere die benutze Linie als gelöscht.
                        line.get(0).setType(0);
                        line.get(1).setType(0);
                        return otherPoint;
                    } else if (line.get(1).equals(currentPoint) && line.get(0).equals(otherPoint)) { //vereinfach in eine IF bedingung.
                        line.get(0).setType(0);
                        line.get(1).setType(0);
                        return otherPoint;
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            }
        }
        //null if otherPoint != 1, 2, 3
        System.out.println("Hier gibts noch ein Problem: Type of this Point is " + currentPoint.getType());
        return null;
    }

    /**
     * Aktualisiert den type des Punktes in der singlePointsAndConnectors Liste.
     * Ist der Punkt ein SP (type 1) und wurde benutzt, erhält er nun ( type 0 )
     * Ist der Punkt ein CN und wurde noch nie benutzt ( type 3 ) erhält er nun nach einmaliger benutzung type 2
     * Wurde der Connector Punkt bereits einmal benutzt ( type 2 ) wird er als nicht mehr verfügbar markiert ( type 0 )
     * @param singlePointsAndConnectors enthält verfügbare Punkte mit types
     * @param currentPoint aktueller Punkt, dessen type aktualisiert wird.
     */
    public void updateType (ArrayList<Point> singlePointsAndConnectors,
                            Point currentPoint){

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
                }
            }
        }
    }

    /**
     * Rekursiver Algorithmus zur Bildung von Linienzügen.
     * @param starterList
     * @param singlePointsAndConnectors
     * @param linienZug
     * @param linienzuege
     * @return
     */
    public ArrayList<ArrayList<Point>> CraftConnectedLines(
            ArrayList<ArrayList<Point>> starterList,
            ArrayList<Point> singlePointsAndConnectors,
            ArrayList<Point> linienZug,
            ArrayList<ArrayList<Point>> linienzuege) {

        //gute Idee: Die Listen einfach durchschuffeln, dann kommt randomness rein in die erstellung.
        //Oder einfach singlePoints and Connectors shuffeln.
        //Oder get current Point

        // Basisfall: Wenn keine Punkte mehr übrig sind, um einen Linienzug zu bilden,
        // beende die Rekursion und gib die aktualisierte Liste der Linienzüge zurück.
        if (allPointsAreUsed(singlePointsAndConnectors)) {
            return linienzuege;
        }

        // Wir nehmen einen Punkt aus der Liste singlePointsAndConnectors.
        // entweder CN oder letzten SP
        Point currentPoint = getCurrentPoint(singlePointsAndConnectors,linienZug);

        // Erste Überlegung: Wenn der aktuelle Punkt ein SinglePoint ist (Type = 1)
        if (currentPoint.getType() == 1) {
            Point nextPoint = searchForCNorSP(singlePointsAndConnectors,currentPoint,starterList);
            //Bei übereinstimmung werden außerdem die Knoten in StarterList als gelöscht markiert
            if (nextPoint.getType() == 1){
                //Es gab keinen connector, also auch kein Linienzug. Einfache Linie wird hinzugefügt zulinienzüge.
                ArrayList<Point> linienzug = new ArrayList<>();
                linienzug.add(currentPoint);
                linienzug.add(nextPoint);
                linienzuege.add(new ArrayList<>(linienzug));
                linienzug.clear();
                //currentPoint.setType(0) und otherPoint.setType(0) in singlePointsAnd Connectors
                //Beide sind type = 1 und kommen daher nur 1 mal vor.
                //vergleich läuft über equals methode.
                updateType(singlePointsAndConnectors,currentPoint);
                updateType(singlePointsAndConnectors,nextPoint);
                //searchForCNorSP hat bereits die knoten der Linie gelöscht markiert

                //Wenn tryCreateLine erfolgreich ist und die Linie gefunden wird, wird CraftConnectedLines rekursiv mit der aktualisierten starterList aufgerufen,
                // da starterList als Referenz übergeben wird. Das bedeutet, dass die Änderungen, die in tryCreateLine an starterList vorgenommen werden, in der übergeordneten Funktion CraftConnectedLines sichtbar sind.
                //Daher wird, nachdem tryCreateLine erfolgreich war und CraftConnectedLines rekursiv aufgerufen wurde, die aktualisierte starterList verwendet, um den nächsten Linienzug zu erstellen.

                return CraftConnectedLines(starterList,singlePointsAndConnectors,linienzug,linienzuege);
            }
            if (nextPoint.getType() == 2 || nextPoint.getType()== 3){
                // Es gibt einen Connector und ein Linienzug wird gebaut
                ArrayList<Point> linienzug = new ArrayList<>();
                linienzug.add(currentPoint);
                linienzug.add(nextPoint);
                // In diesem Fall wird der Linienzug noch nicht zu Linienzüge hinzugefügt, sondern erst weiter ausgebaut
                updateType(singlePointsAndConnectors,currentPoint);
                updateType(singlePointsAndConnectors, nextPoint);

                return CraftConnectedLines(starterList,singlePointsAndConnectors,linienzug,linienzuege);
            }
        }
        if (currentPoint.getType() == 2 || currentPoint.getType() == 3){
            Point nextPoint = searchForCNorSP(singlePointsAndConnectors,currentPoint,starterList);
            if (nextPoint.getType() == 1){
                //Es gab keinen connector, also auch kein Linienzug. Einfache Linie wird hinzugefügt zulinienzüge.
                linienZug.add(nextPoint);
                linienzuege.add(new ArrayList<>(linienZug));
                linienZug.clear();
                //currentPoint.setType(0) und otherPoint.setType(0) in singlePointsAnd Connectors
                //Beide sind type = 1 und kommen daher nur 1 mal vor.
                //vergleich läuft über equals methode.
                updateType(singlePointsAndConnectors,currentPoint);
                updateType(singlePointsAndConnectors,nextPoint);
                //searchForCNorSP hat bereits die knoten der Linie gelöscht markiert

                return CraftConnectedLines(starterList,singlePointsAndConnectors,linienZug,linienzuege);
            }
            if (nextPoint.getType() == 2 || nextPoint.getType()== 3){
                // Es gibt einen Connector und ein Linienzug wird gebaut
                linienZug.add(nextPoint);

                // In diesem Fall wird der Linienzug noch nicht zu Linienzüge hinzugefügt, sondern erst weiter ausgebaut
                updateType(singlePointsAndConnectors,currentPoint);
                updateType(singlePointsAndConnectors, nextPoint);

                return CraftConnectedLines(starterList,singlePointsAndConnectors,linienZug,linienzuege);
            }
        }

        return null;
    }

    /**
     * Berechnet die Distanz zweier Punkte in einem kartesischen Koordinatensystem,
     * mit Hilfe des Satzes von Pythagoras.
     * @param p1 Punkt 1 mit seinen Koordinaten
     * @param p2 Punkt 2 mit seinen Koordinaten
     * @return Distanz der beiden Punkte zueinander
     */
    public double calculateDistance(Point p1, Point p2){
        //Satz des Pythagoras besagt: a^2 + b^2 = c^2, also ist c = Wurzel( a^2 + b^2 )
        int deltaX = p2.getX() - p1.getX();
        int deltaY = p2.getY() - p1.getY();

        //durch das Quadrieren kommt immer eine positive Zahl raus
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        return distance;
    }

    // ich kann noch einen Paramer einbauen, sodass die Distanz der Linien nicht addiert wird,
    //sondern nur die flugdistanz Zwischen p1 und p2 berechnet wird.

    /**
     * Sortiert eine Liste aus Linienzügen basierend auf ihrer Länge absteigend.
     * Die Länge eines Linienzugs setzt sich zusammen aus der Summe der Längen der Linien die diesen bilden.
     * @param linienzuege Liste an Linienzügen, wobei ein Linienzug aus mehreren Punkten besteht.
     * @return absteigend sortierte Liste der Linienzüge, basierend auf ihrer Länge.
     */
    public Map<Double, ArrayList<Point>> sortLinienzuege (ArrayList<ArrayList<Point>> linienzuege){

        //TreeMap fuer feste Reihenfolge bekommt reverse comparator uebergeben.
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
