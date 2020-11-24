package com.zz.web.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.zz.web.commonutil.JWTUtils;
import com.zz.web.enums.TokenState;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Z sir
 * @Description TODO
 * @date 2020/11/24 10:43
 */
@Slf4j
public class JwtRealm extends AuthorizingRealm {
    /*
     * 多重写一个support
     * 标识这个Realm是专门用来验证JwtToken
     * 不负责验证其他的token（UsernamePasswordToken）
     * */
    @Override
    public boolean supports(AuthenticationToken token) {
        //这个token就是从过滤器中传入的jwtToken
        return token instanceof JwtToken;
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("进入 JwtRealm  === 授权 >doGetAuthorizationInfo");
        //获取用户名 (因为 principals 定义为token数据  详见JWTtoken  )
        String jwt=(String)principals.getPrimaryPrincipal();
        Map<String, Object> jwtmap = JWTUtils.checkSign(jwt);



        //角色权限 || 获取角色和权限信息（一般从数据库或缓存中获取）
        String role = (String) jwtmap.get("roleID");
        log.info("role---------->{}",role);
        //资源权限
       // Set<String> setPression=getPressionByUserName();

        //设置SimpleAuthorizationInfo信息，（角色和权限）
        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();

        simpleAuthorizationInfo.addRole(role);//放入角色权限

        //放入资源权限
        //simpleAuthorizationInfo.setStringPermissions(setPression);
        return simpleAuthorizationInfo;

    }

    //认证
    //这个token就是从过滤器中传入的jwtToken
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("进入 JwtRealm  === 认证 >doGetAuthenticationInfo");
        String jwt = (String) token.getPrincipal();
        if (jwt == null) {
            throw new AuthenticationException("jwtToken 不允许为空");
        }
        //判断
        Map<String, Object> jwtmap = JWTUtils.checkSign(jwt);
        log.info(jwtmap.toString());
        if (jwtmap.get("state") != TokenState.VALID) {
            throw new AuthenticationException("jwtToken  验证失败");
        }
        //下面是验证这个user是否是真实存在的
        String username = (String) jwtmap.get("userId");//判断数据库中username是否存在
        log.info("在使用token登录"+username);
        return new SimpleAuthenticationInfo(jwt,jwt,"jwtRealm");
        //这里返回的是类似账号密码的东西，但是jwtToken都是jwt字符串。还需要一个该Realm的类名

    }

}