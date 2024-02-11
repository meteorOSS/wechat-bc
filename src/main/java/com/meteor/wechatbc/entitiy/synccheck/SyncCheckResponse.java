package com.meteor.wechatbc.entitiy.synccheck;

import lombok.Data;

/**
 * 轮询查看新消息通知接口的响应信息
 */
@Data
public class SyncCheckResponse {
    private SyncCheckRetcode syncCheckRetcode;
    private SyncCheckSelector syncCheckSelector;

    public SyncCheckResponse(String retcode,String selector){
        this.syncCheckRetcode = SyncCheckRetcode.form(retcode);
        this.syncCheckSelector = SyncCheckSelector.form(selector);
    }
}
