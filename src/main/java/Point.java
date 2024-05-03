import java.util.ArrayList;

public class Point {

    private int x;
    private int y;
    private int type = 4;
    //0 falls der typ noch nicht zugewiesen wurde


    //Konstruktor
    public Point(int x, int y, int type){
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    //true wenn x und y von 2 Punkten übereinstimmen, false sonst. true auch wenn das obj mit sich selbst verglichen wird.
    //das muss später bei den vergleichen mit liste1,liste2. liste3 beachten.
    //wenn ich zwei punkt vergleiche muss ich sicherstellen, dass ich auch immer checke ob es sich um dieselbe instanz handelt
    //mit point1 != point2
    public boolean equals(Object obj) {
        if (this == obj) {
            return false; // Gleiches Objekt im Speicher
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Objekt ist null oder nicht vom selben Typ
        }
        Point otherPoint = (Point) obj; // Casting des Objekts zu Point
        return this.x == otherPoint.x && this.y == otherPoint.y; // Vergleich der x- und y-Koordinaten
    }


    // toString-Methode für die Darstellung des Punktes
    @Override
    public String toString() {
        return "( x,y: " + x + ", " + y + ")" +" and type " + type;
    }

}
