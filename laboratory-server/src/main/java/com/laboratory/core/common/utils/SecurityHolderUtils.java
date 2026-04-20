package com.laboratory.core.common.utils;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 获取当前登录用户信息
 *
 * @author com.zyp
 * @since 2025-07-18
 */
public class SecurityHolderUtils {

    /**
     * 获取当前登录用户ID
     */
    public static Integer getUserId() {
        return Integer.parseInt(Objects.requireNonNull(getClaims("userId")));
    }

    /**
     * 获取当前登录用户名称
     */
    public static String getUsername() {
        return getClaims("nickname");
    }


    /**
     * 获取当前登录用户角色ID
     */
    public static Integer getRoleId() {
        return Integer.parseInt(Objects.requireNonNull(getClaims("roleId")));
    }


    /**
     * 判断当前用户是否是管理员
     */
    public static boolean isAdmin(){
        return getRoleId() == 1;
    }

    private static String getClaims(String key) {
        HttpServletRequest request = CommonUtils.getRequest();
        String token = request.getHeader("token");
        if (StrUtil.isNotEmpty(token)) {
            DecodedJWT decoded = JwtUtils.verifyToken(token);
            return decoded.getClaims().get(key).asString();
        }

        return "";
    }
}
