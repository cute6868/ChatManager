package site.chatmanager.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

public interface UpdateMapper {

    // ===================== users_basic_info 表 =====================
    // 更新用户昵称、用户头像（直接对外）
    @Update("update users_basic_info set nickname = #{nickname}, avatar = #{avatar} where uid = #{uid}")
    public Integer updateBasicInfo(@Param("uid") Long uid, @Param("nickname") String nickname, @Param("avatar") String avatar);

    // 更新用户昵称
    @Update("update users_basic_info set nickname = #{nickname} where uid = #{uid}")
    public Integer updateNickname(@Param("uid") Long uid, @Param("nickname") String nickname);

    // 更新用户头像
    @Update("update users_basic_info set avatar = #{avatar} where uid = #{uid}")
    public Integer updateAvatar(@Param("uid") Long uid, @Param("avatar") String avatar);

    // 更新用户兴趣爱好（不直接对外）
    @Update("update users_basic_info set interests = #{interests} where uid = #{uid}")
    public Integer updateInterests(@Param("uid") Long uid, @Param("interests") String interests);


    // ===================== users_core_info 表 =====================
    // 更新用户账号、用户邮箱、用户手机
    @Update("update users_core_info set account = #{account}, email = #{email}, cellphone = #{cellphone} where uid = #{uid}")
    public Integer updateCoreInfo(@Param("uid") Long uid, @Param("account") String account, @Param("email") String email, @Param("cellphone") String cellphone);

    // 更新用户账号
    @Update("update users_core_info set account = #{account} where uid = #{uid}")
    public Integer updateAccount(@Param("uid") Long uid, @Param("account") String account);

    // 更新用户邮箱
    @Update("update users_core_info set email = #{email} where uid = #{uid}")
    public Integer updateEmail(@Param("uid") Long uid, @Param("email") String email);

    // 更新用户手机
    @Update("update users_core_info set cellphone = #{cellphone} where uid = #{uid}")
    public Integer updateCellphone(@Param("uid") Long uid, @Param("cellphone") String cellphone);

    // 更新密码
    @Update("update users_core_info set password = #{password} where uid = #{uid}")
    public Integer updatePassword(@Param("uid") Long uid, @Param("password") String password);

    // 更新账号状态
    @Update("update users_core_info set status = #{status} where uid = #{uid}")
    public Integer updateStatus(@Param("uid") Long uid, @Param("status") Integer status);

    // 更新用户身份
    @Update("update users_core_info set role = #{role} where uid = #{uid}")
    public Integer updateRole(@Param("uid") Long uid, @Param("role") Integer role);

    // 更新用户最近一次登录的时间
    @Update("update users_core_info set last_login_at = #{lastLoginAt} where uid = #{uid}")
    public Integer updateLastLoginTime(@Param("uid") Long uid, @Param("lastLoginAt") LocalDateTime lastLoginAt);


    // ===================== users_models_config 表 =====================
    // 更新用户模型配置
    @Update("update users_models_config set models = #{models} where uid = #{uid}")
    public Integer updateModelsConfig(@Param("uid") Long uid, @Param("models") String models);


    // ===================== users_history_info_shard_(0~9) 表 =====================
    // 更新用户历史记录和其对应的时间
    @Update("update users_history_info_shard_0 set time = #{time}, question = #{question} where uid = #{uid}")
    public Integer updateHistoryInfo(@Param("uid") Long uid, @Param("time") LocalDateTime time, @Param("question") String question);

}
