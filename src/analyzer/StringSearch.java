package analyzer;

public class StringSearch {
    public static boolean naive(String text, String sub) {
        for (int i = 0; i < text.length() - sub.length() + 1; i++) {
            boolean match = true;
            for (int j = 0; j < sub.length(); j++) {
                if (text.charAt(i + j) != sub.charAt(j)) {
                    match = false;
                    break;
                }
            }
            if (match) {
                return true;
            }
        }
        return false;
    }

    private static int[] prefixFunction (String text) {
        if (text.isEmpty()) {
            return null;
        }
        char[] chars = text.toCharArray();
        int[] p = new int[text.length()];
        p[0] = 0;
        for (int i = 1; i < text.length(); i++) {
            if (chars[i] == chars[p[i - 1]]) {
                p[i] = p[i - 1] + 1;
            } else if (chars[i] == chars[0]) {
                p[i] = 1;
            } else {
                p[i] = 0;
            }
        }
        return p;
    }

    public static boolean KMPSearchContains(String text, String pattern) {
        int[] prefixFunc = prefixFunction(pattern);
        int j = 0;
        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && text.charAt(i) != pattern.charAt(j)) {
                j = prefixFunc[j - 1];
            }
            if (text.charAt(i) == pattern.charAt(j)) {
                j += 1;
            }
            if (j == pattern.length()) {
                return true;
            }
        }
        return false;
    }

    public static boolean rabinKarpContains(String text, String pattern) {
        if (text.length() < pattern.length()) {
            return false;
        }
        int a = 149;
        long m = 1_000_000_000 + 9;

        long patternHash = 0;
        long currSubstrHash = 0;
        long pow = 1;

        for (int i = 0; i < pattern.length(); i++) {
            patternHash += (pattern.charAt(i)) * pow;
            patternHash %= m;

            currSubstrHash += (text.charAt(text.length() - pattern.length() + i)) * pow;
            currSubstrHash %= m;

            if (i != pattern.length() - 1) {
                pow = pow * a % m;
            }
        }

        for (int i = text.length(); i >= pattern.length(); i--) {
            if (patternHash == currSubstrHash) {
                boolean patternIsFound = true;

                for (int j = 0; j < pattern.length(); j++) {
                    if (text.charAt(i - pattern.length() + j) != pattern.charAt(j)) {
                        patternIsFound = false;
                        break;
                    }
                }

                if (patternIsFound) {
                    return true;
                }
            }

            if (i > pattern.length()) {
                currSubstrHash = (currSubstrHash - (text.charAt(i - 1)) * pow % m + m) * a % m;
                currSubstrHash = (currSubstrHash + (text.charAt(i - pattern.length() - 1))) % m;
            }
        }

        return false;
    }
}
