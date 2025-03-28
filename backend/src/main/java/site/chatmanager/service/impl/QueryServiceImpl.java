package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import site.chatmanager.mapper.QueryMapper;
import site.chatmanager.pojo.Result;
import site.chatmanager.pojo.data.PersonalityData;
import site.chatmanager.service.QueryService;

@Slf4j
@Component
public class QueryServiceImpl implements QueryService {

    @Autowired
    private QueryMapper queryMapper;

    @Override
    public ResponseEntity<Result> queryUserBasicInfo(Long uid) {
        log.info("queryUserBasicInfo");

        PersonalityData data = queryMapper.queryBasicInfo(uid);    // 查询：用户昵称、用户头像
        Result result = Result.success("查询成功", data);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> queryUserHistoryInfo(Long uid) {
        log.info("queryUserHistoryInfo");
        // 查询：用户聊天历史信息
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> queryUserCoreInfo(Long uid) {
        log.info("queryUserCoreInfo");
        // 查询：用户账号，用户邮箱，用户手机号
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> queryUserConfigInfo(Long uid) {
        log.info("queryUserConfigInfo");
        // 查询：用户模型配置信息（比如：豆包、deepseek、通义千问、讯飞星火等）
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
