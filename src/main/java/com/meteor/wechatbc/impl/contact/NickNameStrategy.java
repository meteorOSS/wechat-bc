package com.meteor.wechatbc.impl.contact;

import com.meteor.wechatbc.entitiy.contact.Contact;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 根据名称获取
 */
public class NickNameStrategy implements RetrievalStrategy{

    private Map<String, Contact> contactMap;

    public NickNameStrategy(Map<String,Contact> source){
        contactMap = new ConcurrentHashMap<>();
        source.values().stream().forEach(contact -> {
            contactMap.put(contact.getNickName(),contact);
        });
    }


    @Override
    public Contact getContact(Map<String,Contact> source, String key) {

        if(contactMap.containsKey(key)) return contactMap.get(key);
        else {
            for (Contact value : source.values()) {
                if(value.getNickName().equalsIgnoreCase(key)) {
                    contactMap.put(key,value);
                    return value;
                }
            }
        }

        return null;

    }
}
