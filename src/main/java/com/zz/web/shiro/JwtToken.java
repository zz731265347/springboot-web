package com.zz.web.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author Z sir
 * @Description TODO
 * @date 2020/11/24 10:40
 */

//这个就类似UsernamePasswordToken
public class JwtToken implements AuthenticationToken {

    private String jwt;

    public JwtToken(String jwt) {
        this.jwt = jwt;
    }

    @Override//类似是用户名
    public Object getPrincipal() {
        return jwt;
    }

    @Override//类似密码
    public Object getCredentials() {
        return jwt;
    }
    //返回的都是jwt
}