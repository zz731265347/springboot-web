package com.zz.web.controller;

import com.alibaba.fastjson.JSON;
import com.zz.web.commonutil.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Z sir
 * @Description TODO
 * @date 2020/11/25 10:18
 */
@Slf4j
@RestController
public class  CommonController {
    
    @RequestMapping("/code/200")
    public String code200(){
        ResponseVO success = ResponseVO.success();
        return  JSON.toJSONString(success);
    }

    @RequestMapping("/code/401")
    public String code401(){
        ResponseVO fail = new ResponseVO("401","没有权限");
        return  JSON.toJSONString(fail);
    }

    @RequestMapping("/code/402")
    public String code402(){
        ResponseVO fail = new ResponseVO("402","用户名或名密码不匹配");
        return  JSON.toJSONString(fail);
    }



}
