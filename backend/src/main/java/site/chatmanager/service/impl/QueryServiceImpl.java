package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import site.chatmanager.mapper.QueryMapper;
import site.chatmanager.pojo.Result;
import site.chatmanager.pojo.data.CoreData;
import site.chatmanager.pojo.data.HistoryData;
import site.chatmanager.pojo.data.PersonalityData;
import site.chatmanager.service.QueryService;
import site.chatmanager.utils.EncryptionUtils;

import java.util.List;

@Slf4j
@Component
public class QueryServiceImpl implements QueryService {

    @Autowired
    private QueryMapper queryMapper;

    @Override
    public ResponseEntity<Result> queryUserBasicInfo(Long uid) {
        PersonalityData data = queryMapper.queryBasicInfo(uid);     // 查询：用户昵称、用户头像
        Result result = Result.success("查询成功", data);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> queryUserHistoryInfo(Long uid) {
        List<HistoryData> data = queryMapper.queryHistoryInfo(uid);     // 查询：用户聊天历史信息
        Result result = Result.success("查询成功", data);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> queryUserCoreInfo(Long uid) {
        CoreData data = queryMapper.queryCoreInfo(uid);     // 查询：用户账号，用户邮箱，用户手机号
        data.setEmail(EncryptionUtils.normalSecurityDecrypt(data.getEmail()));
        // 目前手机号不参与运营
        //data.setCellphone(EncryptionUtils.normalSecurityDecrypt(data.getCellphone()));
        Result result = Result.success("查询成功", data);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> queryUserConfigInfo(Long uid) {
        String data = queryMapper.queryModelsConfig(uid);   // 查询：用户模型配置信息
        Result result = Result.success("查询成功", data);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
