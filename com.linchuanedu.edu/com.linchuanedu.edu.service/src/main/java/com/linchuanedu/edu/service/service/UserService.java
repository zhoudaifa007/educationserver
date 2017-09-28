package com.linchuanedu.edu.service.service;

import com.linchuanedu.edu.common.model.DO.UserDO;
import com.linchuanedu.edu.common.model.DTO.CreateUserDTO;
import com.linchuanedu.edu.service.cache.UserCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by pansp
 * Date: 2017-7-21
 * Time: 15:52
 */

@Service
public class UserService {

    @Resource
    private UserCache userCache;

    public void createUser(CreateUserDTO createUserDTO){
        userCache.createUser(createUserDTO);
    }

    public List<UserDO> getUser(){
       return userCache.getUser();
    }
}
