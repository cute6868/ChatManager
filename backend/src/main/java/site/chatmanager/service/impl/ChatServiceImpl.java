package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import site.chatmanager.mapper.QueryMapper;
import site.chatmanager.pojo.data.DialogData;
import site.chatmanager.service.ChatService;
import site.chatmanager.pojo.Result;

import java.util.List;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private QueryMapper queryMapper;

    @Override
    public ResponseEntity<Result> recommend(Long uid) {

        // 查询用户兴趣
        String interests = queryMapper.queryInterests(uid);

        // 调用AI模型，获取推荐结果

        // 返回结果
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<Result> chat(Long uid, DialogData data) {

        // 1.用户选择了哪些模型
        List<String> models = data.getModels();

        // 2.用户询问了什么问题
        String question = data.getQuestion();

        // 3.同时发送问题，模型开始回答


        // 4.多个模型实时回答的内容，要实时都返回给用户

        // 返回数据内容
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
