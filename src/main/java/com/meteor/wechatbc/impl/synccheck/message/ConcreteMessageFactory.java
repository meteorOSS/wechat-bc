package com.meteor.wechatbc.impl.synccheck.message;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.meteor.wechatbc.entitiy.message.Message;
import com.meteor.wechatbc.impl.WeChatClient;
import com.meteor.wechatbc.impl.model.MsgType;
import com.meteor.wechatbc.impl.model.message.*;


public class ConcreteMessageFactory implements MessageFactory {

    private final WeChatClient weChatClient;

    public ConcreteMessageFactory(WeChatClient weChatClient){
        this.weChatClient = weChatClient;
    }





    /**
     * 根据响应的格式获取message的子类
     * @param messageJson
     * @return
     */
    @Override
    public Message createMessage(JSONObject messageJson) {
        Message message = JSON.toJavaObject(messageJson, Message.class);
        MsgType msgType = message.getMsgType();

        if(msgType == MsgType.TextMsg){
            return JSON.toJavaObject(messageJson, TextMessage.class);
        }else if(msgType == MsgType.ImageMsg){
            ImageMessage imageMessage = JSON.toJavaObject(messageJson, ImageMessage.class);
            imageMessage.setBytes(weChatClient.getWeChatCore().getHttpAPI().getMsgImage(String.valueOf(message.getMsgId()))); // 设置图片原始数据
            return imageMessage;
        }else if(msgType == MsgType.ImgEmoteMsg){
            return JSON.toJavaObject(messageJson, ImageEmoteMessage.class);
        }else if(msgType==MsgType.VideoMsg){
            VideoMessage videoMessage = JSON.toJavaObject(messageJson, VideoMessage.class);
            videoMessage.setBytes(weChatClient.getWeChatCore().getHttpAPI().getVideo(videoMessage.getMsgId()));
            return videoMessage;
        }else if(msgType==MsgType.RevokeMsg){
            RevokeMessage revokeMessage = JSON.toJavaObject(messageJson, RevokeMessage.class);
            revokeMessage.setOldMessage(weChatClient.getSyncCheckRunnable().getMessageCache().getIfPresent(revokeMessage.getOldMessageID()));
            return revokeMessage;
        }else if(msgType==MsgType.APPMsg){
            if(message.getContent().contains("收款")){
                PayMessage payMessage = JSON.toJavaObject(messageJson,PayMessage.class);
                payMessage.extractAmount();
                payMessage.extractNotes();
                return payMessage;
            }
        }
        return message;
    }
}

