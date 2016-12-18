package nl.jeroennijs.adventofcode2016;

import java.io.IOException;
import java.util.Arrays;

import static nl.jeroennijs.adventofcode2016.Utils.readLines;



public class Day03 {
    public static void main(String[] args) throws IOException {
        final int[][] trianglesTable = parse(readLines("day03.txt"));
        System.out.println("Total possible horizontal: " + countHorizontalPossible(trianglesTable));
        System.out.println("Total possible vertical: " + countVerticalPossible(trianglesTable));
    }

    private static int[][] parse(String[] triangles) {
        int[][] trianglesTable = new int[triangles.length][3];
        int i = 0;
        for (String triangle : triangles) {
            trianglesTable[i] = parseRow(triangle);
            i++;
        }
        return trianglesTable;
    }

    private static int[] parseRow(String triangle) {
        return Arrays.stream(triangle.split(" +")).mapToInt(Integer::valueOf).toArray();
    }

    private static long countHorizontalPossible(int[][] trianglesTable) {
        return Arrays.stream(trianglesTable).filter(Day03::isValidTriangle).count();
    }

    private static int countVerticalPossible(int[][] trianglesTable) {
        int totalVerticalPossible = 0;
        for (int i = 0; i < trianglesTable.length; i = i + 3) {
            for (int column = 0; column < 3 ; column++) {
                if (isValidTriangle(trianglesTable[i][column], trianglesTable[i + 1][column], trianglesTable[i + 2][column])) {
                    totalVerticalPossible++;
                }
            }
        }
        return totalVerticalPossible;
    }

    private static boolean isValidTriangle(int... unsortedSides) {
        int[] sides = Arrays.copyOf(unsortedSides, unsortedSides.length);
        Arrays.sort(sides);
        return sides[0] + sides[1] > sides[2];
    }
}
