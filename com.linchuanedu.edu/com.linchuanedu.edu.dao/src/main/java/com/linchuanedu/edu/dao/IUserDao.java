package com.linchuanedu.edu.dao;

import com.linchuanedu.edu.common.model.DO.UserDO;
import com.linchuanedu.edu.common.model.DTO.CreateUserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by pansp
 * Date: 2017-7-23
 * Time: 10:20
 */

@Mapper
@Component(value = "iUserDao")
public interface IUserDao {
    void createUser(CreateUserDTO createUserDTO);

    List<UserDO> getUser(Map<String, Object> params);
}
