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
        System.out.println("The game starts");
        gameField.PrintGameField();
        boolean repeat = true;

        while (repeat) {
            System.out.println("Take a shot");
            Coordinate shot = new Coordinate(scanner.nextLine());

            if (gameField.coordinateFitIsInField(shot)) {
                gameField.TakeShot(shot);
                gameField.PrintGameField();
                repeat = false;
            }

        }
    }


}


class GameField {
    public String[][] Value; // [row][col]
    Dictionary<String, Integer> RowIndex = new Hashtable<>();
    Dictionary<String, String> ReverseRowIndex = new Hashtable<>();
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

        CreateReverseRowIndex();

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

    public boolean coordinateFitIsInField(Coordinate coordinate) {
        int rowIndex = RowIndex.get(String.valueOf(coordinate.row));
        int columnIndex = coordinate.column;

        return rowIndex >= 1 && rowIndex <= 11 && columnIndex >= 1 && columnIndex <= 10;

    }

    public void TakeShot(Coordinate coordinate) {


        int rowIndex = RowIndex.get(String.valueOf(coordinate.row));
        int columnIndex = coordinate.column;

        if (this.Value[rowIndex][columnIndex].equals("O")) {
            this.Value[rowIndex][columnIndex] = "X";
            System.out.println("You hit a ship!");
        } else {
            System.out.println("You missed!");
        }
    }

    private void CreateReverseRowIndex() {

        Enumeration<String> keys = this.RowIndex.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            this.ReverseRowIndex.put(String.valueOf(this.RowIndex.get(key)), key);
        }
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
            AddShipInRow(coordinates, placedSign);

        } else if (coordinates.first.column == coordinates.second.column) {
            // same column

            AddShipInColumn(coordinates, placedSign);
        }

    }

    private void AddShipInColumn(Coordinates coordinates, String placedSign) {
        int start = RowIndex.get(String.valueOf(coordinates.lowerRowBound));
        int end = RowIndex.get(String.valueOf(coordinates.upperRowBound));

        Enumeration<String> keys = RowIndex.keys();

        for (int i = start; i <= end; i++) {

            if (keys.hasMoreElements()) {
                this.Value[i][coordinates.first.column] = placedSign;
                String letter = this.ReverseRowIndex.get(String.valueOf(i));
                AddToRestrictedArea(letter, coordinates.first.column);


                if (i == start && i > 1) { // oben
                    char helper = coordinates.first.row;
                    AddToRestrictedArea(--helper, coordinates.first.column);
                } else if (i == end && i < 11) {// unten
                    char helper = coordinates.second.row;

                    AddToRestrictedArea(++helper, coordinates.first.column);
                }
                if (coordinates.first.column > 2) // linke spalte
                {
                    AddToRestrictedArea(letter, coordinates.first.column - 1);
                }
                if (coordinates.first.column < 10) // rechte spalte
                {
                    AddToRestrictedArea(letter, coordinates.first.column + 1);
                }
            }
        }
    }

    private void AddShipInRow(Coordinates coordinates, String placedSign) {
        int index = RowIndex.get(String.valueOf(coordinates.first.row));

        int start = coordinates.lowerColumnBound;
        int end = coordinates.upperColumnBound;

        for (int i = start; i <= end; i++) {
            this.Value[index][i] = placedSign;
            AddToRestrictedArea(coordinates.first.row, i);

            if (i == start && i > 1) {
                AddToRestrictedArea(coordinates.first.row, i - 1); //left
            } else if (i == end && i < 11) {
                AddToRestrictedArea(coordinates.first.row, i + 1); // right
            }
            if (index > 1) //oben
            {
                char helper = coordinates.first.row;
                AddToRestrictedArea(--helper, i);
            }
            if (index < 11) //unten
            {
                char helper = coordinates.first.row;
                AddToRestrictedArea(++helper, i);
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
                System.out.println("Too close to another ship");
                break;
            }
        }
        return coordinates;
    }

    private void AddToRestrictedArea(String row, int column) {
        RestrictedArea.add(row + String.valueOf(column));
    }

    private void AddToRestrictedArea(char row, int column) {
        AddToRestrictedArea(String.valueOf(row), column);
        //RestrictedArea.add(String.valueOf(row) + String.valueOf(column));
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
        Fleet.add(new Ship(0, "Aircraft Carrier", 5));
        Fleet.add(new Ship(1, "Battleship", 4));
        Fleet.add(new Ship(2, "Submarine", 3));
        Fleet.add(new Ship(3, "Cruiser", 3));
        Fleet.add(new Ship(4, "Destroyer", 2));
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




