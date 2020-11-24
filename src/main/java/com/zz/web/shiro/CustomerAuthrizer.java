package com.zz.web.shiro;

import io.jsonwebtoken.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Set;

/**
 * @author Z sir
 * @Description TODO
 * @date 2020/11/24 22:25
 */
@Slf4j
public class CustomerAuthrizer extends ModularRealmAuthorizer {


    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {

        assertRealmsConfigured();
        Set<String> realmNames = principals.getRealmNames();
        //获取realm的名字
        String realmName = realmNames.iterator().next();
        for (Realm realm : getRealms()) {
            if (!(realm instanceof Authorizer)) continue;
            //匹配名字
            if(realmName.contains("JwtRealm")) {
                if (realm instanceof JwtRealm) {
                    return ((JwtRealm) realm).isPermitted(principals, permission);
                }
            }

        }
        return false;
    }


    // 其实对于我们的项目 只用到String类型   下面可以不用重写
    @Override
    public boolean isPermitted(PrincipalCollection principals, Permission permission) {

        assertRealmsConfigured();
        Set<String> realmNames = principals.getRealmNames();
        //获取realm的名字
        String realmName = realmNames.iterator().next();
        for (Realm realm : getRealms()) {
            if (!(realm instanceof Authorizer)) continue;
            //匹配名字
            if(realmName.equals("JwtRealm")) {
                if (realm instanceof JwtRealm) {
                    return ((JwtRealm) realm).isPermitted(principals, permission);
                }
            }

        }
        return false;
    }

    @Override
    public boolean hasRole(PrincipalCollection principals, String roleIdentifier) {

        log.info("--------------------[{}]",roleIdentifier);
        assertRealmsConfigured();
        Set<String> realmNames = principals.getRealmNames();
        //获取realm的名字
        String realmName = realmNames.iterator().next();
        for (Realm realm : getRealms()) {
            log.info("--------2-----------[{}]",realm.getName());
            if (!(realm instanceof Authorizer)) continue;
            //匹配名字
            if(realmName.contains("JwtRealm")) {
                if (realm instanceof JwtRealm) {
                    return ((JwtRealm) realm).hasRole(principals, roleIdentifier);
                }
            }

        }
        return false;
    }










}