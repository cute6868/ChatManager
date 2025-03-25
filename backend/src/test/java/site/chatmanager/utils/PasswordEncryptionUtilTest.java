package site.chatmanager.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordEncryptionUtilTest {

    @Test
    @DisplayName("测试密码加密功能")
    public void testEncryptPassword() {
        String rawPassword = "testPassword";
        String encryptedPassword = PasswordEncryptionUtil.encryptPassword(rawPassword);

        // 确保加密后的密码不为空
        assertNotNull(encryptedPassword, "加密后的密码不应为空");

        // 确保加密后的密码不是原始密码
        assertNotEquals(rawPassword, encryptedPassword, "加密后的密码不应与原始密码相同");
    }

    @Test
    @DisplayName("测试密码匹配功能 - 匹配测试")
    public void testMatchesPassword_Match() {
        String rawPassword = "testPassword";
        String encryptedPassword = PasswordEncryptionUtil.encryptPassword(rawPassword);

        // 验证原始密码和加密后的密码是否匹配
        assertTrue(PasswordEncryptionUtil.matchesPassword(rawPassword, encryptedPassword), "原始密码和加密后的密码应匹配");
    }

    @Test
    @DisplayName("测试密码匹配功能 - 不匹配测试")
    public void testMatchesPassword_NotMatch() {
        String rawPassword = "testPassword";
        String wrongPassword = "wrongPassword";
        String encryptedPassword = PasswordEncryptionUtil.encryptPassword(rawPassword);

        // 验证错误密码和加密后的密码是否不匹配
        assertFalse(PasswordEncryptionUtil.matchesPassword(wrongPassword, encryptedPassword), "错误密码和加密后的密码不应匹配");
    }
}
