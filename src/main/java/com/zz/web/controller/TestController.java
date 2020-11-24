package com.zz.web.controller;

import com.alibaba.fastjson.JSON;
import com.zz.web.commonutil.JWTUtils;
import com.zz.web.commonutil.Md5Util;
import com.zz.web.commonutil.ResponseVO;
import com.zz.web.entity.People;
import com.zz.web.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@RestController
@Slf4j
public class TestController {

    @Autowired
    @Qualifier("testServiceImpl")
    private TestService testService;


    @RequestMapping("/test")
    public String test(){
      People people = testService.getPeopleByID(1);
        String toJSONString = JSON.toJSONString(people);
        return toJSONString ;
   }

    @RequestMapping("/login")
    public String login(@RequestBody Map<String, String> input, HttpServletResponse response){
        String username = input.get("username");
        String password = input.get("password");
        log.info("username:{},password:{}",username,password);

        //获得当前用户到登录对象，现在状态为未认证
        Subject subject= SecurityUtils.getSubject();
        //用户名密码令牌
        AuthenticationToken token=new UsernamePasswordToken(username, Md5Util.MD5(password));

        try {
            //将令牌传到shiro提供的login方法验证，需要自定义realm
            subject.login(token);
            //没有异常表示验证成功
        } catch (Exception e){
            return  JSON.toJSONString(ResponseVO.fail("用户名或密码不正确"));
        }
        //登录完成
        //查询相关权限 返回客服端
        //创建jwtToken
        String jwt = JWTUtils.createToken(username,"admin");
        ResponseVO responseVO = ResponseVO.success();
        responseVO.setToken(jwt);
        return  JSON.toJSONString(responseVO);

    }


    @RequestMapping("/system/test")
    public String Systemtest(){
        People people = testService.getPeopleByID(1);
        String toJSONString = JSON.toJSONString(people);
        return toJSONString ;
    }



}
