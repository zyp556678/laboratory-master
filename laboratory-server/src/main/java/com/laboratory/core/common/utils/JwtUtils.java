package com.laboratory.core.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 *
 * @since 2025-03-03 12:34
 * <p>
 * jwt 工具类
 */
@Component
public class JwtUtils {

    // 过期时间
    private static final Long EXPIRE_TIME = 8 * 60 * 60 * 1000L; // 8 小时

    // 加密密钥 - 从配置文件读取
    private static String SECRET;

    // Redis模板
    private static StringRedisTemplate redisTemplate;

    // Redis Key前缀
    private static final String BLACKLIST_PREFIX = "jwt:blacklist:";

    @Value("${jwt.secret}")
    private void setSecret(String secret) {
        JwtUtils.SECRET = secret;
    }

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        JwtUtils.redisTemplate = redisTemplate;
    }

    /**
     * 创建 token
     *
     * @param claimMap 载体
     * @return token
     */
    public static String createToken(Map<String, String> claimMap) {
        // 过期时间=当前时间加设定时间
        Date expiration = new Date(System.currentTimeMillis() + EXPIRE_TIME);

        // 设置 JWT 头部
        Map<String, Object> map = new HashMap<>();
        map.put("typ", "JWT");
        map.put("alg", "HS256");

        // 创建 token
        JWTCreator.Builder builder = JWT.create();

        // 创建 payload 载体
        claimMap.forEach(builder::withClaim);

        // 生成 token
        return builder.withHeader(map)      // 设置头部
                .withExpiresAt(expiration)  // 设置过期时间
                .sign(Algorithm.HMAC256(SECRET)); // 设置签名解密算法
    }


    /**
     * 验证 token
     *
     * @param token token
     * @return 解密后的 token
     */
    public static DecodedJWT verifyToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }


    /**
     * 添加到黑名单 - 存储到Redis
     * @param token token
     */
    public static void addBlacklist(String token){
        if (redisTemplate != null) {
            // 设置24小时过期时间，与JWT默认过期时间一致
            redisTemplate.opsForValue().set(BLACKLIST_PREFIX + token, "1", 24, TimeUnit.HOURS);
        }
    }

    /**
     * 是否在黑名单 - 从Redis获取
     * @param token token
     * @return 是否在黑名单
     */
    public static boolean isBlacklist(String token){
        if (redisTemplate != null) {
            return redisTemplate.hasKey(BLACKLIST_PREFIX + token);
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println("========== 加密 token ==========");
        Map<String, String> map = new HashMap<>();
        map.put("userId", "1");
        map.put("username", "admin");
        map.put("password", "123456");
        String token = createToken(map);
        System.out.println(token);

        System.out.println("\n========== 解密 token ==========");
        DecodedJWT decoded = verifyToken(token);
        Claim userId = decoded.getClaims().get("userId");

        System.out.println(Integer.parseInt(userId.asString()));

        decoded.getClaims().forEach((k, v) -> System.out.println(k + ": " + v));
    }
}
