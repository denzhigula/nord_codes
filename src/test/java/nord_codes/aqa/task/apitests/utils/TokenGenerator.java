package nord_codes.aqa.task.apitests.utils;

import java.security.SecureRandom;

import static nord_codes.aqa.task.apitests.config.TestConfig.*;

public class TokenGenerator {
    private static final String TOKEN_SOURCE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String WORKAROUND_TOKEN_SOURCE = "ABCDEF0123456789";
    private static final String INVALID_TOKEN_SOURCE = "abcdefghijklmnopqrstuvwxyz!@#$%^&*()";

    private static final SecureRandom random = new SecureRandom();

    public static String generateValidToken() {
        return generateValidToken(true);
    }

    public static String generateValidToken(boolean isWorkAround) {
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);
        String actualPattern = TOKEN_SOURCE;
        if (isWorkAround) {
            actualPattern = WORKAROUND_TOKEN_SOURCE;
        }
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            token.append(actualPattern.charAt(random.nextInt(actualPattern.length())));
        }
        return token.toString();
    }

    public static String generateInvalidToken(int length) {
        StringBuilder token = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            token.append(TOKEN_SOURCE.charAt(random.nextInt(TOKEN_SOURCE.length())));
        }
        return token.toString();
    }

    public static String generateTokenWithInvalidChars() {
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            token.append(INVALID_TOKEN_SOURCE.charAt(random.nextInt(INVALID_TOKEN_SOURCE.length())));
        }
        return token.toString();
    }
}
