package com.meteor.wechatbc.impl.synccheck;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.cache.CacheBuilder;
import com.meteor.wechatbc.HttpAPI;
import com.meteor.wechatbc.entitiy.contact.Contact;
import com.meteor.wechatbc.entitiy.message.Message;
import com.meteor.wechatbc.entitiy.session.SyncKey;
import com.meteor.wechatbc.entitiy.synccheck.SyncCheckResponse;
import com.meteor.wechatbc.entitiy.synccheck.SyncCheckRetcode;
import com.meteor.wechatbc.entitiy.synccheck.SyncCheckSelector;
import com.meteor.wechatbc.impl.WeChatClient;
import com.meteor.wechatbc.impl.event.EventManager;
import com.meteor.wechatbc.impl.event.sub.MessageEvent;
import com.meteor.wechatbc.impl.event.sub.OwnerMessageEvent;
import com.meteor.wechatbc.impl.event.sub.ReceiveMessageEvent;
import com.meteor.wechatbc.impl.model.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 消息检查
 */
public class SyncCheckRunnable {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final Logger logger = LogManager.getLogger("SYNC-CHECK");

    public final WeChatClient weChatClient;

    public SyncCheckRunnable(WeChatClient weChatClient){
        this.weChatClient = weChatClient;
        this.query();
    }

    /**
     * 相同的message可能要做多种事件转发
     * 在此存储相同的message对象
     */
    private Cache<String,Message> messageCache =
            Caffeine.newBuilder().maximumSize(1000)
                    .expireAfterWrite(5, TimeUnit.MINUTES) // 五分钟应该足以进行任何操作
                    .build();

    /**
     * 处理消息。根据消息的类型做不同的处理 (转发事件)
     */
    private void handlerMessage(){

        HttpAPI httpAPI = weChatClient.getWeChatCore().getHttpAPI();
        JSONObject jsonObject = httpAPI.getMessage();

        Session session = weChatClient.getWeChatCore().getSession();
        SyncKey syncKey = JSON.toJavaObject(jsonObject.getJSONObject("SyncKey"), SyncKey.class);
        if(syncKey.getCount()>0){
            session.setSyncKey(syncKey);
        }

        SyncKey checkKey = JSON.toJavaObject(jsonObject.getJSONObject("SyncCheckKey"), SyncKey.class);

        if(checkKey.getCount()>0){
            // 更新缓存中的SyncKey
            session.setCheckSyncKey(checkKey);
        }

        JSONArray addMsgList = jsonObject.getJSONArray("AddMsgList");

        for (int i = 0; i < addMsgList.size(); i++) {

            Message message = JSON.toJavaObject(addMsgList.getJSONObject(i),Message.class);

            messageCache.put(String.valueOf(message.getMsgId()),message);

            logger.debug(message.toString());

            String nickName = Optional.ofNullable(weChatClient.getContactManager().getContactCache().get(message.getFromUserName()))
                            .map(Contact::getNickName)
                                    .orElse("未知");

            String toUser = Optional.ofNullable(weChatClient.getContactManager().getContactCache().get(message.getToUserName()))
                            .map(Contact::getNickName).orElse("未知");

//            logger.info("[{}] -> [{}] : {}",nickName,toUser,message.getContent());
            logger.info(nickName+">"+toUser+":"+message.getContent());
            if(message.getContent().equalsIgnoreCase("ping")){
                JSONObject jsonObject1 = httpAPI.sendMessage(message.getFromUserName(), "你好，我是二次元");
                logger.debug(jsonObject1.toString());
            }

            callMessageEvent(new MessageEvent(messageCache.getIfPresent(String.valueOf(message.getMsgId()))));

        }
    }

    /**
     * 转播事件
     */
    private void callMessageEvent(MessageEvent messageEvent){
        Message message = messageEvent.getMessage();
        EventManager eventManager = weChatClient.getEventManager();
        eventManager.callEvent(messageEvent);

        Session session = weChatClient.getWeChatCore().getSession();
        Contact owner = session.getWxInitInfo().getUser();

        if(message.getFromUserName().equalsIgnoreCase(owner.getUserName())){
            // 本人发出消息
            eventManager.callEvent(new OwnerMessageEvent(message));
        }else {
            // 接收消息
            eventManager.callEvent(new ReceiveMessageEvent(message));
        }
    }

    /**
     * 轮询获取新的消息状态
     */
    public void query(){
        executorService.submit(()->{
            HttpAPI httpAPI = weChatClient.getWeChatCore().getHttpAPI();
            try {
                SyncCheckResponse syncCheckResponse = httpAPI.syncCheck();
                if(syncCheckResponse.getSyncCheckRetcode()== SyncCheckRetcode.NORMAL){
                    handlerMessage();
                }else {
                    logger.error(syncCheckResponse.getSyncCheckRetcode().getMessage());
                    weChatClient.getLogger().info("请尝试重新登录...");
                    weChatClient.stop();
                }
            }catch (Exception e){
                logger.info(e.getMessage());
                logger.info("在尝试异步获取消息时遇到了一个错误");
            }
            this.query();
        });
    }
}
