package com.example.thumb.sys.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.thumb.sys.entity.Blog;
import com.example.thumb.sys.entity.Thumb;
import com.example.thumb.sys.entity.User;
import com.example.thumb.sys.mapper.BlogMapper;
import com.example.thumb.sys.vo.BlogVo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author ADMIN
* @description 针对表【blog】的数据库操作Service
* @createDate 2026-04-12 15:45:17
*/
@Service
@AllArgsConstructor
public class BlogService extends ServiceImpl<BlogMapper, Blog> {

    private final UserService userService;
    private final ThumbService thumbService;

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
        Set<Long> thumbSet = thumbService.lambdaQuery()
                .in(Thumb::getBlogid, ids)
                .eq(Thumb::getUserid, user.getId())
                .list().stream()
                .map(Thumb::getBlogid).collect(Collectors.toSet());
                //.collect(Collectors.toMap(Thumb::getBlogid, e->Boolean.TRUE));
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
        Thumb thumb = thumbService.lambdaQuery()
                .eq(Thumb::getUserid, user.getId())
                .eq(Thumb::getBlogid, blog.getId())
                .one();
        blogVo.setHasThumb(thumb != null);
        return blogVo;
    }


}
