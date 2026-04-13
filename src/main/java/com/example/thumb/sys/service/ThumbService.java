package com.example.thumb.sys.service;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.thumb.common.constants.Constants;
import com.example.thumb.sys.entity.Blog;
import com.example.thumb.sys.entity.Thumb;
import com.example.thumb.sys.entity.User;
import com.example.thumb.sys.mapper.ThumbMapper;
import com.example.thumb.sys.vo.MsgVo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
* @author ADMIN
* @description 针对表【thumb】的数据库操作Service
* @createDate 2026-04-12 15:45:27
*/
@Slf4j
@Service
@AllArgsConstructor
public class ThumbService extends ServiceImpl<ThumbMapper, Thumb> {

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserService userService;
    private final BlogService blogService;

    /**
     * 点赞和取消点赞
     */
    @Transactional
    public MsgVo thumb(HttpServletRequest request, String blogId) {
        if (request == null || !StringUtils.hasText(blogId)){
            throw new RuntimeException("参数有误");
        }
        User user = userService.getLoginUser(request);
        Assert.notNull(user, "请登录");
        synchronized (user.getId().toString().intern()){
            Thumb thumb = this.lambdaQuery()
                    .eq(Thumb::getBlogid, blogId)
                    .eq(Thumb::getUserid, user.getId())
                    .one();
            boolean isUpdate = thumb != null;
            if (!isUpdate){
                thumb= new Thumb();
                thumb.setId(IdUtil.objectId());
                thumb.setBlogid(blogId);
                thumb.setUserid(user.getId());
                boolean thumbFlag = this.save(thumb);
                boolean blogFlag = blogService.lambdaUpdate()
                        .eq(Blog::getId, blogId)
                        .setSql("thumbCount = thumbCount + 1")
                        .update();
                if (!thumbFlag || !blogFlag){
                    log.error("点赞失败，用户{}博客{}",user.getId(), blogId);
                    throw new RuntimeException("点赞失败");
                }
                //插入缓存
                redisTemplate.opsForHash()
                        .put(Constants.THUMB_KEY_PREFIX + user.getId(), blogId, blogId);
            } else {
                boolean thumbFlag = this.removeById(thumb.getId());
                boolean blogFlag = blogService.lambdaUpdate()
                        .eq(Blog::getId, blogId)
                        .setSql("thumbCount = thumbCount - 1")
                        .update();
                if (!thumbFlag || !blogFlag){
                    log.error("取消点赞失败，用户{}--博客{}",user.getId(), blogId);
                    throw new RuntimeException("取消点赞失败");
                }
                //删除缓存
                Boolean flag = redisTemplate.opsForHash()
                        .hasKey(Constants.THUMB_KEY_PREFIX + user.getId(), blogId);
                if (flag){
                    redisTemplate.opsForHash()
                            .delete(Constants.THUMB_KEY_PREFIX + user.getId(), blogId);
                }
            }
            return MsgVo.of(!isUpdate ? "点赞成功" : "取消点赞", thumb.getId());
        }
    }

}
