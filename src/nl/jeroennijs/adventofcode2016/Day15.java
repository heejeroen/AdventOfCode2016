package nl.jeroennijs.adventofcode2016;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;



public class Day15 {
    private static Pattern discInitPattern = Pattern.compile("Disc #(\\d) has (\\d+) positions; at time=(\\d+), it is at position (\\d+)\\.");

    public static void main(String[] args) throws IOException {
        List<Disc> discs1 = readDiscs();

        System.out.println("First capsule falls through at time: " + getTimeWhenDiscsOpen(discs1) + " (discs:" + print(discs1) + ")");

        List<Disc> discs2 = readDiscs();
        discs2.add(new Disc("7", 11, 0));

        System.out.println("Second capsule falls through at time: " + getTimeWhenDiscsOpen(discs2) + " (discs:" + print(discs2) + ")");
    }

    private static int getTimeWhenDiscsOpen(List<Disc> discs) {
        int time = 0;
        while (!isAllOpen(discs)) {
            discs.forEach(Disc::turn);
            time++;
        }
        return time;
    }

    private static List<Disc> readDiscs() throws IOException {
        return Arrays.stream(Utils.readLines("day15.txt"))
            .map(discInitLine -> discInitPattern.matcher(discInitLine))
            .filter(Matcher::matches)
            .map(matcher -> new Disc(matcher.group(1), Integer.valueOf(matcher.group(2)), Integer.valueOf(matcher.group(4))))
            .collect(Collectors.toList());
    }

    private static String print(List<Disc> discs) {
        StringBuilder result = new StringBuilder();
        for (Disc disc : discs) {
            result.append(" ");
            result.append(disc);
        }
        return result.toString();
    }

    private static boolean isAllOpen(List<Disc> discs) {
        int time = 1;
        for (Disc disc : discs) {
            if (!disc.isSlotOpenIn(time)) return false;
            time++;
        }
        return true;
    }

    private static class Disc {
        private final String id;
        private final int nrPositions;
        private int currentPosition;

        public Disc(String id, int nrPositions, int startPosition) {
            this.id = id;
            this.nrPositions = nrPositions;
            this.currentPosition = startPosition;
        }

        boolean isSlotOpenIn(int time) {
            return (currentPosition + time) % nrPositions == 0;
        }

        void turn() {
            currentPosition++;
            if (currentPosition == nrPositions) {
                currentPosition = 0;
            }
        }

        @Override public String toString() {
            return id + ":" + currentPosition;
        }
    }
}
