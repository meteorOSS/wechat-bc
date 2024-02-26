package com.meteor.wechatbc.impl.contact;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.meteor.wechatbc.impl.HttpAPI;
import com.meteor.wechatbc.entitiy.contact.Contact;
import com.meteor.wechatbc.impl.WeChatClient;
import lombok.Getter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 微信联系人
 */
public class ContactManager {


    private class ContactCacheLoader implements CacheLoader<String,Contact>{
        @Override
        public @Nullable Contact load(@NonNull String key) throws Exception {
            return getContact(key);
        }
    }

    private final WeChatClient weChatClient;

    @Getter private LoadingCache<String, Contact> contactCache
            = Caffeine.newBuilder().maximumSize(1000)
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build(new ContactCacheLoader());

    private Map<String,Contact> contactMap;

    public ContactManager(WeChatClient weChatClient){
        this.weChatClient = weChatClient;
        this.contactMap = ref();
        contactMap.forEach((k,v)->{
            System.out.println(v.getUserName()+"/"+v.getNickName());
        });
        this.weChatClient.getLogger().info("联系人列表数量: "+contactMap.size());
    }


    private Contact getContact(String userName){

        Contact user = weChatClient.getWeChatCore().getSession().getWxInitInfo().getUser();
        if(user.getUserName().equalsIgnoreCase(userName)){
            return user;
        }

        if(contactMap.containsKey(userName)) return contactMap.get(userName);

        else {
            this.contactMap = ref();
            return contactMap.get(userName);
        }
    }

    /**
     * 刷新联系人列表
     */
    public Map<String,Contact> ref(){
        HttpAPI httpAPI = weChatClient.getWeChatCore().getHttpAPI();
        JSONObject response = httpAPI.getContact();
        JSONArray memberList = response.getJSONArray("MemberList");
        Map<String,Contact> map = new HashMap<>();
        for (int i = 0; i < memberList.size(); i++) {
            JSONObject jsonObject = memberList.getJSONObject(i);
            Contact contact = JSON.toJavaObject(jsonObject, Contact.class);
            contact.setWeChatClient(this.weChatClient);
            map.put(contact.getUserName(),contact);
        }
        return map;
    }

}
