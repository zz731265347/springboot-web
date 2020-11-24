package com.zz.web.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Z sir
 * @Description 此类的作用是用于  根据不同的请求进入不同realm
 *              用于login  还是 jwt realm
 * @date 2020/11/24 16:08
 */
@Slf4j
public class UserModularRealmAuthenticator extends ModularRealmAuthenticator {

    //当subject.login()方法执行,下面的代码即将执行
    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("UserModularRealmAuthenticator:method doAuthenticate() 执行 ");
        // 判断getRealms()是否返回为空
        assertRealmsConfigured();
        // 所有Realm
        Collection<Realm> realms = getRealms();
        // 盛放登录类型对应的所有Realm集合
        Collection<Realm> typeRealms = new ArrayList<>();

        // 强制转换回自定义的Token
        try{
            JwtToken jwtToken = (JwtToken) authenticationToken;
            for(Realm realm : realms){
                log.info("正在遍历的realm是:{}",realm.getName());
                if (realm.getName().contains("JwtRealm")){
                    typeRealms.add(realm);
                }
            }
            log.info("进入了jwttoken的验证");
            return doSingleRealmAuthentication(typeRealms.iterator().next(), jwtToken);
        }catch (ClassCastException e){
            typeRealms.clear();
            log.info("进入了Logintoken的验证");
            for (Realm realm : realms) {
                log.info("正在遍历的realm是:{}",realm.getName());
                if (realm.getName().contains("LoginRealm")){
                    log.info("当前realm:{}被注入:",realm.getName());
                    typeRealms.add(realm);
                }
            }
             return doSingleRealmAuthentication(typeRealms.iterator().next(), authenticationToken);

        }

    }

}