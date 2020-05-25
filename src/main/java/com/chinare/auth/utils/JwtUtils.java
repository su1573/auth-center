package com.chinare.auth.utils;

import java.util.Calendar;
import java.util.Date;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtils {
    /**
     * 获得token中的信息无需secret解密也能获得
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名,expireTime后过期
     * @param username 用户名
     * @param time 过期时间s
     * @return 加密的token
     */
    public static String sign(String username, String salt, long time) {
        Date date = new Date(System.currentTimeMillis()+time*1000);
		Algorithm algorithm = Algorithm.HMAC256(salt);
		// 附带username信息
		return JWT.create()
		        .withClaim("username", username)
		        .withExpiresAt(date)
		        .withIssuedAt(new Date(System.currentTimeMillis()-300000))
		        .sign(algorithm);
    }


    /**
     * token是否过期
     * @return true：过期
     */
    public static boolean isTokenExpired(String token) {
        Date now = Calendar.getInstance().getTime();
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt().before(now);
    }

    /**
     * 生成随机盐,长度32位
     * @return
     */
    public static String generateSalt(){
        SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
        String hex = secureRandom.nextBytes(16).toHex();
        return hex;
    }
    
    /**
     * 保存user登录信息，返回token
     * @param userDto
     */
    public static String generateJwtToken(String username) {
    	String salt = "12345";//JwtUtils.generateSalt();
    	/**
    	 * @todo 将salt保存到数据库或者缓存中
    	 * redisTemplate.opsForValue().set("token:"+username, salt, 3600, TimeUnit.SECONDS);
    	 */   	
    	return JwtUtils.sign(username, salt, 3600); //生成jwt token，设置过期时间为1小时
    }
    
}
