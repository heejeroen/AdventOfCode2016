package nl.jeroennijs.adventofcode2016;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static nl.jeroennijs.adventofcode2016.Utils.readLines;



public class Day07 {
    public static void main(String[] args) throws IOException {
        int totalTLS = 0;
        int totalSSL = 0;
        for (String address : readLines("day07.txt")) {
            final ParseResult parseResult = parseAddress(address.toCharArray());
            if (supportsTLS(parseResult.getRegulars(), parseResult.getHypers())) {
                totalTLS++;
            }
            if (supportsSSL(parseResult.getRegulars(), parseResult.getHypers())) {
                totalSSL++;
            }
        }
        System.out.println("Number of addresses with TLS: " + totalTLS);
        System.out.println("Number of addresses with SSL: " + totalSSL);
    }

    private static ParseResult parseAddress(char[] chars) {
        ParseResult result = new ParseResult();
        StringBuilder regular = new StringBuilder();
        StringBuilder hyper = new StringBuilder();
        boolean isHyper = false;
        for (char c : chars) {
            if (c == '[') {
                isHyper = true;
                if (regular.length() > 0) {
                    result.addRegular(regular.toString());
                    regular = new StringBuilder();
                }
                continue;
            }
            if (c == ']') {
                isHyper = false;
                if (hyper.length() > 0) {
                    result.addHyper(hyper.toString());
                    hyper = new StringBuilder();
                }
                continue;
            }
            if (isHyper) {
                hyper.append(c);
            } else {
                regular.append(c);
            }
        }
        if (!isHyper && regular.length() > 0)
            result.addRegular(regular.toString());

        return result;
    }

    private static boolean supportsTLS(List<String> regulars, List<String> hypers) {
        return hasAbba(hypers) ? false : hasAbba(regulars);
    }

    private static boolean hasAbba(List<String> values) {
        for (String value : values) {
            if (hasAbba(value.toCharArray())) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasAbba(char[] chars) {
        for (int i = 0; i < chars.length - 3; i++) {
            if (isAbba(chars, i)) {
                return true;
            }
        }
        return false;
    }

    private static boolean supportsSSL(List<String> regulars, List<String> hypers) {
        List<String> abas = collectAbas(regulars);
        List<String> babs = collectAbas(hypers);

        return hasAbasCorrespondingBabs(abas, babs);
    }

    private static boolean hasAbasCorrespondingBabs(List<String> abas, List<String> babs) {
        for (String aba : abas) {
            for (String bab : babs) {
                if (isCorresponding(aba, bab)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isCorresponding(String aba, String bab) {
        return aba.charAt(0) == bab.charAt(1) && aba.charAt(1) == bab.charAt(0);
    }

    private static List<String> collectAbas(List<String> values) {
        List<String> abas = new ArrayList<>();
        for (String value : values) {
            final char[] chars = value.toCharArray();
            for (int i = 0; i < chars.length - 2; i++) {
                if (isAba(chars, i)) {
                    abas.add(value.substring(i, i + 3));
                }
            }
        }
        return abas;
    }

    private static boolean isAbba(char[] chars, int i) {
        return chars[i] != chars[i + 1] && chars[i + 3] == chars[i] && chars[i + 2] == chars[i + 1];
    }

    private static boolean isAba(char[] chars, int i) {
        return chars[i] != chars[i + 1] && chars[i + 2] == chars[i];
    }

    public static class ParseResult {
        private List<String> regulars = new ArrayList<>();
        private List<String> hypers = new ArrayList<>();

        void addRegular(String regular) {
            regulars.add(regular);
        }

        void addHyper(String hyper) {
            hypers.add(hyper);
        }

        List<String> getRegulars() {
            return regulars;
        }

        List<String> getHypers() {
            return hypers;
        }
    }
}
