package com.meteor.wechatbc.entitiy.synccheck;

import lombok.Getter;

public enum SyncCheckRetcode {
    NORMAL("0", "正常"),
    TICK_ERROR("-14", "ticket错误"),
    PARAM_ERROR("1", "传入参数错误"),
    LOGIN_OUT("1100", "已登出微信"),
    NOT_LOGIN("1101", "未登录微信"),
    COOKIE_ERROR("1102","cookie值无效"),
    LOGIN_FAIL("1203","当前登录环境异常"),
    OFTEN("1205","操作频繁");

    @Getter private String code;
    @Getter private String message;
    SyncCheckRetcode(String code,String message){
        this.code = code;
        this.message = message;
    }
    public static SyncCheckRetcode form(String code){
        for (SyncCheckRetcode value : SyncCheckRetcode.values()) {
            if(value.code.equalsIgnoreCase(code)) return value;
        }
        return NORMAL;
    }


}
