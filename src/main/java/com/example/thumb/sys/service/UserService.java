package com.example.thumb.sys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.thumb.common.Constants;
import com.example.thumb.sys.entity.User;
import com.example.thumb.sys.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
* @author ADMIN
* @description 针对表【user】的数据库操作Service
* @createDate 2026-04-12 15:45:08
*/
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    /**
     * 登录
     * @param userid
     * @return
     */
    public User login(HttpServletRequest request, String userid) {
        Assert.hasLength(userid, "参数为空");
        User user = this.lambdaQuery().eq(User::getId, userid).one();
        Assert.notNull(user, "未获取到用户信息");
        request.getSession().setAttribute(Constants.USER_LOGIN, user);
        return user;
    }

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    public User getLoginUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute(Constants.USER_LOGIN);
    }
}
