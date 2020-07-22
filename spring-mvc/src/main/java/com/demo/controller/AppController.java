package com.demo.controller;

import com.demo.entity.User;
import com.demo.event.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试用 controller
 *
 * @author mason
 */
@RestController
public class AppController {

    @Autowired
    private UserService userService;

    @Autowired
    private User user;

    @GetMapping("/event")
    public User userRegisterEvent() {
        userService.register("mason");
        return user;
    }

}
