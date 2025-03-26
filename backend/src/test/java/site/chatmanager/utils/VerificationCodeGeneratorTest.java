package site.chatmanager.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VerificationCodeGeneratorTest {

    @Test
    @DisplayName("生成6位数字验证码")
    public void testGenerateCode_Default() {
        String code = VerificationCodeGenerator.generateCode();
        assertEquals(6, code.length());
        assertTrue(code.matches("\\d+"));
    }

    @Test
    @DisplayName("生成指定长度和类型的验证码")
    public void testGenerateCode_Custom() {
        String code = VerificationCodeGenerator.generateCode(8, VerificationCodeGenerator.CodeType.ALL_CHARACTERS);
        assertEquals(8, code.length());
        assertTrue(code.matches("[0-9a-zA-Z!@#$%^&*()_+-=\\[\\]{}|;':\",./<>?]+"));
    }

    @Test
    @DisplayName("生成仅包含数字的验证码")
    public void testGenerateCode_DigitsOnly() {
        String code = VerificationCodeGenerator.generateCode(5, VerificationCodeGenerator.CodeType.DIGITS_ONLY);
        assertEquals(5, code.length());
        assertTrue(code.matches("\\d+"));
    }

    @Test
    @DisplayName("生成仅包含字母的验证码")
    public void testGenerateCode_LettersOnly() {
        String code = VerificationCodeGenerator.generateCode(7, VerificationCodeGenerator.CodeType.LETTERS_ONLY);
        assertEquals(7, code.length());
        assertTrue(code.matches("[a-zA-Z]+"));
    }

    @Test
    @DisplayName("生成包含数字和字母的验证码")
    public void testGenerateCode_DigitsAndLetters() {
        String code = VerificationCodeGenerator.generateCode(10, VerificationCodeGenerator.CodeType.DIGITS_AND_LETTERS);
        assertEquals(10, code.length());
        assertTrue(code.matches("[0-9a-zA-Z]+"));
    }

    @Test
    @DisplayName("生成包含所有字符的验证码")
    public void testGenerateCode_AllCharacters() {
        String code = VerificationCodeGenerator.generateCode(12, VerificationCodeGenerator.CodeType.ALL_CHARACTERS);
        assertEquals(12, code.length());
        assertTrue(code.matches("[0-9a-zA-Z!@#$%^&*()_+-=\\[\\]{}|;':\",./<>?]+"));
    }

    @Test
    @DisplayName("验证码长度小于等于0时抛出异常")
    public void testGenerateCode_InvalidLength() {
        assertThrows(IllegalArgumentException.class, () -> {
            VerificationCodeGenerator.generateCode(0, VerificationCodeGenerator.CodeType.DIGITS_ONLY);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            VerificationCodeGenerator.generateCode(-5, VerificationCodeGenerator.CodeType.DIGITS_ONLY);
        });
    }

    @Test
    @DisplayName("验证码类型为null时抛出异常")
    public void testGenerateCode_NullType() {
        assertThrows(NullPointerException.class, () -> {
            VerificationCodeGenerator.generateCode(6, null);
        });
    }
}
