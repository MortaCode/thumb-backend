package com.example.thumb.sys.vo;

import lombok.Getter;

@Getter
public enum LuaStateEnum {

    SUCCESS(1),
    FAIL(0);

    private long value;
    LuaStateEnum(int value){
        this.value = value;
    }
}
