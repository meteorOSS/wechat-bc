package com.meteor.wechatbc.event;

import com.meteor.wechatbc.impl.event.EventManager;
import lombok.Getter;
import lombok.Setter;

/**
 * 所有事件的基类
 */
public class Event {

    @Getter @Setter
    private EventManager eventManager;

    /**
     * 只允许对子类进行构造
     */
    protected Event(){
    }
}
