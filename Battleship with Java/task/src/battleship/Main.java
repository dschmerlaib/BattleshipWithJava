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


    private static String GetPartsAsString(Coordinates coordinates) {


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


    private static int GetLength(Coordinates coordinates) {


        if (coordinates.first.row == coordinates.second.row) {
            return Math.abs(coordinates.first.column - coordinates.second.column) + 1;
        } else if (coordinates.first.row > coordinates.second.row) {
            return Math.abs(coordinates.first.row - coordinates.second.row) + 1;
        } else {
            return Math.abs(coordinates.second.row - coordinates.first.row) + 1;
        }


    }


    private static String[][] CreateGameField() {
        String[][] gameField = new String[11][11];
        char initChar = 'A';
        for (int i = 0; i < 11; i++) {
            if (i > 0) {
                gameField[i][0] = String.valueOf(initChar);
                ++initChar;

                gameField[0][i] = String.valueOf(i);

            } else {
                gameField[i][0] = " ";
                gameField[0][i] = " ";
            }
            for (int j = 1; j < 11; j++) {
                gameField[i][j] = "~";
            }
        }
        return gameField;
    }

}

class GameField {
    public String[][] Value; // [row][col]

    public GameField() {
        Value = CreateField();
    }

    private String[][] CreateField() {
        String[][] gameField = new String[11][11];
        char initChar = 'A';
        for (int i = 0; i < 11; i++) {
            if (i > 0) {
                gameField[i][0] = String.valueOf(initChar);
                ++initChar;

                gameField[0][i] = String.valueOf(i);

            } else {
                gameField[i][0] = " ";
                gameField[0][i] = " ";
            }
            for (int j = 1; j < 11; j++) {
                gameField[i][j] = "~";
            }
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
    }

    public void AddShipPosition(Coordinates coordinates) {

        for (int i = 0; i < this.Value.length; i++) {
        }

        // same row
        if (coordinates.first.row == coordinates.second.row) {
            int index = 0;
            for (int i = 0; i < this.Value.length; i++) {

                index = i;
                if (this.Value[i][0].equals(String.valueOf(coordinates.first.row))) {
                    break;
                }
            }
            int start = coordinates.first.column <= coordinates.second.column ? coordinates.first.column : coordinates.second.column;
            int end = coordinates.first.column <= coordinates.second.column ? coordinates.second.column : coordinates.first.column;
            for (int i = start; i <= end; i++) {
                this.Value[index][i] = "O";
            }

        } else if (coordinates.first.column == coordinates.second.column) {
            // gleiche spalte

            for (int i = 1; i < this.Value.length; i++) { // 1.Zeile nummerierung
                //Hier Fehler
                char counter = this.Value[i][0].charAt(0);
                if (counter > coordinates.first.column && counter <= coordinates.second.row) { //<= >=
                    this.Value[i][coordinates.first.column] = "O";

                }
            }
        }
    }
}

class Coordinate {
    public char row;
    public int column;

    Coordinate(String coord) {
        String[] parts = coord.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        row = parts[0].charAt(0);
        column = Integer.valueOf(parts[1]);
    }
}

class Coordinates {
    final char lowerRowBound = 'A';
    final char upperRowBound = 'J';

    final int lowerColumnBound = 1;
    final int upperColumnBound = 10;

    public Coordinate first;
    public Coordinate second;
    boolean haveErrors = false;

    Coordinates(String coordinateString) {
        String[] coords = coordinateString.split(" ");
        first = new Coordinate(coords[0]);
        second = new Coordinate(coords[1]);

        haveErrors = first.row != second.row && first.column != second.column || first.column > upperColumnBound || second.column > upperColumnBound || first.column < lowerColumnBound || second.column < lowerColumnBound || first.row < lowerRowBound || second.row < lowerRowBound || first.row > upperRowBound || second.row > upperRowBound;


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

