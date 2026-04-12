package com.example.thumb.sys.controller;

import com.example.thumb.sys.entity.User;
import com.example.thumb.sys.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("user")
public class UserController {

    private final UserService userService;


    /**
     * 登录
     * @param request
     * @param userid
     * @return
     */
    @GetMapping("login")
    public ResponseEntity<User> login(HttpServletRequest request, String userid){
        return ResponseEntity.ok(userService.login(request, userid));
    }

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    @GetMapping("login/getCur")
    public ResponseEntity<User> getLoginUser(HttpServletRequest request){
        return ResponseEntity.ok(userService.getLoginUser(request));
    }
}
