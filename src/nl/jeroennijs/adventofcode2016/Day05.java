package nl.jeroennijs.adventofcode2016;

import java.security.MessageDigest;

import static nl.jeroennijs.adventofcode2016.Utils.getMd5Digest;
import static nl.jeroennijs.adventofcode2016.Utils.getMd5Hash;



public class Day05 {
    public static void main(String[] args) {
        final long start = System.currentTimeMillis();
        System.out.println("Start calculation of password 1");
        final String doorId = "ojvtpuvg";
        String password1 = findPassword1(doorId);
        System.out.println("Start calculation of password 2");
        String password2 = findPassword2(doorId);
        System.out.println("End calculation in " + (System.currentTimeMillis() - start));
        System.out.println("Password 1 = " + password1);
        System.out.println("Password 2 = " + password2);
    }

    private static String findPassword1(String doorId) {
        final MessageDigest md = getMd5Digest();
        String password = "";
        int i = 0;
        while (password.length() < 8) {
            final String hash = getMd5Hash(md, doorId + String.valueOf(i));
            if (hash.startsWith("00000")) {
                password += hash.charAt(5);
                System.out.println("Next character found after " + i + " iterations");
            }
            i++;
        }
        return password;
    }

    private static String findPassword2(String doorId) {
        final MessageDigest md = getMd5Digest();
        char[] password = new char[8];
        int i = 0;
        int charsFound = 0;
        while (charsFound < 8) {
            final String hash = getMd5Hash(md, doorId + String.valueOf(i));
            if (hash.startsWith("00000") && hash.charAt(5) >= '0' && hash.charAt(5) <= '7') {
                final int index = Character.getNumericValue(hash.charAt(5));
                if (password[index] == 0) {
                    password[index] = hash.charAt(6);
                    charsFound++;
                    System.out.println("Next character found after " + i + " iterations");
                }
            }
            i++;
        }
        return String.valueOf(password);
    }
}
