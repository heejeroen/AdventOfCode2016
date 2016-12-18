package nl.jeroennijs.adventofcode2016;

import java.io.IOException;

import static nl.jeroennijs.adventofcode2016.Utils.readLines;



public class Day06 {
    public static void main(String[] args) throws IOException {
        String[] messageParts = readLines("day06.txt");
        StringBuilder result1 = new StringBuilder();
        StringBuilder result2 = new StringBuilder();
        for (int i = 0; i < messageParts[0].length(); i++) {
            int[] frequencies = getFrequencies(messageParts, i);
            int index1 = getIndexWithHighestFrequency(frequencies);
            result1.append((char) (index1 + 'a'));
            int index2 = getIndexWithLowestFrequency(frequencies, frequencies[index1]);
            result2.append((char) (index2 + 'a'));
        }
        System.out.println("Message 1 = " + result1);
        System.out.println("Message 2 = " + result2);
    }

    private static int[] getFrequencies(String[] messageParts, int index) {
        int[] frequencies = new int[26];
        for (String messagePart : messageParts) {
            frequencies[messagePart.charAt(index) - 'a']++;
        }
        return frequencies;
    }

    private static int getIndexWithHighestFrequency(int[] frequencies) {
        int j = 0;
        int max = 0;
        int index = 0;
        for (int frequency : frequencies) {
            if (frequency > max) {
                max = frequency;
                index = j;
            }
            j++;
        }
        return index;
    }

    private static int getIndexWithLowestFrequency(int[] frequencies, int maxFrequency) {
        int j = 0;
        int min = maxFrequency;
        int index = 0;
        for (int frequency : frequencies) {
            if (frequency > 0 && frequency < min) {
                min = frequency;
                index = j;
            }
            j++;
        }
        return index;
    }
}
