package com.meteor.wechatbc.impl.synccheck;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.meteor.wechatbc.HttpAPI;
import com.meteor.wechatbc.entitiy.message.Message;
import com.meteor.wechatbc.entitiy.session.SyncKey;
import com.meteor.wechatbc.entitiy.synccheck.SyncCheckResponse;
import com.meteor.wechatbc.entitiy.synccheck.SyncCheckRetcode;
import com.meteor.wechatbc.entitiy.synccheck.SyncCheckSelector;
import com.meteor.wechatbc.impl.WeChatClient;
import com.meteor.wechatbc.impl.model.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 消息检查
 */
public class SyncCheckRunnable {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final Logger logger = LoggerFactory.getLogger("SYNC-CHECK");

    public final WeChatClient weChatClient;

    public SyncCheckRunnable(WeChatClient weChatClient){
        this.weChatClient = weChatClient;
        this.query();
    }


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
            logger.info("[chat] "+message.getFromUserName()+" > "+message.getContent());
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
                    SyncCheckSelector syncCheckSelector = syncCheckResponse.getSyncCheckSelector();
                    if(syncCheckSelector == SyncCheckSelector.NORMAL) return;
//                    logger.info("[新的消息] "+syncCheckSelector.getMessage());
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
