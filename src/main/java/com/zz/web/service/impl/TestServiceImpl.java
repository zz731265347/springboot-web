package com.zz.web.service.impl;

import com.zz.web.dao.PeopleDao;
import com.zz.web.entity.People;
import com.zz.web.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TestServiceImpl implements TestService {

    @Resource
    private PeopleDao peopleDao;

    @Override
    public People getPeopleByID(int id) {
        return peopleDao.getPeopleByID(id);
    }
}
