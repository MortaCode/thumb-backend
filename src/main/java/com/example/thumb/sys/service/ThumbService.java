package com.example.thumb.sys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.thumb.sys.entity.Thumb;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.thumb.sys.mapper.ThumbMapper;
import org.springframework.stereotype.Service;

/**
* @author ADMIN
* @description 针对表【thumb】的数据库操作Service
* @createDate 2026-04-12 15:45:27
*/
@Service
public class ThumbService extends ServiceImpl<ThumbMapper, Thumb> {

}
