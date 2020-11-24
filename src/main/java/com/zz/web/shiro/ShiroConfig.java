package com.zz.web.shiro;


import com.zz.web.filter.JwtFilter;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.*;

/**
 * @author Z sir
 * @Description TODO
 * @date 2020/11/20 10:54
 */

@Configuration
public class ShiroConfig {

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     * 所以我们需要修改下doGetAuthenticationInfo中的
     * @return
     */
    @Bean(name = "credentialsMatcher")
    public CredentialsMatcher credentialsMatcher() {
        return new CredentialsMatcher();
    }

    /**
     * 用于 登录时的验证
     * @return
     */
    @Bean("loginRealm")
    public LoginRealm myShiroRealm() {
        LoginRealm customRealm = new LoginRealm();
        //  此处配置认证时密码比较的方式
        customRealm.setCredentialsMatcher(credentialsMatcher());
        return customRealm;
    }

    /**
     * 用于 各个请求访问时的 jwt验证
     * @return
     */

    @Bean("jwtRealm")
    public Realm jwtRealm() {
        Realm jwtRealm = new JwtRealm();
        return jwtRealm;
    }



    @Bean
    public UserModularRealmAuthenticator userModularRealmAuthenticator() {
        //自己重写的ModularRealmAuthenticator
        UserModularRealmAuthenticator modularRealmAuthenticator = new UserModularRealmAuthenticator();
        // 设置  有一个 reaml 校验通过就行
        modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return modularRealmAuthenticator;
    }

    // 配置 SecurityManager
    @Bean(name = "securityManager")
    public SecurityManager securityManager(@Qualifier("jwtRealm")  Realm jwtRealm,
                                           @Qualifier("loginRealm")  Realm loginRealm,
             @Qualifier("userModularRealmAuthenticator") UserModularRealmAuthenticator userModularRealmAuthenticator) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setAuthenticator(userModularRealmAuthenticator);
        // 设置realm.
        List<Realm> realms = new ArrayList<>();
        // 添加多个realm
        realms.add(loginRealm);
        realms.add(jwtRealm);
        securityManager.setRealms(realms);

        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);



       /* CustomerAuthrizer customModularRealmAuthorizer = new CustomerAuthrizer();
        customModularRealmAuthorizer.setRealms(realms);
        securityManager.setAuthorizer(customModularRealmAuthorizer);*/


        return securityManager;
    }




    // shiro 通用配置 定义3个类对象注入spring
    //Filter工厂，设置对应的过滤条件和跳转条件
    /* Shiro内置过滤器，可以实现拦截器相关的拦截器
     * 常用的过滤器：
     * anon：无需认证（登录）可以访问
     * authc：必须认证才可以访问
     * user：如果使用rememberMe的功能可以直接访问
     * perms：该资源必须得到资源权限才可以访问
     * role：该资源必须得到角色权限才可以访问
     **/
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);



        Map<String, Filter> filterMap = new HashMap<>();
        //这个地方其实另外两个filter可以不设置，默认就是
        filterMap.put("anon", new AnonymousFilter());
        filterMap.put("logout", new LogoutFilter());
        filterMap.put("jwt", new JwtFilter());
        shiroFilterFactoryBean.setFilters(filterMap);


        Map<String, String> map = new HashMap<>();
        //登出
        map.put("/logout", "logout");

        map.put("/login", "anon");
        map.put("/system/**", "jwt,roles[admin]");
        //对所有用户认证
        map.put("/**", "jwt");
        //登录
        shiroFilterFactoryBean.setLoginUrl("/login");
        //首页
        shiroFilterFactoryBean.setSuccessUrl("/index");
        //错误页面，认证不通过跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }








    /**
     * 以下Bean开启shiro权限注解
     *
     * @return DefaultAdvisorAutoProxyCreator
     *//*
    @Bean
    public static DefaultAdvisorAutoProxyCreator creator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }*/

}
