package nl.jeroennijs.adventofcode2016;

import java.io.CharArrayReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day09 {

    private static final Pattern repeatPattern = Pattern.compile("(\\d+)x(\\d+)");

    public static void main(String[] args) throws IOException {
        try (Reader reader = new FileReader("src/nl/jeroennijs/adventofcode2016/input/day09.txt")) {
            long result = count(reader);
            System.out.println("Number of chars in result: " + result);
        }
    }

    private static long count(Reader reader) throws IOException {
        long count = 0L;
        int i = reader.read();
        while (i != -1) {
            count += countCharacters((char) i, reader);
            i = reader.read();
        }
        return count;
    }

    private static long countCharacters(char c, Reader reader) throws IOException {
        if (c != '(') {
            return 1L;
        }
        Repeater repeater = Repeater.valueOf(readRepeatInstruction(reader));
        char[] textToRepeat = getTextToRepeat(reader, repeater.getNumberOfChars());
        try (Reader textToRepeatReader = new CharArrayReader(textToRepeat)) {
            return repeater.getRepeatTimes() * count(textToRepeatReader);
        }
    }

    private static char[] getTextToRepeat(Reader reader, int numberOfChars) throws IOException {
        char[] buf = new char[numberOfChars];
        reader.read(buf, 0, numberOfChars);
        return buf;
    }

    private static String readRepeatInstruction(Reader reader) throws IOException {
        StringBuilder repeatInstruction = new StringBuilder();
        char c = (char) reader.read();
        while (c != ')') {
            repeatInstruction.append(c);
            c = (char) reader.read();
        }
        return repeatInstruction.toString();
    }

    public static class Repeater {
        int numberOfChars;
        int repeatTimes;

        static Repeater valueOf(String s) {
            Matcher repeatMatcher = repeatPattern.matcher(s);
            if (repeatMatcher.matches()) {
                return new Repeater(Integer.valueOf(repeatMatcher.group(1)), Integer.valueOf(repeatMatcher.group(2)));
            }
            return new Repeater(0, 0);
        }

        private Repeater(int numberOfChars, int repeatTimes) {
            this.numberOfChars = numberOfChars;
            this.repeatTimes = repeatTimes;
        }

        int getNumberOfChars() {
            return numberOfChars;
        }

        int getRepeatTimes() {
            return repeatTimes;
        }
    }
}
