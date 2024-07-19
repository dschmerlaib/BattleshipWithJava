package battleship;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String[][] gameField = CreateGameField();
        PrintGameField(gameField);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the coordinates of the ship:");
        String coordinates = scanner.nextLine();
        int length = GetLength(coordinates.split(" "));
        String parts = GetPartsAsString(coordinates.split(" "));
        System.out.println("Length: " + length);
        System.out.println("Parts: " + parts);


    }

    private static String GetPartsAsString(String[] coordinates) {
        char[] x = coordinates[0].toCharArray();
        char[] y = coordinates[1].toCharArray();

        StringBuilder stringBuilder = new StringBuilder();

        if (x[0] == y[0]) {

            int x0 = Character.getNumericValue(x[1]);
            int y0 = Character.getNumericValue(y[1]);

            if (x0 < y0) {
                for (int i = x0; i <= y0; i++) {
                    stringBuilder.append(x[0]);
                    stringBuilder.append(i);
                    stringBuilder.append(" ");
                }
            } else {

                for (int i = x0; i >= y0; i--) {
                    stringBuilder.append(x[0]);
                    stringBuilder.append(i);
                    stringBuilder.append(" ");
                }
            }

           /* for (int i = x0; i <= y0; i++) {
                stringBuilder.append(x[0]);
                stringBuilder.append(i);
                stringBuilder.append(" ");
            }*/
        } else if (x[0] > y[0]) {
            for (char i = y[0]; i <= x[0]; i++) {
                stringBuilder.append(i);
                stringBuilder.append(x[1]);
                stringBuilder.append(" ");
            }
        } else {
            for (char i = x[0]; i <= y[0]; i++) {
                stringBuilder.append(i);
                stringBuilder.append(x[1]);
                stringBuilder.append(" ");
            }
        }
// Todo G8 G10 Test 
        return stringBuilder.toString();
    }

    private static int GetLength(String[] coordinates) {

        char[] x = coordinates[0].toCharArray();
        char[] y = coordinates[1].toCharArray();

        if (x[0] == y[0]) {
            return Math.abs(x[1] - y[1]) + 1;
        } else if (x[0] > y[0]) {
            return Math.abs(x[0] - y[0]) + 1;
        } else {
            return Math.abs(y[0] - x[0]) + 1;
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
