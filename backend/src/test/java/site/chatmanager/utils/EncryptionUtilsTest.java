package site.chatmanager.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionUtilsTest {

    @Test
    @DisplayName("高等安全加密：正常输入")
    void testHighSecurityEncrypt_NormalInput() {
        String text = "test";
        assertDoesNotThrow(() -> {
            String encrypted = EncryptionUtils.highSecurityEncrypt(text);
            assertNotNull(encrypted);
        });
    }

    @Test
    @DisplayName("高等安全加密：输入为 null")
    void testHighSecurityEncrypt_NullInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            EncryptionUtils.highSecurityEncrypt(null);
        });
    }

    @Test
    @DisplayName("高等安全验证：正常输入")
    void testHighSecurityVerify_NormalInput() {
        String text = "test";
        String encrypted = EncryptionUtils.highSecurityEncrypt(text);
        assertDoesNotThrow(() -> {
            boolean result = EncryptionUtils.highSecurityVerify(text, encrypted);
            assertTrue(result);
        });
    }

    @Test
    @DisplayName("高等安全验证：明文为 null")
    void testHighSecurityVerify_NullText() {
        String encrypted = EncryptionUtils.highSecurityEncrypt("test");
        assertThrows(IllegalArgumentException.class, () -> {
            EncryptionUtils.highSecurityVerify(null, encrypted);
        });
    }

    @Test
    @DisplayName("高等安全验证：密文为 null")
    void testHighSecurityVerify_NullEncryptedText() {
        assertThrows(IllegalArgumentException.class, () -> {
            EncryptionUtils.highSecurityVerify("test", null);
        });
    }

    @Test
    @DisplayName("中等安全加密：正常输入")
    void testMediumSecurityEncrypt_NormalInput() {
        String text = "test";
        assertDoesNotThrow(() -> {
            String encrypted = EncryptionUtils.mediumSecurityEncrypt(text);
            assertNotNull(encrypted);
        });
    }

    @Test
    @DisplayName("中等安全加密：输入为 null")
    void testMediumSecurityEncrypt_NullInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            EncryptionUtils.mediumSecurityEncrypt(null);
        });
    }

    @Test
    @DisplayName("中等安全加密：密钥长度不为 16 位")
    void testMediumSecurityEncrypt_InvalidKeyLength() {
        String text = "test";
        String key = "12345";
        assertThrows(IllegalArgumentException.class, () -> {
            EncryptionUtils.mediumSecurityEncrypt(text, key);
        });
    }

    @Test
    @DisplayName("中等安全加密：输入文本过长")
    void testMediumSecurityEncrypt_TextTooLong() {
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 246; i++) {
            longText.append("a");
        }
        assertThrows(IllegalArgumentException.class, () -> {
            EncryptionUtils.mediumSecurityEncrypt(longText.toString());
        });
    }

    @Test
    @DisplayName("中等安全解密：正常输入")
    void testMediumSecurityDecrypt_NormalInput() {
        String text = "test";
        String encrypted = EncryptionUtils.mediumSecurityEncrypt(text);
        assertDoesNotThrow(() -> {
            String decrypted = EncryptionUtils.mediumSecurityDecrypt(encrypted);
            assertEquals(text, decrypted);
        });
    }

    @Test
    @DisplayName("中等安全解密：密文为 null")
    void testMediumSecurityDecrypt_NullEncryptedText() {
        assertThrows(IllegalArgumentException.class, () -> {
            EncryptionUtils.mediumSecurityDecrypt(null);
        });
    }

    @Test
    @DisplayName("中等安全解密：密钥长度不为 16 位")
    void testMediumSecurityDecrypt_InvalidKeyLength() {
        String encrypted = EncryptionUtils.mediumSecurityEncrypt("test");
        String key = "12345";
        assertThrows(IllegalArgumentException.class, () -> {
            EncryptionUtils.mediumSecurityDecrypt(encrypted, key);
        });
    }

    @Test
    @DisplayName("中等安全解密：密文格式不正确")
    void testMediumSecurityDecrypt_InvalidFormat() {
        String encrypted = "invalidFormat";
        assertThrows(IllegalArgumentException.class, () -> {
            EncryptionUtils.mediumSecurityDecrypt(encrypted);
        });
    }

    @Test
    @DisplayName("普通安全加密：正常输入")
    void testNormalSecurityEncrypt_NormalInput() {
        String text = "test";
        assertDoesNotThrow(() -> {
            String encrypted = EncryptionUtils.normalSecurityEncrypt(text);
            assertNotNull(encrypted);
        });
    }

    @Test
    @DisplayName("普通安全加密：输入为 null")
    void testNormalSecurityEncrypt_NullInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            EncryptionUtils.normalSecurityEncrypt(null);
        });
    }

    @Test
    @DisplayName("普通安全加密：密钥长度不为 16 位")
    void testNormalSecurityEncrypt_InvalidKeyLength() {
        String text = "test";
        String key = "12345";
        assertThrows(IllegalArgumentException.class, () -> {
            EncryptionUtils.normalSecurityEncrypt(text, key);
        });
    }

    @Test
    @DisplayName("普通安全加密：输入文本过长")
    void testNormalSecurityEncrypt_TextTooLong() {
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 246; i++) {
            longText.append("a");
        }
        assertThrows(IllegalArgumentException.class, () -> {
            EncryptionUtils.normalSecurityEncrypt(longText.toString());
        });
    }

    @Test
    @DisplayName("普通安全解密：正常输入")
    void testNormalSecurityDecrypt_NormalInput() {
        String text = "test";
        String encrypted = EncryptionUtils.normalSecurityEncrypt(text);
        assertDoesNotThrow(() -> {
            String decrypted = EncryptionUtils.normalSecurityDecrypt(encrypted);
            assertEquals(text, decrypted);
        });
    }

    @Test
    @DisplayName("普通安全解密：密文为 null")
    void testNormalSecurityDecrypt_NullEncryptedText() {
        assertThrows(IllegalArgumentException.class, () -> {
            EncryptionUtils.normalSecurityDecrypt(null);
        });
    }

    @Test
    @DisplayName("普通安全解密：密钥长度不为 16 位")
    void testNormalSecurityDecrypt_InvalidKeyLength() {
        String encrypted = EncryptionUtils.normalSecurityEncrypt("test");
        String key = "12345";
        assertThrows(IllegalArgumentException.class, () -> {
            EncryptionUtils.normalSecurityDecrypt(encrypted, key);
        });
    }
}