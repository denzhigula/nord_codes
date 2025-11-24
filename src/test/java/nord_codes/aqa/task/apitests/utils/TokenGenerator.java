package nord_codes.aqa.task.apitests.utils;

import java.security.SecureRandom;

public class TokenGenerator {
    private static final String PATTERN = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String WORKAROUND_PATTERN = "ABCDEF0123456789";

    private static final int TOKEN_LENGTH = 32;
    private static final SecureRandom random = new SecureRandom();

    public static String generateValidToken() {
        return generateValidToken(true);
    }

    public static String generateValidToken(boolean isWorkAround) {
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);
        String actualPattern = PATTERN;
        if (isWorkAround) {
            actualPattern = WORKAROUND_PATTERN;
        }
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            token.append(actualPattern.charAt(random.nextInt(actualPattern.length())));
        }
        return token.toString();
    }

    public static String generateInvalidToken(int length) {
        StringBuilder token = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            token.append(PATTERN.charAt(random.nextInt(PATTERN.length())));
        }
        return token.toString();
    }

    public static String generateTokenWithInvalidChars() {
        String invalidChars = "abcdefghijklmnopqrstuvwxyz!@#$%^&*()";
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            token.append(invalidChars.charAt(random.nextInt(invalidChars.length())));
        }
        return token.toString();
    }
}
