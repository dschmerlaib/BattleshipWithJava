package battleship;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        GameField gameField = new GameField();
        Ships ships = new Ships();
        gameField.PrintGameField();

        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < ships.Fleet.size(); i++) {
            Ship ship = ships.Fleet.get(i);

            System.out.println("Enter the coordinates of the " + ship.getInfoText());
            boolean repeat = true;
            while (repeat) {

                String input = scanner.nextLine();
                Coordinates coordinates = ship.setPosition(input);
                coordinates = gameField.CheckRestriction(coordinates);
                if (ship.position.haveErrors) {
                    System.out.println("Error!");

                } else {
                    gameField.AddShipPosition(coordinates);
                    gameField.PrintGameField();
                    repeat = false;
                }
            }

        }

    }


}


class GameField {
    public String[][] Value; // [row][col]
    Dictionary<String, Integer> RowIndex = new Hashtable<>();
    HashSet<String> RestrictedArea = new HashSet<>();

    public GameField() {

        RowIndex.put("A", 1);
        RowIndex.put("B", 2);
        RowIndex.put("C", 3);
        RowIndex.put("D", 4);
        RowIndex.put("E", 5);
        RowIndex.put("F", 6);
        RowIndex.put("G", 7);
        RowIndex.put("H", 8);
        RowIndex.put("I", 9);
        RowIndex.put("J", 10);

        Value = CreateField();
    }

    private String[][] CreateField() {

        String[][] gameField = new String[11][11];

        for (String[] row : gameField)
            Arrays.fill(row, "~");

        gameField[0][0] = " ";
        Enumeration<String> keys = RowIndex.keys();


        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            int i = RowIndex.get(key);
            gameField[0][i] = String.valueOf(i);
            gameField[i][0] = key;
        }


