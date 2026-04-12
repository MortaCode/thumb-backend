package com.example.thumb.sys.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.thumb.common.constants.Constants;
import com.example.thumb.sys.entity.Blog;
import com.example.thumb.sys.entity.User;
import com.example.thumb.sys.mapper.BlogMapper;
import com.example.thumb.sys.mapper.ThumbMapper;
import com.example.thumb.sys.vo.BlogVo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author ADMIN
* @description 针对表【blog】的数据库操作Service
* @createDate 2026-04-12 15:45:17
*/
@Service
@AllArgsConstructor
public class BlogService extends ServiceImpl<BlogMapper, Blog> {

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserService userService;

    /**
     * 获取博客列表
     * @param request
     * @param blogIds 逗号拼接
     * @return
     */
    public List<BlogVo> searchByIds(HttpServletRequest request, String blogIds) {
        Assert.hasLength(blogIds, "参数为空");
        List<String> ids = Arrays.asList(blogIds.split(","));
        User user = userService.getLoginUser(request);
        List<BlogVo> blogList = BeanUtil.copyToList(this.lambdaQuery().in(Blog::getId, ids).list(), BlogVo.class);
        if (user == null || CollectionUtils.isEmpty(blogList)){return blogList;}
        //数据库版本
//        Set<String> thumbSet = thumbMapper.selectList(
//                new LambdaQueryWrapper<>(Thumb.class)
//                .in(Thumb::getBlogid, ids)
//                .eq(Thumb::getUserid, user.getId())
//                ).stream().map(Thumb::getBlogid).collect(Collectors.toSet());
//                //.collect(Collectors.toMap(Thumb::getBlogid, e->Boolean.TRUE));
        //缓存版本
        List<Object> list = Optional.ofNullable(blogIds).stream().map(e->(Object)e).collect(Collectors.toList());
        Set<String> thumbSet = Optional.ofNullable(
                redisTemplate.opsForHash()
                        .multiGet(Constants.USER_THUMB_PREFIX + user.getId(), list)
                ).orElse(new ArrayList<>()).stream()
                .filter(Objects::nonNull)
                .map(Objects::toString).collect(Collectors.toSet());
        return blogList.stream().map(e -> {
            e.setHasThumb(thumbSet.contains(e.getId()));
            return e;
        }).collect(Collectors.toList());
    }

    /**
     * 获取博客
     * @param request
     * @param blogId
     * @return
     */
    public BlogVo searchById(HttpServletRequest request, String blogId) {
        Assert.hasLength(blogId, "参数为空");
        Blog blog = this.getById(blogId);
        User user = userService.getLoginUser(request);
        return buildBlog(blog, user);
    }

    /**
     * 构建单个博客
     * @param blog
     * @param user
     * @return
     */
    private BlogVo buildBlog(Blog blog, User user) {
        BlogVo blogVo = BeanUtil.toBean(blog, BlogVo.class);
        if (user == null){
            return blogVo;
        }
        //数据库版本
//        Thumb thumb = thumbMapper.selectOne(
//                new LambdaQueryWrapper<>(Thumb.class)
//                .eq(Thumb::getUserid, user.getId())
//                .eq(Thumb::getBlogid, blog.getId())
//        );
        //缓存版本
        Boolean hasThumb = redisTemplate.opsForHash()
                .hasKey(Constants.USER_THUMB_PREFIX + user.getId(), blog.getId());
        blogVo.setHasThumb(hasThumb);
        return blogVo;
    }


}
