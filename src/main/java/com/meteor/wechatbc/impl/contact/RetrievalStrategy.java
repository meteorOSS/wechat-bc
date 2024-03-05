package com.meteor.wechatbc.impl.contact;

import com.meteor.wechatbc.entitiy.contact.Contact;

import java.util.Map;

/**
 * 搜索策略
 */
public interface RetrievalStrategy {
    Contact getContact(Map<String,Contact> source, String key);
}
