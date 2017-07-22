package com.linchuanedu.edu.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by pansp
 * Date: 2017-7-21
 * Time: 15:52
 */

@Controller
@RequestMapping(value = "/v1/edu/app/user")
public class UserController {

    @RequestMapping(value = "/signin",method = RequestMethod.POST, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    public void userSignIn() {

    }

}
