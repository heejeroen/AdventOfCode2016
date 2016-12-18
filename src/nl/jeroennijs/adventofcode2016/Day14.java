package nl.jeroennijs.adventofcode2016;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import static nl.jeroennijs.adventofcode2016.Utils.getMd5Digest;
import static nl.jeroennijs.adventofcode2016.Utils.getMd5Hash;



public class Day14 {
    private static Map<Integer, String> cache = new HashMap<>();

    public static void main(String[] args) {
        final MessageDigest md = getMd5Digest();
        final String salt = "zpqevtbw";
        generateKeys(md, salt, 0);
        generateKeys(md, salt, 2016);
    }

    private static void generateKeys(final MessageDigest md, final String salt, final int iterations) {
        System.out.println("Generating keys with " + iterations + " extra iterations");
        cache.clear();
        int keysFound = 0;
        for (int i = 0; keysFound < 64; i++) {
            final String hash = calculateHash(md, salt, i, iterations);
            final char tripletCharacter = containsTriplet(hash);
            if (tripletCharacter != 0) {
                if (nextHashesContainQuintuple(md, salt, i, tripletCharacter, iterations)) {
                    System.out.println("Index " + i + " has key " + keysFound + " = " + hash);
                    keysFound++;
                }
            }
            cache.remove(i);
        }
    }

    private static char containsTriplet(final String hash) {
        final char[] charArray = hash.toCharArray();
        for (int i = 0; i < charArray.length - 2; i++) {
            final char c = charArray[i];
            if (c == charArray[i + 1] && c == charArray[i + 2]) {
                return c;
            }
        }
        return 0;
    }

    private static boolean nextHashesContainQuintuple(final MessageDigest md, final String salt, final int start, final char tripletCharacter, final int iterations) {
        final String quintuple = new String(new char[]{tripletCharacter, tripletCharacter, tripletCharacter, tripletCharacter, tripletCharacter});
        for (int i = 0; i < 1000; i++) {
            if (calculateHash(md, salt, i + start + 1, iterations).contains(quintuple)) {
                return true;
            }
        }
        return false;
    }

    private static String calculateHash(final MessageDigest md, final String salt, final int i, final int iterations) {
        if (cache.containsKey(i)) {
            return cache.get(i);
        }
        String hash = getMd5Hash(md, salt + String.valueOf(i)).toLowerCase();
        for (int j = 0; j < iterations; j++) {
            hash = getMd5Hash(md, hash).toLowerCase();
        }
        cache.put(i, hash);
        return hash;
    }

}
