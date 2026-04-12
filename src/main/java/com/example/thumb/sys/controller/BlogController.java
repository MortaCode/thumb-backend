package com.example.thumb.sys.controller;

import com.example.thumb.sys.service.BlogService;
import com.example.thumb.sys.vo.BlogVo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("blog")
public class BlogController {

    private final BlogService blogService;

    /**
     * 获取博客
     * @param request
     * @param blogId
     * @return
     */
    @GetMapping("searchById")
    public ResponseEntity<BlogVo> searchById(HttpServletRequest request, String blogId){
        return ResponseEntity.ok(blogService.searchById(request, blogId));
    }

    /**
     * 获取博客列表
     * @param request
     * @param blogIds 逗号拼接
     * @return
     */
    @GetMapping("searchByIds")
    public ResponseEntity<List<BlogVo>> searchByIds(HttpServletRequest request, String blogIds){
        return ResponseEntity.ok(blogService.searchByIds(request, blogIds));
    }




}
