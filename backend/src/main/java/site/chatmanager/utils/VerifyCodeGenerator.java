package site.chatmanager.utils;

import java.security.SecureRandom;

public final class VerifyCodeGenerator {
    private static final String DIGITS = "0123456789";
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()_+-=[]{}|;':\",./<>?";
    private static final SecureRandom random = new SecureRandom();

    public enum CodeType {
        DIGITS_ONLY,
        LETTERS_ONLY,
        DIGITS_AND_LETTERS,
        ALL_CHARACTERS
    }

    private static String getCharacters(CodeType type) {
        return switch (type) {
            case DIGITS_ONLY -> DIGITS;
            case LETTERS_ONLY -> LETTERS;
            case DIGITS_AND_LETTERS -> DIGITS + LETTERS;
            case ALL_CHARACTERS -> DIGITS + LETTERS + SPECIAL_CHARACTERS;
            default -> DIGITS;
        };
    }

    // 自定义生成验证码
    public static String generateCode(int length, CodeType type) {
        if (length <= 0 || type == null) return null;

        StringBuilder code = new StringBuilder();
        String characters = getCharacters(type);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }

        return code.toString();
    }

    // 默认生成6位纯数字验证码
    public static String generateCode() {
        return generateCode(6, CodeType.DIGITS_ONLY);
    }

}