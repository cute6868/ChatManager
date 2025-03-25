package site.chatmanager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import site.chatmanager.pojo.data.CoreData;
import site.chatmanager.pojo.data.HistoryData;
import site.chatmanager.pojo.data.PersonalityData;
import site.chatmanager.pojo.User;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface QueryMapper {

    // ===================== 存在性查询（比如：注册业务） =====================
    // 通过账号查询 uid
    public Long queryUidByAccount(String account);

    // 通过邮箱查询 uid
    public Long queryUidByEmail(String email);

    // 通过手机查询 uid
    public Long queryUidByCellphone(String cellphone);


    // ===================== 认证性查询（比如：登录业务） =====================
    // 对于账号认证方式
    // 认证信息：账号状态、用户身份、uid、用户密码
    public CoreData queryAuthInfoByAccount(String account);

    // 对于邮箱和手机认证方式
    // 认证信息：账号状态、用户身份
    public CoreData queryAuthInfo(Long uid);


    // ===================== 获取性查询（比如：展示业务） =====================

    // ~~~~~ users_basic_info 表 ~~~~~
    // 查询用户昵称、用户头像（直接对外）
    public PersonalityData queryBasicInfo(Long uid);

    // 查询：用户兴趣爱好（不直接对外）
    public PersonalityData queryInterests(Long uid);


    // ~~~~~ users_core_info 表 ~~~~~
    // 查询：用户账号、用户邮箱、用户手机、（不得查询密码，但是可以修改密码）
    public CoreData queryCoreInfo(Long uid);


    // ~~~~~ users_models_config 表 ~~~~~
    // 查询用户模型配置
    public String queryModelsConfig(Long uid);


    // ~~~~~ users_history_info_shard_(0~9) 表 ~~~~~
    // 查询用户历史记录和其对应的时间
    public HistoryData queryHistoryInfo(Long uid);


    // ===================== 时间相关查询 =====================
    // 查询最近一次登录的时间
    public LocalDateTime queryLastLoginTime(Long uid);

    // 查询用户注册的时间
    public LocalDateTime queryRegisterTime(Long uid);


    // ===================== 其他查询（比如：管理员批量查询） =====================
    // 限制每次查询n条数据：
    // 查询 users_basic_info表中的 uid, nickname, avatar, create_at
    // 查询 users_core_info表中的 account, email, cellphone, status, role, last_login_at
    // 要求uid相同的为同一条数据，合并这些数据为一条数据
    public List<User> queryAllUsers(@Param("offset") int offset, @Param("limit") int limit);
}
