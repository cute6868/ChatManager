package site.chatmanager.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public final class EncryptionUtil {
    private static final BCryptPasswordEncoder highSecurityEncoder = new BCryptPasswordEncoder();
    private static final String AES_ALGORITHM = "AES";
    private static final int AES_KEY_SIZE = 128;

    /**
     * 高安全加密，适用于密码等敏感信息
     *
     * @param plainText 明文
     * @return 加密后的文本
     */
    public static String highSecurityEncrypt(String plainText) {
        return highSecurityEncoder.encode(plainText);
    }

    /**
     * 验证高安全加密后的文本是否匹配
     *
     * @param plainText       明文
     * @param encryptedText 加密后的文本
     * @return 如果匹配返回 true，否则返回 false
     */
    public static boolean highSecurityMatches(String plainText, String encryptedText) {
        return highSecurityEncoder.matches(plainText, encryptedText);
    }

    /**
     * 生成 AES 密钥
     *
     * @return 生成的密钥
     * @throws Exception 异常
     */
    public static String generateAESKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
        keyGenerator.init(AES_KEY_SIZE, new SecureRandom());
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /**
     * 简单加密，适用于手机、邮箱、验证码等信息
     *
     * @param plainText 明文
     * @param key       AES 密钥
     * @return 加密后的文本
     * @throws Exception 异常
     */
    public static String simpleEncrypt(String plainText, String key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(key), AES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * 简单解密，适用于邮箱、验证码等信息
     *
     * @param encryptedText 加密后的文本
     * @param key           AES 密钥
     * @return 解密后的明文
     * @throws Exception 异常
     */
    public static String simpleDecrypt(String encryptedText, String key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(key), AES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}