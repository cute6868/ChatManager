package site.chatmanager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface InsertMapper {

    // ===================== users_basic_info 表 =====================
    // 插入用户的基本数据
    public int insertBasicInfo(@Param("uid") Long uid, @Param("nickname") String nickname, @Param("create_at") LocalDateTime create_at);


    // ===================== users_core_info 表 =====================
    // 插入用户的核心数据
    public int insertCoreInfo(@Param("uid") Long uid, @Param("account") String account, @Param("password") String password, @Param("email") String email, @Param("last_login_at") LocalDateTime last_login_at);


    // ===================== users_models_config 表 =====================
    // 插入用户模型配置
    public int insertModelsConfig(@Param("uid") Long uid);


    // ===================== users_record_pointer 表 =====================
    // 插入用户历史记录序号
    public int insertRecordSequenceNum(@Param("uid") Long uid, @Param("newest") Integer newest, @Param("oldest") Integer oldest);


    // ===================== users_record_shard_(0~9) 表 =====================
    // 插入编号、uid、记录时间、历史记录、历史序号
    public int insertRecord(@Param("id") Long id, @Param("uid") Long uid, @Param("time") LocalDateTime time, @Param("question") String question, @Param("sequence_num") Integer sequence_num);

}
