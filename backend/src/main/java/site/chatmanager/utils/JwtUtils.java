package site.chatmanager.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    // 密钥，应设置为安全且不硬编码的值
    private static final String SECRET_KEY = "9#46Dn!76Rz@vF1f%40Bb%";

    // 令牌有效期，这里设置为 3 天
    private static final long EXPIRATION_TIME = 3 * 24 * 60 * 60 * 1000;

    /**
     * 生成 JWT 令牌
     *
     * @param uid  用户 ID
     * @param role 用户身份
     * @return 生成的 JWT 令牌
     */
    public static String generateToken(Long uid, Integer role) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", uid);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * 从 JWT 令牌中获取用户信息
     *
     * @param token 待解析的 JWT 令牌
     * @return 包含用户 ID 和用户身份的数组，若获取失败返回 null
     */
    public static Object[] getInfoFromToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            Long uid = claims.get("uid", Long.class);
            Integer role = claims.get("role", Integer.class);
            return new Object[]{uid, role};
        } catch (Exception e) {
            return null;
        }
    }
}