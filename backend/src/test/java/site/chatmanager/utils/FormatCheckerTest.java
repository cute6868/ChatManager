package site.chatmanager.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class FormatCheckerTest {

    @DisplayName("账号长度不足")
    @ParameterizedTest
    @ValueSource(strings = {"123", "abcd", "12345",})
    public void testCheckAccount_LengthLessThanSix_ShouldReturnFalse(String account) {
        boolean isLegal = FormatChecker.checkAccount(account);
        Assertions.assertFalse(isLegal);
    }

    @DisplayName("账号长度过长")
    @ParameterizedTest
    @ValueSource(strings = {"123456789012345678901", "1234567890123456789012"})
    public void testCheckAccount_LengthGreaterThanTwenty_ShouldReturnFalse(String account) {
        boolean isLegal = FormatChecker.checkAccount(account);
        Assertions.assertFalse(isLegal);
    }

    @DisplayName("账号包含无效字符")
    @ParameterizedTest
    @ValueSource(strings = {"123456!", "abc_def", "123 456"})
    public void testCheckAccount_ContainsInvalidCharacters_ShouldReturnFalse(String account) {
        boolean isLegal = FormatChecker.checkAccount(account);
        Assertions.assertFalse(isLegal);
    }

    @DisplayName("账号格式有效")
    @ParameterizedTest
    @ValueSource(strings = {"123456", "abcdef", "123abc456", "a1b2c3d4e5f6g7h8i9j0"})
    public void testCheckAccount_ValidFormat_ShouldReturnTrue(String account) {
        boolean isLegal = FormatChecker.checkAccount(account);
        Assertions.assertTrue(isLegal);
    }

    @DisplayName("账号为null")
    @ParameterizedTest
    @NullSource
    public void testCheckAccount_NullInput_ShouldReturnFalse(String account) {
        boolean isLegal = FormatChecker.checkAccount(account);
        Assertions.assertFalse(isLegal);
    }

    @DisplayName("邮箱格式有效")
    @ParameterizedTest
    @ValueSource(strings = {"test@qq.com", "user.name+tag+sorting@qq.com", "user@qq.com"})
    public void testCheckEmail_ValidFormat_ShouldReturnTrue(String email) {
        boolean isLegal = FormatChecker.checkEmail(email);
        Assertions.assertTrue(isLegal);
    }

    @DisplayName("邮箱格式无效")
    @ParameterizedTest
    @ValueSource(strings = {"plainaddress", "@missingusername.com", "username@.com", "username@com", "username@domain..com"})
    public void testCheckEmail_InvalidFormat_ShouldReturnFalse(String email) {
        boolean isLegal = FormatChecker.checkEmail(email);
        Assertions.assertFalse(isLegal);
    }

    @DisplayName("邮箱为null")
    @ParameterizedTest
    @NullSource
    public void testCheckEmail_NullInput_ShouldReturnFalse(String email) {
        boolean isLegal = FormatChecker.checkEmail(email);
        Assertions.assertFalse(isLegal);
    }

    @DisplayName("邮箱缺少 '@' 符号")
    @ParameterizedTest
    @ValueSource(strings = {"userexample.com"})
    public void testCheckEmail_MissingAtSymbol_ShouldReturnFalse(String email) {
        boolean isLegal = FormatChecker.checkEmail(email);
        Assertions.assertFalse(isLegal);
    }

    @DisplayName("邮箱缺少域名")
    @ParameterizedTest
    @ValueSource(strings = {"user@"})
    public void testCheckEmail_MissingDomain_ShouldReturnFalse(String email) {
        boolean isLegal = FormatChecker.checkEmail(email);
        Assertions.assertFalse(isLegal);
    }

    @DisplayName("邮箱无效的顶级域名")
    @ParameterizedTest
    @ValueSource(strings = {"user@example.c", "user@example.abcdefgh"})
    public void testCheckEmail_InvalidTopLevelDomain_ShouldReturnFalse(String email) {
        boolean isLegal = FormatChecker.checkEmail(email);
        Assertions.assertFalse(isLegal);
    }

    @DisplayName("邮箱包含无效字符")
    @ParameterizedTest
    @ValueSource(strings = {"user@exa_mple.com", "user@exa mple.com"})
    public void testCheckEmail_ContainsInvalidCharacters_ShouldReturnFalse(String email) {
        boolean isLegal = FormatChecker.checkEmail(email);
        Assertions.assertFalse(isLegal);
    }

    @DisplayName("邮箱域名连续点")
    @ParameterizedTest
    @ValueSource(strings = {"user@domain..com"})
    public void testCheckEmail_ConsecutiveDotsInDomain_ShouldReturnFalse(String email) {
        boolean isLegal = FormatChecker.checkEmail(email);
        Assertions.assertFalse(isLegal);
    }

    @DisplayName("邮箱域名以点开头或结尾")
    @ParameterizedTest
    @ValueSource(strings = {"user@.domain.com", "user@domain.com."})
    public void testCheckEmail_DomainStartOrEndWithDot_ShouldReturnFalse(String email) {
        boolean isLegal = FormatChecker.checkEmail(email);
        Assertions.assertFalse(isLegal);
    }

    @DisplayName("密码为null")
    @ParameterizedTest
    @NullSource
    public void testCheckPassword_NullInput_ShouldReturnFalse(String password) {
        boolean isLegal = FormatChecker.checkPassword(password);
        Assertions.assertFalse(isLegal);
    }

    @DisplayName("密码长度小于8")
    @ParameterizedTest
    @ValueSource(strings = {"1234567", "abcdefg"})
    public void testCheckPassword_LengthLessThanEight_ShouldReturnFalse(String password) {
        boolean isLegal = FormatChecker.checkPassword(password);
        Assertions.assertFalse(isLegal);
    }

    @DisplayName("密码长度大于32")
    @ParameterizedTest
    @ValueSource(strings = {"123456789012345678901234567890123"})
    public void testCheckPassword_LengthGreaterThanThirtyTwo_ShouldReturnFalse(String password) {
        boolean isLegal = FormatChecker.checkPassword(password);
        Assertions.assertFalse(isLegal);
    }

    @DisplayName("密码仅由数字组成")
    @ParameterizedTest
    @ValueSource(strings = {"12345678", "12345678901234567890123456789012"})
    public void testCheckPassword_OnlyDigits_ShouldReturnFalse(String password) {
        boolean isLegal = FormatChecker.checkPassword(password);
        Assertions.assertFalse(isLegal);
    }

    @DisplayName("密码格式有效")
    @ParameterizedTest
    @ValueSource(strings = {"1234567a", "abcdefg123", "A1b2C3d4!", "Valid@Password123"})
    public void testCheckPassword_ValidFormat_ShouldReturnTrue(String password) {
        boolean isLegal = FormatChecker.checkPassword(password);
        Assertions.assertTrue(isLegal);
    }
}
