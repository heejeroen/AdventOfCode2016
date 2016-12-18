package nl.jeroennijs.adventofcode2016;

import java.io.IOException;

import static nl.jeroennijs.adventofcode2016.Utils.readLines;



public class Day01 {
    public static void main(String[] args) throws IOException {
        int orientation = 90;
        long x = 0;
        long y = 0;
        for (String line : readLines("day01.txt")) {
            for (String instruction : line.split(", ")) {
                final char leftOrRight = instruction.charAt(0);
                orientation = turn(orientation, leftOrRight);
                int blocks = Integer.valueOf(instruction.substring(1));
                x = x + blocks * Math.round(Math.cos(radians(orientation)));
                y = y + blocks * Math.round(Math.sin(radians(orientation)));
                System.out.println("Instruction: " + instruction + " orientation: " + orientation + " x: " + x + " y: " + y);
            }
        }
        System.out.println("x: " + x + " y: " + y + " distance: " + (Math.abs(x) + Math.abs(y)));
    }

    private static double radians(int orientation) {
        return 2 * Math.PI * orientation / 360;
    }

    private static int turn(int orientation, char leftOrRight) {
        return orientation + (leftOrRight == 'L' ? 1 : -1) * 90;
    }
}
