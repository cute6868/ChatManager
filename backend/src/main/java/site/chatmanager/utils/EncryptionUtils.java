package site.chatmanager.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
public final class EncryptionUtils {
    private static final BCryptPasswordEncoder highSecurityEncoder = new BCryptPasswordEncoder();
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_CBC_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String AES_ECB_ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final int AES_BLOCK_SIZE = 16;
    private static final int MAX_ENCRYPTED_LENGTH = 255;
    private static final int SALT_LENGTH = 5;
    private static final String SALT_PREFIX = "abcde";
    private static final String SALT_SUFFIX = "vwxyz";
    private static final String DEFAULT_AES_KEY = "abcdefghijklmnop";

    private EncryptionUtils() {
    }

    /**
     * 高等安全加密方法，不使用盐值，相同明文加密结果不同，无密钥
     * 可传入的 text 长度理论上没有严格限制，因 BCrypt 处理能力足够
     *
     * @param text 待加密的明文
     * @return 加密后的密文，失败返回 null
     */
    public static String highSecurityEncrypt(String text) {
        if (text == null) {
            log.error("高等安全加密失败：输入文本为 null");
            return null;
        }
        try {
            return highSecurityEncoder.encode(text);
        } catch (Exception e) {
            log.error("高等安全加密失败：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 验证高等安全加密后的密文与明文是否匹配
     *
     * @param text          明文
     * @param encryptedText 高等安全加密后的密文
     * @return 如果匹配返回 true，否则返回 false
     */
    public static boolean highSecurityVerify(String text, String encryptedText) {
        if (text == null || encryptedText == null) {
            log.error("高等安全验证失败：输入文本或加密文本为 null");
            return false;
        }
        try {
            return highSecurityEncoder.matches(text, encryptedText);
        } catch (Exception e) {
            log.error("高等安全验证失败：{}", e.getMessage());
            return false;
        }
    }

    /**
     * 中等安全加密方法，使用盐值，密钥固定 16 位，相同明文加密结果不同
     * 可传入的 text 最大长度为 245 字符
     *
     * @param text 待加密的明文
     * @return 加密后的密文，失败返回 null
     */
    public static String mediumSecurityEncrypt(String text) {
        return mediumSecurityEncrypt(text, DEFAULT_AES_KEY);
    }

    /**
     * 中等安全加密方法，使用盐值，密钥固定 16 位，相同明文加密结果不同
     * 可传入的 text 最大长度为 245 字符
     *
     * @param text 待加密的明文
     * @param key  自定义的 16 位密钥
     * @return 加密后的密文，失败返回 null
     */
    public static String mediumSecurityEncrypt(String text, String key) {
        if (text == null) {
            log.error("中等安全加密失败：输入文本为 null");
            return null;
        }
        if (key == null || key.length() != AES_BLOCK_SIZE) {
            log.error("中等安全加密失败：密钥长度必须为 16 位");
            return null;
        }
        int maxTextLength = MAX_ENCRYPTED_LENGTH - SALT_LENGTH * 2 - AES_BLOCK_SIZE * 4 / 3 - 1;
        if (text.length() > maxTextLength) {
            log.error("中等安全加密失败：输入文本过长，最大长度为 {} 字符", maxTextLength);
            return null;
        }
        try {
            String saltedText = SALT_PREFIX + text + SALT_SUFFIX;
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_CBC_ALGORITHM);
            byte[] iv = new byte[AES_BLOCK_SIZE];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(iv);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            byte[] encryptedBytes = cipher.doFinal(saltedText.getBytes(StandardCharsets.UTF_8));
            String ivBase64 = Base64.getEncoder().encodeToString(iv);
            String encryptedBase64 = Base64.getEncoder().encodeToString(encryptedBytes);
            return ivBase64 + ":" + encryptedBase64;
        } catch (Exception e) {
            log.error("中等安全加密失败：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 中等安全解密方法
     *
     * @param encryptedText 中等安全加密后的密文
     * @param key           解密使用的 16 位密钥
     * @return 解密后的明文，失败返回 null
     */
    public static String mediumSecurityDecrypt(String encryptedText, String key) {
        if (encryptedText == null || key == null || key.length() != AES_BLOCK_SIZE) {
            log.error("中等安全解密失败：输入密文或密钥无效");
            return null;
        }
        try {
            String[] parts = encryptedText.split(":");
            if (parts.length != 2) {
                log.error("中等安全解密失败：密文格式不正确");
                return null;
            }
            byte[] iv = Base64.getDecoder().decode(parts[0]);
            byte[] encryptedBytes = Base64.getDecoder().decode(parts[1]);
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_CBC_ALGORITHM);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            String decryptedText = new String(decryptedBytes, StandardCharsets.UTF_8);
            return removeSalt(decryptedText);
        } catch (Exception e) {
            log.error("中等安全解密失败：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 普通安全加密方法，使用盐值，密钥固定 16 位，相同明文加密结果相同
     * 可传入的 text 最大长度为 245 字符
     *
     * @param text 待加密的明文
     * @return 加密后的密文，失败返回 null
     */
    public static String normalSecurityEncrypt(String text) {
        return normalSecurityEncrypt(text, DEFAULT_AES_KEY);
    }

    /**
     * 普通安全加密方法，使用盐值，密钥固定 16 位，相同明文加密结果相同
     * 可传入的 text 最大长度为 245 字符
     *
     * @param text 待加密的明文
     * @param key  自定义的 16 位密钥
     * @return 加密后的密文，失败返回 null
     */
    public static String normalSecurityEncrypt(String text, String key) {
        if (text == null) {
            log.error("普通安全加密失败：输入文本为 null");
            return null;
        }
        if (key == null || key.length() != AES_BLOCK_SIZE) {
            log.error("普通安全加密失败：密钥长度必须为 16 位");
            return null;
        }
        int maxTextLength = MAX_ENCRYPTED_LENGTH - SALT_LENGTH * 2;
        if (text.length() > maxTextLength) {
            log.error("普通安全加密失败：输入文本过长，最大长度为 {} 字符", maxTextLength);
            return null;
        }
        try {
            String saltedText = SALT_PREFIX + text + SALT_SUFFIX;
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_ECB_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(saltedText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            log.error("普通安全加密失败：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 普通安全解密方法
     *
     * @param encryptedText 普通安全加密后的密文
     * @param key           解密使用的 16 位密钥
     * @return 解密后的明文，失败返回 null
     */
    public static String normalSecurityDecrypt(String encryptedText, String key) {
        if (encryptedText == null || key == null || key.length() != AES_BLOCK_SIZE) {
            log.error("普通安全解密失败：输入密文或密钥无效");
            return null;
        }
        try {
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_ECB_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            String decryptedText = new String(decryptedBytes, StandardCharsets.UTF_8);
            return removeSalt(decryptedText);
        } catch (Exception e) {
            log.error("普通安全解密失败：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 移除解密后文本的盐值
     *
     * @param text 包含盐值的文本
     * @return 移除盐值后的文本
     */
    private static String removeSalt(String text) {
        if (text.length() >= SALT_LENGTH * 2) {
            return text.substring(SALT_LENGTH, text.length() - SALT_LENGTH);
        }
        return text;
    }
}