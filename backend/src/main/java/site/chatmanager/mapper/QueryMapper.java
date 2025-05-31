package site.chatmanager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import site.chatmanager.pojo.container.*;

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
    public AuthData queryAuthInfoByAccount(String account);

    // 对于邮箱和手机认证方式
    // 认证信息：账号状态、用户身份
    public AuthData queryAuthInfo(Long uid);

    // 登录之后，需要通过邮箱认证身份才能修改核心信息
    public String queryEmail(Long uid);


    // ===================== 获取性查询（比如：展示业务） =====================

    // ~~~~~ users_basic_info 表 ~~~~~
    // 查询用户昵称、用户头像（直接对外）
    public ProfileData queryProfile(Long uid);

    // 查询：用户兴趣爱好（不直接对外）
    public String queryInterests(Long uid);

    // 查询：用户已经选择的模型
    public String querySelectedModels(Long uid);


    // ~~~~~ models_avatar 表 ~~~~~
    // 查询：模型头像
    public String queryModelAvatar(Integer modelId);


    // ~~~~~ users_core_info 表 ~~~~~
    // 查询：用户账号、用户邮箱、用户手机（不得查询密码，但是可以修改密码）
    public ContactData queryContactInfo(Long uid);


    // ~~~~~ users_models_config 表 ~~~~~
    // 查询用户模型配置
    public String queryModelsConfig(Long uid);


    // ~~~~~ users_record_pointer 表 ~~~~~
    // 查询用户的最新序号、最旧序号
    public SequenceData queryRecordSequenceNum(Long uid);


    // ~~~~~ users_record_shard_(0~9) 表 ~~~~~
    // 查询用户的问题、时间、序号
    public List<RecordData> queryRecord(Long uid);


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
    public List<DetailData> queryAllUsers(@Param("offset") int offset, @Param("limit") int limit);
}
