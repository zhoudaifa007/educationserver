package com.linchuanedu.edu.service.cache;

import com.linchuanedu.edu.common.model.DO.UserDO;
import com.linchuanedu.edu.common.model.DTO.CreateUserDTO;
import com.linchuanedu.edu.dao.IUserDao;
import com.linchuanedu.edu.plugin.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by pansp
 * Date: 2017-7-21
 * Time: 15:52
 */

@Component
public class UserCache {

    @Resource
    private IUserDao iUserDao;

    public void createUser(CreateUserDTO createUserDTO){
        iUserDao.createUser(createUserDTO);
    }

    public List<UserDO> getUser() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        PageParams pageParams = new PageParams();
        pageParams.setUseFlag(true);
        pageParams.setCheckFlag(false);
        pageParams.setPage(2);
        pageParams.setPageSize(5);
        paramMap.put("roleName", "test");
        paramMap.put("$pageParams", pageParams);
        List<UserDO> list =  iUserDao.getUser(paramMap);
        Integer total = pageParams.getTotal();
        System.out.println("---------");
        System.out.println(total);
        return list;
    }

}
