package com.linchuanedu.edu.service.service;

import com.linchuanedu.edu.service.cache.UserCache;
import org.springframework.stereotype.Service;

/**
 * Created by pansp
 * Date: 2017-7-21
 * Time: 15:52
 */

@Service
public class UserService {

    private UserCache userCache;

    public void createUser(String phone, String password){
        userCache.createUser(phone,password);
    }
}
