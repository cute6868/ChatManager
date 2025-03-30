package site.chatmanager.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 使用示例:
 * <p>
 * public class Main {
 * public static void main(String[] args) {
 * // 初始化工作机器 ID 和数据中心 ID
 * SnowflakeIdUtils.init(2, 3);
 * <p>
 * // 生成 ID
 * long newId = SnowflakeIdUtils.generateId();
 * System.out.println("生成的 ID: " + newId);
 * <p>
 * // 校验 ID 合法性
 * boolean isValid = SnowflakeIdUtils.isValidId(newId);
 * System.out.println("ID 是否合法: " + isValid);
 * }
 * }
 * <p>
 * 注意：
 * 单实例场景：无需调用 init，可直接使用默认值正常生成和校验 ID。
 * 分布式场景：必须调用 init 方法为每个实例设置唯一的 workerId 和 dataCenterId，以避免 ID 冲突。
 */

public final class SnowflakeIdUtils {
    // 起始时间戳（可根据实际需求调整，例如项目上线时间）
    // 这里设置为 2025-03-25 10:30:15 的时间戳（毫秒）
    private static final long START_TIMESTAMP = LocalDateTime.of(2025, 3, 25, 10, 30)
            .toInstant(ZoneOffset.UTC).toEpochMilli();

    // 工作机器 ID 所占的位数（最多支持 32 台机器）
    private static final long WORKER_ID_BITS = 5L;
    // 数据中心 ID 所占的位数（最多支持 32 个数据中心）
    private static final long DATA_CENTER_ID_BITS = 5L;
    // 序列号所占的位数（同一毫秒内最多生成 4096 个 ID）
    private static final long SEQUENCE_BITS = 12L;

    // 工作机器 ID 的最大值
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    // 数据中心 ID 的最大值
    private static final long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);

    // 工作机器 ID 左移的位数 = 序列号位数
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    // 数据中心 ID 左移的位数 = 序列号位数 + 工作机器 ID 位数
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    // 时间戳左移的位数 = 序列号位数 + 工作机器 ID 位数 + 数据中心 ID 位数
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

    // 用于防止序列号溢出的掩码（12 个 1，即 0b111111111111）
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    // 工作机器 ID（0 ~ MAX_WORKER_ID）
    private static long workerId = 1;
    // 数据中心 ID（0 ~ MAX_DATA_CENTER_ID）
    private static long dataCenterId = 1;
    // 序列号（同一毫秒内的自增计数）
    private static long sequence = 0L;
    // 上一次生成 ID 的时间戳（毫秒）
    private static long lastTimestamp = -1L;

    /**
     * 初始化工作机器 ID 和数据中心 ID
     *
     * @param workerId     工作机器 ID（0 ~ 31）
     * @param dataCenterId 数据中心 ID（0 ~ 31）
     */
    public static void init(long workerId, long dataCenterId) {
        // 校验工作机器 ID 的合法性
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException("Worker ID 必须在 0 到 " + MAX_WORKER_ID + " 之间");
        }
        // 校验数据中心 ID 的合法性
        if (dataCenterId > MAX_DATA_CENTER_ID || dataCenterId < 0) {
            throw new IllegalArgumentException("Data Center ID 必须在 0 到 " + MAX_DATA_CENTER_ID + " 之间");
        }
        SnowflakeIdUtils.workerId = workerId;
        SnowflakeIdUtils.dataCenterId = dataCenterId;
    }

    /**
     * 生成唯一 ID
     *
     * @return 64 位的雪花 ID（long 类型）
     */
    public static synchronized long generateId() {
        // 获取当前时间戳（毫秒）
        long currentTimestamp = System.currentTimeMillis();

        // 处理时钟回拨（如果当前时间戳小于上一次生成 ID 的时间戳）
        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException(String.format(
                    "时钟回拨检测到！当前时间戳：%d，上一次时间戳：%d",
                    currentTimestamp, lastTimestamp
            ));
        }

        // 如果当前时间戳与上一次相同，递增序列号
        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            // 如果序列号溢出，等待到下一个毫秒
            if (sequence == 0) {
                currentTimestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            // 时间戳改变，重置序列号
            sequence = 0L;
        }

        // 更新上一次生成 ID 的时间戳
        lastTimestamp = currentTimestamp;

        // 组装 ID：时间戳部分 + 数据中心 ID + 工作机器 ID + 序列号
        return (currentTimestamp - START_TIMESTAMP) << TIMESTAMP_LEFT_SHIFT |
                dataCenterId << DATA_CENTER_ID_SHIFT |
                workerId << WORKER_ID_SHIFT |
                sequence;
    }

    /**
     * 等待直到下一个毫秒的时间戳
     *
     * @param lastTimestamp 上一次生成 ID 的时间戳
     * @return 下一个有效的时间戳
     */
    private static long waitNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    /**
     * 检查是否为有效的 ID
     *
     * @param id 要检查的 ID
     * @return 如果 ID 有效返回 true，否则返回 false
     */
    public static boolean isValidId(long id) {
        if (id <= 0) {
            return false;
        }

        // 提取时间戳部分
        long timestampPart = (id >> TIMESTAMP_LEFT_SHIFT) + START_TIMESTAMP;
        long currentTimestamp = System.currentTimeMillis();
        if (timestampPart < START_TIMESTAMP || timestampPart > currentTimestamp) {
            return false;
        }

        // 提取工作机器 ID 部分（无需额外校验，位运算已保证范围）
        long workerId = (id >> WORKER_ID_SHIFT) & MAX_WORKER_ID;

        // 提取数据中心 ID 部分（无需额外校验，位运算已保证范围）
        long dataCenterId = (id >> DATA_CENTER_ID_SHIFT) & MAX_DATA_CENTER_ID;

        return true;
    }
}