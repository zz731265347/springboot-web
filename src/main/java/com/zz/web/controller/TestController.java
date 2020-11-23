package com.zz.web.controller;

import com.alibaba.fastjson.JSON;
import com.zz.web.entity.People;
import com.zz.web.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
}
