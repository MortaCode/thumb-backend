//package com.example.thumb.common.utils;
//
//import com.example.thumb.common.constants.Constants;
//import jakarta.annotation.Resource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Component
//public class RedisUtil {
//    @Autowired
//    private final RedisTemplate<String, Object> redisTemplate;
//
//
//    /**
//     * 是否已点赞
//     */
//    public static boolean hasThumb(String userId, String blogId){
//        return  redisTemplate.opsForHash()
//                .hasKey(Constants.USER_THUMB_PREFIX+userId, blogId);
//    }
//
//    /**
//     * 是否已点赞
//     */
//    public static List<Object> hasThumb(String userId, List<String> blogIds){
//        List<Object> list = Optional.ofNullable(blogIds).stream().map(e->(Object)e).collect(Collectors.toList());
//        return redisTemplate.opsForHash()
//                .multiGet(Constants.USER_THUMB_PREFIX+userId, list);
//    }
//
//    /**
//     * 点赞
//     * @param userId
//     * @param blogId
//     * @return
//     */
//    public static void doThumb(String userId, String blogId){
//        redisTemplate.opsForHash()
//                .put(Constants.USER_THUMB_PREFIX+userId, blogId, blogId);
//    }
//
//    /**
//     * 取消点赞
//     * @param userId
//     * @param blogId
//     * @return
//     */
//    public static void undoThumb(String userId, String blogId){
//
//        Boolean flag = redisTemplate.opsForHash()
//                .hasKey(Constants.USER_THUMB_PREFIX+userId, blogId);
//        if (flag){
//            redisTemplate.opsForHash()
//                    .delete(Constants.USER_THUMB_PREFIX+userId, blogId);
//        }
//    }
//}
