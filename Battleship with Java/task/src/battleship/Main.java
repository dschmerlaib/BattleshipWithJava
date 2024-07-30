package battleship;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String[][] gameField = CreateGameField();
        PrintGameField(gameField);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the coordinates of the ship:");
        String input = scanner.nextLine();

        Coordinates coordinates = new Coordinates(input);

        if (coordinates.haveErrors) {
            System.out.println("Error!");
        } else {

            int length = GetLength(coordinates);
            String parts = GetPartsAsString(coordinates);
            System.out.println("Length: " + length);
            System.out.println("Parts: " + parts);

        }
    }


    private static String GetPartsAsString(Coordinates coordinates) {


        StringBuilder stringBuilder = new StringBuilder();

        if (coordinates.first.row == coordinates.second.row) {

            int x0 = coordinates.first.column;// Character.getNumericValue(x[1]);
            int y0 = coordinates.second.column; //Character.getNumericValue(y[1]);

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

           /* for (int i = x0; i <= y0; i++) {
                stringBuilder.append(x[0]);
                stringBuilder.append(i);
                stringBuilder.append(" ");
            }*/
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

    private static void PrintGameField(String[][] gameField) {
        for (int i = 0; i < 11; i++) {

            for (int j = 0; j < 11; j++) {

                System.out.print(gameField[i][j] + " ");
            }
            System.out.println();
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

