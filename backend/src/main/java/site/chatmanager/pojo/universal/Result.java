package site.chatmanager.pojo.universal;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Getter;

@Getter
public class Result {

    private Integer code; // 业务状态码：0表示成功，1、2、3...表示不同原因的失败，-1服务器异常，-2自定义异常
    private String msg;  // 提示信息
    private Object data;  // 要返回的数据

    public static Result success() {
        Result result = new Result();
        result.code = 0;
        result.msg = "success";
        result.data = null;
        return result;
    }

    public static Result success(String msg) {
        Result result = new Result();
        result.code = 0;
        result.msg = msg;
        result.data = null;
        return result;
    }

    public static Result success(String msg, Object data) {
        Result result = new Result();
        result.code = 0;
        result.msg = msg;
        result.data = data;
        return result;
    }

    public static Result failure() {
        Result result = new Result();
        result.code = 1;
        result.msg = "failure";
        result.data = null;
        return result;
    }

    public static Result failure(String msg) {
        Result result = new Result();
        result.code = 1;
        result.msg = msg;
        result.data = null;
        return result;
    }

    public static Result failure(Integer code, String msg) {
        Result result = new Result();
        result.code = code;
        result.msg = msg;
        result.data = null;
        return result;
    }

    public static Result failure(Integer code, String msg, Object data) {
        Result result = new Result();
        result.code = code;
        result.msg = msg;
        result.data = data;
        return result;
    }

}
