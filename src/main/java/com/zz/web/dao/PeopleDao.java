package com.zz.web.dao;

import com.zz.web.entity.People;
import org.springframework.stereotype.Repository;


public interface PeopleDao {

    People getPeopleByID(int id);
}
