package site.chatmanager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface UpdateMapper {

    // ===================== users_basic_info 表 =====================
    // 更新用户昵称、用户头像（直接对外）
    public Integer updateBasicInfo(@Param("uid") Long uid, @Param("nickname") String nickname, @Param("avatar") String avatar);

    // 更新用户昵称
    public Integer updateNickname(@Param("uid") Long uid, @Param("nickname") String nickname);

    // 更新用户头像
    public Integer updateAvatar(@Param("uid") Long uid, @Param("avatar") String avatar);

    // 更新用户兴趣爱好（不直接对外）
    public Integer updateInterests(@Param("uid") Long uid, @Param("interests") String interests);


    // ===================== users_core_info 表 =====================
    // 更新用户账号、用户邮箱、用户手机
    public Integer updateCoreInfo(@Param("uid") Long uid, @Param("account") String account, @Param("email") String email, @Param("cellphone") String cellphone);

    // 更新用户账号
    public Integer updateAccount(@Param("uid") Long uid, @Param("account") String account);

    // 更新用户邮箱
    public Integer updateEmail(@Param("uid") Long uid, @Param("email") String email);

    // 更新用户手机
    public Integer updateCellphone(@Param("uid") Long uid, @Param("cellphone") String cellphone);

    // 更新密码
    public Integer updatePassword(@Param("uid") Long uid, @Param("password") String password);

    // 更新账号状态
    public Integer updateStatus(@Param("uid") Long uid, @Param("status") Integer status);

    // 更新用户身份
    public Integer updateRole(@Param("uid") Long uid, @Param("role") Integer role);

    // 更新用户最近一次登录的时间
    public Integer updateLastLoginTime(@Param("uid") Long uid, @Param("lastLoginAt") LocalDateTime lastLoginAt);


    // ===================== users_models_config 表 =====================
    // 更新用户模型配置
    public Integer updateModelsConfig(@Param("uid") Long uid, @Param("models") String models);


    // ===================== users_history_pointer 表 =====================
    // 更新用户历史记录指针
    public Integer updateHistoryInfoSequenceNum(@Param("uid") Long uid, @Param("newest") Integer newest, @Param("oldest") Integer oldest);


    // ===================== users_history_info_shard_(0~9) 表 =====================
    // 更新用户历史记录和时间
    public Integer updateHistoryInfo(@Param("uid") Long uid, @Param("time") LocalDateTime time, @Param("question") String question);

}
