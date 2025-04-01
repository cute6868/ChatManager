package site.chatmanager.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class SnowflakeIdUtilsTest {

    @Test
    @DisplayName("正常生成 ID 且验证有效")
    void generateAndValidateId() {
        long id = SnowflakeIdUtils.generateId();
        boolean isValid = SnowflakeIdUtils.isValidId(id);
        assertTrue(isValid, "生成的 ID 应有效");
        assertTrue(id > 0, "生成的 ID 应为正数");
    }

    @ParameterizedTest
    @CsvSource({
            "32, 1, Worker ID 必须在 0 到 31 之间",
            "-1, 1, Worker ID 必须在 0 到 31 之间",
            "1, 32, Data Center ID 必须在 0 到 31 之间",
            "1, -1, Data Center ID 必须在 0 到 31 之间"
    })
    @DisplayName("初始化参数校验测试")
    void initParameterValidationTest(long workerId, long dataCenterId, String expectedErrorMsg) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> SnowflakeIdUtils.init(workerId, dataCenterId)
        );
        assertTrue(exception.getMessage().contains(expectedErrorMsg),
                "异常信息应包含预期错误提示");
    }

    @ParameterizedTest
    @CsvSource({
            "123456789012345678, false, 无效 ID（随机数）",
            "9223372036854775807, false, 无效 ID（Long.MAX_VALUE）",
            "-1, false, 无效 ID（负数）"
    })
    @DisplayName("无效 ID 验证测试")
    void invalidIdValidationTest(long id, boolean expectedResult, String testDesc) {
        boolean isValid = SnowflakeIdUtils.isValidId(id);
        assertEquals(expectedResult, isValid, testDesc);
    }

    @Test
    @DisplayName("有效 ID 验证测试")
    void validIdValidationTest() {
        long id = SnowflakeIdUtils.generateId();
        assertTrue(SnowflakeIdUtils.isValidId(id), "生成的 ID 应有效");
    }

    @Test
    @DisplayName("时钟回拨检测测试")
    void clockBackwardDetectionTest() throws Exception {
        Field lastTimestampField = SnowflakeIdUtils.class.getDeclaredField("lastTimestamp");
        lastTimestampField.setAccessible(true);

        long futureTimestamp = System.currentTimeMillis() + 1000;
        lastTimestampField.set(null, futureTimestamp);

        assertThrows(RuntimeException.class, SnowflakeIdUtils::generateId);

        // 重置状态
        lastTimestampField.set(null, -1L);
    }
}