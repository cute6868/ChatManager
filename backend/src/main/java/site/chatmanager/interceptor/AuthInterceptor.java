package site.chatmanager.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import site.chatmanager.pojo.Result;
import site.chatmanager.service.common.RedisService;
import site.chatmanager.utils.JwtUtils;

import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    private static final Pattern UID_PATTERN = Pattern.compile("/(\\d+)(/|$)");

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {

        // 设置响应的内容类型为 JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 1.获取请求路径
        String path = request.getRequestURI();
        if (path == null) {
            sendErrorResult(response, "请求错误");
            return false;
        }

        // 2.获取请求头中的 token
        String token = request.getHeader("Authorization");

        // 3.如果 token 不存在
        if (token == null || token.isEmpty()) {
            // 3.1 如果发送的是登录或注册请求
            if (path.contains("/api/login") || path.contains("/api/register")) {
                return true; // 直接放行
            }
            // 3.2 如果是其他请求，则返回未登录错误
            sendErrorResult(response, "请先登录");
            return false;
        }

        // 4.验证 token 有效性，若无效则返回无效令牌错误
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // 如果有Bearer前缀，则去除
        }
        Object[] infoFromToken = JwtUtils.getInfoFromToken(token);
        if (infoFromToken == null) {
            // 解析失败，说明 token 无效，用户并没有成功登录
            // 返回 401 错误
            sendErrorResult(response, "无效令牌");
            return false;
        } else {
            // 解析成功，说明 token 有效，用户已经成功登录
            // 如果是登录请求，则返回已登录错误
            if (path.contains("/api/login") || path.contains("/api/register")) {
                sendErrorResult(response, "请退出登录后操作");
                return false;
            }
        }

        // 5.检查 token 的 jti 是否在黑名单中
        Long uid = (Long) infoFromToken[0];
        String jti = (String) infoFromToken[2];
        String blacklistKey = "b:uid:" + uid;
        if (redisService.isMemberOfZSetAndValid(blacklistKey, jti)) {
            sendErrorResult(response, "令牌已失效，请重新登录");
            return false;
        }

        // 5.检查请求路径是否需要验证 uid
        if (path.contains("/api/query/")
                || path.contains("/api/chat/")
                || path.contains("/api/logout/")
                || path.contains("/api/authenticate/")
                || path.contains("/api/update/")
                || path.contains("/api/deactivate/")) {
            // 从路径中提取 uid
            Matcher matcher = UID_PATTERN.matcher(path);
            if (matcher.find()) {
                try {
                    Long pathUid = Long.parseLong(matcher.group(1));
                    // 比较路径中的 uid 和 token 中的 uid
                    if (!uid.equals(pathUid)) {
                        sendErrorResult(response, "请求不合法");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    sendErrorResult(response, "请求不合法");
                    return false;
                }
            }
        }

        // 6.所有验证通过，放行请求
        return true;
    }

    private void sendErrorResult(HttpServletResponse response, String msg) throws Exception {
        // 封装结果对象
        Result result = Result.failure(msg);

        // 将结果对象转换为 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResult = objectMapper.writeValueAsString(result);

        // 将 JSON 字符串写入响应体并刷新输出流
        PrintWriter out = response.getWriter();
        out.print(jsonResult);
        out.flush();
    }
}