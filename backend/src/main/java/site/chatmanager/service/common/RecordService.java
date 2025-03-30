package site.chatmanager.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import site.chatmanager.mapper.InsertMapper;
import site.chatmanager.mapper.QueryMapper;
import site.chatmanager.mapper.UpdateMapper;
import site.chatmanager.pojo.data.RecordData;
import site.chatmanager.pojo.data.SequenceData;
import site.chatmanager.utils.SnowflakeIdUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RecordService {

    @Autowired
    private QueryMapper queryMapper;

    @Autowired
    private InsertMapper insertMapper;

    @Autowired
    private UpdateMapper updateMapper;

    // 初始化环形区域大小 (起点为1, 终点为20，环形循环：1->20->1)
    private static final int RING_SIZE = 20;

    // 获取记录
    public List<RecordData> getRecord(Long uid) {

        // 获取记录序号
        SequenceData sn = queryMapper.queryRecordSequenceNum(uid);
        Integer newest = sn.getNewest();
        Integer oldest = sn.getOldest();

        // 判断指针是否为空
        if (newest == null || oldest == null) {
            return null;
        }

        // 获取所有历史记录
        List<RecordData> allRecords = queryMapper.queryRecord(uid);

        // 从历史记录里面提取序号，放到Map中，完成映射:  Map<序号，历史记录>
        Map<Integer, RecordData> map = new HashMap<>();
        allRecords.forEach(record -> map.put(record.getSequence_num(), record));

        // 初始化结果列表
        List<RecordData> resultList = new ArrayList<>();

        // 根据指针状态，分情况完成遍历
        if (oldest <= newest) {
            // 当环形区域没有形成时，是连续区间：newest -> oldest
            for (int i = newest; i >= oldest; i--) {
                RecordData record = map.get(i);
                if (record != null) {
                    resultList.add(record); // 通过map快速获取"序号"对应的"历史记录"，然后添加到"结果列表"中
                }
            }
        } else {
            // 当环形区域形成时，涉及到跨边界操作
            // 这里将其拆分成两个部分
            // newest -> 1
            // 20 -> oldest
            for (int i = newest; i >= 1; i--) {
                RecordData record = map.get(i);
                if (record != null) {
                    resultList.add(record);
                }
            }
            for (int i = RING_SIZE; i >= oldest; i--) {
                RecordData record = map.get(i);
                if (record != null) {
                    resultList.add(record);
                }
            }
        }

        return resultList;
    }

    // 添加历史记录
    @Transactional
    public boolean addHistory(Long uid, String question) {

        // 获取历史记录指针
        SequenceData sn = queryMapper.queryRecordSequenceNum(uid);
        Integer newest = sn.getNewest();
        Integer oldest = sn.getOldest();

        // 准备数据
        long id = SnowflakeIdUtils.generateId();    // 通过雪花算法生成唯一id
        LocalDateTime time = LocalDateTime.now();  // 获取当前时间

        // 如果指针为空，则说明是第一次添加记录
        if (newest == null || oldest == null) {
            int result1 = insertMapper.insertRecord(id, uid, time, question, 1);  // 插入第一条记录
            int result2 = insertMapper.insertRecordSequenceNum(uid, 1, 1);  // 插入历史记录指针
            return result1 > 0 && result2 > 0;
        }

        // 如果指针不为空，则说明已经有记录了
        // 先更新指针
        newest = newest + 1;

        // 根据不同情况，完成指针的修正
        if (oldest < newest) {  // 在没有形成环形区域时(比如：更新后的newest为18)，或者刚刚形成环形区域时(比如：更新后的newest为21)

            if (newest <= RING_SIZE) {  // 如果更新后的newest <= RING_SIZE，则说明指针没有形成环形区域，不需要修正
                int result1 = insertMapper.insertRecord(id, uid, time, question, newest);  // 没有形成环形区域，使用"插入"数据
                int result2 = updateMapper.updateRecordSequenceNum(uid, newest, oldest);  // 更新历史记录指针
                return result1 > 0 && result2 > 0;
            } else {    // 如果更新后的newest > RING_SIZE，则说明指针刚刚形成环形区域，需要修正newest和oldest
                newest = newest - RING_SIZE;
                oldest = newest + 1;
                int result1 = updateMapper.updateRecord(uid, newest, time, question);  // 刚刚形成了环形区域，使用"更新"数据
                int result2 = updateMapper.updateRecordSequenceNum(uid, newest, oldest);  // 更新历史记录指针
                return result1 > 0 && result2 > 0;
            }

        } else if (newest.equals(oldest)) {  // 如果更新后的newest的大小和oldest相等，说明指针早就形成环形区域了，需要修正newest和oldest

            if (oldest < RING_SIZE) {  // oldest指针没到达下一个循环时
                // newest指针不需要修正，因为上面已经更新过了
                // oldest指针需要修正
                oldest = newest + 1;
            } else {  // oldest指针已经到达下一个循环时
                // newest指针不需要修正，因为上面已经更新过了
                // oldest指针需要修正
                oldest = oldest + 1 - RING_SIZE;
            }

            int result1 = updateMapper.updateRecord(uid, newest, time, question);    // 早就形成了环形区域，使用"更新"数据
            int result2 = updateMapper.updateRecordSequenceNum(uid, newest, oldest);  // 更新历史记录指针
            return result1 > 0 && result2 > 0;

        } else {    // 都不符合，抛出异常
            throw new RuntimeException("记录指针出现错误，无法按照规则正常运行，请检查数据库或操作逻辑是否正确");
        }
    }
}
