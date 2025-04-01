package site.chatmanager.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTest {

    @Test
    @DisplayName("测试生成令牌方法")
    public void testGenerateToken() {
        Long uid = 123L;
        Integer role = 2;
        String token = JwtUtils.generateToken(uid, role);
        assertNotNull(token, "生成的令牌不应为 null");
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    @DisplayName("测试获取用户信息方法")
    public void testGetInfoFromToken(Long uid) {
        Integer role = 2;
        String token = JwtUtils.generateToken(uid, role);
        Object[] info = JwtUtils.getInfoFromToken(token);
        assertNotNull(info, "获取的用户信息不应为 null");
        assertEquals(uid, info[0], "获取的用户 ID 应与生成时一致");
        assertEquals(role, info[1], "获取的用户角色应与生成时一致");
    }

    @Test
    @DisplayName("测试获取用户信息方法 - 无效令牌")
    public void testGetInfoFromToken_InvalidToken() {
        String invalidToken = "invalid_token";
        Object[] info = JwtUtils.getInfoFromToken(invalidToken);
        assertNull(info, "无效令牌获取的用户信息应为 null");
    }
}