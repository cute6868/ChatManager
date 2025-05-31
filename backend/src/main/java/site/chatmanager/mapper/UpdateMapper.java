package site.chatmanager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface UpdateMapper {

    // ===================== users_basic_info 表 =====================
    // 更新用户昵称
    public int updateNickname(@Param("uid") Long uid, @Param("nickname") String nickname);

    // 更新用户头像
    public int updateAvatar(@Param("uid") Long uid, @Param("avatar") String avatar);

    // 更新用户兴趣爱好（不直接对外）
    public int updateInterests(@Param("uid") Long uid, @Param("interests") String interests);

    // 更新用户模型选择
    public int updateSelectedModels(@Param("uid") Long uid, @Param("selected") String selected);


    // ===================== users_core_info 表 =====================
    // 更新用户账号
    public int updateAccount(@Param("uid") Long uid, @Param("account") String account);

    // 更新用户邮箱
    public int updateEmail(@Param("uid") Long uid, @Param("email") String email);

    // 更新用户手机
    public int updateCellphone(@Param("uid") Long uid, @Param("cellphone") String cellphone);

    // 更新密码
    public int updatePassword(@Param("uid") Long uid, @Param("password") String password);

    // 更新账号状态
    public int updateStatus(@Param("uid") Long uid, @Param("status") Integer status);

    // 更新用户身份
    public int updateRole(@Param("uid") Long uid, @Param("role") Integer role);

    // 更新用户最近一次登录的时间
    public int updateLastLoginTime(@Param("uid") Long uid, @Param("lastLoginAt") LocalDateTime lastLoginAt);


    // ===================== users_models_config 表 =====================
    // 更新用户模型配置
    public int updateModelsConfig(@Param("uid") Long uid, @Param("models") String models);


    // ===================== users_record_pointer 表 =====================
    // 更新用户记录序号
    public int updateRecordSequenceNum(@Param("uid") Long uid, @Param("newest") Integer newest, @Param("oldest") Integer oldest);


    // ===================== users_record_shard_(0~9) 表 =====================
    // 更新时间和问题
    public int updateRecord(@Param("uid") Long uid, @Param("sequence_num") Integer sequence_num, @Param("time") LocalDateTime time, @Param("question") String question);

}
