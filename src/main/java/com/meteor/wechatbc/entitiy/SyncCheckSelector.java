package com.meteor.wechatbc.entitiy;

/**
 * 异步消息更新类型
 */
public enum SyncCheckSelector {
    NORMAL("0", "正常"),
    NEW_MSG("2", "有新消息"),
    MOD_CONTACT("4", "有人修改了自己的昵称或你修改了别人的备注"),
    ADD_OR_DEL_CONTACT("6", "存在删除或者新增的好友信息"),
    ENTER_OR_LEAVE_CHAT("7", "进入或离开聊天界面");
    private String code;
    private String message;
    SyncCheckSelector(String code,String message){
            this.code = code;
            this.message = message;
    }
}
