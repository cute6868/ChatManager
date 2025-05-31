package site.chatmanager.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import site.chatmanager.pojo.universal.Result;
import site.chatmanager.service.universal.RedisService;
import site.chatmanager.utils.JwtUtils;

import java.io.PrintWriter;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {

        // 设置响应的内容类型为 JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 1.获取请求路径
        String path = request.getRequestURI();
        if (path == null) {
            sendErrorResult(response, "无效的请求");
            return false;
        }

        // 2.获取请求头中的 token
        String token = request.getHeader("Authorization");

        // 3.如果 token 不存在
        if (token == null || token.isEmpty()) {
            // 3.1 如果发送的是登录或注册请求
            if (path.contains("/api/login") || path.contains("/api/register") || path.contains("/api/reset")) {
                return true; // 直接放行
            }
            // 3.2 如果是其他请求，则返回未登录错误
            sendErrorResult(response, "无效的登录信息，请前往登录");
            return false;
        }

        // 4.验证 token 有效性
        if (token.startsWith("Bearer ")) token = token.substring(7); // 如果有Bearer前缀，则去除
        Object[] infoFromToken = JwtUtils.getInfoFromToken(token);
        if (infoFromToken == null) {
            // 解析失败，有两种可能性：
            // (1) token过期了，从而失效
            // (2) token是乱写的，从而无效

            // 对于第一种情况，用户发送的是登录或注册请求，允许重新登录
            if (path.contains("/api/login") || path.contains("/api/register") || path.contains("/api/reset")) {
                return true; // 直接放行
            }

            // 对于第二种情况，用户发送的是其他请求，返回无效令牌错误
            sendErrorResult(response, "抱歉，您的登录信息已过期，请重新登录");
            return false;
        }

        // 5.检查 token 的 jti 是否在黑名单中
        Long uid = (Long) infoFromToken[0];
        String jti = (String) infoFromToken[2];
        String blacklistKey = "b:uid:" + uid;
        if (redisService.isMemberOfZSetAndValid(blacklistKey, jti)) {
            // 当前 token 有效，但是已经被加入到黑名单中，说明该 token 被强行作废，不可登录
            // (1) 如果用户携带这个作废的 token 只是为了完成登录或注册请求，可以放行
            if (path.contains("/api/login") || path.contains("/api/register") || path.contains("/api/reset")) {
                return true;
            }

            // (2) 如果是其他请求，则返回无效令牌错误
            sendErrorResult(response, "抱歉，您的登录信息已过期，请重新登录");
            return false;
        }

        // 5.运行到这里，说明 token 有效，且没有加入到黑名单中
        // 如果用户携带这个 token 为了完成登录或注册请求，不能放行
        if (path.contains("/api/login") || path.contains("/api/register") || path.contains("/api/reset")) {
            sendErrorResult(response, "当前为已登录状态，请退出登录后重试");
            return false;
        }

        // 5.检查请求路径是否需要验证 uid
        if (path.startsWith("/api/query/")
                || path.startsWith("/api/chat/")
                || path.startsWith("/api/logout/")
                || path.startsWith("/api/authenticate/")
                || path.startsWith("/api/update/")
                || path.startsWith("/api/deactivate/")) {

            // 提取路径中的第一个路径段
            String[] pathSegments = path.split("/");
            if (pathSegments.length >= 4) { // 确保路径至少有4个部分：空字符串、api、路径前缀、数字
                    try {
                        // 提取并验证紧跟在路径前缀后的数字
                        String segmentAfterPrefix = pathSegments[3];
                        Long pathUid = Long.parseLong(segmentAfterPrefix);

                        // 比较路径中的 uid 和 token 中的 uid
                        if (!uid.equals(pathUid)) {
                            sendErrorResult(response, "登录信息异常，请重新登录");
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        // 该段不是数字，不进行验证
                    }
            }
        }

        // 6.所有验证通过，放行请求
        return true;
    }

    private void sendErrorResult(HttpServletResponse response, String msg) throws Exception {
        // 封装结果对象
        Result result = Result.failure(9,msg);

        // 将结果对象转换为 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResult = objectMapper.writeValueAsString(result);

        // 将 JSON 字符串写入响应体并刷新输出流
        PrintWriter out = response.getWriter();
        out.print(jsonResult);
        out.flush();
    }
}