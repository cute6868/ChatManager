package site.chatmanager.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EncryptionUtilsTest {

    @Test
    @DisplayName("高安全加密和验证 - 成功")
    public void testHighSecurityEncryptAndMatches() {
        String plainText = "mySecretPassword";
        String encryptedText = EncryptionUtils.highSecurityEncrypt(plainText);
        assertTrue(EncryptionUtils.highSecurityMatches(plainText, encryptedText));
    }

    @Test
    @DisplayName("中等安全加密和解密 - 成功")
    public void testMediumSecurityEncryptAndDecrypt() throws Exception {
        String plainText = "mySensitiveData";
        String encryptedText = EncryptionUtils.mediumSecurityEncrypt(plainText);
        String decryptedText = EncryptionUtils.mediumSecurityDecrypt(encryptedText);
        assertEquals(plainText, decryptedText);
    }

    @Test
    @DisplayName("普通安全加密和解密 - 成功")
    public void testNormalSecurityEncryptAndDecrypt() throws Exception {
        String plainText = "myData";
        String encryptedText = EncryptionUtils.normalSecurityEncrypt(plainText);
        String decryptedText = EncryptionUtils.normalSecurityDecrypt(encryptedText);
        assertEquals(plainText, decryptedText);
    }

    @Test
    @DisplayName("中等安全加密和解密 - 使用自定义密钥 - 成功")
    public void testMediumSecurityEncryptAndDecryptWithCustomKey() throws Exception {
        String plainText = "mySensitiveData";
        String customKey = "myCustomKey123";
        String encryptedText = EncryptionUtils.mediumSecurityEncrypt(plainText, customKey);
        String decryptedText = EncryptionUtils.mediumSecurityDecrypt(encryptedText, customKey);
        assertEquals(plainText, decryptedText);
    }

    @Test
    @DisplayName("普通安全加密和解密 - 使用自定义密钥 - 成功")
    public void testNormalSecurityEncryptAndDecryptWithCustomKey() throws Exception {
        String plainText = "myData";
        String customKey = "myCustomKey123";
        String encryptedText = EncryptionUtils.normalSecurityEncrypt(plainText, customKey);
        String decryptedText = EncryptionUtils.normalSecurityDecrypt(encryptedText, customKey);
        assertEquals(plainText, decryptedText);
    }
}
