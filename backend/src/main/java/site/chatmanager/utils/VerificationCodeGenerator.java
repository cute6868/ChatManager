package site.chatmanager.utils;

import java.security.SecureRandom;

public final class VerificationCodeGenerator {
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
        switch (type) {
            case DIGITS_ONLY:
                return DIGITS;
            case LETTERS_ONLY:
                return LETTERS;
            case DIGITS_AND_LETTERS:
                return DIGITS + LETTERS;
            case ALL_CHARACTERS:
                return DIGITS + LETTERS + SPECIAL_CHARACTERS;
            default:
                return DIGITS;
        }
    }

    /**
     * 生成验证码的方法
     *
     * @param length 验证码的长度
     * @param type   验证码的类型
     * @return 生成的验证码
     * @throws IllegalArgumentException 如果长度小于等于 0
     * @throws NullPointerException     如果类型为 null
     */
    public static String generateCode(int length, CodeType type) {
        if (length <= 0) {
            throw new IllegalArgumentException("验证码长度必须大于 0");
        }
        if (type == null) {
            throw new NullPointerException("验证码类型不能为空");
        }
        StringBuilder code = new StringBuilder();
        String characters = getCharacters(type);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }

        return code.toString();
    }

    /**
     * 生成默认 6 位数字验证码
     *
     * @return 生成的 6 位数字验证码
     */
    public static String generateCode() {
        return generateCode(6, CodeType.DIGITS_ONLY);
    }

}