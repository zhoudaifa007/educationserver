package com.linchuanedu.edu.service.service;

import com.linchuanedu.edu.common.model.DTO.CreateUserDTO;
import com.linchuanedu.edu.service.cache.UserCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}
