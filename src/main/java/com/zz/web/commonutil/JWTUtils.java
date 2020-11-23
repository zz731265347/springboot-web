package com.zz.web.commonutil;

import com.alibaba.druid.util.StringUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.zz.web.enums.TokenState;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Z sir
 * @Description
 *
 * @date 2020/11/20 10:52
 */
@Slf4j
public class JWTUtils {

    /**
     * 过期时间30分钟
     */
    private static final long EXPIRE_TIME = 30 * 60 * 1000;
    /**
     * jwt 密钥
     */
    private static final String SECRET = "jwt_secret";

    /**
     * 生成签名，五分钟后过期
     * @param userId
     * @return
     */
    /*
    标准中注册的声明 (建议但不强制使用) ：

    iss: jwt签发者
    sub: jwt所面向的用户
    aud: 接收jwt的一方
    exp: jwt的过期时间，这个过期时间必须要大于签发时间
    nbf: 定义在什么时间之前，该jwt都是不可用的.
    iat: jwt的签发时间
    jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。

     */
    public static String createToken(String userId,String roleid) {
        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            return JWT.create()
                    // 将 user id 保存到 token 里面 对应payload  uid
                    .withClaim("uid",userId)
                    //  设置用户角色 类型
                    .withClaim("rol",roleid)
                    // 30分钟后token过期
                    .withExpiresAt(date)
                    // token 的密钥
                    .sign(algorithm);
        } catch (Exception e) {
            return null;
        }
    }



    /**
     * 校验token
     * @param token
     * @return
     */
    public static Map<String, Object> checkSign(String token ,String userid) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            Claim rolClaim = jwt.getClaims().get("rol");
            //  String user_id_claim = rol;
            if (null == rolClaim || StringUtils.isEmpty(rolClaim.asString())) {
                // token 校验失败, 抛出Token验证非法异
                throw   new JWTVerificationException(" JWT 验证不通过");
            }
             String tuserId  =  jwt.getClaims().get("uid").asString();;
            if(!userid.equals(tuserId) ){
                throw   new JWTVerificationException("uid 不匹配");
            }
            String roleId  = rolClaim.asString();
            resultMap.put("state", TokenState.VALID);
            resultMap.put("userId",tuserId);
            resultMap.put("roleID",roleId);
            return resultMap;
        } catch (JWTVerificationException exception) {
            log.info("token 无效，请重新获取");
            resultMap.put("state", TokenState.INVALID);
            return resultMap;
        }catch (NullPointerException exception) {
            log.info("token 获取错误");
            resultMap.put("state", TokenState.INVALID);
            return resultMap;
        }
    }

    public static void main(String[] args) {
        String token  = JWTUtils.createToken("125801", "2");
        System.out.println("["+ token +"]");
        String mytoken = "WQiOiIxMjU4MDEiLCJleHAiOjE2MDYxMjAxMzksInJvbCI6IjIifQ.c7wy5GUpdzxBkCjGZe2LDfBC0cMFYY5AJa336IaOT6A";
        Map tokenMap = JWTUtils.checkSign(mytoken,"125801");
        System.out.println(tokenMap.get("state"));
    }

}
