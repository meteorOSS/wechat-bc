package com.meteor.wechatbc.impl.contact;

import com.meteor.wechatbc.entitiy.contact.Contact;

import java.util.Map;

public class UserNameStrategy implements RetrievalStrategy{
    @Override
    public Contact getContact(Map<String, Contact> source, String key) {
        return source.get(key);
    }
}
