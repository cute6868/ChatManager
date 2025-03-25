package site.chatmanager.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SnowflakeIdUtilsTest {

    @BeforeEach
    public void setUp() {
        // 初始化默认的工作机器 ID 和数据中心 ID
        SnowflakeIdUtils.init(1, 1);
    }

    @Test
    @DisplayName("测试初始化方法 - 正常情况")
    public void testInit_Normal() {
        SnowflakeIdUtils.init(2, 3);
        long id = SnowflakeIdUtils.generateId();
        assertTrue(SnowflakeIdUtils.isValidUid(id), "生成的 ID 应合法");
    }

    @Test
    @DisplayName("测试初始化方法 - 工作机器 ID 超出范围")
    public void testInit_InvalidWorkerId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            SnowflakeIdUtils.init(33, 3);
        });
        assertEquals("Worker ID 必须在 0 到 31 之间", exception.getMessage(), "应抛出非法参数异常");
    }

    @Test
    @DisplayName("测试初始化方法 - 数据中心 ID 超出范围")
    public void testInit_InvalidDataCenterId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            SnowflakeIdUtils.init(2, 33);
        });
        assertEquals("Data Center ID 必须在 0 到 31 之间", exception.getMessage(), "应抛出非法参数异常");
    }

    @Test
    @DisplayName("测试生成 ID 方法 - 正常情况")
    public void testGenerateId_Normal() {
        long id1 = SnowflakeIdUtils.generateId();
        long id2 = SnowflakeIdUtils.generateId();
        assertNotEquals(id1, id2, "生成的 ID 应不相同");
        assertTrue(SnowflakeIdUtils.isValidUid(id1), "生成的 ID 应合法");
        assertTrue(SnowflakeIdUtils.isValidUid(id2), "生成的 ID 应合法");
    }

    @Test
    @DisplayName("测试校验 ID 合法性方法 - 正常情况")
    public void testIsValidUid_Normal() {
        long id = SnowflakeIdUtils.generateId();
        assertTrue(SnowflakeIdUtils.isValidUid(id), "生成的 ID 应合法");
    }

    @Test
    @DisplayName("测试校验 ID 合法性方法 - 非法 ID")
    public void testIsValidUid_Invalid() {
        assertFalse(SnowflakeIdUtils.isValidUid(0), "ID 0 应不合法");
        assertFalse(SnowflakeIdUtils.isValidUid(-1), "ID -1 应不合法");
        assertFalse(SnowflakeIdUtils.isValidUid(Long.MAX_VALUE), "ID Long.MAX_VALUE 应不合法");
    }
}
