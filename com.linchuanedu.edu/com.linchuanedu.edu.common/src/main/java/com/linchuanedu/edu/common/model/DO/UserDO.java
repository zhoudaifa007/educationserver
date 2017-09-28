package com.linchuanedu.edu.common.model.DO;

/**
 * Created by pansp
 * Date: 2017-7-21
 * Time: 16:21
 */

public class UserDO {

    private Long id;

    private String account;

    private String userName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
