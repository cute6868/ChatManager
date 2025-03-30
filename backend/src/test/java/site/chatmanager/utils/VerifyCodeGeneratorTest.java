package site.chatmanager.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VerifyCodeGeneratorTest {

    @Test
    @DisplayName("生成6位数字验证码")
    public void testGenerateCode_Default() {
        String code = VerifyCodeGenerator.generateCode();
        assertEquals(6, code.length());
        assertTrue(code.matches("\\d+"));
    }

    @Test
    @DisplayName("生成指定长度和类型的验证码")
    public void testGenerateCode_Custom() {
        String code = VerifyCodeGenerator.generateCode(8, VerifyCodeGenerator.CodeType.ALL_CHARACTERS);
        assertEquals(8, code.length());
        assertTrue(code.matches("[0-9a-zA-Z!@#$%^&*()_+-=\\[\\]{}|;':\",./<>?]+"));
    }

    @Test
    @DisplayName("生成仅包含数字的验证码")
    public void testGenerateCode_DigitsOnly() {
        String code = VerifyCodeGenerator.generateCode(5, VerifyCodeGenerator.CodeType.DIGITS_ONLY);
        assertEquals(5, code.length());
        assertTrue(code.matches("\\d+"));
    }

    @Test
    @DisplayName("生成仅包含字母的验证码")
    public void testGenerateCode_LettersOnly() {
        String code = VerifyCodeGenerator.generateCode(7, VerifyCodeGenerator.CodeType.LETTERS_ONLY);
        assertEquals(7, code.length());
        assertTrue(code.matches("[a-zA-Z]+"));
    }

    @Test
    @DisplayName("生成包含数字和字母的验证码")
    public void testGenerateCode_DigitsAndLetters() {
        String code = VerifyCodeGenerator.generateCode(10, VerifyCodeGenerator.CodeType.DIGITS_AND_LETTERS);
        assertEquals(10, code.length());
        assertTrue(code.matches("[0-9a-zA-Z]+"));
    }

    @Test
    @DisplayName("生成包含所有字符的验证码")
    public void testGenerateCode_AllCharacters() {
        String code = VerifyCodeGenerator.generateCode(12, VerifyCodeGenerator.CodeType.ALL_CHARACTERS);
        assertEquals(12, code.length());
        assertTrue(code.matches("[0-9a-zA-Z!@#$%^&*()_+-=\\[\\]{}|;':\",./<>?]+"));
    }

    @Test
    @DisplayName("验证码长度小于等于0时返回null")
    public void testGenerateCode_InvalidLength() {
        assertNull(VerifyCodeGenerator.generateCode(0, VerifyCodeGenerator.CodeType.DIGITS_ONLY));
        assertNull(VerifyCodeGenerator.generateCode(-5, VerifyCodeGenerator.CodeType.DIGITS_ONLY));
    }

    @Test
    @DisplayName("验证码类型为null时返回null")
    public void testGenerateCode_NullType() {
        assertNull(VerifyCodeGenerator.generateCode(6, null));
    }
}