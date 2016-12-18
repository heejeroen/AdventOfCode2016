package nl.jeroennijs.adventofcode2016;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nl.jeroennijs.adventofcode2016.Utils.readLines;



public class Day04 {

    private static final Pattern ROOM_PATTERN = Pattern.compile("([a-z-]+)-([0-9]+)\\[([a-z]+)\\]");

    public static void main(String[] args) throws IOException {
        int total = 0;
        for (String roomId : readLines("day04.txt")) {
            final Room room = getRoom(roomId);
            if (room != null) {
                final boolean validRoom = isValidRoom(room);
                if (validRoom) {
                    total += Integer.valueOf(room.getSectorId());
                    System.out.println("Decrypted room name with sector Id " + room.getSectorId() + ": " + decryptName(room.getName(), room.getSectorId()));
                }
            }
        }
        System.out.println("Sum of sector IDs of valid rooms = " + total);
    }

    private static String decryptName(String name, String sectorId) {
        Integer i = Integer.valueOf(sectorId);
        StringBuilder result = new StringBuilder();
        for (char c : name.toCharArray()) {
            result.append(rotate(c, i));
        }
        return result.toString();
    }

    private static char rotate(char c, int sectorId) {
        if (c == '-') return c;

        char newChar = (char) (sectorId % 26 + c);

        return newChar > 'z' ? (char) (newChar - 26) : newChar;
    }

    private static boolean isValidRoom(Room room) {
        String checksum = calculateChecksum(room.getName());
        return room.getChecksum().equals(checksum);
    }

    private static String calculateChecksum(String name) {
        String cleanName = name.replaceAll("-", "");
        int[] frequencies = getFrequencies(cleanName);
        StringBuilder checksum = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int index = getIndexOfMaximum(frequencies);
            checksum.append((char) (index + 'a'));
            frequencies[index] = 0;
        }
        return checksum.toString();
    }

    private static int getIndexOfMaximum(int[] frequencies) {
        int max = 0;
        int index = 0;
        for (int j = 0; j < 26; j++) {
            if (frequencies[j] > max) {
                max = frequencies[j];
                index = j;
            }
        }
        return index;
    }

    private static int[] getFrequencies(String cleanName) {
        int[] frequencies = new int[26];
        for (char c : cleanName.toCharArray()) {
            frequencies[c - 'a']++;
        }
        return frequencies;
    }

    private static Room getRoom(String id) {
        final Matcher matcher = ROOM_PATTERN.matcher(id);
        if (matcher.matches()) return new Room(matcher.group(1), matcher.group(2), matcher.group(3));
        return null;
    }

    public static class Room {
        private String name;
        private String sectorId;
        private String checksum;

        Room(String name, String sectorId, String checksum) {
            this.name = name;
            this.sectorId = sectorId;
            this.checksum = checksum;
        }

        String getName() {
            return name;
        }

        String getSectorId() {
            return sectorId;
        }

        String getChecksum() {
            return checksum;
        }
    }
}
