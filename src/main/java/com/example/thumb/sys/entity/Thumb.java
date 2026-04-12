package com.example.thumb.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName thumb
 */
@TableName(value ="thumb")
@Data
public class Thumb {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private String id;

    /**
     *
     */
    private String userid;

    /**
     *
     */
    private String blogid;

    /**
     * 创建时间
     */
    private Date createtime;
}