        return gameField;
    }

    public void PrintGameField() {
        for (int i = 0; i < 11; i++) {

            for (int j = 0; j < 11; j++) {

                System.out.print(Value[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("\n");
    }

    public void AddShipPosition(Coordinates coordinates) {

        final String placedSign = "O";

        if (coordinates.first.row == coordinates.second.row) {
            // same row


            int index = RowIndex.get(String.valueOf(coordinates.first.row));

            int start =coordinates.lowerColumnBound; //Math.min(coordinates.first.column, coordinates.second.column);
            int end =coordinates.upperColumnBound;// Math.max(coordinates.first.column, coordinates.second.column);

            for (int i = start; i <= end; i++) {
                this.Value[index][i] = placedSign;
                AddToRestricted(coordinates.first.row, i);
            }

        } else if (coordinates.first.column == coordinates.second.column) {
            // same column

            for (int i = 1; i < this.Value.length; i++) {
                char counter = this.Value[i][0].charAt(0);
                if (counter >= coordinates.lowerRowBound && counter <= coordinates.upperRowBound) { //<= >=
                    this.Value[i][coordinates.first.column] = placedSign;

                }
            }
        }
    }

    Coordinates CheckRestriction(Coordinates coordinates) {

        HashSet<String> stringsToCheck = new HashSet<String>();
        StringBuilder stringBuilder = new StringBuilder();

        if (coordinates.first.row == coordinates.second.row) //same row
        {

            for (int i = coordinates.lowerColumnBound; i <= coordinates.upperColumnBound; i++) {
                // stringsToCheck.add(String.valueOf(coordinates.first.row + i)); //maybe char +int = other char
                stringBuilder.append(coordinates.first.row);
                stringBuilder.append(i);
                stringsToCheck.add(stringBuilder.toString());
                stringBuilder.setLength(0);
            }

        } else //same column
        {
            for (char i = coordinates.lowerRowBound; i <= coordinates.upperRowBound; i++) {
                // stringsToCheck.add(String.valueOf(coordinates.first.row + i)); //maybe char +int = other char
                stringBuilder.append(i);
                stringBuilder.append(coordinates.first.column);
                stringsToCheck.add(stringBuilder.toString());
                stringBuilder.setLength(0);
            }
        }

        for (String stringCoordinate : stringsToCheck) {
            if (RestrictedArea.contains(stringCoordinate)) {
                coordinates.haveErrors = true;
                break;
            }
        }
        return coordinates;
    }

    private void AddToRestricted(char row, int column) {
        RestrictedArea.add(String.valueOf(row) + String.valueOf(column));
    }

    private String GetPartsAsString(Coordinates coordinates) {


        StringBuilder stringBuilder = new StringBuilder();

        if (coordinates.first.row == coordinates.second.row) {

            int x0 = coordinates.first.column;
            int y0 = coordinates.second.column;

            if (x0 < y0) {
                for (int i = x0; i <= y0; i++) {
                    stringBuilder.append(coordinates.first.row);
                    stringBuilder.append(i);
                    stringBuilder.append(" ");
                }
            } else {

                for (int i = x0; i >= y0; i--) {
                    stringBuilder.append(coordinates.first.row);
                    stringBuilder.append(i);
                    stringBuilder.append(" ");
                }
            }

        } else if (coordinates.first.row > coordinates.second.row) {
            for (char i = coordinates.second.row; i <= coordinates.first.row; i++) {
                stringBuilder.append(i);
                stringBuilder.append(coordinates.first.column);
                stringBuilder.append(" ");
            }
        } else {
            for (char i = coordinates.first.row; i <= coordinates.second.row; i++) {
                stringBuilder.append(i);
                stringBuilder.append(coordinates.first.column);
                stringBuilder.append(" ");
            }
        }

        return stringBuilder.toString();
    }
}

class Coordinate {
    public char row;
    public int column;

    Coordinate(String coord) {
        String[] parts = coord.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        row = parts[0].charAt(0);
        column = Integer.parseInt(parts[1]);
    }
}

class Coordinates {


    public Coordinate first;
    public Coordinate second;
    boolean haveErrors = false;

    public char lowerRowBound;
    public char upperRowBound;
    public String lowerRowBoundString;
    public String upperRowBoundString;


    public int lowerColumnBound;
    public int upperColumnBound;

    Coordinates(String coordinateString) {
        String[] coords = coordinateString.split(" ");
        first = new Coordinate(coords[0]);
        second = new Coordinate(coords[1]);
        DefineBounds();
        haveErrors = checkForErrors();

    }

    private boolean checkForErrors() {
        char lowerBound = 'A';
        char upperBound = 'J';
        int lowerColumnBound = 1;
        int upperColumnBound = 10;

        return first.row != second.row
                && first.column != second.column
                || first.column > upperColumnBound
                || second.column > upperColumnBound
                || first.column < lowerColumnBound
                || second.column < lowerColumnBound
                || first.row < lowerBound
                || second.row < lowerBound
                || first.row > upperBound
                || second.row > upperBound;

    }

    public void DefineBounds() {
        lowerRowBound = first.row <= second.row ? first.row : second.row;
        upperRowBound = first.row <= second.row ? second.row : first.row;

        lowerRowBoundString = String.valueOf(lowerRowBound);
        upperRowBoundString = String.valueOf(upperRowBound);


        lowerColumnBound = Math.min(first.column, second.column);
        upperColumnBound = Math.max(first.column, second.column);
    }
}

class Ships {

    public ArrayList<Ship> Fleet = new ArrayList<Ship>();

    Ships() {
        // Fleet.add(new Ship(0, "Aircraft Carrier", 5));
        //  Fleet.add(new Ship(1, "Battleship", 4));
        //  Fleet.add(new Ship(2, "Submarine", 3));
        Fleet.add(new Ship(3, "Cruiser", 3));
       // Fleet.add(new Ship(4, "Destroyer", 2));
    }

}

class Ship {
    public String name;
    public int length;
    public int orderNumber;

    public Coordinates position;

    public Ship(int orderNumber, String name, int length) {
        this.name = name;
        this.length = length;
        this.orderNumber = orderNumber;
    }

    public String getInfoText() {
        return name + "(" + length + " cells)";
    }

    public Coordinates setPosition(String position) {
        this.position = new Coordinates(position);
        this.position.haveErrors = !shipFits() || !shipInLine();

        return this.position;
    }

    private boolean shipFits() {
        return this.length == GetLength();
    }

    private boolean shipInLine() {
        return this.position.first.row == this.position.second.row || this.position.first.column == this.position.second.column;
    }

    public int GetLength() {


        if (this.position.first.row == this.position.second.row) {
            return Math.abs(this.position.first.column - this.position.second.column) + 1;
        } else if (this.position.first.row > this.position.second.row) {
            return Math.abs(this.position.first.row - this.position.second.row) + 1;
        } else {
            return Math.abs(this.position.second.row - this.position.first.row) + 1;
        }
    }
}

