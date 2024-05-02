package com.meteor.wechatbc.impl.event.sub;

import com.meteor.wechatbc.entitiy.synccheck.SyncCheckSelector;
import com.meteor.wechatbc.event.Event;
import com.meteor.wechatbc.impl.WeChatClient;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 微信登出事件
 */
@AllArgsConstructor
@Data
public class ClientDeathEvent extends Event {

    private WeChatClient weChatClient;

    private SyncCheckSelector syncCheckSelector;
}
