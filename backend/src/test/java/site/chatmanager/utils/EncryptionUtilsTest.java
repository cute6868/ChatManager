package site.chatmanager.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionUtilsTest {

    private static final String TEST_TEXT = "testText";
    private static final String TEST_KEY = "abcdefghijklmnop";

    @Test
    @DisplayName("测试高等安全加密及验证方法")
    void testHighSecurityEncryptAndVerify() {
        String encrypted = EncryptionUtils.highSecurityEncrypt(TEST_TEXT);
        assertNotNull(encrypted, "高等安全加密结果不应为 null");
        assertTrue(EncryptionUtils.highSecurityVerify(TEST_TEXT, encrypted), "高等安全加密验证应通过");
    }

    @Test
    @DisplayName("测试中等安全加密和解密方法")
    void testMediumSecurityEncryptAndDecrypt() {
        String encrypted = EncryptionUtils.mediumSecurityEncrypt(TEST_TEXT, TEST_KEY);
        assertNotNull(encrypted, "中等安全加密结果不应为 null");
        String decrypted = EncryptionUtils.mediumSecurityDecrypt(encrypted, TEST_KEY);
        assertEquals(TEST_TEXT, decrypted, "中等安全加密解密后应与原始文本相同");
    }

    @Test
    @DisplayName("测试普通安全加密和解密方法")
    void testNormalSecurityEncryptAndDecrypt() {
        String encrypted = EncryptionUtils.normalSecurityEncrypt(TEST_TEXT, TEST_KEY);
        assertNotNull(encrypted, "普通安全加密结果不应为 null");
        String decrypted = EncryptionUtils.normalSecurityDecrypt(encrypted, TEST_KEY);
        assertEquals(TEST_TEXT, decrypted, "普通安全加密解密后应与原始文本相同");
    }
}