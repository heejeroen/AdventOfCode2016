package nl.jeroennijs.adventofcode2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day10 {
    private static Map<Long, Bot> bots = new TreeMap<>();
    private static Map<Long, OutputBin> outputBins = new TreeMap<>();

    private static final Pattern setValuePattern = Pattern.compile("value (\\d+) goes to bot (\\d+)");
    private static final Pattern giveInstructionPattern = Pattern.compile("bot (\\d+) gives low to (bot|output) (\\d+) and high to (bot|output) (\\d+)");

    public static void main(String[] args) throws IOException {
        String fileName = "src/nl/jeroennijs/adventofcode2016/input/day10.txt";
        processLines(fileName, Day10::parseGiveInstructionLine);
        processLines(fileName, Day10::parseSetValueLine);
        print(bots);
        print(outputBins);
        System.out.println("Result of bin 0 x bin 1 x bin 2 = "
                + outputBins.get(0L).getValue()*outputBins.get(1L).getValue()*outputBins.get(2L).getValue());
    }

    private static void processLines(String fileName, Consumer<String> consumer) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.lines().forEach(consumer);
        }
    }

    private static <T> void print(Map<Long, T> map) {
        for (T value : map.values()) {
            System.out.println(value);
        }
    }

    private static void parseSetValueLine(String line) {
        Matcher matcher = setValuePattern.matcher(line);
        if (matcher.matches()) {
            Bot bot = getBot(Long.valueOf(matcher.group(2)));
            bot.setValue(Long.valueOf(matcher.group(1)));
        }
    }

    private static void parseGiveInstructionLine(String line) {
        Matcher matcher = giveInstructionPattern.matcher(line);
        if (matcher.matches()) {
            Bot from = getBot(Long.valueOf(matcher.group(1)));
            from.setLowSetter(getValueSetter(matcher.group(2), matcher.group(3)));
            from.setHighSetter(getValueSetter(matcher.group(4), matcher.group(5)));
        }
    }

    private static ValueSetter getValueSetter(String botOrOutput, String id) {
        Long longId = Long.valueOf(id);

        return botOrOutput.equals("bot") ? getBot(longId) : getOutputBin(longId);
    }

    private static Bot getBot(Long botId) {
        if (!bots.containsKey(botId)) {
            bots.put(botId, new Bot(botId));
        }
        return bots.get(botId);
    }

    private static OutputBin getOutputBin(Long outputId) {
        if (!outputBins.containsKey(outputId)) {
            outputBins.put(outputId, new OutputBin(outputId));
        }
        return outputBins.get(outputId);
    }

    public interface ValueSetter {
        void setValue(long value);
    }

    public static class Bot implements ValueSetter {
        long botId;
        long value1 = -1L;
        long value2 = -1L;
        ValueSetter lowSetter;
        ValueSetter highSetter;

        Bot(long botId) {
            this.botId = botId;
        }

        @Override
        public void setValue(long value) {
            if (value1 == -1L) {
                value1 = value;
            } else {
                value2 = value;
            }

            if (value1 != -1L && value2 != -1L) {
                lowSetter.setValue(getLow());
                highSetter.setValue(getHigh());
            }
        }

        void setLowSetter(ValueSetter lowSetter) {
            this.lowSetter = lowSetter;
        }

        void setHighSetter(ValueSetter highSetter) {
            this.highSetter = highSetter;
        }

        long getLow() {
            return Math.min(value1, value2);
        }

        long getHigh() {
            return Math.max(value1, value2);
        }

        @Override
        public String toString() {
            return "Bot with id: " + botId + " has low: " + getLow() + ", high: " + getHigh();
        }
    }

    public static class OutputBin implements ValueSetter {
        long outputId;
        long value;

        OutputBin(long outputId) {
            this.outputId = outputId;
        }

        @Override
        public void setValue(long value) {
            this.value = value;
        }

        long getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Output bin with id: " + outputId + " has value: " + getValue();
        }
    }
}
