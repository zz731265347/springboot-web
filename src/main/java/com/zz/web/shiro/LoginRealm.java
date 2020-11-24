package com.zz.web.shiro;

import com.zz.web.commonutil.Md5Util;
import com.zz.web.commonutil.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @author Z sir
 * @Description TODO
 * @date 2020/11/20 14:34
 */
@Slf4j
public class LoginRealm extends AuthorizingRealm {



    /*
    *   用于授权
    */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("进入 LoginRealm  === 授权 >doGetAuthorizationInfo");
        return null;
    }

    /*
    *  用于认证数据来源
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("进入 LoginRealm  === 认证 >doGetAuthenticationInfo");
       String username = (String) authenticationToken.getPrincipal();
       // 数据库获取用户数据及密匙
        /*if (user == null) {
            // 抛出账号不存在异常
            throw new UnknownAccountException();
        }*/
        String uName = "zz";
        String uPassw = PasswordUtil. encryptPassw(Md5Util.MD5("123456"),uName);

        return new SimpleAuthenticationInfo(uName, uPassw, "loginRealm" );


    }
}
