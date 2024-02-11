package com.meteor.wechatbc.entitiy.synccheck;

import lombok.Getter;

/**
 * 异步消息更新类型
 */
public enum SyncCheckSelector {
    NORMAL("0", "正常"),
    NEW_MSG("2", "有新消息"),
    MOD_CONTACT("4", "昵称修改或备注"),
    ADD_OR_DEL_CONTACT("6", "删除或者新增的好友信息"),
    ENTER_OR_LEAVE_CHAT("7", "进入或离开聊天界面");

    @Getter private String code;
    @Getter private String message;
    SyncCheckSelector(String code,String message){
            this.code = code;
            this.message = message;
    }
    public static SyncCheckSelector form(String code){
        for (SyncCheckSelector value : SyncCheckSelector.values()) {
            if(value.code.equalsIgnoreCase(code)) return value;
        }
        return NORMAL;
    }
}
