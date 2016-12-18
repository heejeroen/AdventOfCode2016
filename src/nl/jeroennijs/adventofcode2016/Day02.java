package nl.jeroennijs.adventofcode2016;

import java.io.IOException;

import static nl.jeroennijs.adventofcode2016.Utils.readLines;



public class Day02 {
    private static final String[][] keypadOne = {
        { "1", "2", "3" },
        { "4", "5", "6" },
        { "7", "8", "9" }
    };
    private static final String[][] keypadTwo = {
        { "", "", "1", "", "" },
        { "", "2", "3", "4", "" },
        { "5", "6", "7", "8", "9" },
        { "", "A", "B", "C", "" },
        { "", "", "D", "", "" }
    };

    public static void main(String[] args) throws IOException {
        String[] instructions = readLines("day02.txt");

        String codeOne = findCode(instructions, 1, 1, keypadOne);
        System.out.println("Code 1 = " + codeOne);
        String codeTwo = findCode(instructions, 0, 2, keypadTwo);
        System.out.println("Code 2 = " + codeTwo);
    }

    private static String findCode(String[] instructions, int startX, int startY, String[][] keypad) {
        int x = startX;
        int y = startY;
        int maxX = keypad[0].length - 1;
        int maxY = keypad.length - 1;
        StringBuilder result = new StringBuilder();
        for (String instruction : instructions) {
            for (char instructionChar : instruction.toCharArray()) {
                switch (instructionChar) {
                    case 'U':
                        if (y > 0 && !keypad[y - 1][x].isEmpty())
                            y--;
                        break;
                    case 'D':
                        if (y < maxY && !keypad[y + 1][x].isEmpty())
                            y++;
                        break;
                    case 'L':
                        if (x > 0 && !keypad[y][x - 1].isEmpty())
                            x--;
                        break;
                    case 'R':
                        if (x < maxX && !keypad[y][x + 1].isEmpty())
                            x++;
                        break;
                    default:
                        break;
                }
            }
            result.append(keypad[y][x]);
        }
        return result.toString();
    }
}
