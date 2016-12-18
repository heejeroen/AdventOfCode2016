package nl.jeroennijs.adventofcode2016;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nl.jeroennijs.adventofcode2016.Utils.readLines;



public class Day08 {
    private static final char PIXEL_ON = '#';
    private static final char PIXEL_OFF = '.';
    private static Pattern rectInstruction = Pattern.compile("rect (\\d+)x(\\d+)");
    private static Pattern rotateInstruction = Pattern.compile("rotate (column|row) (x|y)=(\\d+) by (\\d+)");



    public enum RowOrColumn {
        row,
        column
    }

    public static void main(String[] args) throws IOException {
        char[][] grid = new char[6][50];
        fillRect(grid, grid[0].length, grid.length, PIXEL_OFF);
        print(grid);

        executeInstructions(grid, readLines("day08.txt"));

        int pixels = countPixels(grid);
        System.out.println("Pixels lit = " + pixels);
    }

    private static void executeInstructions(char[][] grid, String[] instructions) {
        for (String instruction : instructions) {
            System.out.println(instruction);
            if (!tryRect(instruction, grid)) {
                tryRotate(instruction, grid);
            }
            print(grid);
        }
    }

    private static boolean tryRect(String instruction, char[][] grid) {
        final Matcher rectMatcher = rectInstruction.matcher(instruction);
        if (!rectMatcher.matches()) {
            return false;
        }
        fillRect(grid, Integer.valueOf(rectMatcher.group(1)), Integer.valueOf(rectMatcher.group(2)), PIXEL_ON);
        return true;
    }

    private static void tryRotate(String instruction, char[][] grid) {
        final Matcher rotateMatcher = rotateInstruction.matcher(instruction);
        if (!rotateMatcher.matches()) {
            return;
        }
        rotate(grid, RowOrColumn.valueOf(rotateMatcher.group(1)), Integer.valueOf(rotateMatcher.group(3)), Integer.valueOf(rotateMatcher.group(4)));
    }

    private static void print(char[][] grid) {
        for (char[] row : grid) {
            System.out.println(row);
        }
        System.out.println();
    }

    private static void fillRect(char[][] grid, Integer sizeX, Integer sizeY, char value) {
        for (int row = 0; row < sizeY; row++) {
            Arrays.fill(grid[row], 0, sizeX, value);
        }
    }

    private static void rotate(char[][] grid, RowOrColumn rowOrColumn, Integer rowOrColumnNumber, Integer distance) {
        char[] bitsToRotate = getBitsToRotate(grid, rowOrColumn, rowOrColumnNumber);
        bitsToRotate = rotateRight(bitsToRotate, distance);
        insertIntoGrid(grid, bitsToRotate, rowOrColumn, rowOrColumnNumber);
    }

    private static char[] getBitsToRotate(char[][] grid, RowOrColumn rowOrColumn, Integer rowOrColumnNumber) {
        switch (rowOrColumn) {
            case row:
                return grid[rowOrColumnNumber];
            case column:
                return getColumn(grid, rowOrColumnNumber);
            default:
                throw new IllegalArgumentException("Unknown enum");
        }
    }

    private static char[] getColumn(char[][] grid, int column) {
        char[] result = new char[grid.length];
        for (int i = 0; i < grid.length; i++) {
            result[i] = grid[i][column];
        }
        return result;
    }

    private static char[] rotateRight(char[] bitsToRotate, int distance) {
        String result = String.valueOf(bitsToRotate);
        final int split = bitsToRotate.length - distance % bitsToRotate.length;
        return (result.substring(split) + result.substring(0, split)).toCharArray();
    }

    private static void insertIntoGrid(char[][] grid, char[] value, RowOrColumn rowOrColumn, int rowOrColumnNumber) {
        switch (rowOrColumn) {
            case row:
                grid[rowOrColumnNumber] = value;
                break;
            case column:
                insertIntoColumn(grid, value, rowOrColumnNumber);
                break;
        }
    }

    private static void insertIntoColumn(char[][] grid, char[] value, int column) {
        for (int i = 0; i < value.length; i++) {
            grid[i][column] = value[i];
        }
    }

    private static int countPixels(char[][] grid) {
        int total = 0;
        for (char[] row : grid) {
            for (char cell : row) {
                if (cell == PIXEL_ON)
                    total++;
            }
        }
        return total;
    }
}
