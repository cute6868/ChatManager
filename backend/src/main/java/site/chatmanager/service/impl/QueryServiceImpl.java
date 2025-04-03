package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import site.chatmanager.mapper.QueryMapper;
import site.chatmanager.pojo.Result;
import site.chatmanager.pojo.data.ContactData;
import site.chatmanager.pojo.data.ProfileData;
import site.chatmanager.pojo.data.RecordData;
import site.chatmanager.service.QueryService;
import site.chatmanager.service.common.RecordService;
import site.chatmanager.utils.EncryptionUtils;

import java.util.List;

@Slf4j
@Service
public class QueryServiceImpl implements QueryService {

    @Autowired
    private QueryMapper queryMapper;

    @Autowired
    private RecordService recordService;

    @Override
    public ResponseEntity<Result> queryUserProfile(Long uid) {

        // 查询用户昵称、用户头像
        ProfileData data = queryMapper.queryProfile(uid);
        if (data == null) {
            Result result = Result.failure("数据为空");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 返回数据
        Result result = Result.success("查询成功", data);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @Override
    public ResponseEntity<Result> queryUserRecord(Long uid) {

        // 查询用户记录
        List<RecordData> data = recordService.getRecord(uid);
        if (data == null) {
            Result result = Result.failure("数据为空");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 返回数据
        Result result = Result.success("查询成功", data);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @Override
    public ResponseEntity<Result> queryUserContactInfo(Long uid) {

        // 查询用户联系信息：账号、邮箱、手机号
        ContactData data = queryMapper.queryContactInfo(uid);
        if (data == null) {
            Result result = Result.failure("数据为空");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 解密邮箱，解密手机号
        data.setEmail(EncryptionUtils.normalSecurityDecrypt(data.getEmail()));
        //data.setCellphone(EncryptionUtils.normalSecurityDecrypt(data.getCellphone()));  // 目前不参与运营

        // 返回数据
        Result result = Result.success("查询成功", data);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> queryUserModelConfig(Long uid) {

        // 查询用户模型配置
        String data = queryMapper.queryModelsConfig(uid);
        if (data == null) {
            Result result = Result.failure("数据为空");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        // 返回数据
        Result result = Result.success("查询成功", data);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
