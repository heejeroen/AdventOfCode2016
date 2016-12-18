package nl.jeroennijs.adventofcode2016;

public class Day16 {
    public static void main(String[] args) {
        final String start = "10001110011110000";
        System.out.println("Checksum 1 is " + generateContentAndChecksum(start, 272));
        System.out.println("Checksum 2 is " + generateContentAndChecksum(start, 35651584));
    }

    private static String generateContentAndChecksum(final String start, int desiredLength) {
        return calculateChecksum(generateContent(start, desiredLength));
    }

    private static String generateContent(final String start, int desiredLength) {
        if (start.length() >= desiredLength) return start.substring(0, desiredLength);
        return generateContent(start + "0" + invert(new StringBuilder(start).reverse().toString()), desiredLength);
    }

    private static String invert(final String value) {
        final StringBuilder result = new StringBuilder();
        for (char c : value.toCharArray()) {
            result.append(c == '0' ? '1' : '0');
        }
        return result.toString();
    }

    private static String calculateChecksum(final String content) {
        if (content.length() % 2 == 1) return content;
        return calculateChecksum(reducePairs(content));
    }

    private static String reducePairs(final String value) {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < value.length() - 1; i = i + 2) {
            result.append(value.charAt(i) == value.charAt(i + 1) ? '1' : '0');
        }
        return result.toString();
    }
}
