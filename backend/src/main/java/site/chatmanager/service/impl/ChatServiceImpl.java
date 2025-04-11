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
import site.chatmanager.utils.FormatChecker;

import java.util.List;

import static site.chatmanager.api.API.askQuestion;

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

        String question = data.getQuestion();
        List<String> modelList = data.getModelList();   // 获取用户选择的模型

        // 检查用户选择的模型是否合法
        boolean isLegal = FormatChecker.checkModelList(modelList);
        if (!isLegal) {
            Result result = Result.failure("模型选择不符合要求");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 调用api模块的功能，获取回答结果
        String answer = askQuestion(question, modelList);

        // 返回数据内容
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
